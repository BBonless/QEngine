package Root.GUI.Layers;

import Root.Compute.GPU;
import Root.Compute.InfoUtil;
import Root.GUI.Layer;
import Root.Misc.Util.BufferU;
import Root.Misc.Util.PrintU;
import Root.Simulation.MemorySharing;
import imgui.ImGui;
import imgui.type.ImBoolean;

import java.nio.FloatBuffer;

import static Root.Compute.GPU.*;
import static org.lwjgl.opencl.CL10.*;

public class GPUTesting_Layer implements Layer {

    FloatBuffer ParticleBuffer;
    FloatBuffer NeighborBuffer;
    FloatBuffer ParameterBuffer;
    FloatBuffer OutputBuffer;

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

        ImGui.end();
    }

    private void GPU_CREATE_BUFFER() {
        ParticleBuffer = MemorySharing.GetParticleBuffer();
        NeighborBuffer = MemorySharing.GetNeighborBuffer();
        ParameterBuffer = MemorySharing.GetParameterBuffer();
        OutputBuffer = Stack.callocFloat(ParticleBuffer.capacity());

        int Flags = CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR;
        GPU.Programs.get("Solver").CreateFloatBuffer(0, ParticleBuffer, Flags);
        GPU.Programs.get("Solver").CreateFloatBuffer(1, NeighborBuffer, Flags);
        GPU.Programs.get("Solver").CreateFloatBuffer(2, ParameterBuffer, Flags);
        //GPU.Programs.get("Solver").CreateFloatBuffer(3, OutputBuffer, Flags);
        clFinish(CommandQueue);
    }

    private void GPU_TEST_STEP() {
        GPU.Programs.get("Solver").WriteFloatBuffer(0, ParticleBuffer);
        //PrintU.FloatBuffer(ParticleBuffer, 17);
        GPU.Programs.get("Solver").WriteFloatBuffer(1, NeighborBuffer);
        //PrintU.FloatBufferDump(NeighborBuffer, 17 * 64);
        GPU.Programs.get("Solver").WriteFloatBuffer(2, ParameterBuffer);
        //PrintU.FloatBuffer(ParameterBuffer, 17);
        //GPU.Programs.get("Solver").WriteFloatBuffer(3, OutputBuffer);
        FloatBuffer cum = Stack.callocFloat(ParticleBuffer.capacity());
        Programs.get("Solver").CreateWriteFloatBuffer(3, cum, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR);
        //PrintU.FloatBuffer(OutputBuffer, 17);
        clFinish(CommandQueue);

        Programs.get("Solver").AutoSetKernelArgs();
        Programs.get("Solver").Dimensions = 1;
        Programs.get("Solver").GlobalSize = ParticleBuffer.capacity() / 17;
        Programs.get("Solver").AutoEnqueue(
                new int[] {3},
                cum
        );

        PrintU.FloatBuffer(cum, 17);
        //PrintU.FloatBuffer(OutputBuffer, 17);

    }
}
