package Root.GUI.Layers;

import Root.Engine;
import Root.GUI.Layer;
import Root.Geometry.*;
import Root.Misc.Structures.ObjectTree;
import Root.Misc.Util.Util;
import Root.Objects.Components.Component;
import Root.Objects.ObjectManager;
import Root.Objects.WorldObject;
import Root.Rendering.Gizmo;
import Root.Shaders.ShaderManager;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiSliderFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import imgui.type.ImString;
import org.joml.Vector3f;

import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.Queue;

import static org.lwjgl.assimp.Assimp.aiProcess_JoinIdenticalVertices;
import static org.lwjgl.assimp.Assimp.aiProcess_Triangulate;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;

public class Browser_Layer implements Layer {

    private Queue<String> Nodes = new LinkedList<>();

    private ImInt ChildIndex = new ImInt(0);

    public static ObjectTree CurrentObject = ObjectManager.Tree;

    private float[] PositionValue = new float[] {0,0,0};
    private float[] RotationValue = new float[] {0,0,0};
    private float[] ScaleValue = new float[] {1,1,1};

    private float[] AmbientValue = new float[] {0,0,0};
    private float[] DiffuseValue = new float[] {0,0,0};
    private float[] SpecularValue = new float[] {0,0,0};
    private ImFloat ShininessValue = new ImFloat(0);

    private ImInt ShaderIndex = new ImInt(0);

    private WorldObject NewbornObject;
    private String NewObjectPopupMessage = "";
    private ImString NewbornObjectName = new ImString(256);
    private ImString NewbornObjectImportFilepath = new ImString(256);

    private boolean AddComponent = false;
    private ImInt ComponentTypeIndex = new ImInt(0);

    public static Queue<Component> DeletionQueue = new LinkedList<>();

    private void NewObjectSelected(ObjectTree NewObject) {
        CurrentObject = NewObject;

        ResetTransformValues();

        ResetMaterialValues();
    }

    private void ResetTransformValues() {
        PositionValue = Util.VecToFloatArr(CurrentObject.Element.Position);
        RotationValue = Util.VecToFloatArr(CurrentObject.Element.Rotation);
        ScaleValue = Util.VecToFloatArr(CurrentObject.Element.Scale);
    }

    private void ResetMaterialValues() {
        AmbientValue = Util.VecToFloatArr(CurrentObject.Element.Material.Ambient);
        DiffuseValue = Util.VecToFloatArr(CurrentObject.Element.Material.Diffuse);
        SpecularValue = Util.VecToFloatArr(CurrentObject.Element.Material.Specular);
        ShininessValue.set(CurrentObject.Element.Material.Shininess);
    }

    private WorldObject Objects_Add_DebugBox() {
        WorldObject NewObject = new WorldObject(CubeMesh.Generate());

        ObjectTree NewObjectNode = new ObjectTree(NewObject);

        CurrentObject.AddChild(NewObjectNode);

        Engine.RenderQueue.add(NewObject);

        return NewObject;
    }

    private WorldObject Objects_Add_Sphere() {
        WorldObject NewObject = new WorldObject(SphereMesh.Generate(1, 12, 10));

        ObjectTree NewObjectNode = new ObjectTree(NewObject);

        CurrentObject.AddChild(NewObjectNode);

        Engine.RenderQueue.add(NewObject);

        return NewObject;
    }

    private WorldObject Object_Add_Empty() {
        WorldObject NewObject = new WorldObject();

        ObjectTree NewObjectNode = new ObjectTree(NewObject);

        CurrentObject.AddChild(NewObjectNode);

        Engine.RenderQueue.add(NewObject);

        return NewObject;
    }

    private WorldObject Objects_Add_File() {
        WorldObject NewObject = new WorldObject();

        ObjectTree NewObjectNode = new ObjectTree(NewObject);

        CurrentObject.AddChild(NewObjectNode);

        Engine.RenderQueue.add(NewObject);

        return NewObject;
    }

    public Browser_Layer() {
        ResetTransformValues();
        ResetMaterialValues();
    }

    private void CancelCreateObject() {
        System.out.println("Test");
        ObjectManager.DeleteObject(NewbornObject);
        NewbornObject = null;
        ImGui.closeCurrentPopup();
    }

    private void Popups_ImGUI() {
        if (ImGui.beginPopupModal("New Object", ImGuiWindowFlags.NoSavedSettings)) {
            ImGui.text("Enter Name:");

            ImGui.pushItemWidth(160);
            ImGui.inputText("", NewbornObjectName);
            if (ImGui.isKeyPressed(GLFW_KEY_ENTER)) {
                if (ObjectManager.Tree.ValidateName(NewbornObjectName.get())) {
                    NewbornObject.Name = NewbornObjectName.get();
                    NewbornObjectName.clear();
                    NewbornObject = null;
                    ImGui.closeCurrentPopup();
                } else {
                    if (NewbornObjectName.get().isBlank()) {
                        NewbornObjectName.set("Name cannot be blank!");
                    }
                    else {
                        NewbornObjectName.set("Name already exists!");
                    }
                }
            }
            ImGui.popItemWidth();

            if (NewObjectPopupMessage.equals("Import")) {
                ImGui.pushItemWidth(400);

                ImGui.separator();
                ImGui.inputText("Model Path", NewbornObjectImportFilepath);
                if (ImGui.button("Load") || ImGui.isKeyPressed(GLFW_KEY_ENTER)) {
                    Mesh[] ImportMesh = MeshLoader.Import(
                            NewbornObjectImportFilepath.get(),
                            aiProcess_JoinIdenticalVertices | aiProcess_Triangulate
                    );

                    if (ImportMesh == null) {
                        NewbornObjectImportFilepath.set("File not found!");
                    }
                    else {
                        NewbornObject.Mesh = ImportMesh[0];
                        NewbornObjectImportFilepath.set("Mesh loaded successfully!");
                    }
                }

                ImGui.popItemWidth();
            }

            if (ImGui.button("Add")) {
                NewObjectPopupMessage = "";
                NewbornObjectImportFilepath.set("");

                if (ObjectManager.Tree.ValidateName(NewbornObjectName.get())) {
                    NewbornObject.Name = NewbornObjectName.get();
                    NewbornObjectName.clear();
                    NewbornObject = null;
                    ImGui.closeCurrentPopup();
                } else {
                    if (NewbornObjectName.get().isBlank()) {
                        NewbornObjectName.set("Name cannot be blank!");
                    }
                    else {
                        NewbornObjectName.set("Name already exists!");
                    }
                }
            }

            ImGui.sameLine();
            if (ImGui.button("Cancel")) {
                CancelCreateObject();
            }
            ImGui.endPopup();
        }

        if (ImGui.beginPopupModal("Add Component", ImGuiWindowFlags.NoSavedSettings)) {
            AddComponent = false;
            ImGui.pushItemWidth(260);
            if (ImGui.combo("Type", ComponentTypeIndex, ObjectManager.ComponentTypes)) {
                System.out.println(ObjectManager.ComponentTypes[ComponentTypeIndex.get()]);
            }
            ImGui.popItemWidth();
            if (ImGui.button("Add")) {
                ObjectManager.AddComponent(CurrentObject.Element, ComponentTypeIndex.get());
                ImGui.closeCurrentPopup();
            }
            ImGui.sameLine();
            if (ImGui.button("Cancel")) {
                ImGui.closeCurrentPopup();
            }
            ImGui.endPopup();
        }
    }

    boolean f = false;

    FloatBuffer Particles;
    FloatBuffer Neighbors;
    FloatBuffer Parameters;
    FloatBuffer OutFB;
    FloatBuffer OutPFB;
    FloatBuffer Out;

    public static float[] cum = new float[3];

    @Override
    public void Render_ImGUI() {
        ImGui.begin("Object Browser", new ImBoolean(true), ImGuiWindowFlags.MenuBar);

        boolean HasChildren = !CurrentObject.GetChildren().isEmpty();
        boolean Root = CurrentObject.Parent == null;

        Popups_ImGUI();

        if (ImGui.beginMenuBar()) {
            if (ImGui.beginMenu("Objects")) {
                if (ImGui.beginMenu("Add")) {
                    if (ImGui.menuItem("Debug Box")) {
                        NewbornObject = Objects_Add_DebugBox();
                    }
                    if(ImGui.menuItem("Sphere")) {
                        NewbornObject = Objects_Add_Sphere();
                    }
                    if(ImGui.menuItem("Empty")) {
                        NewbornObject = Object_Add_Empty();
                    }

                    ImGui.separator();

                    if (ImGui.menuItem("Import 3D Mesh")) {
                        NewbornObject = Objects_Add_File();
                        NewObjectPopupMessage = "Import";
                    }

                    ImGui.endMenu();
                }
                ImGui.endMenu();
            }

            ImGui.endMenuBar();
        }

        if (NewbornObject != null) {
            ImGui.openPopup("New Object");
        }

        if (AddComponent) {
            ImGui.openPopup("Add Component");
        }

        ImGui.text("Current Object: ");
        ImGui.sameLine();
        ImString CurrentObjectName = new ImString(256); CurrentObjectName.set(CurrentObject.Element.Name);
        if (ImGui.inputText("", CurrentObjectName, Root ? ImGuiInputTextFlags.ReadOnly : 0)) {
            CurrentObject.Element.Name = CurrentObjectName.get();
        }

        ImGui.listBox("Children", ChildIndex, GetChildNodeArray(CurrentObject) );


        if (HasChildren && ChildIndex.get() < CurrentObject.ChildCount()) {
            if (ImGui.button("Forward")) {
                NewObjectSelected(CurrentObject.GetChild(ChildIndex.get()));
            }
        }
        else {
            ImGui.pushStyleColor(ImGuiCol.Button, 0.25f, 0.25f, 0.25f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.25f, 0.25f, 0.25f, 1.0f);
            ImGui.button("Forward");
            ImGui.popStyleColor(2);
        }

        ImGui.sameLine();
        if (Root) {
            ImGui.pushStyleColor(ImGuiCol.Button, 0.25f, 0.25f, 0.25f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.25f, 0.25f, 0.25f, 1.0f);
            ImGui.button("Back");
            ImGui.popStyleColor(2);
        }
        else {
            if (ImGui.button("Back")) {
                NewObjectSelected(CurrentObject.Parent);
            }
        }


        if (ImGui.dragFloat3("Position", PositionValue, 0.01f)) {
            CurrentObject.Element.Position = Util.FloatArrToVec(PositionValue);
            ObjectManager.ComponentInternalUpdate(CurrentObject.Element);
        }

        if (ImGui.dragFloat3("Rotation", RotationValue, 0.01f)) {
            CurrentObject.Element.Rotation = Util.FloatArrToVec(RotationValue);
            ObjectManager.ComponentInternalUpdate(CurrentObject.Element);
        }

        if (ImGui.dragFloat3("Scale", ScaleValue, 0.01f)) {
            CurrentObject.Element.Scale = Util.FloatArrToVec(ScaleValue);
            ObjectManager.ComponentInternalUpdate(CurrentObject.Element);
        }

        if (ImGui.button("CUM BUTTON!!!")) {
            Test2_Layer.IntersectionPoints.clear();
            long s = System.currentTimeMillis();
            System.out.println(" " + Operations.InMeshX(CurrentObject.Element, new Vector3f(cum[0],cum[1], cum[2])) );
            System.out.println("Time Taken: " + (System.currentTimeMillis() - s) + "ms");
        }
        if (ImGui.inputFloat3("Cum", cum)) {}
        Gizmo.PushSphereGizmo(new Vector3f(cum[0],cum[1], cum[2]), 0.2f);

        if (ImGui.button("CUMSHOT ")) {

        }
        ////////////

        if (ImGui.beginTabBar("ObjectOptions")) {

            if (ImGui.beginTabItem("Shading")) {
                if (ImGui.isItemClicked()) {
                    System.out.println("Click");
                    ResetMaterialValues();
                }

                if (ImGui.combo("Shader", ShaderIndex, ShaderManager.GetShaderList())) {
                    CurrentObject.Element.Shader = ShaderManager.Shaders.get(ShaderManager.GetShaderList()[ShaderIndex.get()]);
                }

                if (ImGui.colorEdit3("Ambient", AmbientValue)) {
                    CurrentObject.Element.Material.Ambient = Util.FloatArrToVec(AmbientValue);
                }

                if (ImGui.colorEdit3("Diffuse", DiffuseValue)) {
                    CurrentObject.Element.Material.Diffuse = Util.FloatArrToVec(DiffuseValue);
                }

                if(ImGui.colorEdit3("Specular", SpecularValue)) {
                    CurrentObject.Element.Material.Specular = Util.FloatArrToVec(SpecularValue);
                }

                if (ImGui.dragFloat("Shininess", ShininessValue.getData(), 0.1f, 0.0f, 256f, "%.1f", ImGuiSliderFlags.Logarithmic)) {
                    CurrentObject.Element.Material.Shininess = ShininessValue.get();
                }

                ImGui.endTabItem();
            }

            for (Component Comp : CurrentObject.Element.Components) {
                Comp.GUI();
            }
            while (!DeletionQueue.isEmpty()) {
                DeletionQueue.poll().Delete();
            }

            if (ImGui.tabItemButton("+")) {
                AddComponent = true;
            }

            ImGui.endTabBar();
        }

        ImGui.end();
    }

    private String[] GetChildNodeArray(ObjectTree Node) {
        String[] Children = new String[Math.max(Node.ChildCount(), 8)];

        for (int i = 0; i < Children.length; i++) {
            if (i < Node.ChildCount()) {
                String ChildName = Node.GetChild(i).Element.Name;
                if (ChildName == null) {
                    ChildName = "No Name";
                }
                Children[i] = ChildName;
            }
            else {
                Children[i] = "";
            }
        }

        return Children;
    }
}
