package Root.Rendering;

import Root.Objects.Geometry.Mesh;
import Root.Objects.Geometry.SphereMesh;
import Root.Shaders.Material;
import Root.Objects.WorldObject;
import org.joml.Vector3f;

import java.util.Stack;

public class Gizmo {
    public static Stack<WorldObject> Gizmos = new Stack<>();

    private static Mesh SphereGizmoMesh;
    private static Material GizmoMaterial;

    public static void Init() {
        SphereGizmoMesh = SphereMesh.Generate(1, 12, 8);

        GizmoMaterial = new Material(
                new Vector3f(0f, 1f, 0f),
                new Vector3f(0f, 1f, 0f),
                new Vector3f(0f, 0f, 0f),
                256f
        );
    }

    public static void PushSphereGizmo(Vector3f Position, float Radius) {
        WorldObject NewGizmo = new WorldObject(SphereGizmoMesh);

        NewGizmo.Material = GizmoMaterial;

        NewGizmo.SetTransform(
                Position,
                null,
                new Vector3f(Radius, Radius, Radius)
        );

        Gizmos.push(NewGizmo);
    }

    public static void PushSphereGizmo(Vector3f Position) {
        WorldObject NewGizmo = new WorldObject(SphereGizmoMesh);

        NewGizmo.Material = GizmoMaterial;

        NewGizmo.SetTransform(
                Position,
                null,
                new Vector3f(1,1,1)
        );

        Gizmos.push(NewGizmo);
    }

}
