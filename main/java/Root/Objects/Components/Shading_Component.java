package Root.Objects.Components;

import Root.Misc.Util.Util;
import Root.Objects.WorldObject;
import Root.Shaders.ShaderManager;
import Root.Shaders.ShaderProgram;
import imgui.ImGui;
import imgui.flag.ImGuiSliderFlags;
import imgui.type.ImFloat;
import imgui.type.ImInt;

public class Shading_Component extends Component {

    public float[] AmbientValue;
    public float[] DiffuseValue;
    public float[] SpecularValue;
    public ImFloat ShininessValue;

    private ImInt ShaderIndex = new ImInt(0);

    public Shading_Component() {
        Name = "Shading";
        Type = ComponentManager.ComponentType.Shading;
    }

    @Override
    public void Attach(WorldObject Target) {
        super.Attach(Target);
        AmbientValue = Util.VecToFloatArr(Parent.Material.Ambient);
        DiffuseValue = Util.VecToFloatArr(Parent.Material.Diffuse);
        SpecularValue = Util.VecToFloatArr(Parent.Material.Specular);
        ShininessValue = new ImFloat(Parent.Material.Shininess);
    }

    @Override
    public void Upload(ShaderProgram Shader) {

    }

    @Override
    public void InternalGUI() {
        if (ImGui.combo("Shader", ShaderIndex, ShaderManager.GetShaderList())) {
            Parent.Shader = ShaderManager.Shaders.get(ShaderManager.GetShaderList()[ShaderIndex.get()]);
        }

        if (ImGui.colorEdit3("Ambient", AmbientValue)) {
            Parent.Material.Ambient = Util.FloatArrToVec(AmbientValue);
        }

        if (ImGui.colorEdit3("Diffuse", DiffuseValue)) {
            Parent.Material.Diffuse = Util.FloatArrToVec(DiffuseValue);
        }

        if(ImGui.colorEdit3("Specular", SpecularValue)) {
            Parent.Material.Specular = Util.FloatArrToVec(SpecularValue);
        }

        if (ImGui.dragFloat("Shininess", ShininessValue.getData(), 0.1f, 0.0f, 256f, "%.1f", ImGuiSliderFlags.Logarithmic)) {
            Parent.Material.Shininess = ShininessValue.get();
        }
    }

    @Override
    public void InternalUpdate() {

    }

    @Override
    public void Update() {

    }
}
