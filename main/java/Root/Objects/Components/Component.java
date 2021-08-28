package Root.Objects.Components;

import Root.GUI.Layers.Browser_Layer;
import Root.Objects.WorldObject;
import Root.Shaders.ShaderProgram;
import imgui.ImGui;
import imgui.type.ImString;

public abstract class Component {
    protected WorldObject Parent;

    public String Name = "No Name";

    abstract public void Upload(ShaderProgram Shader);

    abstract public void InternalGUI();

    abstract public void InternalUpdate();

    abstract public void Update();

    private boolean OpenOptions = false;
    private ImString NameValue = new ImString(64);
    public void GUI() {
        if (ImGui.beginPopup("Options")) {
            NameValue.clear();
            ImGui.text("Rename Component");
            ImGui.inputText("", NameValue);
            if (ImGui.isItemDeactivatedAfterEdit()) {
                Name = ValidateName(NameValue.get());
                ImGui.closeCurrentPopup();
            }

            if (ImGui.button("Remove Component")) {
                Browser_Layer.DeletionQueue.add(this);
                ImGui.closeCurrentPopup();
            }
            ImGui.endPopup();
        }

        if (ImGui.beginTabItem(Name)) {
            if (ImGui.isMouseDoubleClicked(0) && ImGui.isItemHovered()) {
                OpenOptions = true;
            }
            InternalGUI();
            ImGui.endTabItem();
        }

        if (OpenOptions) {
            OpenOptions = false;
            ImGui.openPopup("Options");
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
