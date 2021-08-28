package Root.Compute;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.lwjgl.PointerBuffer;

import java.nio.FloatBuffer;
import java.util.HashMap;

import static Root.Compute.GPU.*;
import static Root.Compute.InfoUtil.checkCLError;
import static org.lwjgl.opencl.CL10.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.apache.commons.io.*;

public class ComputeProgram {

    //Programs must have the same filename as the kernel's name!!!!
    public long Kernel;

    private static String LoadSource(String Path) throws IOException {
        return Files.readString(Paths.get(Path));
    }

    public ComputeProgram(long Context, String SourcePath) {
        String Source = null;
        try {
            Source = LoadSource(SourcePath);
        } catch (IOException E) {
            System.err.println("Could not read Program at: " + SourcePath + " !!");
            return;
        }
        String SourceFilename = FilenameUtils.getBaseName(SourcePath);

        long Program = clCreateProgramWithSource(Context, Source, null);
        checkCLError(
                clBuildProgram(Program, GPU.Device, "", null, NULL)
        );
        Kernel = clCreateKernel(Program, SourceFilename, ErrorcodeReturn);
        checkCLError(ErrorcodeReturn);
        clReleaseProgram(Program);
    }

    public int GlobalSize = 1;
    public int LocalSize = Integer.MIN_VALUE;
    public int Dimensions = 1;

    public HashMap<Integer,Long> MemoryObjects = new HashMap<Integer,Long>();

    public void CreateFloatBuffer(int ArgumentIndex, FloatBuffer Capacity, int Flags) {
        long MemoryObject = clCreateBuffer(Context, Flags, Capacity, ErrorcodeReturn);
        checkCLError(ErrorcodeReturn);
        MemoryObjects.put(ArgumentIndex, MemoryObject);
    }

    public void WriteFloatBuffer(int ArgumentIndex, FloatBuffer Data) {
        checkCLError(clEnqueueWriteBuffer(CommandQueue, MemoryObjects.get(ArgumentIndex), true, 0, Data, null, null));
    }

    public void CreateWriteFloatBuffer(int ArgumentIndex, FloatBuffer InitialData, int Flags) {
        CreateFloatBuffer(ArgumentIndex, InitialData, Flags);
        WriteFloatBuffer(ArgumentIndex, InitialData);
    }

    public void AutoSetKernelArgs() {
        MemoryObjects.forEach((ArgumentIndex, MemObject) -> {
            //System.out.println(Kernel + " " + ArgumentIndex + " " + MemObject);
            clSetKernelArg1p(Kernel, ArgumentIndex, MemObject);
        });
    }

    public void AutoEnqueue(int[] OutputArguementIndices, FloatBuffer... OutputBuffers) {
        if (OutputArguementIndices.length != OutputBuffers.length) {
            System.err.println("List of Argument Indices does not match amount of Buffers provided!!");
            return;
        }

        for (int i = 0; i < 4; i++) {
            System.out.println(i + " " + MemoryObjects.get(i));
        }

        PointerBuffer GlobalWorksizeBuffer = GPU.Stack.callocPointer(1);
        GlobalWorksizeBuffer.put(0, GlobalSize);

        PointerBuffer LocalWorksizeBuffer = GPU.Stack.callocPointer(1);
        LocalWorksizeBuffer.put(0, LocalSize);

        PointerBuffer KernelEvent = GPU.Stack.callocPointer(1);

        clEnqueueNDRangeKernel(
                CommandQueue,
                Kernel,
                Dimensions,
                null,
                GlobalWorksizeBuffer,
                LocalSize == Integer.MIN_VALUE ? null : LocalWorksizeBuffer,
                null,
                KernelEvent
        );
        clWaitForEvents(KernelEvent);

        for (int i = 0; i < OutputArguementIndices.length; i++) {
            clEnqueueReadBuffer(
                    CommandQueue,
                    MemoryObjects.get(OutputArguementIndices[i]),
                    true,
                    0,
                    OutputBuffers[i],
                    null,
                    null
            );
        }

        clFinish(CommandQueue);

        System.out.println("Kernel Finished Executing");
    }

}
