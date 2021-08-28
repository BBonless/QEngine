package Root.Simulation;

import java.nio.FloatBuffer;

import static Root.Compute.GPU.Stack;

public class MemorySharing {

    public static FloatBuffer GetParticleBuffer() {
        FloatBuffer Buffer = Stack.callocFloat(SimEngine.Particles.length * 17);
        for (int ParticleIndex = 0; ParticleIndex < SimEngine.Particles.length; ParticleIndex++) {
            Particle P = SimEngine.Particles[ParticleIndex];

            Buffer.put((ParticleIndex * 17) + 0, P.Position.x);
            Buffer.put((ParticleIndex * 17) + 1, P.Position.y);
            Buffer.put((ParticleIndex * 17) + 2, P.Position.z);

            Buffer.put((ParticleIndex * 17) + 3, P.PastPosition.x);
            Buffer.put((ParticleIndex * 17) + 4, P.PastPosition.y);
            Buffer.put((ParticleIndex * 17) + 5, P.PastPosition.z);

            Buffer.put((ParticleIndex * 17) + 6, P.Velocity.x);
            Buffer.put((ParticleIndex * 17) + 7, P.Velocity.y);
            Buffer.put((ParticleIndex * 17) + 8, P.Velocity.z);

            Buffer.put((ParticleIndex * 17) + 9, P.PastAcceleration.x);
            Buffer.put((ParticleIndex * 17) + 10, P.PastAcceleration.y);
            Buffer.put((ParticleIndex * 17) + 11, P.PastAcceleration.z);

            Buffer.put((ParticleIndex * 17) + 12, P.Force.x);
            Buffer.put((ParticleIndex * 17) + 13, P.Force.y);
            Buffer.put((ParticleIndex * 17) + 14, P.Force.z);

            Buffer.put((ParticleIndex * 17) + 15, P.Pressure);

            Buffer.put((ParticleIndex * 17) + 16, P.Density);
        }
        return Buffer;
    }

    public static void GetParticleBuffer(FloatBuffer Buffer) {
        for (int ParticleIndex = 0; ParticleIndex < SimEngine.Particles.length; ParticleIndex++) {
            Particle P = SimEngine.Particles[ParticleIndex];

            Buffer.put((ParticleIndex * 17) + 0, P.Position.x);
            Buffer.put((ParticleIndex * 17) + 1, P.Position.y);
            Buffer.put((ParticleIndex * 17) + 2, P.Position.z);

            Buffer.put((ParticleIndex * 17) + 3, P.PastPosition.x);
            Buffer.put((ParticleIndex * 17) + 4, P.PastPosition.y);
            Buffer.put((ParticleIndex * 17) + 5, P.PastPosition.z);

            Buffer.put((ParticleIndex * 17) + 6, P.Velocity.x);
            Buffer.put((ParticleIndex * 17) + 7, P.Velocity.y);
            Buffer.put((ParticleIndex * 17) + 8, P.Velocity.z);

            Buffer.put((ParticleIndex * 17) + 9, P.PastAcceleration.x);
            Buffer.put((ParticleIndex * 17) + 10, P.PastAcceleration.y);
            Buffer.put((ParticleIndex * 17) + 11, P.PastAcceleration.z);

            Buffer.put((ParticleIndex * 17) + 12, P.Force.x);
            Buffer.put((ParticleIndex * 17) + 13, P.Force.y);
            Buffer.put((ParticleIndex * 17) + 14, P.Force.z);

            Buffer.put((ParticleIndex * 17) + 15, P.Pressure);

            Buffer.put((ParticleIndex * 17) + 16, P.Density);
        }
    }

    public static FloatBuffer GetNeighborBuffer() {
        FloatBuffer Buffer = Stack.callocFloat(SimEngine.Particles.length * 64 * 17);
        for (int ParticleIndex = 0; ParticleIndex < SimEngine.Particles.length; ParticleIndex++) {
            Particle P = SimEngine.Particles[ParticleIndex];

            for (int NeighborIndex = 0; NeighborIndex < 64; NeighborIndex++) {
                if (NeighborIndex < P.Neighbors.size() ) {
                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 0, P.Position.x);
                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 1, P.Position.y);
                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 2, P.Position.z);

                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 3, P.PastPosition.x);
                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 4, P.PastPosition.y);
                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 5, P.PastPosition.z);

                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 6, P.Velocity.x);
                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 7, P.Velocity.y);
                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 8, P.Velocity.z);

                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 9, P.PastAcceleration.x);
                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 10, P.PastAcceleration.y);
                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 11, P.PastAcceleration.z);

                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 12, P.Force.x);
                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 13, P.Force.y);
                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 14, P.Force.z);

                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 15, P.Pressure);

                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 16, P.Density);
                }
                else {
                    for (int i = 0; i < 17; i++) {
                        Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + i, Float.NaN);
                    }
                }
            }
        }
        return Buffer;
    }

    public static void GetNeighborBuffer(FloatBuffer Buffer) {
        for (int ParticleIndex = 0; ParticleIndex < SimEngine.Particles.length; ParticleIndex++) {
            Particle P = SimEngine.Particles[ParticleIndex];

            for (int NeighborIndex = 0; NeighborIndex < 64; NeighborIndex++) {
                if (NeighborIndex < P.Neighbors.size() ) {
                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 0, P.Position.x);
                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 1, P.Position.y);
                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 2, P.Position.z);

                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 3, P.PastPosition.x);
                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 4, P.PastPosition.y);
                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 5, P.PastPosition.z);

                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 6, P.Velocity.x);
                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 7, P.Velocity.y);
                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 8, P.Velocity.z);

                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 9, P.PastAcceleration.x);
                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 10, P.PastAcceleration.y);
                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 11, P.PastAcceleration.z);

                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 12, P.Force.x);
                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 13, P.Force.y);
                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 14, P.Force.z);

                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 15, P.Pressure);

                    Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + 16, P.Density);
                }
                else {
                    for (int i = 0; i < 17; i++) {
                        Buffer.put((ParticleIndex * 64) + (NeighborIndex * 17) + i, Float.NaN);
                    }
                }
            }
        }
    }

    public static FloatBuffer GetParameterBuffer() {
        FloatBuffer Buffer = Stack.callocFloat(16);
        Buffer.put(0, Preferences.SmoothingRadius);
        Buffer.put(1, Preferences.ParticleMass);
        Buffer.put(2, Kernels.KernelRadiusPow1);
        Buffer.put(3, Kernels.KernelRadiusPow2);
        Buffer.put(4, Kernels.KernelRadiusPow6);
        Buffer.put(5, Kernels.KernelRadiusPow9);
        Buffer.put(6, Preferences.RestDensity);
        Buffer.put(7, Preferences.Stiffness);
        Buffer.put(8, Preferences.ParticleViscosity);
        Buffer.put(9, Preferences.Gravity.x);
        Buffer.put(10, Preferences.Gravity.y);
        Buffer.put(11, Preferences.Gravity.z);
        Buffer.put(12, Preferences.BoundarySize.x);
        Buffer.put(13, Preferences.BoundarySize.y);
        Buffer.put(14, Preferences.BoundarySize.z);
        Buffer.put(15, Preferences.Timestep);
        return Buffer;
    }

    public static void GetParameterBuffer(FloatBuffer Buffer) {
        Buffer.put(0, Preferences.SmoothingRadius);
        Buffer.put(1, Preferences.ParticleMass);
        Buffer.put(2, Kernels.KernelRadiusPow1);
        Buffer.put(3, Kernels.KernelRadiusPow2);
        Buffer.put(4, Kernels.KernelRadiusPow6);
        Buffer.put(5, Kernels.KernelRadiusPow9);
        Buffer.put(6, Preferences.RestDensity);
        Buffer.put(7, Preferences.Stiffness);
        Buffer.put(8, Preferences.ParticleViscosity);
        Buffer.put(9, Preferences.Gravity.x);
        Buffer.put(10, Preferences.Gravity.y);
        Buffer.put(11, Preferences.Gravity.z);
        Buffer.put(12, Preferences.BoundarySize.x);
        Buffer.put(13, Preferences.BoundarySize.y);
        Buffer.put(14, Preferences.BoundarySize.z);
        Buffer.put(15, Preferences.Timestep);
    }

}
