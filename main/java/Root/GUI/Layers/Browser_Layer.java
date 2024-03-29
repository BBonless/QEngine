package Root.GUI.Layers;

import Root.GUI.Layer;
import Root.Geometry.*;
import Root.Misc.Structures.ObjectTree;
import Root.Objects.Components.Component;
import Root.Objects.Components.ComponentManager;
import Root.Objects.ObjectManager;
import Root.Objects.WorldObject;
import Root.Textures.Texture;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import imgui.type.ImString;
import org.joml.Math;

import static Root.IO.File.Load.Load;
import static Root.IO.File.Save.Save;
import static Root.Objects.Components.ComponentManager.IsComponentUserAddable;
import static Root.Objects.ObjectManager.AddObject;
import static Root.Objects.ObjectManager.Tree;
import static org.lwjgl.assimp.Assimp.aiProcess_JoinIdenticalVertices;
import static org.lwjgl.assimp.Assimp.aiProcess_Triangulate;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;

public class Browser_Layer implements Layer {

    private ImInt CurrentChildIndex = new ImInt(0);

    //Represents the Object the user currently has selected, initially it is set to the Root object.
    public static ObjectTree CurrentObject = ObjectManager.Tree;
    public static Component CurrentOpenComponent = null;

    private WorldObject NewbornObject;
    private ImString NewbornObjectName = new ImString(256);
    private ImString NewbornObjectImportFilepath = new ImString(256);

    private boolean AddComponent = false;
    private ImInt ComponentTypeIndex = new ImInt(0);

    //Icon for the Rename button
    private Texture EditTexture;

    public Browser_Layer() {
        EditTexture = new Texture("/Icons/Edit32.png");
    }

    private void SelectNewObject(ObjectTree NewObject) {
        CurrentObject = NewObject;

        CurrentChildIndex.set(0);
    }

    private boolean ValidateObjectCreation() {
        //Check if name is error message
        if (NewbornObjectName.get().contains("Name cannot be blank!") ||
            NewbornObjectName.get().contains("Name already exists!"))
        {
            return false;
        }

        //Check if name is blank
        if (NewbornObjectName.get().isBlank())
        {
            NewbornObjectName.set("Name cannot be blank!");
            return false;
        }

        //Check if name already exists, otherwise name is valid
        if (ObjectManager.Tree.ValidateName(NewbornObjectName.get())) {
            if (NewbornObject != null) {
                NewbornObject.Name = NewbornObjectName.get();
            }
            return true;
        } else {
            NewbornObjectName.set("Name already exists!");
            return false;
        }
    }

    private void CancelObjectCreation() {
        System.out.println("Test");
        ObjectManager.DeleteObject(NewbornObject);
        NewbornObject = null;
        ImGui.closeCurrentPopup();
    }

    private void CleanupObjectCreation() {
        NewbornObjectImportFilepath.set("");

        NewbornObjectName.clear();
        NewbornObject = null;
        ImGui.closeCurrentPopup();
    }

    private void Popups_ComponentAlreadyExists() {
        if (ImGui.beginPopupModal("Component Already Exists", ImGuiWindowFlags.NoSavedSettings)) {
            ImGui.text("This component already exists on this object!");
            ImGui.endPopup();
        }
    }

    private void Popups_AddObject() {
        if (ImGui.beginPopupModal("New Object", ImGuiWindowFlags.NoSavedSettings)) {
            ImGui.text("Enter Name:");

            ImGui.pushItemWidth(160);
            ImGui.inputText("", NewbornObjectName);
            if (ImGui.isKeyPressed(GLFW_KEY_ENTER)) {
                if (ValidateObjectCreation()) {
                    CleanupObjectCreation();
                }
            }
            ImGui.popItemWidth();

            if (ImGui.button("Add")) {
                if (ValidateObjectCreation()) {
                    CleanupObjectCreation();
                }
            }

            ImGui.sameLine();
            if (ImGui.button("Cancel")) {
                CancelObjectCreation();
            }

            ImGui.endPopup();
        }
    }

    private void Popups_RenameObject() {
        if (ImGui.beginPopupModal("Rename Object", ImGuiWindowFlags.NoSavedSettings)) {
            ImGui.text("Enter New Name:");

            ImGui.pushItemWidth(160);
            ImGui.inputText("", NewbornObjectName);
            if (ImGui.isKeyPressed(GLFW_KEY_ENTER)) {
                if (ValidateObjectCreation()) {
                    CurrentObject.Element.Name = NewbornObjectName.get();
                    CleanupObjectCreation();
                }
            }
            ImGui.popItemWidth();

            if (ImGui.button("Rename")) {
                if (ValidateObjectCreation()) {
                    CurrentObject.Element.Name = NewbornObjectName.get();
                    CleanupObjectCreation();
                }
            }

            ImGui.sameLine();
            if (ImGui.button("Cancel")) {
                ImGui.closeCurrentPopup();
            }

            ImGui.endPopup();
        }
    }

    private void Popups_ImportObject() {
        if (ImGui.beginPopupModal("Import Object", ImGuiWindowFlags.NoSavedSettings)) {
            ImGui.text("Enter Name:");

            ImGui.pushItemWidth(160);
            ImGui.inputText("", NewbornObjectName);
            if (ImGui.isKeyPressed(GLFW_KEY_ENTER)) {
                if (ValidateObjectCreation()) {
                    CleanupObjectCreation();
                }
            }
            ImGui.popItemWidth();

            ImGui.pushItemWidth(400);

            ImGui.separator();
            ImGui.inputText("Model Path", NewbornObjectImportFilepath);
            if (ImGui.button("Load") || ImGui.isKeyPressed(GLFW_KEY_ENTER)) {
                Mesh[] ImportedMesh = MeshLoader.Import(
                        NewbornObjectImportFilepath.get(),
                        aiProcess_JoinIdenticalVertices | aiProcess_Triangulate
                );

                if (ImportedMesh == null) {
                    NewbornObjectImportFilepath.set("File not found!");
                }
                else {
                    if (ImportedMesh.length > 1) {
                        ObjectTree CurrentObjectAux = CurrentObject;
                        System.out.println(NewbornObject == null);
                        System.out.println(NewbornObject.Container == null);
                        SelectNewObject(NewbornObject.Container);
                        for (int i = 0; i < ImportedMesh.length; i++) {
                            WorldObject MeshPart = ObjectManager.AddObject(ObjectManager.ObjectType.Empty);

                            MeshPart.Name = "Part" + i;

                            MeshPart.Mesh = ImportedMesh[i];
                        }
                        SelectNewObject(CurrentObjectAux);
                    } else {
                        NewbornObject.Mesh = ImportedMesh[0];
                    }
                    NewbornObjectImportFilepath.set("Mesh loaded successfully!");
                }
            }

            ImGui.popItemWidth();

            if (ImGui.button("Add")) {
                if (ValidateObjectCreation()) {
                    CleanupObjectCreation();
                }
            }

            ImGui.sameLine();
            if (ImGui.button("Cancel")) {
                CancelObjectCreation();
            }
            ImGui.endPopup();
        }
    }

    private void Popups_AddComponent() {
        if (ImGui.beginPopupModal("Add Component", ImGuiWindowFlags.NoSavedSettings)) {
            AddComponent = false;
            ImGui.pushItemWidth(260);
            ImGui.combo("Type", ComponentTypeIndex, ComponentManager.ComponentTypeList);
            ImGui.popItemWidth();
            if (ImGui.button("Add")) {
                ComponentManager.AddComponent(CurrentObject.Element, ComponentTypeIndex.get());
                ComponentTypeIndex.set(0);
                ImGui.closeCurrentPopup();
            }
            ImGui.sameLine();
            if (ImGui.button("Cancel")) {
                ImGui.closeCurrentPopup();
            }
            ImGui.endPopup();
        }
    }

    private void Popups_ImGUI() {

        Popups_AddObject();

        Popups_ImportObject();

        Popups_AddComponent();

        Popups_RenameObject();
    }

    Component ComponentChoppingBlock = null;

    @Override
    public void Render_ImGUI() {
        ImGui.begin("Object Browser", new ImBoolean(true), ImGuiWindowFlags.MenuBar);

        boolean CurrentObjHasChildren = !CurrentObject.Children.isEmpty();
        boolean CurrentObjIsLight = CurrentObject.Element.Name == "Light";
        boolean CurrentObjIsRoot = CurrentObject.Parent == null;

        Popups_ImGUI();

        if (ImGui.beginMenuBar()) {
            if (ImGui.beginMenu("File")) {
                if (ImGui.menuItem("Save")) {
                    Save("C:\\Users\\quent\\Desktop\\Scene.qes");
                }
                if (ImGui.menuItem("Load")) {
                    Load("C:\\Users\\quent\\Desktop\\Scene.qes");
                }
                ImGui.endMenu();
            }

            if (ImGui.beginMenu("Object")) {
                if (ImGui.beginMenu("Create")) {
                    if (ImGui.menuItem("Cube")) {
                        NewbornObject = AddObject(ObjectManager.ObjectType.DebugBox);
                    }
                    if(ImGui.menuItem("Sphere")) {
                        NewbornObject = AddObject(ObjectManager.ObjectType.Sphere);
                    }
                    if(ImGui.menuItem("Empty")) {
                        NewbornObject = AddObject(ObjectManager.ObjectType.Empty);
                    }

                    ImGui.separator();

                    if (ImGui.menuItem("Import 3D Mesh")) {
                        NewbornObject = AddObject(ObjectManager.ObjectType.Import);
                    }

                    ImGui.endMenu();
                }

                if (!CurrentObjIsRoot && !CurrentObjIsLight) {
                    if (ImGui.menuItem("Delete Current Object")) {
                        WorldObject ToDelete = CurrentObject.Element;
                        SelectNewObject(CurrentObject.Parent);
                        ObjectManager.DeleteObject(ToDelete);
                    }
                }

                if (ImGui.beginMenu("Delete Component")) {
                    if (CurrentObject.Element.Components.size() > 2 && !CurrentObjIsLight && !CurrentObjIsRoot) {
                        for (Component C : CurrentObject.Element.Components) {
                            if (IsComponentUserAddable(C)) {
                                if (ImGui.menuItem(C.Name)) {
                                    ComponentChoppingBlock = C;
                                }
                            }
                        }

                        ImGui.separator();
                        if (IsComponentUserAddable(CurrentOpenComponent)) {
                            if (ImGui.menuItem("Current Open Component")) {
                                CurrentOpenComponent.Delete();
                            }
                        }
                    }
                    else {
                        ImGui.menuItem("No Components to Delete!");
                    }

                    ImGui.endMenu();
                }

                ImGui.endMenu();
            }

            ImGui.endMenuBar();
        }

        if (ComponentChoppingBlock != null) {
            ComponentChoppingBlock.Delete();
            ComponentChoppingBlock = null;
        }

        if (NewbornObject != null) {
            ImGui.openPopup(NewbornObject.Name);
        }

        if (AddComponent) {
            ImGui.openPopup("Add Component");
        }

        ImGui.text("Current Object: "); ImGui.sameLine(); ImGui.pushItemWidth(ImGui.getWindowSizeX() / 2);
        ImGui.inputText("", new ImString(CurrentObject.Element.Name), ImGuiInputTextFlags.ReadOnly);
        ImGui.popItemWidth();

        //If the current object is the root or a light
        ImGui.sameLine();
        if (CurrentObjIsRoot || CurrentObjIsLight) {
            ImGui.pushStyleColor(ImGuiCol.Button, 0.25f, 0.25f, 0.25f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.25f, 0.25f, 0.25f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.25f, 0.25f, 0.25f, 1.0f);
            if (ImGui.imageButton(EditTexture.Handle, 14, 14));
            ImGui.popStyleColor(3);
        }
        else {
            if (ImGui.imageButton(EditTexture.Handle, 14, 14)) {
                ImGui.openPopup("Rename Object");
            }
        }


        ImGui.listBox("Children", CurrentChildIndex, GetChildNodeArray(CurrentObject) );

        if (CurrentObjHasChildren && CurrentChildIndex.get() < CurrentObject.Children.size()) {
            if (ImGui.button("Forward")) {
                SelectNewObject(CurrentObject.Children.get(CurrentChildIndex.get()));
            }
        }
        else {
            ImGui.pushStyleColor(ImGuiCol.Button, 0.25f, 0.25f, 0.25f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.25f, 0.25f, 0.25f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.25f, 0.25f, 0.25f, 1.0f);
            ImGui.button("Forward");
            ImGui.popStyleColor(3);
        }

        ImGui.sameLine();
        if (CurrentObjIsRoot) {
            ImGui.pushStyleColor(ImGuiCol.Button, 0.25f, 0.25f, 0.25f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.25f, 0.25f, 0.25f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.25f, 0.25f, 0.25f, 1.0f);
            ImGui.button("Back");
            ImGui.popStyleColor(3);
        }
        else {
            if (ImGui.button("Back")) {
                SelectNewObject(CurrentObject.Parent);
            }
        }

        ////////////

        if (ImGui.beginTabBar("Components")) {

            for (Component Comp : CurrentObject.Element.Components) {
                Comp.GUI();
            }

            if (IsComponentUserAddable(CurrentOpenComponent) && (CurrentObject.Element.Components.size() > 2 && !CurrentObjIsLight && !CurrentObjIsRoot)) {
                ImGui.separator();
                ImGui.pushStyleColor(ImGuiCol.Button, 0.514f, 0.224f, 0.173f, 1.0f);
                if (ImGui.button("Delete Component")) {
                    CurrentOpenComponent.Delete();
                }
                ImGui.popStyleColor();
            }

            if (!CurrentObjIsRoot && ImGui.tabItemButton("+")) {
                AddComponent = true;
            }

            ImGui.endTabBar();
        }

        ImGui.end();
    }

    private String[] GetChildNodeArray(ObjectTree Node) {
        String[] Children = new String[Math.max(Node.Children.size(), 8)];

        for (int i = 0; i < Children.length; i++) {
            if (i < Node.Children.size()) {
                String ChildName = Node.Children.get(i).Element.Name;
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
