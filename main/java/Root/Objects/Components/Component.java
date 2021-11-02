package Root.Objects.Components;

import Root.GUI.Layers.Browser_Layer;
import Root.Misc.Util.ArrayU;
import Root.Objects.ObjectManager;
import Root.Objects.WorldObject;
import Root.Shaders.ShaderProgram;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.type.ImString;

public abstract class Component {
    protected transient WorldObject Parent;

    public String Name = "No Name";
    public ComponentManager.ComponentType Type = ComponentManager.ComponentType.None;

    private boolean PendingDelete = false;

    abstract public void Upload(ShaderProgram Shader);

    abstract public void InternalGUI();

    abstract public void InternalUpdate();

    abstract public void Update();

    public void GUI() {
        if (ImGui.beginTabItem(Name)) {

            InternalGUI();

            //Only allow user-addable components to be deleted
            if (ArrayU.StrArrContains(ComponentManager.ComponentTypeList, Name)) {
                ImGui.pushStyleColor(ImGuiCol.Button, 0.514f, 0.224f, 0.173f, 1.0f);
                if (ImGui.button("Remove Component")) {
                    PendingDelete = true;
                }
                ImGui.popStyleColor();
            }


            ImGui.endTabItem();
        }


    }

    private String ValidateName(String Name) {
        String Result = Name;
        for (Component Comp : Parent.Components) {
            if (Comp.Name.equals(Name)) {
                Result = Result.concat("Copy");
            }
        }
        return Result;
    }

    public void Attach(WorldObject Target) {
        Parent = Target;
        ValidateName(Name);
        Target.Components.add(this);
    }

    public void Delete() {
        Parent.Components.remove(this);
    }
}
