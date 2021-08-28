package Root.Misc.Util;

import java.nio.FloatBuffer;

public class BufferU {

    public static float[] FloatBuffer2Array(FloatBuffer Buffer) {
        float[] Result = new float[Buffer.capacity()];
        Buffer.put(Result);
        return Result;
    }

}
