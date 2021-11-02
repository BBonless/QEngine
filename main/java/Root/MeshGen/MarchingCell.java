package Root.MeshGen;

import org.joml.Vector3f;

public class MarchingCell {
    public MarchingPoint[] Vertices = new MarchingPoint[8];
    public Vector3f[] Edges = new Vector3f[12];
}
