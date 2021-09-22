package Root.GUI.Layers;

import Root.GUI.Layer;
import Root.Geometry.Mesh;
import Root.Misc.Util.PrintU;
import Root.Misc.Util.Util;
import Root.Rendering.Gizmo;
import imgui.internal.ImGui;
import imgui.type.ImBoolean;
import org.joml.Vector3f;

import java.util.ArrayList;

public class Test2_Layer implements Layer {

    ImBoolean Draw = new ImBoolean(false);
    ImBoolean DrawAll = new ImBoolean(false);
    int[] TriIndex = new int[] {0};

    float[] TriData;

    public static ArrayList<Vector3f> IntersectionPoints = new ArrayList<>();

    @Override
    public void Render_ImGUI() {
        ImGui.begin("Test 2");

        if (ImGui.button("Reload")) {
            D();
        }

        ImGui.checkbox("Draw", Draw);
        ImGui.checkbox("Draw All", DrawAll);
        ImGui.dragInt("Triangle Index", TriIndex);

        if (Draw.get()) {
            D2();
        }

        for(Vector3f p : IntersectionPoints) {
            Gizmo.PushSphereGizmo(p, 0.04f);
            Gizmo.SetGizmoColor(new Vector3f(0,0,0));
        }

        ImGui.end();
    }

    private void D() {
        Mesh M = Browser_Layer.CurrentObject.Element.Mesh;

        TriData = new float[M.Indices.length * 3];

        int Count = 0;
        for(int Index : M.Indices) {
            TriData[Count++] = M.VertexData[(8 * Index) + 0];
            TriData[Count++] = M.VertexData[(8 * Index) + 1];
            TriData[Count++] = M.VertexData[(8 * Index) + 2];
        }
    }

    private void D2() {
        if (DrawAll.get()) {
            for (int i = 0; i < TriData.length; i+=9) {
                Vector3f Vert1 = new Vector3f(TriData[i+0],TriData[i+1],TriData[i+2]);
                Vector3f Vert2 = new Vector3f(TriData[i+3],TriData[i+4],TriData[i+5]);
                Vector3f Vert3 = new Vector3f(TriData[i+6],TriData[i+7],TriData[i+8]);

                Gizmo.PushSphereGizmo(Vert1, 0.01f);
                Gizmo.PushSphereGizmo(Vert2, 0.01f);
                Gizmo.PushSphereGizmo(Vert3, 0.01f);
            }
        }
        else {
            if (TriIndex[0] >= 0 && TriIndex[0] <= TriData.length / 9) {
                Vector3f Vert1 = new Vector3f(TriData[(TriIndex[0]*9)+0],TriData[(TriIndex[0]*9)+1],TriData[(TriIndex[0]*9)+2]);
                Vector3f Vert2 = new Vector3f(TriData[(TriIndex[0]*9)+3],TriData[(TriIndex[0]*9)+4],TriData[(TriIndex[0]*9)+5]);
                Vector3f Vert3 = new Vector3f(TriData[(TriIndex[0]*9)+6],TriData[(TriIndex[0]*9)+7],TriData[(TriIndex[0]*9)+8]);

                Gizmo.PushSphereGizmo(Vert1, 0.01f);
                Gizmo.PushSphereGizmo(Vert2, 0.01f);
                Gizmo.PushSphereGizmo(Vert3, 0.01f);
            }
        }
    }
}
