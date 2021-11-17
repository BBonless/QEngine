package Root.Objects;

import Root.Engine.Engine;
import Root.GUI.Layers.Browser_Layer;
import Root.Geometry.CubeMesh;
import Root.Misc.Structures.ObjectTree;
import Root.Objects.Components.*;
import Root.Geometry.SphereMesh;
import Root.Engine.Rendering.Renderer;
import Root.Shaders.Material;
import org.joml.Vector3f;

public class ObjectManager {

    public enum ObjectType {
        DebugBox,
        Sphere,
        Empty,
        Import
    }

    public static ObjectTree Tree = new ObjectTree(new WorldObject("Root"));

    private static void CreateLightObject() {
        WorldObject Light = new WorldObject("Light");
        Light.Mesh = SphereMesh.Generate(1, 16, 12);
        new LightSource_Component().Attach(Light);
        Light.SetTransform(
                new Vector3f(0, 10, 0),
                null,
                null
        );
        Light.Material = new Material(
                new Vector3f(1,1,1),
                new Vector3f(1,1,1),
                new Vector3f(0.5f, 0.5f, 0.5f),
                256f
        );
        Renderer.Light = (LightSource_Component)Light.Components.get(0);
        Engine.RenderQueue.add(Light);
        Tree.AddChild(Light);
    }

    public static WorldObject AddObject(ObjectType Type) {
        WorldObject NewObject;

        switch (Type) {
            case Empty:
            case Import:
                NewObject = new WorldObject();
                break;
            case DebugBox:
                NewObject = new WorldObject(CubeMesh.Generate());
                break;
            case Sphere:
                NewObject = new WorldObject(SphereMesh.Generate(1, 12, 10));
                break;
            default:
                throw new RuntimeException("Impossible Object Type");
        }

        switch (Type) {
            case Import:
                NewObject.Name = "Import Object";
                break;
            case DebugBox:
            case Sphere:
            case Empty:
                NewObject.Name = "New Object";
                break;
        }

        ObjectTree NewObjectNode = new ObjectTree(NewObject);

        Browser_Layer.CurrentObject.AddChild(NewObjectNode);
        NewObject.Container = NewObjectNode;

        new Transform_Component().Attach(NewObject);

        new Shading_Component().Attach(NewObject);

        Engine.RenderQueue.add(NewObject);

        return NewObject;
    }

    public static void DeleteObject(WorldObject Target) {
        Engine.RenderQueue.remove(Target);

        if (Target.Container != null) {
            Target.Container.Parent.RemoveChild(Target.Container);
        }

        Target = null;
    }

    public static void ComponentInternalUpdate(WorldObject Target) {
        for (Component Comp : Target.Components) {
            Comp.InternalUpdate();
        }
    }

    public static void ComponentUpdate() {
        ComponentUpdate(Tree);
    }

    private static void ComponentUpdate(ObjectTree Node) {
        for (Component Comp : Node.Element.Components) {
            Comp.Update();
        }

        if (Node.HasChildren()) {
            for (ObjectTree Child : Node.Children) {
                ComponentUpdate(Child);
            }
        }
    }

    public static void Init() {
        CreateLightObject();
    }
}
