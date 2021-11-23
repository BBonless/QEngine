package Root.Simulation;

import java.nio.FloatBuffer;

import static Root.Compute.GPU.Stack;

public class MemorySharing {

    public static FloatBuffer GetParticleBuffer() {
        FloatBuffer Buffer = Stack.callocFloat(SimEngine.DynamicParticles.length * 17);
        for (int ParticleIndex = 0; ParticleIndex < SimEngine.DynamicParticles.length; ParticleIndex++) {
            Particle P = SimEngine.DynamicParticles[ParticleIndex];

            Buffer.put((ParticleIndex * 17) + 0, P.Position.x);
            Buffer.put((ParticleIndex * 17) + 1, P.Position.y);
            Buffer.put((ParticleIndex * 17) + 2, P.Position.z);

            /*Buffer.put((ParticleIndex * 17) + 3, P.PastPosition.x);
            Buffer.put((ParticleIndex * 17) + 4, P.PastPosition.y);
            Buffer.put((ParticleIndex * 17) + 5, P.PastPosition.z);*/

            Buffer.put((ParticleIndex * 17) + 6, P.Velocity.x);
            Buffer.put((ParticleIndex * 17) + 7, P.Velocity.y);
            Buffer.put((ParticleIndex * 17) + 8, P.Velocity.z);

            Buffer.put((ParticleIndex * 17) + 9, P.Acceleration.x);
            Buffer.put((ParticleIndex * 17) + 10, P.Acceleration.y);
            Buffer.put((ParticleIndex * 17) + 11, P.Acceleration.z);

            Buffer.put((ParticleIndex * 17) + 12, P.Force.x);
            Buffer.put((ParticleIndex * 17) + 13, P.Force.y);
            Buffer.put((ParticleIndex * 17) + 14, P.Force.z);

            Buffer.put((ParticleIndex * 17) + 15, P.Pressure);

            Buffer.put((ParticleIndex * 17) + 16, P.Density);
        }
        return Buffer;
    }

    public static void GetParticleBuffer(FloatBuffer Buffer) {
        for (int ParticleIndex = 0; ParticleIndex < SimEngine.DynamicParticles.length; ParticleIndex++) {
            Particle P = SimEngine.DynamicParticles[ParticleIndex];

            Buffer.put((ParticleIndex * 17) + 0, P.Position.x);
            Buffer.put((ParticleIndex * 17) + 1, P.Position.y);
            Buffer.put((ParticleIndex * 17) + 2, P.Position.z);

            /*Buffer.put((ParticleIndex * 17) + 3, P.PastPosition.x);
            Buffer.put((ParticleIndex * 17) + 4, P.PastPosition.y);
            Buffer.put((ParticleIndex * 17) + 5, P.PastPosition.z);*/

            Buffer.put((ParticleIndex * 17) + 6, P.Velocity.x);
            Buffer.put((ParticleIndex * 17) + 7, P.Velocity.y);
            Buffer.put((ParticleIndex * 17) + 8, P.Velocity.z);

            Buffer.put((ParticleIndex * 17) + 9, P.Acceleration.x);
            Buffer.put((ParticleIndex * 17) + 10, P.Acceleration.y);
            Buffer.put((ParticleIndex * 17) + 11, P.Acceleration.z);

            Buffer.put((ParticleIndex * 17) + 12, P.Force.x);
            Buffer.put((ParticleIndex * 17) + 13, P.Force.y);
            Buffer.put((ParticleIndex * 17) + 14, P.Force.z);

            Buffer.put((ParticleIndex * 17) + 15, P.Pressure);

            Buffer.put((ParticleIndex * 17) + 16, P.Density);
        }
    }

    public static FloatBuffer GetNeighborBuffer() {
        FloatBuffer Buffer = Stack.callocFloat(SimEngine.DynamicParticles.length * 64 * 17);

        for (int ParticleIndex = 0; ParticleIndex < SimEngine.DynamicParticles.length; ParticleIndex++) {
            Particle Particle = SimEngine.DynamicParticles[ParticleIndex];

            for (int NeighborIndex = 0; NeighborIndex < 64; NeighborIndex++) {
                System.out.print(NeighborIndex + " / ");
                System.out.println(Particle.Neighbors.size());
                if (NeighborIndex < Particle.Neighbors.size()) {
                    Particle Neighbor = Particle.Neighbors.get(NeighborIndex);

                    Buffer.put( (ParticleIndex * (64 * 17)) + (17 * NeighborIndex) + 0, Neighbor.Position.x );
                    Buffer.put( (ParticleIndex * (64 * 17)) + (17 * NeighborIndex) + 1, Neighbor.Position.y );
                    Buffer.put( (ParticleIndex * (64 * 17)) + (17 * NeighborIndex) + 2, Neighbor.Position.z );

                    Buffer.put( (ParticleIndex * (64 * 17)) + (17 * NeighborIndex) + 6, Neighbor.Velocity.x );
                    Buffer.put( (ParticleIndex * (64 * 17)) + (17 * NeighborIndex) + 7, Neighbor.Velocity.y );
                    Buffer.put( (ParticleIndex * (64 * 17)) + (17 * NeighborIndex) + 8, Neighbor.Velocity.z );

                    Buffer.put( (ParticleIndex * (64 * 17)) + (17 * NeighborIndex) + 9, Neighbor.Acceleration.x );
                    Buffer.put( (ParticleIndex * (64 * 17)) + (17 * NeighborIndex) + 10, Neighbor.Acceleration.y );
                    Buffer.put( (ParticleIndex * (64 * 17)) + (17 * NeighborIndex) + 11, Neighbor.Acceleration.z );

                    Buffer.put( (ParticleIndex * (64 * 17)) + (17 * NeighborIndex) + 12, Neighbor.Force.x );
                    Buffer.put( (ParticleIndex * (64 * 17)) + (17 * NeighborIndex) + 13, Neighbor.Force.y );
                    Buffer.put( (ParticleIndex * (64 * 17)) + (17 * NeighborIndex) + 14, Neighbor.Force.z );

                    Buffer.put( (ParticleIndex * (64 * 17)) + (17 * NeighborIndex) + 15, Neighbor.Pressure );

                    Buffer.put( (ParticleIndex * (64 * 17)) + (17 * NeighborIndex) + 16, Neighbor.Density );

                } else {
                    for (int i = 0; i < 17; i++) {
                        Buffer.put((ParticleIndex * (64 * 17)) + (17 * NeighborIndex) + i, Float.NaN);
                    }
                }
            }
        }

        return Buffer;

    }

    public static void GetNeighborBuffer(FloatBuffer Buffer) {
        for (int ParticleIndex = 0; ParticleIndex < SimEngine.DynamicParticles.length; ParticleIndex++) {
            Particle Particle = SimEngine.DynamicParticles[ParticleIndex];

            for (int NeighborIndex = 0; NeighborIndex < 64; NeighborIndex++) {
                if (NeighborIndex < Particle.Neighbors.size()) {
                    Particle Neighbor = Particle.Neighbors.get(NeighborIndex);

                    Buffer.put( (ParticleIndex * (64 * 17)) + (17 * NeighborIndex) + 0, Neighbor.Position.x );
                    Buffer.put( (ParticleIndex * (64 * 17)) + (17 * NeighborIndex) + 1, Neighbor.Position.y );
                    Buffer.put( (ParticleIndex * (64 * 17)) + (17 * NeighborIndex) + 2, Neighbor.Position.z );

                    Buffer.put( (ParticleIndex * (64 * 17)) + (17 * NeighborIndex) + 6, Neighbor.Velocity.x );
                    Buffer.put( (ParticleIndex * (64 * 17)) + (17 * NeighborIndex) + 7, Neighbor.Velocity.y );
                    Buffer.put( (ParticleIndex * (64 * 17)) + (17 * NeighborIndex) + 8, Neighbor.Velocity.z );

                    Buffer.put( (ParticleIndex * (64 * 17)) + (17 * NeighborIndex) + 9, Neighbor.Acceleration.x );
                    Buffer.put( (ParticleIndex * (64 * 17)) + (17 * NeighborIndex) + 10, Neighbor.Acceleration.y );
                    Buffer.put( (ParticleIndex * (64 * 17)) + (17 * NeighborIndex) + 11, Neighbor.Acceleration.z );

                    Buffer.put( (ParticleIndex * (64 * 17)) + (17 * NeighborIndex) + 12, Neighbor.Force.x );
                    Buffer.put( (ParticleIndex * (64 * 17)) + (17 * NeighborIndex) + 13, Neighbor.Force.y );
                    Buffer.put( (ParticleIndex * (64 * 17)) + (17 * NeighborIndex) + 14, Neighbor.Force.z );

                    Buffer.put( (ParticleIndex * (64 * 17)) + (17 * NeighborIndex) + 15, Neighbor.Pressure );

                    Buffer.put( (ParticleIndex * (64 * 17)) + (17 * NeighborIndex) + 16, Neighbor.Density );

                } else {
                    for (int i = 0; i < 17; i++) {
                        Buffer.put((ParticleIndex * (64 * 17)) + (17 * NeighborIndex) + i, Float.NaN);
                    }
                }
            }
        }
    }

    public static FloatBuffer GetParameterBuffer() {
        FloatBuffer Buffer = Stack.callocFloat(17);
        Buffer.put(0, Preferences.SmoothingRadius[0]);
        Buffer.put(1, Preferences.ParticleMass[0]);
        Buffer.put(2, Kernels.KernelRadiusPow1);
        Buffer.put(3, Kernels.KernelRadiusPow2);
        Buffer.put(4, Kernels.KernelRadiusPow6);
        Buffer.put(5, Kernels.KernelRadiusPow9);
        Buffer.put(6, Preferences.RestDensity[0]);
        Buffer.put(7, Preferences.Stiffness[0]);
        Buffer.put(8, Preferences.ParticleViscosity[0]);
        Buffer.put(9, Preferences.Gravity[0]);
        Buffer.put(10, Preferences.Gravity[1]);
        Buffer.put(11, Preferences.Gravity[2]);
        Buffer.put(12, Preferences.BoundarySize[0]);
        Buffer.put(13, Preferences.BoundarySize[1]);
        Buffer.put(14, Preferences.BoundarySize[2]);
        Buffer.put(15, Preferences.Timestep[0]);
        Buffer.put(16, Preferences.BoundaryElasticity[0]);
        return Buffer;
    }

    public static void GetParameterBuffer(FloatBuffer Buffer) {
        Buffer.put(0, Preferences.SmoothingRadius[0]);
        Buffer.put(1, Preferences.ParticleMass[0]);
        Buffer.put(2, Kernels.KernelRadiusPow1);
        Buffer.put(3, Kernels.KernelRadiusPow2);
        Buffer.put(4, Kernels.KernelRadiusPow6);
        Buffer.put(5, Kernels.KernelRadiusPow9);
        Buffer.put(6, Preferences.RestDensity[0]);
        Buffer.put(7, Preferences.Stiffness[0]);
        Buffer.put(8, Preferences.ParticleViscosity[0]);
        Buffer.put(9, Preferences.Gravity[0]);
        Buffer.put(10, Preferences.Gravity[1]);
        Buffer.put(11, Preferences.Gravity[2]);
        Buffer.put(12, Preferences.BoundarySize[0]);
        Buffer.put(13, Preferences.BoundarySize[1]);
        Buffer.put(14, Preferences.BoundarySize[2]);
        Buffer.put(15, Preferences.Timestep[0]);
    }

}
