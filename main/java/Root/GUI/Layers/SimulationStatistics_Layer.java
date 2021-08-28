package Root.GUI.Layers;

import Root.GUI.Layer;
import Root.Simulation.SimEngine;
import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImInt;

public class SimulationStatistics_Layer implements Layer {
    @Override
    public void Render_ImGUI() {
        ImGui.begin("Simulation Stats");

        ImGui.pushItemWidth(160);

        ImInt ParticleCount = new ImInt(SimEngine.Particles == null ? 0 : SimEngine.Particles.length);
        ImGui.inputInt("Particle Amount", ParticleCount, 0, 0, ImGuiInputTextFlags.ReadOnly);

        ImGui.popItemWidth();

        ImGui.end();
    }
}
