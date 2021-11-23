package Root.Simulation;

import Root.Engine.Engine;
import Root.GUI.Layers.MeshGeneration_Layer;
import Root.Geometry.Mesh;
import Root.Geometry.SphereMesh;
import Root.MeshGen.MarchingCubes;
import Root.MeshGen.MarchingGrid;
import Root.Shaders.Material;
import Root.Shaders.ShaderManager;
import Root.Shaders.ShaderProgram;
import Root.Misc.Util.Util;
import Root.Objects.WorldObject;
import org.joml.Vector3f;

import java.util.ArrayList;

public class SimEngine {

    public static Particle[] DynamicParticles;
    public static ArrayList<ArrayList<Particle>> StaticParticles = new ArrayList<>();
    public static float[] ParticlePositions;
    private static int ParticleCounter = 0;

    public static WorldObject FluidParticleObject;
    public static WorldObject FluidMeshObject;

    public static SpatialGrid Grid;

    private static boolean ParticlesReady = false;

    public static void Init() {
        FluidMeshObject = new WorldObject();

        if (FluidParticleObject != null) {
            Engine.RenderQueue.remove(FluidParticleObject);
        }

        Mesh ParticleMesh = SphereMesh.Generate(Preferences.ParticleDrawSize[0], 10, 8);

        FluidParticleObject = new WorldObject(ParticleMesh);

        FluidParticleObject.Material = new Material(
                new Vector3f(0.05f, 0.05f, 0.6f),
                new Vector3f(0.05f, 0.05f, 0.6f),
                new Vector3f(0.5f, 0.5f, 0.5f),
                2f
        );

        FluidParticleObject.Shader = new ShaderProgram(
                ShaderManager.LoadShaderResource("/LitInstanced.vert"),
                ShaderManager.LoadShaderResource("/LitInstanced.frag")
        );

        FluidParticleObject.SetInstanced(Preview());

        Engine.RenderQueue.add(FluidParticleObject);

        UpdateGrid();
    }

    public static void UpdateGrid() {
        Grid = new SpatialGrid(Util.FloatArrToVec(Preferences.BoundarySize), Preferences.GridX[0], Preferences.GridY[0], Preferences.GridZ[0]);
    }

    public static void Reset(){
        Preferences.UpdateSize();

        DynamicParticles = new Particle[Preferences.ParticleBlockSize[0]];
        ParticlePositions = new float[DynamicParticles.length * 3];

        Particles_Spawn();

        ParticlesReady = true;
    }

    public static float[] Step() {

        if (!ParticlesReady) {
            Reset();
        }

        Grid.Clear();
        Grid.Fill(DynamicParticles);
        Grid.Fill(StaticParticles);

        //Ensures ParticlePositions is the size it needs to be to fit all the particles
        //Needed as when particles are added or removed, DynamicParticles is updated, ParticlePositions isn't
        if (ParticlePositions.length != DynamicParticles.length * 3) {
            ParticlePositions = new float[DynamicParticles.length * 3];
        }
        ParticleCounter = 0;

        for (int i = 0; i < Preferences.SimulationIterations[0]; i++) {

            //Find Neighbors
            for (Particle P1 : DynamicParticles) {
                Particle_SetNeighborsGrid(P1);
            }

            for (int j = 0; j < Preferences.SolverIterations[0]; j++) {

                //Set Density & Pressure
                for (Particle P1 : DynamicParticles) {
                    Particle_SetDensityPressure(P1);
                }

                //Set Net Force
                for (Particle P1 : DynamicParticles) {
                    Particle_SetNetForce(P1);
                }

            }

            //Integrate Particles
            for (Particle P1 : DynamicParticles) {
                Particle_Integrate(P1);
            }
        }

        if (MeshGeneration_Layer.UpdateFluidMesh.get()) {
            if (MeshGeneration_Layer.Overflow.get()) {
                MarchingGrid.FillOverflow();
            }
            else {
                MarchingGrid.Fill();
            }
            MarchingCubes.March();
        }

        //System.out.println(ParticlePositions.length);
        return ParticlePositions;
    }

    //Calculates the positions where the points will appear
    public static float[] Preview() {
        int Count = 0;
        Preferences.UpdateSize();
        float[] Points = new float[Preferences.ParticleBlockSize[0] * 3];
        for (int x = 0; x < Preferences.ParticleBlockSizeX[0]; x++) {
            for (int y = 0; y < Preferences.ParticleBlockSizeY[0]; y++) {
                for (int z = 0; z < Preferences.ParticleBlockSizeZ[0]; z++) {

                    Vector3f SpawnPosition = Preferences.GetSpawnPosition(x,y,z);

                    Points[Count*3+0] = SpawnPosition.x;
                    Points[Count*3+1] = SpawnPosition.y;
                    Points[Count*3+2] = SpawnPosition.z;
                    Count++;

                }
            }
        }
        return Points;
    }

    private static void Particles_Spawn() {
        int Count = 0;

        for (int x = 0; x < Preferences.ParticleBlockSizeX[0]; x++) {
            for (int y = 0; y < Preferences.ParticleBlockSizeY[0]; y++) {
                for (int z = 0; z < Preferences.ParticleBlockSizeZ[0]; z++) {

                    Particle NewParticle = new Particle();

                    NewParticle.Position = Preferences.GetSpawnPosition(x,y,z);

                    DynamicParticles[Count++] = NewParticle;

                }
            }
        }
    }

    private static void Particle_SetNeighborsBruteforce(Particle P1) {
        P1.Neighbors.clear();

        for (Particle P2 : DynamicParticles) {
            //If particles are the same, skip
            if (P1 == P2) { continue; }

            //Calculate distance squared between both particles
            //If they are close enough to each other, add to P1's neighbor list
            if (Util.DistanceSquared(P1, P2) <= Preferences.SmoothingRadiusSqr) {
                P1.Neighbors.add(P2);
            }
        }
    }

    private static void Particle_SetNeighborsGrid(Particle P1) {
        Grid.NeighborQuery(P1);
    }

    private static void Particle_SetDensityPressure(Particle P1) {
        //region Density
        P1.Density = 0;

        for (Particle P2 : P1.Neighbors) {
            //If particles are the same, skip
            if (P1 == P2) { continue; }

            //Check if the Neighbor is inside of the Kernel's radius
            //No point doing calculation if it isn't
            if (Util.DistanceSquared(P1, P2) <= Preferences.SmoothingRadiusSqr) {
                P1.Density += Preferences.ParticleMass[0] * Kernels.Poly6(P1, P2);
            }
        }

        //Density must be >= RestDensity
        if (P1.Density < Preferences.RestDensity[0]) {
            P1.Density = Preferences.RestDensity[0];
        }
        //endregion

        //region Pressure
        P1.Pressure = Preferences.Stiffness[0] * (P1.Density - Preferences.RestDensity[0]);
        //endregion
    }

    private static void Particle_SetNetForce(Particle P1) {
        P1.Force.mul(0);

        Vector3f PressureGradient = new Vector3f(0,0,0);
        Vector3f ViscosityGradient = new Vector3f(0,0,0);

        for (Particle P2 : P1.Neighbors) {
            //If Particles are the same, skip
            if (P1 == P2) { continue; }

            PressureGradient.add(Physics_ComputePressureForce(P1, P2));

            ViscosityGradient.add(Physics_ComputeViscosityForce(P1, P2));
        }
        P1.Force
                .add(PressureGradient)
                .add(ViscosityGradient)
                .add(Util.FloatArrToVec(Preferences.Gravity))
                .add(Util.RandomVector(1).mul(0.1f));
    }

    private static void Particle_Integrate(Particle P1) {
        Vector3f NewVelocity = new Vector3f(0,0,0);
        P1.Acceleration.add(P1.Force, NewVelocity);
        P1.Velocity.add( NewVelocity.mul(0.5f).mul(Preferences.Timestep[0]) );

        Vector3f DeltaPosition = new Vector3f(0,0,0);

        Vector3f AuxForce = new Vector3f(0,0,0);
        P1.Force.get(AuxForce);
        AuxForce.mul(Preferences.Timestep[0] * Preferences.Timestep[0] * 0.5f);

        Vector3f AuxVel = new Vector3f(0,0,0);
        P1.Velocity.get(AuxVel);
        AuxVel.mul(Preferences.Timestep[0]);
        AuxVel.add(AuxForce, DeltaPosition);

        P1.Position.add(DeltaPosition);
        P1.Acceleration = P1.Force;

        Particle_EnforceBoundary(P1);

        for (int i = 0; i < 3; i++) {
            ParticlePositions[ParticleCounter*3+i] = P1.Position.get(i);
        }
        ParticleCounter++;
    }
    
    private static void Particle_EnforceBoundary(Particle P1) {

        if ( P1.Position.x < Preferences.BoundarySize[0] / -2 )
        {
            P1.Position.x = (Preferences.BoundarySize[0] / -2) + 0.001f;
            P1.Velocity.x = -P1.Velocity.x * Preferences.BoundaryElasticity[0];
            //P1.Velocity.mul(Preferences.BoundaryElasticity * -1);
            P1.Force.x = 0;
        }
        else if ( P1.Position.x > Preferences.BoundarySize[0] / 2 )
        {
            P1.Position.x = (Preferences.BoundarySize[0] / 2) - 0.001f;
            P1.Velocity.x = -P1.Velocity.x * Preferences.BoundaryElasticity[0];
            //P1.Velocity.mul(Preferences.BoundaryElasticity * -1);
            P1.Force.x = 0;
        }

        if ( P1.Position.y < Preferences.BoundarySize[1] / -2 )
        {
            P1.Position.y = (Preferences.BoundarySize[1] / -2) + 0.001f;
            P1.Velocity.y = (-P1.Velocity.y * Preferences.BoundaryElasticity[0]) + 1;
            //P1.Velocity.mul(Preferences.BoundaryElasticity * -1);
            P1.Force.y = 0;

            //P1.Velocity.add(new Vector3f(0, 1, 0));  //Pushes particles slighlty upwards so they don't compress at the bottom
        }
        else if ( P1.Position.y > Preferences.BoundarySize[1] / 2 )
        {
            P1.Position.y = (Preferences.BoundarySize[1] / 2) - 0.001f;
            P1.Velocity.y = -P1.Velocity.y * Preferences.BoundaryElasticity[0];
            //P1.Velocity.mul(Preferences.BoundaryElasticity * -1);
            P1.Force.y = 0;
        }

        if ( P1.Position.z < Preferences.BoundarySize[2] / -2 )
        {
            P1.Position.z = (Preferences.BoundarySize[2] / -2) + 0.001f;
            P1.Velocity.z = (-P1.Velocity.z * Preferences.BoundaryElasticity[0]) + 1;
            //P1.Velocity.mul(Preferences.BoundaryElasticity * -1);
            P1.Force.z = 0;
        }
        else if ( P1.Position.z > Preferences.BoundarySize[2] / 2 )
        {
            P1.Position.z = (Preferences.BoundarySize[2] / 2) - 0.001f;
            P1.Velocity.z = -P1.Velocity.z * Preferences.BoundaryElasticity[0];
            //P1.Velocity.mul(Preferences.BoundaryElasticity * -1);
            P1.Force.z = 0;
        }
    }

    public static Vector3f Physics_ComputePressureForce(Particle P1, Particle P2) {
        //Calculate value based on Formula
        float Dividend = P1.Pressure + P2.Pressure;
        float Divisor = 2 * P1.Density * P2.Density;

        return Kernels.SpikyGrad(P1, P2)
                .mul(-Preferences.ParticleMass[0] * (Dividend / Divisor));
    }

    public static Vector3f Physics_ComputeViscosityForce(Particle P1, Particle P2) {
        Vector3f DeltaVelocity = new Vector3f(0,0,0);
        P1.Velocity.sub(P2.Velocity, DeltaVelocity);

        return DeltaVelocity.mul(
                -Preferences.ParticleViscosity[0] * (
                        Preferences.ParticleMass[0] / P1.Density
                        )
                * Kernels.Laplacian(P1, P2)
        );
    }
}
