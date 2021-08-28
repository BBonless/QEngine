package Root.GUI.Layers;

import Root.GUI.Layer;
import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import org.joml.Vector3f;

import java.text.DecimalFormat;

public class Camera_Layer implements Layer {

    private static ImString CamXPos = null;
    private static ImString CamYPos = null;
    private static ImString CamZPos = null;

    private static ImString CamXRot = null;
    private static ImString CamYRot = null;
    private static ImString CamZRot = null;

    @Override
    public void Render_ImGUI() {
        ImGui.begin("Camera", new ImBoolean(false), ImGuiWindowFlags.NoResize);
        
        ImGui.text("Camera Position");
        ImGui.pushItemWidth(70);
        ImGui.sameLine();
        ImGui.inputText("X", CamXPos, ImGuiInputTextFlags.ReadOnly);
        ImGui.sameLine();
        ImGui.inputText("Y", CamYPos, ImGuiInputTextFlags.ReadOnly);
        ImGui.sameLine();
        ImGui.inputText("Z", CamZPos, ImGuiInputTextFlags.ReadOnly);
        ImGui.popItemWidth();

        ImGui.text("Camera Rotation");
        ImGui.pushItemWidth(70);
        ImGui.sameLine();
        ImGui.inputText("X", CamXRot, ImGuiInputTextFlags.ReadOnly);
        ImGui.sameLine();
        ImGui.inputText("Y", CamYRot, ImGuiInputTextFlags.ReadOnly);
        ImGui.sameLine();
        ImGui.inputText("Z", CamZRot, ImGuiInputTextFlags.ReadOnly);
        ImGui.popItemWidth();
        
        ImGui.end();
    }
    
    public static void SetCamPos(Vector3f Pos) {
        DecimalFormat DF = new DecimalFormat("##.##");
        CamXPos = new ImString(DF.format(Pos.x));
        CamYPos = new ImString(DF.format(Pos.y));
        CamZPos = new ImString(DF.format(Pos.z));
    }

    public static void SetCamRot(Vector3f Rot) {
        DecimalFormat DF = new DecimalFormat("##.##");
        CamXRot = new ImString(DF.format(Rot.x));
        CamYRot = new ImString(DF.format(Rot.y));
        CamZRot = new ImString(DF.format(Rot.z));
    }
}
