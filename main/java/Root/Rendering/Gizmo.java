package Root.Rendering;

import Root.Geometry.Mesh;
import Root.Geometry.SphereMesh;
import Root.Shaders.Material;
import Root.Objects.WorldObject;
import org.joml.Vector3f;

import java.util.Stack;

public class Gizmo {
    public static Stack<WorldObject> Gizmos = new Stack<>();

    private static Mesh SphereGizmoMesh;
    public static Material Material;

    public static void Init() {
        SphereGizmoMesh = SphereMesh.Generate(1, 12, 8);

        Material = new Material(
                new Vector3f(0f, 1f, 0f),
                new Vector3f(0f, 1f, 0f),
                new Vector3f(0f, 0f, 0f),
                256f
        );
    }

    public static void PushSphereGizmo(Vector3f Position, float Radius) {
        WorldObject NewGizmo = new WorldObject(SphereGizmoMesh);

        NewGizmo.Material = Gizmo.Material;

        NewGizmo.SetTransform(
                Position,
                null,
                new Vector3f(Radius, Radius, Radius)
        );

        NewGizmo.Name = "#FILLGIZMO";

        Gizmos.push(NewGizmo);
    }

    public static void PushLineGizmo(Vector3f Point1, Vector3f Point2) {
        Mesh LineGizmoMesh = new Mesh(
            new float[] {
                    Point1.x, Point1.y, Point1.z,
                    Point2.x, Point2.y, Point2.z
            },
            new int[] {
                    0, 1
            }
        );

        WorldObject NewGizmo = new WorldObject(LineGizmoMesh);

        NewGizmo.Material = Material;

        NewGizmo.Name = "#WIREGIZMO";

        Gizmos.push(NewGizmo);
    }

    public static void SetGizmoColor(Vector3f Color) {
        Gizmos.peek().Material = new Material(
                Color,
                Color,
                new Vector3f(0f, 0f, 0f),
                256f
        );
    }

}
