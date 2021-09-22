package Root.GUI.Layers;

import Root.GUI.Layer;
import Root.Misc.Util.MathU;
import Root.Misc.Util.Util;
import Root.Rendering.Gizmo;
import imgui.ImGui;
import imgui.type.ImBoolean;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
import org.joml.Vector3f;

public class Test_Layer implements Layer {

    float[] LineStart = new float[] {-1.1f,-0.23f,0.25f};
    float[] LineEnd = new float[] {1,0,-1};

    float[] TriVert1 = new float[] {-1.3f,0.88f,-1f};
    float[] TriVert2 = new float[] {-0.09f,0.41f,0.48f};
    float[] TriVert3 = new float[] {0.41f,-1.29f,-0.46f};

    float[] Intersection = new float[3];
    float[] FloatSignValues = new float[3];
    int[] SignValues = new int[3];
    boolean InTri = false;

    @Override
    public void Render_ImGUI() {
        ImGui.begin("Test", new ImBoolean(true));

        Gizmo.PushSphereGizmo(new Vector3f(
                TriVert1[0],TriVert1[1],TriVert1[2]
        ), 0.05f);
        Gizmo.PushSphereGizmo(new Vector3f(
                TriVert2[0],TriVert2[1],TriVert2[2]
        ), 0.05f);
        Gizmo.PushSphereGizmo(new Vector3f(
                TriVert3[0],TriVert3[1],TriVert3[2]
        ), 0.05f);
        Gizmo.PushSphereGizmo(new Vector3f(
                LineStart[0],LineStart[1],LineStart[2]
        ), 0.05f);
        Gizmo.SetGizmoColor(new Vector3f(0,0,1));
        Gizmo.PushSphereGizmo(new Vector3f(
                LineEnd[0],LineEnd[1],LineEnd[2]
        ), 0.05f);
        Gizmo.SetGizmoColor(new Vector3f(1,0,0));

        ImGui.dragFloat3("Line Start", LineStart);
        ImGui.dragFloat3("Line End", LineEnd);
        ImGui.separator();
        ImGui.dragFloat3("Triangle Vert 1", TriVert1);
        ImGui.dragFloat3("Triangle Vert 2", TriVert2);
        ImGui.dragFloat3("Triangle Vert 3", TriVert3);
        ImGui.separator();
        if (ImGui.button("Calculate")) {
            InTri = MathU.RaycastTri(
                    Util.FloatArrToVec(LineStart),
                    Util.FloatArrToVec(LineEnd),
                    Util.FloatArrToVec(TriVert1),
                    Util.FloatArrToVec(TriVert2),
                    Util.FloatArrToVec(TriVert3)
            );
        }
        ImGui.dragFloat3("Result", Intersection);
        ImGui.dragFloat3("Solver Values", FloatSignValues);
        ImGui.dragInt4("Signs", SignValues);
        ImGui.checkbox("Intersects Triangle?", InTri);

        ImGui.end();
    }
}
