package Root.Misc.Util;

import Root.Environment;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.FloatBuffer;

public class PrintU {

    public static void FloatArray(float[] Array, int Width) {
        int WidthCounter = 0;
        for (float Value : Array) {
            if (WidthCounter % Width == 0 && WidthCounter != 0) {
                System.out.println();
                WidthCounter = 0;
            }
            System.out.print(Value + " ");
            WidthCounter++;
        }
        System.out.println();
    }

    //Prints out the inputted arrays (of an equal size) in a grid
    public static void FloatArrays(int Width, float[]... Arrays) {
        for (int i = 0; i < Arrays[0].length / Width; i++) {
            for (float[] Array : Arrays) {
                for (int j = 0; j < Width; j++) {
                    System.out.format("%3f", Array[(i * Width) + j]);
                    System.out.print(" ");
                }
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public static void FloatBuffer(FloatBuffer Buffer, int Width) {
        int WidthCounter = 0;
        for (int i = 0; i < Buffer.capacity(); i++) {
            if (WidthCounter % Width == 0 && WidthCounter != 0) {
                System.out.println();
                WidthCounter = 0;
            }
            System.out.print(Buffer.get(i) + " ");
            WidthCounter++;
        }
        System.out.println();
    }

    public static void FloatBufferDump(FloatBuffer Buffer, int Width) {
        StringBuilder SB = new StringBuilder();
        int WidthCounter = 0;
        for (int i = 0; i < Buffer.capacity(); i++) {
            if (WidthCounter % Width == 0 && WidthCounter != 0) {
                SB.append('\n');
                WidthCounter = 0;
            }
            SB.append(Buffer.get(i) + " ");
            WidthCounter++;
        }
        try {
            BufferedWriter BW = new BufferedWriter(
                    new FileWriter("C:\\Users\\quent\\Desktop\\Dump\\Dumptruck" + (Environment.Random.nextInt(1000000)) + ".txt")
            );
            BW.write(SB.toString());
            BW.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Dump Complete!");
    }
}
