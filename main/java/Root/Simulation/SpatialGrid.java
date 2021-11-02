package Root.Simulation;

import Root.Misc.Util.Util;
import org.joml.Math;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.ArrayList;

public class SpatialGrid {
    public int BucketsX;
    public int BucketsY;
    public int BucketsZ;
    public int BucketCount;

    private Vector3f BucketSize = new Vector3f(0,0,0);
    private Vector3f Boundary;
    private Vector3f HalfBoundary = new Vector3f(0,0,0);

    public ArrayList<ArrayList<Particle>> Buckets;

    public SpatialGrid(Vector3f BoundaryIn, int BucketsXIn, int BucketsYIn, int BucketsZIn) {
        BucketsX = BucketsXIn;
        BucketsY = BucketsYIn;
        BucketsZ = BucketsZIn;
        BucketCount = BucketsX*BucketsY*BucketsZ;

        Boundary = BoundaryIn;
        Boundary.mul(0.5f, HalfBoundary);

        BucketSize.x = Boundary.x / BucketsX;
        BucketSize.y = Boundary.y / BucketsY;
        BucketSize.z = Boundary.z / BucketsZ;

        Buckets = new ArrayList<>();

        for (int Bucket = 0; Bucket < BucketCount; Bucket++) {
            Buckets.add(new ArrayList<>());
        }
    }

    public int F3D(int X, int Y, int Z) { //Flatten 3D Coordinates to 1D Index
        return X + BucketsY * (Y + BucketsZ * Z);
    }

    public void Fill(Particle[] Particles) {
        for (Particle P1 : Particles) {
            int Index = PositionIndex1D(P1.Position);

            Buckets.get(Index).add(P1);
        }
    }

    public void Fill(ArrayList<ArrayList<Particle>> ParticlePackets) {
        if (ParticlePackets.isEmpty()) {
            return;
        }

        for (ArrayList<Particle> Particles : ParticlePackets) {
            for (Particle P1 : Particles) {
                int Index = PositionIndex1D(P1.Position);

                Buckets.get(Index).add(P1);
            }
        }
    }

    public void Clear() {
        for (ArrayList<Particle> Bucket : Buckets) {
            Bucket.clear();
        }
    }

    public int PositionIndex1D(Vector3f PositionIn) {

        Vector3f Position = new Vector3f(0,0,0);
        PositionIn.get(Position);

        Position.x = Math.clamp(-HalfBoundary.x + 0.01f, HalfBoundary.x - 0.01f, Position.x);
        Position.y = Math.clamp(-HalfBoundary.y + 0.01f, HalfBoundary.y - 0.01f, Position.y);
        Position.z = Math.clamp(-HalfBoundary.z + 0.01f, HalfBoundary.z - 0.01f, Position.z);
        
        float ConvertedX = Position.x + HalfBoundary.x;
        float DivisorX = ConvertedX / Boundary.x;
        float DividendX = 1f / BucketsX;

        float ConvertedY = Position.y + HalfBoundary.y;
        float DivisorY = ConvertedY / Boundary.y;
        float DividendY = 1f / BucketsY;

        float ConvertedZ = Position.z + HalfBoundary.z;
        float DivisorZ = ConvertedZ / Boundary.z;
        float DividendZ = 1f / BucketsZ;

        int XIndex = (int)Math.floor(DivisorX / DividendX);
        int YIndex = (int)Math.floor(DivisorY / DividendY);
        int ZIndex = (int)Math.floor(DivisorZ / DividendZ);

        return F3D(XIndex,YIndex,ZIndex);
    }

    public Vector3i PositionIndex3D(Vector3f PositionIn) {

        Vector3f Position = new Vector3f(0,0,0);
        PositionIn.get(Position);

        Position.x = Math.clamp(-HalfBoundary.x + 0.01f, HalfBoundary.x - 0.01f, Position.x);
        Position.y = Math.clamp(-HalfBoundary.y + 0.01f, HalfBoundary.y - 0.01f, Position.y);
        Position.z = Math.clamp(-HalfBoundary.z + 0.01f, HalfBoundary.z - 0.01f, Position.z);

        float ConvertedX = Position.x + HalfBoundary.x;
        float DivisorX = ConvertedX / Boundary.x;
        float DividendX = 1f / BucketsX;

        float ConvertedY = Position.y + HalfBoundary.y;
        float DivisorY = ConvertedY / Boundary.y;
        float DividendY = 1f / BucketsY;

        float ConvertedZ = Position.z + HalfBoundary.z;
        float DivisorZ = ConvertedZ / Boundary.z;
        float DividendZ = 1f / BucketsZ;

        Vector3i Result = new Vector3i();
        Result.x = (int)Math.floor(DivisorX / DividendX);
        Result.y = (int)Math.floor(DivisorY / DividendY);
        Result.z = (int)Math.floor(DivisorZ / DividendZ);

        return Result;
    }

    public ArrayList<Particle> NeighborQuery(Vector3f Point, float Radius) {
        ArrayList<Particle> Result = new ArrayList<>();

        Vector3i Index = PositionIndex3D(Point);
        int IndexXOffset = (int)Math.ceil( Radius / BucketSize.x );
        int IndexYOffset = (int)Math.ceil( Radius / BucketSize.y );
        int IndexZOffset = (int)Math.ceil( Radius / BucketSize.z );

        for (int X = Index.x - IndexXOffset; X <= Index.x + IndexXOffset; X++) {
            if (X < 0 || X > BucketsX - 1) {continue;}

            for (int Y = Index.y - IndexYOffset; Y <= Index.y + IndexYOffset; Y++) {
                if (Y < 0 || Y > BucketsY - 1) {continue;}

                for (int Z = Index.z - IndexZOffset; Z <= Index.z + IndexZOffset; Z++) {
                    if (Z < 0 || Z > BucketsZ - 1) {continue;}

                    for (Particle PotentialNeighbor : Buckets.get(F3D(X,Y,Z))) {
                        Vector3f DeltaPos = new Vector3f(0,0,0);
                        PotentialNeighbor.Position.sub(Point, DeltaPos);
                        if (DeltaPos.lengthSquared() < Radius*Radius) {
                            Result.add(PotentialNeighbor);
                        }
                    }
                }
            }
        }
        return Result;
    }

    public ArrayList<Particle> NeighborQueryMeshifyOnly(Vector3f Point, float Radius) {
        ArrayList<Particle> Result = new ArrayList<>();

        Vector3i Index = PositionIndex3D(Point);
        int IndexXOffset = (int)Math.ceil( Radius / BucketSize.x );
        int IndexYOffset = (int)Math.ceil( Radius / BucketSize.y );
        int IndexZOffset = (int)Math.ceil( Radius / BucketSize.z );

        for (int X = Index.x - IndexXOffset; X <= Index.x + IndexXOffset; X++) {
            if (X < 0 || X > BucketsX - 1) {continue;}

            for (int Y = Index.y - IndexYOffset; Y <= Index.y + IndexYOffset; Y++) {
                if (Y < 0 || Y > BucketsY - 1) {continue;}

                for (int Z = Index.z - IndexZOffset; Z <= Index.z + IndexZOffset; Z++) {
                    if (Z < 0 || Z > BucketsZ - 1) {continue;}

                    for (Particle PotentialNeighbor : Buckets.get(F3D(X,Y,Z))) {
                        Vector3f DeltaPos = new Vector3f(0,0,0);
                        PotentialNeighbor.Position.sub(Point, DeltaPos);
                        if (DeltaPos.lengthSquared() < Radius*Radius && !PotentialNeighbor.DoNotMeshify) {
                            Result.add(PotentialNeighbor);
                        }
                    }
                }
            }
        }
        return Result;
    }

    public void NeighborQuery(Particle Target) {
        Target.Neighbors.clear();

        Vector3i Index = PositionIndex3D(Target.Position);

        int IndexXOffset = (int)Math.ceil(Preferences.SmoothingRadiusSqr / BucketSize.x );
        int IndexYOffset = (int)Math.ceil( Preferences.SmoothingRadiusSqr / BucketSize.y );
        int IndexZOffset = (int)Math.ceil(  Preferences.SmoothingRadiusSqr / BucketSize.z );

        for (int X = Index.x - IndexXOffset; X <= Index.x + IndexXOffset; X++) {
            if (X < 0 || X > BucketsX - 1) {continue;}

            for (int Y = Index.y - IndexYOffset; Y <= Index.y + IndexYOffset; Y++) {
                if (Y < 0 || Y > BucketsY - 1) {continue;}

                for (int Z = Index.z - IndexZOffset; Z <= Index.z + IndexZOffset; Z++) {
                    if (Z < 0 || Z > BucketsZ - 1) {continue;}

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
