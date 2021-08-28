package Root.Compute;

import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CL;
import org.lwjgl.opencl.CLCapabilities;
import org.lwjgl.opencl.CLContextCallback;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.HashMap;

import static Root.Compute.InfoUtil.checkCLError;
import static org.lwjgl.opencl.CL10.*;
import static org.lwjgl.opencl.CL11.CL_DEVICE_OPENCL_C_VERSION;
import static org.lwjgl.system.MemoryUtil.*;

public class GPU {

    public static MemoryStack Stack;
    public static IntBuffer ErrorcodeReturn;

    public static long Platform;
    public static long Device;

    private static CLCapabilities PlatformCapabilities;
    private static CLCapabilities DeviceCapabilities;

    protected static long Context;
    private static CLContextCallback ContextCallback;

    public static long CommandQueue;
    
    public static HashMap<String,ComputeProgram> Programs = new HashMap<String,ComputeProgram>();

    public static void AddProgram(String Name, String Path) {
        ComputeProgram Program = new ComputeProgram(
                Context,
                Path
        );
        Programs.put(Name, Program);
    }


    private static void GetPlatformAndDevice(MemoryStack Stack) {
        IntBuffer PlatformCount = Stack.mallocInt(1);
        PointerBuffer AvailablePlatforms = Stack.mallocPointer(1);

        checkCLError(
                clGetPlatformIDs(AvailablePlatforms, PlatformCount) //2nd Arg nmly (IntBuffer)null
        );
        if (PlatformCount.get(0) == 0) {
            throw new RuntimeException("No OpenCL platforms found.");
        }

        Platform = AvailablePlatforms.get(0);
        PlatformCapabilities = CL.createPlatformCapabilities(Platform);

        IntBuffer DeviceCount = Stack.mallocInt(1);
        PointerBuffer AvailableDevices = Stack.mallocPointer(1);
        checkCLError(
                clGetDeviceIDs(Platform, CL_DEVICE_TYPE_ALL, AvailableDevices, DeviceCount)
        );
        if (DeviceCount.get(0) == 0) {
            throw new RuntimeException("No OpenCL devices found.");
        }

        Device = AvailableDevices.get(0);
        DeviceCapabilities = CL.createDeviceCapabilities(Device, PlatformCapabilities);
    }

    private static PointerBuffer GetContextProperties(MemoryStack Stack) {
        PointerBuffer ContextProperties = Stack.mallocPointer(3);

        ContextProperties
                .put(0, CL_CONTEXT_PLATFORM)
                .put(1, Platform)
                .put(2, 0)
        ;

        return ContextProperties;
    }

    private static void GetContextCallback() {
        ContextCallback = CLContextCallback.create((errinfo, private_info, cb, user_data) -> {
            System.err.println("[LWJGL] cl_context_callback");
            System.err.println("\tInfo: " + memUTF8(errinfo));
        });
    }

    /*public static x() {
        System.out.println(SimEngine.Particles[0].Position);
        FloatBuffer In = SimEngine.Particles[0].GetBuffer(Stack);
        //FloatBuffer In = Stack.callocFloat(9);
        FloatBuffer In2 = Stack.callocFloat(17 * 64);
        for (int i = 0; i < 64; i++) {
            if (i < SimEngine.Particles[0].Neighbors.size()) {
                FloatBuffer Penis = SimEngine.Particles[0].Neighbors.get(i).GetBuffer(Stack);

                In2.put(17 * i + 0, Penis.get(0));
                In2.put(17 * i + 1, Penis.get(1));
                In2.put(17 * i + 2, Penis.get(2));

                In2.put(17 * i + 3, Penis.get(3));
                In2.put(17 * i + 4, Penis.get(4));
                In2.put(17 * i + 5, Penis.get(5));

                In2.put(17 * i + 6, Penis.get(6));
                In2.put(17 * i + 7, Penis.get(7));
                In2.put(17 * i + 8, Penis.get(8));

                In2.put(17 * i + 9, Penis.get(9));
                In2.put(17 * i + 10, Penis.get(10));
                In2.put(17 * i + 11, Penis.get(11));

                In2.put(17 * i + 12, Penis.get(12));
                In2.put(17 * i + 13, Penis.get(13));
                In2.put(17 * i + 14, Penis.get(14));

                In2.put(17 * i + 15, Penis.get(15));

                In2.put(17 * i + 16, Penis.get(16));
            }
            else {
                for (int j = 0; j < 17; j++) {
                    In2.put(17 * i + j, Float.NaN);
                }
            }
        }
        FloatBuffer In3 = Stack.callocFloat(16);
        In3.put(0, Preferences.SmoothingRadius);
        In3.put(1, Preferences.ParticleMass);
        In3.put(2, Kernels.KernelRadiusPow1);
        In3.put(3, Kernels.KernelRadiusPow2);
        In3.put(4, Kernels.KernelRadiusPow6);
        In3.put(5, Kernels.KernelRadiusPow9);
        In3.put(6, Preferences.RestDensity);
        In3.put(7, Preferences.Stiffness);
        In3.put(8, Preferences.ParticleViscosity);
        In3.put(9, Preferences.Gravity.x);
        In3.put(10, Preferences.Gravity.y);
        In3.put(11, Preferences.Gravity.z);
        In3.put(12, Preferences.BoundarySize.x);
        In3.put(13, Preferences.BoundarySize.y);
        In3.put(14, Preferences.BoundarySize.z);
        In3.put(15, Preferences.Timestep);
        FloatBuffer Out = Stack.callocFloat(17);

        //Allocate Memory
        long InMEM = clCreateBuffer(Context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, In, ErrorcodeReturn);
        clEnqueueWriteBuffer(CommandQueue, InMEM, true, 0, In, null, null);
        checkCLError(ErrorcodeReturn);

        long In2MEM = clCreateBuffer(Context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, In2, ErrorcodeReturn);
        clEnqueueWriteBuffer(CommandQueue, In2MEM, true, 0, In2, null, null);
        checkCLError(ErrorcodeReturn);

        long In3MEM = clCreateBuffer(Context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, In3, ErrorcodeReturn);
        clEnqueueWriteBuffer(CommandQueue, In3MEM, true, 0, In3, null ,null);
        checkCLError(ErrorcodeReturn);

        long OutMEM = clCreateBuffer(Context, CL_MEM_WRITE_ONLY | CL_MEM_COPY_HOST_PTR, Out, ErrorcodeReturn);
        clEnqueueWriteBuffer(CommandQueue, OutMEM, true, 0, Out, null, null);
        checkCLError(ErrorcodeReturn);

        clFinish(CommandQueue);


    }*/

    public static void PrintInfo() {
        StringBuilder SB = new StringBuilder();

        SB.append("Compute Device successfully initialized!" + '\n' + '\n');

        SB.append("Compute Device Information:" + '\n');

        SB.append("Name: " + InfoUtil.getDeviceInfoStringUTF8(Device, CL_DEVICE_NAME) + '\n');

        SB.append("Type: ");
        long Type = InfoUtil.getDeviceInfoLong(Device, CL_DEVICE_TYPE);
        switch ((int)Type) {
            case CL_DEVICE_TYPE_GPU:
                SB.append("GPU" + '\n'); break;
            case CL_DEVICE_TYPE_CPU:
                SB.append("CPU" + '\n'); break;
            default:
                SB.append("Unknown" + '\n'); break;
        }

        SB.append("Device Version: " + InfoUtil.getDeviceInfoStringUTF8(Device, CL_DEVICE_VERSION) + '\n');

        SB.append("Language Version: " + InfoUtil.getDeviceInfoStringUTF8(Device, CL_DEVICE_OPENCL_C_VERSION) + '\n');

        SB.append("Maximum Compute Units: " + InfoUtil.getDeviceInfoInt(Device, CL_DEVICE_MAX_COMPUTE_UNITS) + " Units" + '\n');

        SB.append("Maximum Workitem Dimension: " + InfoUtil.getDeviceInfoInt(Device, CL_DEVICE_MAX_WORK_ITEM_DIMENSIONS) + 'D' + '\n');

        SB.append("Maximum Workgroup Size: " + InfoUtil.getDeviceInfoLong(Device, CL_DEVICE_MAX_WORK_GROUP_SIZE) + '\n');

        SB.append("Maximum Clock Frequency: " + InfoUtil.getDeviceInfoInt(Device, CL_DEVICE_MAX_CLOCK_FREQUENCY) + " Hz" + '\n');

        SB.append("Allocated Stack Size: " + Stack.getSize() + " Bytes" + '\n');

        System.out.println(SB);

    }

    public static void Init() {
        Stack = MemoryStack.create(500_000_000); //500 MB

        ErrorcodeReturn = Stack.callocInt(1);

        GetPlatformAndDevice(Stack);

        GetContextCallback();

        Context = clCreateContextFromType(GetContextProperties(Stack), CL_DEVICE_TYPE_GPU, null, 0, ErrorcodeReturn);
        checkCLError(ErrorcodeReturn);

        CommandQueue = clCreateCommandQueue(Context, Device, CL_QUEUE_PROFILING_ENABLE, ErrorcodeReturn);
        checkCLError(ErrorcodeReturn);

        PrintInfo();
    }

    public static void Dispose() {
        Programs.forEach((Name, Program) -> {
            Program.MemoryObjects.forEach((Key, MemObject) -> {
                clReleaseMemObject(MemObject);
            });
            Program.MemoryObjects.clear();

            clReleaseKernel(Program.Kernel);
        });
        Programs.clear();

        clReleaseCommandQueue(CommandQueue);
        clReleaseContext(Context);
        CL.destroy();

        try {
            Stack.close();
        } catch (Exception E) {}
    }
}
