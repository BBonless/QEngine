package Root.Simulation;

import Root.Misc.Util.Util;
import org.joml.Vector3f;

public class Preferences {

    public static float[] ParticleSpawnPoint = new float[] {0,0,0};
    public static int[] ParticleBlockSizeX = new int[] {15};
    public static int[] ParticleBlockSizeY = new int[] {15};
    public static int[] ParticleBlockSizeZ = new int[] {15};
    public static int[] ParticleBlockSize = new int[] {1};
    public static float[] ParticleBlockGap = new float[] {1.25f};


    public static float[] Timestep = new float[] {0.025f};
    public static float[] BoundaryElasticity = new float[] {0.1f};
    public static int[] SolverIterations = new int[] {1};
    public static int[] SimulationIterations = new int[] {1};
    public static float[] BoundarySize = new float[] {100,100,100};
    public static float[] Gravity = new float[] {0,-9.81f,0};

    public static float[] Stiffness = new float[] {50};
    public static float[] ParticleMass = new float[] {100};
    public static float[] ParticleViscosity = new float[] {0.07f};
    public static float[] RestDensity = new float[] {82f};

    public static float[] SmoothingRadius = new float[] {0.7f};
    public static float SmoothingRadiusSqr = 0f;

    public static int[] GridX = new int[] {40};
    public static int[] GridY = new int[] {40};
    public static int[] GridZ = new int[] {40};

    public static float[] ParticleDrawSize = new float[] {0.25f};

    public static void Init() {
        UpdateRadius();

        UpdateSize();
    }

    public static void UpdateSize() {
        ParticleBlockSize[0] = ParticleBlockSizeX[0] * ParticleBlockSizeY[0] * ParticleBlockSizeZ[0];
    }

    public static void UpdateRadius() {
        SmoothingRadiusSqr = SmoothingRadius[0]*SmoothingRadius[0];
    }

    public static Vector3f GetSpawnPosition(int X, int Y, int Z) {
        //Find the position a particle will spawn, adjusting for how wide the space between the particles should be
        Vector3f SpawnPosition = new Vector3f(X * ParticleBlockGap[0], Y * ParticleBlockGap[0], Z * ParticleBlockGap[0]);
        Vector3f CenterOffset = new Vector3f(
                ParticleBlockSizeX[0] * ParticleBlockGap[0],
                ParticleBlockSizeY[0] * ParticleBlockGap[0],
                ParticleBlockSizeZ[0] * ParticleBlockGap[0]
        ).div(2);

        //Offset by the point where the spawn block should start
        SpawnPosition.add(Util.FloatArrToVec(ParticleSpawnPoint));

        //Offset the point so that it is centered
        SpawnPosition.sub(CenterOffset);

        return SpawnPosition;
    }

}
