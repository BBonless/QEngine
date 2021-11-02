package Root.Objects.Components;

import Root.Shaders.ShaderProgram;
import imgui.ImGui;

public class Debug_Component extends Component {
    public int Counter = 0;

    public Debug_Component() {
        Name = "Debug";
        Type = ComponentManager.ComponentType.Debug;
    }

    @Override
    public void Upload(ShaderProgram Shader) {}

    @Override
    public void InternalGUI() {
        ImGui.text("Debug! " + Counter);
        if (ImGui.button("-1")) {
            Counter--;
        }

        ImGui.sameLine();
        if (ImGui.button("+1")) {
            Counter++;
        }
    }

    @Override
    public void InternalUpdate() {

    }

    @Override
    public void Update() {

    }
}
