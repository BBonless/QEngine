package Root.Simulation;

import org.joml.Vector3f;

public class Preferences {

    public static Vector3f ParticleSpawnPoint = new Vector3f(0,0,0);
    public static int ParticleBlockSizeX = 10;
    public static int ParticleBlockSizeY = 10;
    public static int ParticleBlockSizeZ = 10;
    public static int ParticleBlockSize = 1;
    public static float ParticleBlockGap = 1.25f;


    public static float Timestep = 0.025f;
    public static float BoundaryElasticity = 0.1f;
    public static int SolverIterations = 3;
    public static int SimulationIterations = 1;
    public static Vector3f BoundarySize = new Vector3f(50,50,50);
    public static Vector3f Gravity = new Vector3f(0, -9.81f, 0);

    public static float Stiffness = 50;
    public static float ParticleMass = 100f;
    public static float ParticleViscosity = 0.07f;
    public static float RestDensity = 30f;

    public static float SmoothingRadius = 0.7f;
    public static float SmoothingRadiusSqr = 0f;

    public static int GridX = 40;
    public static int GridY = 40;
    public static int GridZ = 40;

    public static float ParticleDrawSize = 0.25f;

    public static void Init() {
        UpdateRadius();

        UpdateSize();
    }

    public static void UpdateSize() {
        ParticleBlockSize = ParticleBlockSizeX * ParticleBlockSizeY * ParticleBlockSizeZ;
    }

    public static void UpdateRadius() {
        SmoothingRadiusSqr = SmoothingRadius*SmoothingRadius;
    }

    public static Vector3f GetSpawnPosition(int X, int Y, int Z) {
        //Find the position a particle will spawn, adjusting for how wide the space between the particles should be
        Vector3f SpawnPosition = new Vector3f(X * ParticleBlockGap, Y * ParticleBlockGap, Z * ParticleBlockGap);

        //Offset by the point where the spawn block should start
        SpawnPosition.add(ParticleSpawnPoint);

        return SpawnPosition;
    }

}
