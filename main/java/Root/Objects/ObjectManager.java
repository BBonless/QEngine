package Root.Objects;

import Root.Engine;
import Root.Misc.Structures.ObjectTree;
import Root.Objects.Components.*;
import Root.Geometry.SphereMesh;
import Root.Rendering.Renderer;
import Root.Shaders.Material;
import org.joml.Vector3f;

public class ObjectManager {

    public static ObjectTree Tree = new ObjectTree(new WorldObject("Root"));

    public static String[] ComponentTypes = new String[] {
            "Light Source Component",
            "Inflow Component",
            "Outflow Component",
            "Collision Component",
            "Debug Component"
    };

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

    public static void DeleteObject(WorldObject Target) {
        Engine.RenderQueue.remove(Target);

        if (Target.Container != null) {
            Target.Container.Parent.RemoveChild(Target.Container);
        }

        Target = null;
    }

    public static void AddComponent(WorldObject Target, int Component) {
        switch (Component) {
            case 0:
                new LightSource_Component().Attach(Target);
                break;
            case 1:
                new Inflow_Component().Attach(Target);
                break;
            case 2:
                new Outflow_Component().Attach(Target);
                break;
            case 3:
                new Collision_Component().Attach(Target);
                break;
            case 4:
                new Debug_Component().Attach(Target);
                break;
            default:
                break;
        }
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
            for (ObjectTree Child : Node.GetChildren()) {
                ComponentUpdate(Child);
            }
        }
    }

    public static void Init() {
        CreateLightObject();
    }
}
