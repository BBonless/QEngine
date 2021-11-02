package Root.Objects.Components;

import Root.GUI.Layers.Browser_Layer;
import Root.Misc.Util.Util;
import Root.Objects.ObjectManager;
import Root.Objects.WorldObject;
import Root.Shaders.ShaderProgram;
import imgui.ImGui;

public class Transform_Component extends Component {
    public float[] PositionValue;
    public float[] RotationValue;
    public float[] ScaleValue;

    public Transform_Component() {
        Name = "Transform";
        Type = ComponentManager.ComponentType.Transform;
    }

    @Override
    public void Attach(WorldObject Target) {
        super.Attach(Target);
        PositionValue = Util.VecToFloatArr(Parent.Position);
        RotationValue = Util.VecToFloatArr(Parent.Rotation);
        ScaleValue = Util.VecToFloatArr(Parent.Scale);
    }

    @Override
    public void Upload(ShaderProgram Shader) {

    }

    @Override
    public void InternalGUI() {
        if (ImGui.dragFloat3("Position", PositionValue, 0.01f)) {
            Parent.Position = Util.FloatArrToVec(PositionValue);
            ObjectManager.ComponentInternalUpdate(Parent);
        }

        if (ImGui.dragFloat3("Rotation", RotationValue, 0.01f)) {
            Parent.Rotation = Util.FloatArrToVec(RotationValue);
            ObjectManager.ComponentInternalUpdate(Parent);
        }

        if (ImGui.dragFloat3("Scale", ScaleValue, 0.01f)) {
            Parent.Scale = Util.FloatArrToVec(ScaleValue);
            ObjectManager.ComponentInternalUpdate(Parent);
        }
    }

    @Override
    public void InternalUpdate() {

    }

    @Override
    public void Update() {

    }
}
