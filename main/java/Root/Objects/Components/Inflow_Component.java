package Root.Objects.Components;

import Root.GUI.Layers.SimulationPreferences_Layer;
import Root.Misc.Util.Util;
import Root.Objects.WorldObject;
import Root.Shaders.ShaderProgram;
import Root.Simulation.Particle;
import Root.Simulation.Preferences;
import Root.Simulation.SimEngine;
import imgui.ImGui;
import imgui.type.ImBoolean;
import imgui.type.ImFloat;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Math;

import java.util.ArrayList;

public class    Inflow_Component extends Component {

    public ImFloat Radius = new ImFloat(10);
    public ImFloat Density = new ImFloat(1);
    public ImFloat Separation = new ImFloat(1);
    public float[] InitialVelocity = new float[] {0,0,0};
    public Vector3f RotatedVelocity = new Vector3f(0,0,0);

    public ImFloat Rate = new ImFloat();
    public float Time = 0;

    public ImBoolean Flow = new ImBoolean(false);

    public Vector3f[] SpawnPositions;

    public Inflow_Component() {
        Name = "Inflow";
    }

    public void ComputeParticles() {
        int Max = (int)((Radius.get()*2)/Density.get());
        float HalfRadius = Radius.get() / 2;

        ArrayList<Vector3f> SpawnPositionList = new ArrayList<>();

        for (int X = 0; X < Max; X++) {
            for (int Y = 0; Y < Max; Y++) {

                Vector2f Position = new Vector2f(
                        (X / (float)Max) * Radius.get(),
                        (Y / (float)Max) * Radius.get()
                );

                Position.sub(HalfRadius, HalfRadius);

                if (Position.lengthSquared() <= HalfRadius * HalfRadius) {
                    Position.mul(Separation.get());

                    Vector3f Position3D = new Vector3f(Position, 0)
                    .rotateX(Math.toRadians(Parent.Rotation.x))
                    .rotateY(Math.toRadians(Parent.Rotation.y))
                    .rotateZ(Math.toRadians(Parent.Rotation.z));

                    RotatedVelocity = new Vector3f(InitialVelocity)
                    .rotateX(Math.toRadians(Parent.Rotation.x))
                    .rotateY(Math.toRadians(Parent.Rotation.y))
                    .rotateZ(Math.toRadians(Parent.Rotation.z));

                    Position3D.add(Parent.Position);
                    Position3D.mul(Parent.Scale);

                    SpawnPositionList.add(Position3D);
                }
            }
        }

        SpawnPositions = Util.ArrayList2Array(SpawnPositionList, Vector3f.class);
    }

    public void Spawn() {
        if (Time >= Rate.get()) {
            Particle[] UpdatedParticleArray = new Particle[SimEngine.DynamicParticles.length + SpawnPositions.length];

            System.arraycopy(SimEngine.DynamicParticles, 0, UpdatedParticleArray, 0, SimEngine.DynamicParticles.length);

            for (int i = SimEngine.DynamicParticles.length; i < UpdatedParticleArray.length; i++) {
                Particle NewParticle = new Particle();

                SpawnPositions[i - SimEngine.DynamicParticles.length].get(NewParticle.Position);

                RotatedVelocity.get(NewParticle.Velocity);

                UpdatedParticleArray[i] = NewParticle;
            }

            SimEngine.DynamicParticles = UpdatedParticleArray;

            Time = 0;
        }

        Time += Preferences.Timestep;
    }

    @Override
    public void Attach(WorldObject Target) {
        super.Attach(Target);
        ComputeParticles();
    }

    @Override
    public void Upload(ShaderProgram Shader) {

    }

    @Override
    public void InternalUpdate() {
        ComputeParticles();
    }

    @Override
    public void Update() {
        if (Flow.get() && SimulationPreferences_Layer.PlaySim.get()) {
            Spawn();
        }
    }

    @Override
    public void InternalGUI() {

        ImGui.checkbox("Flow?", Flow);

        ImGui.separator();

        if (ImGui.dragFloat("Inflow Radius", Radius.getData(), 0.05f, 0, 10000f)) {
            InternalUpdate();
        }
        if (ImGui.dragFloat("Inflow Density", Density.getData(), 0.05f, 1, Radius.get())) {
            InternalUpdate();
        }
        if (ImGui.dragFloat("Inflow Separation", Separation.getData(), 0.05f, 0, 10000f)) {
            InternalUpdate();
        }
        if (ImGui.dragFloat3("Initial Force", InitialVelocity, 0.05f)) {
            InternalUpdate();
        }
        if (ImGui.dragFloat("Rate", Rate.getData(), 0.05f, 0, 10000)) {
            InternalUpdate();
        }
    }
}
