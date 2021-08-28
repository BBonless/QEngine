package Root.Objects.Geometry;

public class CubeMesh {
    private static float[] VertexData = new float[] {
            -1,  1, -1,  0,  0, -1,  0,  1, //0
            1,  1, -1,  0,  0, -1,  1,  1,
            -1, -1, -1,  0,  0, -1,  0,  0,
            1, -1, -1,  0,  0, -1,  1,  0,

            1,  1, -1,  1,  0,  0,  1,  0, //4
            1,  1,  1,  1,  0,  0,  1,  1,
            1, -1, -1,  1,  0,  0,  0,  0,
            1, -1,  1,  1,  0,  0,  0,  1,

            -1,  1,  1,  0,  0,  1,  0,  1, //8
            1,  1,  1,  0,  0,  1,  1,  1,
            -1, -1,  1,  0,  0,  1,  0,  0,
            1, -1,  1,  0,  0,  1,  1,  0,

            -1,  1, -1, -1,  0,  0,  1,  0, //12
            -1,  1,  1, -1,  0,  0,  1,  1,
            -1, -1, -1, -1,  0,  0,  0,  0,
            -1, -1,  1, -1,  0,  0,  0,  1,

            -1,  1, -1,  0,  1,  0,  0,  0, //16
            1,  1,  1,  0,  1,  0,  1,  1,
            1,  1, -1,  0,  1,  0,  1,  0,
            -1,  1,  1,  0,  1,  0,  0,  1,

            -1, -1, -1,  0, -1,  0,  0,  0, //20
            1, -1,  1,  0, -1,  0,  1,  1,
            1, -1, -1,  0, -1,  0,  1,  0,
            -1, -1,  1,  0, -1,  0,  0,  1

    };

    private static int[] IndexData = new int[] {
            0, 1, 3,
            0, 2, 3,

            4, 5, 7,
            4, 6, 7,

            8, 9, 11,
            8, 10, 11,

            12, 13, 15,
            12, 14, 15,

            19, 17, 18,
            19, 16, 18,

            23, 21, 22,
            23, 20, 22
    };

    public static Mesh Generate() {
        return new Mesh(VertexData, IndexData);
    }
}
