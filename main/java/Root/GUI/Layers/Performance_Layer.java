package Root.GUI.Layers;

import Root.GUI.Layer;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;

import java.awt.*;
import java.text.DecimalFormat;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Performance_Layer implements Layer {

    private static int FrameCounter = 0;
    private static double PreviousSecondInterval = 0;
    private static double PreviousFrameTime = 0;
    private static StringBuilder FPSStringBuilder = new StringBuilder("Frames per Second: ");
    private static StringBuilder DTStringBuilder = new StringBuilder("Deltatime: ");

    public static int FPS = 0;
    public static double Deltatime = 0;

    private static DecimalFormat DF = new DecimalFormat("##.####");

    public static void TrackFPS() {
        double CurrentFrameTime = glfwGetTime();

        FrameCounter++;

        Deltatime = CurrentFrameTime - PreviousFrameTime;
        PreviousFrameTime = CurrentFrameTime;

        if (CurrentFrameTime - PreviousSecondInterval > 1) {
            PreviousSecondInterval = glfwGetTime();

            FPSStringBuilder.setLength(0);
            FPSStringBuilder.append("Frames per Second: ");
            FPSStringBuilder.append(FrameCounter);

            DTStringBuilder.setLength(0);
            DTStringBuilder.append("Deltatime: ");
            DTStringBuilder.append(DF.format(Deltatime));

            FPS = FrameCounter;

            FrameCounter = 0;
        }

    }

    @Override
    public void Render_ImGUI() {
        ImGui.begin("Performance");

        ImGui.text(FPSStringBuilder.toString());

        ImGui.text(DTStringBuilder.toString());

        ImGui.end();
    }
}
