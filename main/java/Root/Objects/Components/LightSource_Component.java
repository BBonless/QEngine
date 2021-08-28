package Root.Objects.Components;

import Root.Misc.Util.Util;
import Root.Shaders.ShaderProgram;
import Root.Shaders.UniformTypes;
import imgui.ImGui;
import org.joml.Vector3f;

public class LightSource_Component extends Component {
    public Vector3f Color = new Vector3f(1,1,1);
    private float[] ColorValues = new float[] {1,1,1};

    public LightSource_Component(Vector3f ColorIn) {
        Name = "Light Source";
        Color = ColorIn;
    }

    public LightSource_Component() {
        Name = "Light Source";
    }

    public void Upload(ShaderProgram Shader) {
        Shader.SetUniform("Light.Position", Parent.Position, UniformTypes.VEC3);
        Shader.SetUniform("Light.Color", Color, UniformTypes.VEC3);
    }

    public void InternalGUI() {
        if (ImGui.colorPicker3("Light Color", ColorValues)) {
            Color = Util.FloatArrToVec(ColorValues);
        }
    }

    @Override
    public void InternalUpdate() {

    }

    @Override
    public void Update() {

    }
}
