package Root.GUI.Layers;

import Root.GUI.Layer;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import org.joml.Random;

import java.text.DecimalFormat;

import static Root.Environment.ISphereWO;

public class Performance_Layer implements Layer {

    private static ImString FPS;
    private static ImString FT;
    private static ImString DT;

    @Override
    public void Render_ImGUI() {
        ImGui.begin("Performance", new ImBoolean(false), ImGuiWindowFlags.NoResize);

        ImGui.text("Frames per Second: " + FPS);
        ImGui.text("Frame time: " + FT);
        ImGui.text("Delta time: " + DT);

        if (ImGui.button("Move Instances")) {
            float[] ID2 = new float[10000*3];
            Random Random = new Random();
            for (int i = 0; i < 10000*3; i++) {
                ID2[i] = (Random.nextFloat() * 200) - 100;
            }
            ISphereWO.SetInstanced(ID2);
            ISphereWO.Mesh.UpdateInstanceBuffer();
        }

        ImGui.end();
    }

    public static void SetData(float FPSIn, float DTIn) {
        DecimalFormat DF = new DecimalFormat("##.####");
        FPS = new ImString(DF.format(FPSIn));
        FT = new ImString(DF.format(1/FPSIn));
        DT = new ImString(DF.format(DTIn));
    }
}
