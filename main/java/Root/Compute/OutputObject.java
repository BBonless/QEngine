package Root.Compute;

import java.nio.FloatBuffer;

public class OutputObject {
    public int CoresspondingArgumentIndex;
    public FloatBuffer Buffer;

    public OutputObject(int CAI, FloatBuffer BufferIn) {
        CoresspondingArgumentIndex = CAI;
        Buffer = BufferIn;
    }

    public float[] GetArray() {
        float[] Array = new float[Buffer.capacity()];
        Buffer.get(Array);
        return Array;
    }

    public void Print(int Width) {
        StringBuilder Output = new StringBuilder();
        for (int i = 0; i < Buffer.capacity(); i++) {
            if (i % Width == 0 && i != 0) {
                Output.append("\n");
            }

            Output.append(Buffer.get(i));
            Output.append(" ");
        }
        System.out.println(Output.toString());
    }
}
