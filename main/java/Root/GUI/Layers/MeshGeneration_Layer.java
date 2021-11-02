package Root.GUI.Layers;

import Root.Engine;
import Root.GUI.Layer;
import Root.MeshGen.MarchingCubes;
import Root.MeshGen.MarchingGrid;
import Root.Simulation.SimEngine;
import imgui.ImGui;
import imgui.type.ImBoolean;

import static Root.MeshGen.MarchingGrid.*;

public class MeshGeneration_Layer implements Layer {

    public static ImBoolean UpdateFluidMesh = new ImBoolean(false);
    ImBoolean ShowFluidMesh = new ImBoolean(false);
    public static ImBoolean Overflow = new ImBoolean(false);

    @Override
    public void Render_ImGUI() {
        ImGui.begin("Mesh Generation");

        if (ImGui.button("Generate Marching Grid")) {
            if (Overflow.get()) {
                MarchingGrid.FillOverflow();
            }
            else {
                MarchingGrid.Fill();
            }
        }

        ImGui.checkbox("Allow Overflow?", Overflow);
        if (ImGui.isItemHovered()) {
            ImGui.setTooltip("Overflow constructs a mesh even at the boundaries of the simulation, instead of cutting the mesh at the edges.");
        }


        if (ImGui.button("March")) {
            MarchingCubes.March();
        }

        ImGui.checkbox("Update Fluid Mesh?", UpdateFluidMesh);

        if (ImGui.checkbox("Show Fluid Mesh?", ShowFluidMesh)) {
            if (ShowFluidMesh.get()) {
                Engine.RenderQueue.add(SimEngine.FluidMeshObject);
            }
            else {
                Engine.RenderQueue.remove(SimEngine.FluidMeshObject);
            }
        }

        ImGui.end();
    }
}
