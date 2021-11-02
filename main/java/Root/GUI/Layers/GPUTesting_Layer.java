package Root.GUI.Layers;

import Root.Compute.GPU;
import Root.Compute.InfoUtil;
import Root.GUI.Layer;
import Root.Misc.Util.BufferU;
import Root.Misc.Util.PrintU;
import Root.Simulation.MemorySharing;
import Root.Simulation.SimEngine;
import imgui.ImGui;
import imgui.type.ImBoolean;

import java.nio.FloatBuffer;

import static Root.Compute.GPU.*;
import static org.lwjgl.opencl.CL10.*;

public class GPUTesting_Layer implements Layer {

    static FloatBuffer ParticleBuffer;
    static FloatBuffer NeighborBuffer;
    static FloatBuffer ParameterBuffer;

    public static ImBoolean Play = new ImBoolean(false);

    @Override
    public void Render_ImGUI() {
        ImGui.begin("GPU Testing", new ImBoolean(true));

        ImGui.text("Device Name: " + InfoUtil.getDeviceInfoStringUTF8(GPU.Device, CL_DEVICE_NAME) + '\n');

        ImGui.separator();

        if (ImGui.button("GPU Initialize Buffers")) {
            GPU_CREATE_BUFFER();
        }

        if (ImGui.button("GPU Step")) {
            GPU_TEST_STEP();
        }

        if (ImGui.checkbox("GPU Play?", Play)) {}

        ImGui.end();
    }

    private void GPU_CREATE_BUFFER() {
        ParticleBuffer = MemorySharing.GetParticleBuffer();
        NeighborBuffer = MemorySharing.GetNeighborBuffer();
        ParameterBuffer = MemorySharing.GetParameterBuffer();
        PrintU.FloatBuffer(ParameterBuffer, 0);

        int Flags = CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR;
        GPU.Programs.get("Solver").CreateFloatBuffer(0, ParticleBuffer, Flags);
        GPU.Programs.get("Solver").CreateFloatBuffer(1, NeighborBuffer, Flags);
        GPU.Programs.get("Solver").CreateFloatBuffer(2, ParameterBuffer, Flags);
        clFinish(CommandQueue);
    }

    public static void GPU_TEST_STEP() {
        GPU.Programs.get("Solver").WriteFloatBuffer(0, ParticleBuffer);
        GPU.Programs.get("Solver").WriteFloatBuffer(1, NeighborBuffer);
        GPU.Programs.get("Solver").WriteFloatBuffer(2, ParameterBuffer);

        FloatBuffer Output = Stack.callocFloat(ParticleBuffer.capacity());
        GPU.Programs.get("Solver").CreateWriteFloatBuffer(3, Output, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR);

        FloatBuffer OutPositions = Stack.callocFloat((ParticleBuffer.capacity() / 17) * 3);
        Programs.get("Solver").CreateWriteFloatBuffer(4, OutPositions, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR);
        clFinish(CommandQueue);

        Programs.get("Solver").AutoSetKernelArgs();
        Programs.get("Solver").Dimensions = 1;
        Programs.get("Solver").GlobalSize = ParticleBuffer.capacity() / 17;
        Programs.get("Solver").AutoEnqueue(
                new int[] {3, 4},
                Output, OutPositions
        );

        //PrintU.FloatBuffer(Output, 17);
        //PrintU.FloatBuffer(OutPositions, 3);

        float[] kakusei = BufferU.FloatBuffer2Array(OutPositions);


        SimEngine.FluidParticleObject.SetInstanced( kakusei );
        SimEngine.FluidParticleObject.Mesh.UpdateInstanceBuffer();

        Feedback(Output);

    }

    private static void Feedback(FloatBuffer Output) {
        ParticleBuffer = Output;
        //PrintU.FloatBufferDump(NeighborBuffer, 17);
        MemorySharing.GetNeighborBuffer(NeighborBuffer);
    }
}
