package Root.Simulation;

import org.joml.Vector3f;

public class Kernels {

    public static float KernelRadiusPow1;
    public static float KernelRadiusPow2;
    public static float KernelRadiusPow6;
    public static float KernelRadiusPow9;
    private static final float PI = (float)Math.PI;

    public static void Init(float KernelRadius) {

        KernelRadiusPow1 = KernelRadius;
        KernelRadiusPow2 = (float)Math.pow(KernelRadius, 2);
        KernelRadiusPow6 = (float)Math.pow(KernelRadius, 6);
        KernelRadiusPow9 = (float)Math.pow(KernelRadius, 9);

    }

    public static float Poly6(Particle P1, Particle P2) {
        //Calculate the distance between the two particles
        Vector3f DeltaPos = new Vector3f(0,0,0);
        P1.Position.sub(P2.Position, DeltaPos);
        float DistanceSqr = DeltaPos.lengthSquared();

        //Return 0 if the two particles more than KernelRadius^2 apart,
        //As they have no influence on eachother.
        if (KernelRadiusPow2 < DistanceSqr) {
            return 0;
        }

        //Calculate Value based on formula
        float Coefficient = 315f / (64f * PI * KernelRadiusPow9);

        return (float)(Coefficient * Math.pow(KernelRadiusPow2 - DistanceSqr, 3));
    }

    public static Vector3f SpikyGrad(Particle P1, Particle P2) {
        //Calculate the distance between the two particles
        Vector3f DeltaPos = new Vector3f(0,0,0);
        P1.Position.sub(P2.Position, DeltaPos);
        float Distance = DeltaPos.length();

        //Return 0 if the two particles more than KernelRadius apart,
        //As they have no influence on each other.
        if (KernelRadiusPow1 < Distance) {
            return new Vector3f(0,0,0);
        }

        //Calculate Value based on formula
        float Coefficient = 45f / (PI * KernelRadiusPow6);

        Vector3f NormalizedDeltaPos = new Vector3f(0,0,0);
        DeltaPos.normalize(NormalizedDeltaPos);
        NormalizedDeltaPos.mul((float)Math.pow(KernelRadiusPow1 - Distance, 2));

        return NormalizedDeltaPos.mul(-Coefficient);
    }

    public static float Laplacian(Particle P1, Particle P2) {
        //Calculate the distance between the two particles
        Vector3f DeltaPos = new Vector3f(0,0,0);
        P1.Position.sub(P2.Position, DeltaPos);
        float Distance = DeltaPos.length();

        //Return 0 if the two particles more than KernelRadius apart,
        //As they have no influence on each other.
        if (KernelRadiusPow1 < Distance) {
            return 0;
        }

        //Calculate Value based on formula
        float Coefficient = 45f / (PI * KernelRadiusPow6);

        return Coefficient * (KernelRadiusPow1 - Distance);
    }

}
