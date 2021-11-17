package Root.Geometry;

public class BillboardMesh {

    /*public static Mesh Generate(Vector3f Position, float Size) {
        ArrayList<Float> VertexPosData = new ArrayList<>();
        float[] Points = new float[] {
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

        for (int i = 0; i < 4; i++) {
            Vector3f SquareVertex = new Vector3f(Points[(i*3)+0],Points[(i*3)+1],Points[(i*3)+2]);
            Vector3f Component1 = Camera.GetUpVector().mul(SquareVertex.y);
            Vector3f Component2 = Camera.GetRightVector().mul(SquareVertex.x);
            Vector3f WorldspaceSquareVertex = new Vector3f(SquareVertex.add(Component1.add(Component2)));

            for (int j = 0; j < 3; j++) {
                VertexPosData.add(WorldspaceSquareVertex.get(j));
            }
        }

        return new Mesh(
                Util.FloatArrList2Arr(VertexPosData),
                new float[VertexPosData.size()],
                TexCoords,
                Indices
        );

    }*/

}
