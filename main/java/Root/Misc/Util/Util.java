package Root.Misc.Util;

import Root.Simulation.Particle;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
import java.util.stream.Collectors;

public class Util {
    static Random R = new Random();

    public static float[] FloatArrList2Arr(ArrayList<Float> Input) {
        float[] Output = new float[Input.size()];

        for (int i = 0; i < Input.size(); i++) {
            Output[i] = Input.get(i);
        }
        
        return Output;
    }

    public static int[] IntArrList2Arr(ArrayList<Integer> Input) {
        int[] Output = new int[Input.size()];

        for (int i = 0; i < Input.size(); i++) {
            Output[i] = Input.get(i);
        }

        return Output;
    }

    public static Vector3f[] VecArrList2Arr(ArrayList<Vector3f> Input) {
        Vector3f[] Output = new Vector3f[Input.size()];

        for (int i = 0; i < Input.size(); i++) {
            Output[i] = Input.get(i);
        }

        return Output;
    }

    public static <T> T[] ArrayList2Array(ArrayList<T> Input, Class<T> Type) {
        @SuppressWarnings("unchecked")
        T[] Output = (T[]) Array.newInstance(
                Type,
                Input.size()
        );

        for (int i = 0; i < Input.size(); i++) {
            Output[i] = Input.get(i);
        }

        return Output;
    }

    public static float DistanceSquared(Particle P1, Particle P2) {
        Vector3f DeltaPos = new Vector3f(0,0,0);
        P1.Position.sub(P2.Position, DeltaPos);
        return DeltaPos.lengthSquared();
    }

    public static Vector3f RandomVector(float Reach) {
        float X = (R.nextFloat() * (Reach*2)) - Reach;
        float Y = (R.nextFloat() * (Reach*2)) - Reach;
        float Z = (R.nextFloat() * (Reach*2)) - Reach;
        return new Vector3f(X,Y,Z);
    }

    public static Vector3f FloatArrToVec(float[] Array) {
        Vector3f Result = new Vector3f();
        Result.x = Array[0];
        Result.y = Array[1];
        Result.z = Array[2];
        return Result;
    }

    public static float[] VecToFloatArr(Vector3f Vector) {
        float[] Result = new float[] {0,0,0};
        Result[0] = Vector.x;
        Result[1] = Vector.y;
        Result[2] = Vector.z;
        return Result;
    }

    public static String InputStreamToString(InputStream Stream) {
        BufferedReader BR = new BufferedReader(new InputStreamReader(Stream, StandardCharsets.UTF_8));

        return BR.lines().collect(Collectors.joining("\n"));
    }

    private static char[] IllegalPathCharacters = new char[] {'"'};
    public static String CorrectPath(String Path) {
        StringBuilder SB = new StringBuilder();

        for (char Current : Path.toCharArray()) {
            boolean Clear = true;
            for (char Illegal : IllegalPathCharacters) {
                if (Current == Illegal) {
                    Clear = false;
                    break;
                }
            }
            if (Clear) {
                SB.append(Current);
            }
        }

        return SB.toString();
    }

}
