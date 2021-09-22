/*
package Root.Compute;

import org.lwjgl.BufferUtils;

import java.awt.*;
import java.lang.reflect.Array;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.*;
import org.lwjgl.system.MemoryStack;


import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static Root.Compute.InfoUtil.checkCLError;
import static org.lwjgl.opencl.CL10.*;
import static org.lwjgl.opencl.CL.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class GPU3 {

    static final FloatBuffer a = FloatBuffer.wrap(new float[] {1,2,3,4,5,6,7,8,9,10});
    static final FloatBuffer b = FloatBuffer.wrap(new float[] {10,9,8,7,6,5,4,3,2,1});
    static final FloatBuffer an = BufferUtils.createFloatBuffer(a.capacity());

   */
/* private static ArrayList<Long> Platforms = null;
    private static long Platform = 0;
    private static CLCapabilities PlatformCapabilities = null;
    private static ArrayList<Long> Devices = null;
    private static long Device = 0;*//*


    //private static IntBuffer ErrorCodeReturn = IntBuffer.allocate(1);

    //region Get Platforms
    public static IntBuffer GetPlatformCount(MemoryStack Stack) {
        IntBuffer PlatformCount = Stack.mallocInt(1);
        checkCLError(clGetPlatformIDs(null, PlatformCount));
        if (PlatformCount.get(0) == 0) {
            throw new RuntimeException("No OpenCL platforms found.");
        }
        return PlatformCount;
    }

    public static PointerBuffer GetPlatformIDs(MemoryStack Stack) {
        IntBuffer PlatformCount = GetPlatformCount(Stack);
        PointerBuffer PlatformIDs = Stack.mallocPointer(PlatformCount.get(0));
        checkCLError(clGetPlatformIDs(PlatformIDs, (IntBuffer)null));

        return PlatformIDs;
    }

    public static ArrayList<Long> GetPlatformList(MemoryStack Stack) {
        PointerBuffer PlatformIDs = GetPlatformIDs(Stack);

        ArrayList<Long> List = new ArrayList<>(PlatformIDs.capacity());

        for (int i = 0; i < PlatformIDs.capacity(); i++) {

            long Platform = PlatformIDs.get(i);
            CLCapabilities Capabillities = CL.createPlatformCapabilities(Platform);

            if (Capabillities.cl_khr_gl_sharing || Capabillities.cl_APPLE_gl_sharing) {
                List.add(Platform);
            }
        }

        if (List.isEmpty()) {
            throw new IllegalStateException("No OpenCL platform found that supports OpenGL context sharing!");
        }
        else {
            return List;
        }
    }
    //endregion

    public static ArrayList<Long> GetDevices(long Platform, int DeviceType) {
        ArrayList<Long> Devices;

        try (MemoryStack Stack = stackPush()) {
            IntBuffer DeviceCount = Stack.mallocInt(1);
            int ErrorCode = clGetDeviceIDs(Platform, DeviceType, null, DeviceCount);

            if (ErrorCode == CL_DEVICE_NOT_FOUND) {
                Devices = new ArrayList<>();
            } else {
                checkCLError(ErrorCode);

                PointerBuffer DeviceIDs = Stack.mallocPointer(DeviceCount.get(0));
                checkCLError(clGetDeviceIDs(Platform, DeviceType, DeviceIDs, (IntBuffer)null));

                Devices = new ArrayList<>(DeviceIDs.capacity());

                for (int i = 0; i < DeviceIDs.capacity(); i++) {
                    Devices.add(DeviceIDs.get(i));
                }
            }
        }

        return Devices;
    }

    public static void Init() {
        try (MemoryStack Stack = stackPush()) {

        }
    }

    */
/*private static PointerBuffer GetContextProperties() {
        try (MemoryStack Stack = stackPush()) {

            return ContextProperties;
        }
    }*//*


    public static void Main() {
        try (MemoryStack Stack = stackPush()) {

             ArrayList<Long> Platforms = null;
             long Platform = 0;
             CLCapabilities PlatformCapabilities = null;
             ArrayList<Long> Devices = null;
             long Device = 0;
            
            IntBuffer ErrorCodeReturn = Stack.callocInt(1);

            //region Init
            Platforms = GetPlatformList(Stack);
            Platform = Platforms.get(0);
            PlatformCapabilities = CL.createPlatformCapabilities(Platform);

            System.out.println("Platform #1 - " + InfoUtil.getPlatformInfoStringUTF8(Platform, CL_PLATFORM_NAME));

            Devices = GetDevices(Platform, CL_DEVICE_TYPE_GPU);
            Device = Devices.get(0);

            InfoUtil.PrintDeviceInfo(Device);
            //endregion

            CLContextCallback ContextCallback;

            PointerBuffer ContextProperties = Stack.mallocPointer(3);
            ContextProperties
                    .put(0, CL_CONTEXT_PLATFORM)
                    .put(1, Platform)
                    .put(2, 0)
            ;

            long Context = clCreateContext(ContextProperties, Device, CLContextCallback.create((ErrorInfo, PrivateInfo, CB, UserData) -> {
                System.out.println("[LWJGL] CL Context Callback");
                System.out.println("Info: " + memUTF8(ErrorInfo));
            }), NULL, ErrorCodeReturn);
            checkCLError(ErrorCodeReturn);

            long CommandQueue = clCreateCommandQueue(Context, Device, CL_QUEUE_PROFILING_ENABLE, ErrorCodeReturn);
            checkCLError(ErrorCodeReturn);

            //Allocating Memory
            long aMem = clCreateBuffer(Context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, a, ErrorCodeReturn);
            clEnqueueWriteBuffer(CommandQueue, aMem, true, 0, a, null, null);
            checkCLError(ErrorCodeReturn);

            long bMem = clCreateBuffer(Context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, b, ErrorCodeReturn);
            clEnqueueWriteBuffer(CommandQueue, bMem, true, 0, b, null, null);
            checkCLError(ErrorCodeReturn);

            long anMem = clCreateBuffer(Context, CL_MEM_WRITE_ONLY | CL_MEM_COPY_HOST_PTR, an, ErrorCodeReturn);
            checkCLError(ErrorCodeReturn);

            clFinish(CommandQueue);

            //Load program
            String Source = ComputeProgram.LoadSource("C:\\Users\\quent\\IdeaProjects\\QEngine 5.0\\src\\main\\java\\Root\\Compute\\sum.cl");

            //Create program
            long Program = clCreateProgramWithSource(Context, Source, ErrorCodeReturn);
            checkCLError(ErrorCodeReturn);

            checkCLError(clBuildProgram(Program, Device, "", null, NULL));

            long Kernel = clCreateKernel(Program, "sum", ErrorCodeReturn);
            checkCLError(ErrorCodeReturn);

            //Execute Kernel
            PointerBuffer Kernel1DGlobalWorkSize = BufferUtils.createPointerBuffer(1);
            Kernel1DGlobalWorkSize.put(0, a.capacity());
            clSetKernelArg(Kernel, 0, aMem);
            clSetKernelArg(Kernel, 1, bMem);
            clSetKernelArg(Kernel, 2, anMem);
            clEnqueueNDRangeKernel(CommandQueue, Kernel, 1, null, Kernel1DGlobalWorkSize, null, null, null);

            //Read
            clEnqueueReadBuffer(CommandQueue, anMem, true, 0, an, null, null);
            clFinish(CommandQueue);

            //Print
            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            StringBuilder sb3 = new StringBuilder();
            for (int i = 0; i < a.capacity(); i++) {
                sb1.append(a.get(i));
                sb2.append(b.get(i));
                sb3.append(an.get(i));
            }
            System.out.println(sb1.toString());
            System.out.println(sb2.toString());
            System.out.println(sb3.toString());

            clReleaseKernel(Kernel);
            clReleaseProgram(Program);
            clReleaseMemObject(aMem);
            clReleaseMemObject(bMem);
            clReleaseMemObject(anMem);
            clReleaseCommandQueue(CommandQueue);
            clReleaseContext(Context);
            CL.destroy();
        }
    }
}
*/
