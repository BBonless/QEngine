package Root.Geometry;

public class PlaneMesh {
    public static Mesh Generate(float Size) {
        float[] VertexPos = new float[] {
                -Size, -Size, 0,
                Size, -Size, 0,
                -Size,  Size, 0,
                Size,  Size, 0
        };

        float[] TexCoords = new float[] {
                0, 0,
                0, 1,
                1, 0,
                1, 1
        };

        int[] Indices = new int[] {
                0, 1, 3,
                0, 2, 3
        };

        return new Mesh(
                VertexPos,
                new float[VertexPos.length],
                TexCoords,
                Indices
        );

    }
}
