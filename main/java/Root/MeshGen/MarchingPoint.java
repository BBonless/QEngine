package Root.MeshGen;

import org.joml.Vector3f;

public class MarchingPoint {
    public Vector3f Position;
    public float Value;

    public MarchingPoint(Vector3f PositionIn) {
        Position = PositionIn;
        Value = 0;
    }

    public MarchingPoint(Vector3f PositionIn, float ValueIn) {
        Position = PositionIn;
        Value = ValueIn;
    }
}
