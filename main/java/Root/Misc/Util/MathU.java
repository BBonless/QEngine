package Root.Misc.Util;

import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
import org.joml.Vector3f;

import java.util.ArrayList;

public class MathU {

    public static ArrayList<Vector3f> PreviouslyVisitedVerts = new ArrayList<>();

    /**
     * @param LineStart Where the ray begins
     * @param LineEnd Where the ray ends
     * @param TriVert1 First point of the triangle
     * @param TriVert2 Second point of the triangle
     * @param TriVert3 Third point of the triangle
     * @return True if the ray intersects the triangle, False otherwise
     */
    public static boolean RaycastTri(Vector3f LineStart, Vector3f LineEnd, Vector3f TriVert1 /*Anchor*/, Vector3f TriVert2, Vector3f TriVert3) {
        Vector3f Cross = new Vector3f();
        float Sum1 = 0, Sum2 = 0, Ratio;

        //Calculate Cross Product
        Vector3f TriVert2Minus1 = new Vector3f();
        TriVert2.sub(TriVert1, TriVert2Minus1);

        Vector3f TriVert3Minus1 = new Vector3f();
        TriVert3.sub(TriVert1, TriVert3Minus1);

        TriVert2Minus1.cross(TriVert3Minus1, Cross);

        //Calculate Sums
        for (int i = 0; i < 3; i++) {
            Sum1 += Cross.get(i) * (LineStart.get(i) - TriVert1.get(i));
            Sum2 += Cross.get(i) * (LineEnd.get(i)   - LineStart.get(i));
        }

        //Calculate Ratio
        Ratio = (0 - Sum1) / Sum2;

        //Calculate Intersection Point
        Vector3f I = new Vector3f();
        LineStart.mulAdd(1-Ratio, LineEnd.mul(Ratio), I);
        if (Float.isNaN(I.x)) {
            return false;
        }
        //System.out.println(I);

        //Solve to see if point is in triangle
        DMatrixRMaj Triangle = new DMatrixRMaj(new double [][] {
                {TriVert1.x, TriVert2.x, TriVert3.x},
                {TriVert1.y, TriVert2.y, TriVert3.y},
                {TriVert1.z, TriVert2.z, TriVert3.z}
        });

        DMatrixRMaj IntersectionPoint = new DMatrixRMaj(new double [][] {
                {I.x},
                {I.y},
                {I.z}
        });

        DMatrixRMaj PointRatios = new DMatrixRMaj(new double [][] {
                {0},
                {0},
                {0}
        });

        CommonOps_DDRM.solve(Triangle,IntersectionPoint,PointRatios);

       /* System.out.print(PointRatios.get(0,0) + " ");
        System.out.print(PointRatios.get(1,0) + " ");
        System.out.println(PointRatios.get(2,0));

        System.out.print(Math.signum(PointRatios.get(0,0)) + " ");
        System.out.print(Math.signum(PointRatios.get(1,0)) + " ");
        System.out.println(Math.signum(PointRatios.get(2,0)));*/

        for (int i = 0; i < 3; i++) {
            if (PointRatios.get(i,0) < 0) {
                //System.out.println("false");
                return false;
            }
        }
        //System.out.println("true");
        return true;
    }

    public static Vector3f Midpoint(Vector3f Vec1, Vector3f Vec2) {
        return new Vector3f(
                (Vec1.x + Vec2.x) / 2,
                (Vec1.y + Vec2.y) / 2,
                (Vec1.z + Vec2.z) / 2
        );
    }

    public static boolean VectorLessThan(Vector3f Vec1, Vector3f Vec2) {
        if (Vec1.x < Vec2.x) {
            return true;
        }
        if (Vec1.y < Vec2.y) {
            return true;
        }
        if (Vec1.z < Vec2.z) {
            return true;
        }
        return false;
    }

}
