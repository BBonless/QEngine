package Root.Geometry;

import Root.Misc.Util.MathU;
import Root.Objects.WorldObject;
import org.joml.Vector3f;

public class Operations {

    public static int Orient3D(Vector3f... Points) {
        //Max 4 Points
        //"Computes the sign of the signed volume of the tetrahedron Points"
        //https://stackoverflow.com/questions/44513525/testing-whether-a-3d-point-is-inside-a-3d-polyhedron
        Vector3f BmA = new Vector3f();
        Vector3f CmA = new Vector3f();
        Vector3f DmA = new Vector3f();

        Vector3f Cross = new Vector3f();

        Points[1].sub(Points[0], BmA);
        Points[2].sub(Points[0], CmA);
        Points[3].sub(Points[0], DmA);

        BmA.cross(CmA, Cross);
        if ( Cross.dot(DmA) >= 0) {
            return 1;
        }
        else {
            return -1;
        }

    }

    public static boolean IntersectsTriangle(Vector3f RayStart, Vector3f RayEnd, Vector3f[] Triangle) {
        Vector3f TriPoint1 = Triangle[0];
        Vector3f TriPoint2 = Triangle[1];
        Vector3f TriPoint3 = Triangle[2];

        int S1 = Orient3D(
                RayStart,
                TriPoint1,
                TriPoint2,
                TriPoint3
        );
        System.out.print(S1 + " ");
        int S2 = Orient3D(
                RayEnd,
                TriPoint1,
                TriPoint2,
                TriPoint3
        );
        System.out.print(S2 + " ");

        if (S1 == S2) {
            return false;
        }
        else {

            int S3 = Orient3D(
                    RayStart,
                    RayEnd,
                    TriPoint1,
                    TriPoint2
            );
            System.out.print(S3 + " ");

            int S4 = Orient3D(
                    RayStart,
                    RayEnd,
                    TriPoint2,
                    TriPoint3
            );
            System.out.print(S4 + " ");

            int S5 = Orient3D(
                    RayStart,
                    RayEnd,
                    TriPoint3,
                    TriPoint1
            );
            System.out.print(S5 + " ");

            return (S3 == S4 && S4 == S5);

        }
    }

    public static Vector3f RayStart = new Vector3f();
    public static Vector3f RayEndX = new Vector3f();
    public static Vector3f RayEndY = new Vector3f();
    public static Vector3f RayEndZ = new Vector3f();

    public static boolean InMesh(WorldObject Object, Vector3f Point) {

        Point.get(RayStart);
        Point.add(new Vector3f(1000, 1, 1), RayEndX);
        Point.add(new Vector3f(1, 1000, 1), RayEndY);
        Point.add(new Vector3f(1, 1, 1000), RayEndZ);

        int CountX = 0;
        int CountY = 0;
        int CountZ = 0;

        //Get the Mesh's triangles
        Mesh ObjMesh = Object.Mesh;

        float[] TriData = new float[ObjMesh.Indices.length * 3];

        int Count = 0;
        for(int Index : ObjMesh.Indices) {
            TriData[Count++] = ObjMesh.VertexData[(8 * Index) + 0];
            TriData[Count++] = ObjMesh.VertexData[(8 * Index) + 1];
            TriData[Count++] = ObjMesh.VertexData[(8 * Index) + 2];
        }

        //Test Intersection on every triangle
        for (int i = 0; i < TriData.length; i+=9) {
            Vector3f Vert1 = new Vector3f(TriData[i+0],TriData[i+1],TriData[i+2]);
            Vector3f Vert2 = new Vector3f(TriData[i+3],TriData[i+4],TriData[i+5]);
            Vector3f Vert3 = new Vector3f(TriData[i+6],TriData[i+7],TriData[i+8]);

            if (MathU.RaycastTri(
                    RayStart,
                    RayEndX,
                    Vert1,
                    Vert2,
                    Vert3
            )) {CountX++;}

            if (MathU.RaycastTri(
                    RayStart,
                    RayEndY,
                    Vert1,
                    Vert2,
                    Vert3
            )) {CountY++;}

            if (MathU.RaycastTri(
                    RayStart,
                    RayEndZ,
                    Vert1,
                    Vert2,
                    Vert3
            )) {CountZ++;}
        }

        System.out.println(CountX + " " + CountY + " " + CountZ);

        if (CountX % 2 == 0 && CountX != 0) {
            return false;
        }
        else if (CountY % 2 == 0 && CountY != 0) {
            return false;
        }
        else if (CountZ % 2 == 0 && CountZ != 0) {
            return false;
        }
        else {
            return true;
        }
    }

    public static boolean InMeshX(WorldObject Object, Vector3f Point) {

        Vector3f RayStartPos = new Vector3f();
        Vector3f RayEndPos = new Vector3f();
        Vector3f RayStartNeg = new Vector3f();
        Vector3f RayEndNeg = new Vector3f();

        Point.get(RayStartPos);
        Point.add(new Vector3f(1000,0,0), RayEndPos);
        Point.sub(new Vector3f(1000,0,0), RayStartNeg);
        Point.get(RayEndNeg);

        int HitCount = 0;

        //Get the Mesh's triangles
        Mesh ObjMesh = Object.Mesh;

        float[] TriData = new float[ObjMesh.Indices.length * 3];

        int Count = 0;
        for(int Index : ObjMesh.Indices) {
            TriData[Count++] = ObjMesh.VertexData[(8 * Index) + 0];
            TriData[Count++] = ObjMesh.VertexData[(8 * Index) + 1];
            TriData[Count++] = ObjMesh.VertexData[(8 * Index) + 2];
        }

        //Test Intersection on every triangle
        for (int i = 0; i < TriData.length; i+=9) {
            Vector3f Vert1 = new Vector3f(TriData[i+0],TriData[i+1],TriData[i+2]);
            Vector3f Vert2 = new Vector3f(TriData[i+3],TriData[i+4],TriData[i+5]);
            Vector3f Vert3 = new Vector3f(TriData[i+6],TriData[i+7],TriData[i+8]);

            if (MathU.RaycastTri(
                    RayStartPos,
                    RayEndPos,
                    Vert1,
                    Vert2,
                    Vert3
            )) {HitCount++;}

            if (MathU.RaycastTri(
                    RayStartNeg,
                    RayEndNeg,
                    Vert1,
                    Vert2,
                    Vert3
            )) {HitCount++;}
        }

        System.out.print(HitCount);

        if (HitCount % 2 == 0 || HitCount == 0) {
            return false;
        }
        return true;
    }

}
