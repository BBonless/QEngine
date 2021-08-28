/*
package Root.Simulation;

import Root.Misc.Util.Util;
import org.joml.Math;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class SpatialGrid2 {

    private int F3D(int X, int Y, int Z, int Divisions) {
        return X + Divisions * (Y + Divisions * Z);
    }

    private Vector3i PositionIndex3D(Vector3f PositionIn, Vector3f Boundary, int Divisions) {
        Vector3f Position = new Vector3f(0,0,0);
        PositionIn.get(Position);

        Vector3f HalfBoundary = new Vector3f(0,0,0);
        Boundary.div(2, HalfBoundary);

        Position.x = Math.clamp(-HalfBoundary.x + 0.01f, HalfBoundary.x - 0.01f, Position.x);
        Position.y = Math.clamp(-HalfBoundary.y + 0.01f, HalfBoundary.y - 0.01f, Position.y);
        Position.z = Math.clamp(-HalfBoundary.z + 0.01f, HalfBoundary.z - 0.01f, Position.z);

        float ConvertedX = Position.x + HalfBoundary.x;
        float DivisorX = ConvertedX / Boundary.x;
        float DividendX = 1f / Divisions;

        float ConvertedY = Position.y + HalfBoundary.y;
        float DivisorY = ConvertedY / Boundary.y;
        float DividendY = 1f / Divisions;

        float ConvertedZ = Position.z + HalfBoundary.z;
        float DivisorZ = ConvertedZ / Boundary.z;
        float DividendZ = 1f / Divisions;

        Vector3i Result = new Vector3i();
        Result.x = (int)Math.floor(DivisorX / DividendX);
        Result.y = (int)Math.floor(DivisorY / DividendY);
        Result.z = (int)Math.floor(DivisorZ / DividendZ);

        return Result;
    }

    private int PositionIndex1D(Vector3f PositionIn, Vector3f Boundary, int Divisions) {
        Vector3f Position = new Vector3f(0,0,0);
        PositionIn.get(Position);

        Vector3f HalfBoundary = new Vector3f(0,0,0);
        Boundary.div(2, HalfBoundary);

        Position.x = Math.clamp(-HalfBoundary.x + 0.01f, HalfBoundary.x - 0.01f, Position.x);
        Position.y = Math.clamp(-HalfBoundary.y + 0.01f, HalfBoundary.y - 0.01f, Position.y);
        Position.z = Math.clamp(-HalfBoundary.z + 0.01f, HalfBoundary.z - 0.01f, Position.z);

        float ConvertedX = Position.x + HalfBoundary.x;
        float DivisorX = ConvertedX / Boundary.x;
        float DividendX = 1f / Divisions;

        float ConvertedY = Position.y + HalfBoundary.y;
        float DivisorY = ConvertedY / Boundary.y;
        float DividendY = 1f / Divisions;

        float ConvertedZ = Position.z + HalfBoundary.z;
        float DivisorZ = ConvertedZ / Boundary.z;
        float DividendZ = 1f / Divisions;

        int IndexX = (int)Math.floor(DivisorX / DividendX);
        int IndexY = (int)Math.floor(DivisorY / DividendY);
        int IndexZ = (int)Math.floor(DivisorZ / DividendZ);

        return F3D(IndexX, IndexY, IndexZ, Divisions);
    }

    public void NeighborQuery(Particle Target, Particle[] Particles, int[] Buckets, Vector3f Boundary, int Divisions, float BucketSize, float SmoothingRadiusSqr) {
        Target.Neighbors.clear();

        Vector3i Index = PositionIndex3D(Target.Position, Boundary, Divisions);

        int IndexOffset = (int)Math.ceil(SmoothingRadiusSqr / BucketSize);

        for (int X = Index.x - IndexOffset; X <= Index.x + IndexOffset; X++) {
            if (X < 0 || X > Divisions - 1) {continue;}

            for (int Y = Index.y - IndexOffset; Y <= Index.y + IndexOffset; Y++) {
                if (Y < 0 || Y > Divisions - 1) {continue;}

                for (int Z = Index.z - IndexOffset; Z <= Index.z + IndexOffset; Z++) {
                    if (Z < 0 || Z > Divisions - 1) {continue;}

                    int[] Bucket = new int[512];
                    int BucketIndex = F3D(X,Y,Z,Divisions) * 512;
                    for (int Offset = 0; Offset < 512; Offset++) {
                        Bucket[Offset] = Buckets[BucketIndex + Offset];
                    }

                    for (Particle PotentialNeighbor : Buckets.get(F3D(X,Y,Z))) {
                        if (Util.DistanceSquared(PotentialNeighbor, Target) < Preferences.SmoothingRadiusSqr) {
                            Target.Neighbors.add(PotentialNeighbor);
                        }
                    }
                }
            }
        }
    }

}
*/
