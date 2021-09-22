package Root.Misc.Util;

import java.nio.FloatBuffer;

public class BufferU {

    public static float[] FloatBuffer2Array(FloatBuffer Buffer) {
        float[] Result = new float[Buffer.capacity()];
        for (int i = 0; i < Result.length; i++) {
            Result[i] = Buffer.get(i);
        }
        return Result;
    }

}
