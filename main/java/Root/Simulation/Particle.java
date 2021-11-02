package Root.Simulation;

import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.ArrayList;

public class Particle {

    public Vector3f Position = new Vector3f(0,0,0);

    public Vector3f Velocity = new Vector3f(0,0,0);
    public Vector3f PastAcceleration = new Vector3f(0,0,0);
    public Vector3f Force = new Vector3f(0,0,0);

    public float Pressure = 0;
    public float Density = 0;

    public ArrayList<Particle> Neighbors = new ArrayList<>();

    public boolean DoNotMeshify = false;

    public FloatBuffer GetBuffer(MemoryStack Stack) {
        FloatBuffer Buffer = Stack.callocFloat(17);

        Buffer.put(0, Position.x);
        Buffer.put(1, Position.y);
        Buffer.put(2, Position.z);

        Buffer.put(6, Velocity.x);
        Buffer.put(7, Velocity.y);
        Buffer.put(8, Velocity.z);

        Buffer.put(9, PastAcceleration.x);
        Buffer.put(10, PastAcceleration.y);
        Buffer.put(11, PastAcceleration.z);

        Buffer.put(12, Force.x);
        Buffer.put(13, Force.y);
        Buffer.put(14, Force.z);

        Buffer.put(15, Pressure);

        Buffer.put(16, Density);

        return Buffer;
    }
}
