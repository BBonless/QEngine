package Root.Engine.Rendering;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    public static Vector3f Position = new Vector3f(0,0,0);
    public static Vector3f Rotation = new Vector3f(0,0,0);

    public static final float BaseSpeed = 50f;
    public static float Speed = 50f;
    public static float Sensitivity = 0.25f;

    private static Matrix4f ProjectionMatrix = new Matrix4f();
    private static Vector2f Dimensions = new Vector2f(1, 1);
    private static float FieldOfView = 45f;
    private static float NearClipping = 0.1f;
    private static float FarClipping = 1000f;

    public static Matrix4f GetViewMatrix() {
        Matrix4f ViewMatrix = new Matrix4f().identity();

        ViewMatrix.rotateX((float) Math.toRadians(Rotation.x));
        ViewMatrix.rotateY((float) Math.toRadians(Rotation.y));
        ViewMatrix.rotateZ((float) Math.toRadians(Rotation.z));

        ViewMatrix.translate(new Vector3f(Position).mul(-1));

        //ViewMatrix.invert();

        return ViewMatrix;
    }

    public static Matrix4f GetProjectionMatrix() {
        return ProjectionMatrix;
    }

    public static Vector3f GetForwardVector() {
        Matrix4f InvertedView = new Matrix4f();
        Vector3f CameraForward = new Vector3f();

        GetViewMatrix().invert(InvertedView);
        InvertedView.getColumn(2, CameraForward);

        return CameraForward.normalize().mul(-1);
    }

    public static Vector3f GetUpVector() {
        Matrix4f InvertedView = new Matrix4f();
        Vector3f CameraForward = new Vector3f();

        GetViewMatrix().invert(InvertedView);
        InvertedView.getColumn(1, CameraForward);

        return CameraForward.normalize();
    }

    public static Vector3f GetRightVector() {
        Matrix4f InvertedView = new Matrix4f();
        Vector3f CameraForward = new Vector3f();

        GetViewMatrix().invert(InvertedView);
        InvertedView.getColumn(0, CameraForward);

        return CameraForward.normalize();
    }

    public static void SetDimensions(Vector2f NewDimensions) {
        Dimensions = NewDimensions;

        RefreshProjectionMatrix();
    }

    public static void SetFOV(float NewFOV) {
        FieldOfView = NewFOV;

        RefreshProjectionMatrix();
    }

    public static void SetNearClipPlane(float NewNearClipping) {
        NearClipping = NewNearClipping;

        RefreshProjectionMatrix();
    }

    public static void SetFarClipPlane(float NewFarClipping) {
        FarClipping = NewFarClipping;

        RefreshProjectionMatrix();
    }

    private static void RefreshProjectionMatrix() {
        ProjectionMatrix = new Matrix4f().identity();

        ProjectionMatrix.perspective(
                (float)Math.toRadians(FieldOfView),
                (Dimensions.x / Dimensions.y),
                NearClipping,
                FarClipping
        );
    }

    public Camera(float FOV, Vector2f Dimensions, float NearClip, float FarClip) {
        FieldOfView = FOV;
        this.Dimensions = Dimensions;
        NearClipping = NearClip;
        FarClipping = FarClip;

        RefreshProjectionMatrix();
    }

}
