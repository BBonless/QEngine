package Root.Objects.Components;

import Root.Misc.Util.Util;
import Root.Objects.WorldObject;

import java.util.ArrayList;

public class ComponentManager {

    public enum ComponentType {
        Transform,
        Shading,
        Light,
        Inflow,
        Outflow,
        Collision,
        Debug,
        None
    }

    public static String[] ComponentTypeList = new String[] {
            "Light Source Component",
            "Inflow Component",
            "Outflow Component",
            "Collision Component",
            "Debug Component"
    };

    public static boolean IsComponentUserAddable(Component Target) {
        return Target != null ? (Target.Type != ComponentType.Transform && Target.Type != ComponentType.Shading) : false;
    }

    public static void AddComponent(WorldObject Target, int Type) {
        switch (Type) {
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
}
