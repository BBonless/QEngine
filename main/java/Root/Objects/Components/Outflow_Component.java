package Root.Objects.Components;

import Root.GUI.Layers.SimulationPreferences_Layer;
import Root.Rendering.Gizmo;
import Root.Shaders.ShaderProgram;
import Root.Simulation.Particle;
import Root.Simulation.SimEngine;
import imgui.ImGui;
import imgui.type.ImBoolean;
import imgui.type.ImFloat;

import java.util.ArrayList;

public class Outflow_Component extends Component {

    public ImFloat Radius = new ImFloat(1);

    private ImBoolean Outflow = new ImBoolean(false);

    public Outflow_Component() {
        Name = "Outflow";
    }

    public void Suck() {
        ArrayList<Particle> Victims = SimEngine.Grid.NeighborQuery(Parent.Position, Radius.get());

        Particle[] UpdatedParticleArray = new Particle[SimEngine.DynamicParticles.length - Victims.size()];

        int Count = 0;
        Particle Remove = null;
        for (Particle P1 : SimEngine.DynamicParticles) {
            for (Particle P2 : Victims) {
                if (P1.equals(P2)) {
                    Remove = P2;
                    break;
                }
            }
            if (Remove != null) {
                Victims.remove(Remove);
                Remove = null;
            }
            else {
                if (Count < UpdatedParticleArray.length) {
                    UpdatedParticleArray[Count++] = P1;
                }
            }
        }

        SimEngine.DynamicParticles = UpdatedParticleArray;
    }

    @Override
    public void Upload(ShaderProgram Shader) {

    }

    @Override
    public void InternalGUI() {
        Gizmo.PushSphereGizmo(Parent.Position, Radius.get());

        ImGui.checkbox("Outflow?", Outflow);

        ImGui.dragFloat("Radius", Radius.getData(), 0.05f);
    }

    @Override
    public void InternalUpdate() {

    }

    @Override
    public void Update() {
        if (Outflow.get() && SimulationPreferences_Layer.PlaySim.get()) {
            Suck();
        }
    }
}
