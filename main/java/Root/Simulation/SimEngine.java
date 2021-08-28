package Root.Simulation;

import Root.Engine;
import Root.Objects.Geometry.Mesh;
import Root.Objects.Geometry.SphereMesh;
import Root.Shaders.Material;
import Root.Shaders.ShaderProgram;
import Root.Misc.Util.Util;
import Root.Objects.WorldObject;
import org.joml.Vector3f;

public class SimEngine {

    public static Particle[] Particles;
    public static float[] ParticlePositions;
    private static int ParticleCounter = 0;

    public static WorldObject ParticleObject;

    public static SpatialGrid Grid;

    public static void Init() {
        if (ParticleObject != null) {
            Engine.RenderQueue.remove(ParticleObject);
        }

        Mesh ParticleMesh = SphereMesh.Generate(Preferences.ParticleDrawSize, 10, 8);

        ParticleObject = new WorldObject(ParticleMesh);

        ParticleObject.Material = new Material(
                new Vector3f(0.05f, 0.05f, 0.6f),
                new Vector3f(0.05f, 0.05f, 0.6f),
                new Vector3f(0.5f, 0.5f, 0.5f),
                2f
        );

        ParticleObject.Shader = new ShaderProgram(
                ShaderProgram.LoadShaderResource("/LitInstanced.vert"),
                ShaderProgram.LoadShaderResource("/LitInstanced.frag")
        );

        ParticleObject.SetInstanced(dbdb());

        Engine.RenderQueue.add(ParticleObject);

        UpdateGrid();
    }

    public static void UpdateGrid() {
        Grid = new SpatialGrid(Preferences.BoundarySize, Preferences.GridX, Preferences.GridY, Preferences.GridZ);
    }

    public static void Reset(){
        Particles = new Particle[Preferences.ParticleBlockSize];
        ParticlePositions = new float[Particles.length * 3];

        Particles_Spawn();
    }

    public static float[] Step() {

        Grid.Clear();
        Grid.Fill(Particles);

        if (ParticlePositions.length != Particles.length * 3) {
            ParticlePositions = new float[Particles.length * 3];
        }
        ParticleCounter = 0;

        for (int i = 0; i < Preferences.SimulationIterations; i++) {

            for (Particle P1 : Particles) {
                //Find Neighbors
                Particle_SetNeighborsGrid(P1);
            }

            for (int j = 0; j < Preferences.SolverIterations; j++) {

                for (Particle P1 : Particles) {
                    //Set Density & Pressure
                    Particle_SetDensityPressure(P1);
                }

                for (Particle P1 : Particles) {
                    //Set Net Force
                    Particle_SetNetForce(P1);
                }

            }

            for (Particle P1 : Particles) {
                //Integrate Particles
                Particle_Integrate(P1);
            }
        }

        //System.out.println(ParticlePositions.length);
        return ParticlePositions;
    }

    private static float[] dbdb() {
        int Count = 0;
        float[] Results = new float[Preferences.ParticleBlockSize * 3];
        for (int x = 0; x < Preferences.ParticleBlockSizeX; x++) {
            for (int y = 0; y < Preferences.ParticleBlockSizeY; y++) {
                for (int z = 0; z < Preferences.ParticleBlockSizeZ; z++) {

                    Vector3f tpp = Preferences.GetSpawnPosition(x,y,z);

                    Results[Count*3+0] = tpp.x;
                    Results[Count*3+1] = tpp.y;
                    Results[Count*3+2] = tpp.z;
                    Count++;

                }
            }
        }
        return Results;
    }

    private static void Particles_Spawn() {
        int Count = 0;

        for (int x = 0; x < Preferences.ParticleBlockSizeX; x++) {
            for (int y = 0; y < Preferences.ParticleBlockSizeY; y++) {
                for (int z = 0; z < Preferences.ParticleBlockSizeZ; z++) {

                    Particle NewParticle = new Particle();

                    NewParticle.Position = Preferences.GetSpawnPosition(x,y,z);

                    Particles[Count++] = NewParticle;

                }
            }
        }
    }

    private static void Particle_SetNeighborsBruteforce(Particle P1) {
        P1.Neighbors.clear();

        for (Particle P2 : Particles) {
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

            if (Util.DistanceSquared(P1, P2) <= Preferences.SmoothingRadiusSqr) {
                P1.Density += Preferences.ParticleMass * Kernels.Poly6(P1, P2);
            }
        }

        //Density must be >= RestDensity
        if (P1.Density < Preferences.RestDensity) {
            P1.Density = Preferences.RestDensity;
        }
        //endregion

        //region Pressure
        P1.Pressure = Preferences.Stiffness * (P1.Density - Preferences.RestDensity);
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
        P1.Force.add(PressureGradient).add(ViscosityGradient).add(Preferences.Gravity).add(Util.RandomVector(1).mul(0.1f));
    }

    private static void Particle_Integrate(Particle P1) {
        Particle_EnforceBoundary(P1);

        //Try Vector3 ParticleAcceleration = P1.Force * P.ParticleMass; Replace P1.NetForce with PA

        Vector3f NewVelocity = new Vector3f(0,0,0);
        P1.PastAcceleration.add(P1.Force, NewVelocity);
        P1.Velocity.add( NewVelocity.mul(0.5f).mul(Preferences.Timestep) );

        Vector3f DeltaPosition = new Vector3f(0,0,0);

        Vector3f AuxForce = new Vector3f(0,0,0);
        P1.Force.get(AuxForce);
        AuxForce.mul(Preferences.Timestep * Preferences.Timestep * 0.5f);

        Vector3f AuxVel = new Vector3f(0,0,0);
        P1.Velocity.get(AuxVel);
        AuxVel.mul(Preferences.Timestep);
        AuxVel.add(AuxForce, DeltaPosition);

        P1.Position.add(DeltaPosition);
        P1.PastAcceleration = P1.Force;

        ParticlePositions[ParticleCounter*3+0] = P1.Position.x;
        ParticlePositions[ParticleCounter*3+1] = P1.Position.y;
        ParticlePositions[ParticleCounter*3+2] = P1.Position.z;
        ParticleCounter++;
    }
    
    private static void Particle_EnforceBoundary(Particle P1) {
        if ( P1.Position.x < Preferences.BoundarySize.x / -2 )
        {
            P1.Position.x = (Preferences.BoundarySize.x / -2) + 0.001f;
            P1.Velocity.x = -P1.Velocity.x * Preferences.BoundaryElasticity;
            //P1.Velocity.mul(Preferences.BoundaryElasticity * -1);
            P1.Force.x = 0;
        }
        else if ( P1.Position.x > Preferences.BoundarySize.x / 2 )
        {
            P1.Position.x = (Preferences.BoundarySize.x / 2) - 0.001f;
            P1.Velocity.x = -P1.Velocity.x * Preferences.BoundaryElasticity;
            //P1.Velocity.mul(Preferences.BoundaryElasticity * -1);
            P1.Force.x = 0;
        }

        if ( P1.Position.y < Preferences.BoundarySize.y / -2 )
        {
            P1.Position.y = (Preferences.BoundarySize.y / -2) + 0.001f;
            P1.Velocity.y = (-P1.Velocity.y * Preferences.BoundaryElasticity) + 1;
            //P1.Velocity.mul(Preferences.BoundaryElasticity * -1);
            P1.Force.y = 0;

            //P1.Velocity.add(new Vector3f(0, 1, 0));  //Pushes particles slighlty upwards so they don't compress at the bottom
        }
        else if ( P1.Position.y > Preferences.BoundarySize.y / 2 )
        {
            P1.Position.y = (Preferences.BoundarySize.y / 2) - 0.001f;
            P1.Velocity.y = -P1.Velocity.y * Preferences.BoundaryElasticity;
            //P1.Velocity.mul(Preferences.BoundaryElasticity * -1);
            P1.Force.y = 0;
        }

        if ( P1.Position.z < Preferences.BoundarySize.z / -2 )
        {
            P1.Position.z = (Preferences.BoundarySize.z / -2) + 0.001f;
            P1.Velocity.z = (-P1.Velocity.z * Preferences.BoundaryElasticity) + 1;
            //P1.Velocity.mul(Preferences.BoundaryElasticity * -1);
            P1.Force.z = 0;
        }
        else if ( P1.Position.z > Preferences.BoundarySize.z / 2 )
        {
            P1.Position.z = (Preferences.BoundarySize.z / 2) - 0.001f;
            P1.Velocity.z = -P1.Velocity.z * Preferences.BoundaryElasticity;
            //P1.Velocity.mul(Preferences.BoundaryElasticity * -1);
            P1.Force.z = 0;
        }
    }

    public static Vector3f Physics_ComputePressureForce(Particle P1, Particle P2) {
        //Calculate value based on Formula
        float Dividend = P1.Pressure + P2.Pressure;
        float Divisor = 2 * P1.Density * P2.Density;

        return Kernels.SpikyGrad(P1, P2).mul(-Preferences.ParticleMass * (Dividend / Divisor));
    }

    public static Vector3f Physics_ComputeViscosityForce(Particle P1, Particle P2) {
        Vector3f DeltaVelocity = new Vector3f(0,0,0);
        P1.Velocity.sub(P2.Velocity, DeltaVelocity);

        return DeltaVelocity.mul( -Preferences.ParticleViscosity * (Preferences.ParticleMass / P1.Density) * Kernels.Laplacian(P1, P2) );
    }
}
