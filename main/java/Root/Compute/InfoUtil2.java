package Root.Compute;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;

import static Root.Compute.InfoUtil.checkCLError;
import static org.lwjgl.opencl.CL10.clGetPlatformInfo;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.memUTF8;

public class InfoUtil2 {
    public static String getPlatformInfoStringUTF8(long cl_platform_id, int param_name) {
        try (MemoryStack Stack = stackPush()) {
            PointerBuffer pp = Stack.mallocPointer(1);
            checkCLError(clGetPlatformInfo(cl_platform_id, param_name, (ByteBuffer)null, pp));
            int bytes = (int)pp.get(0);

            ByteBuffer buffer = Stack.malloc(bytes);
            checkCLError(clGetPlatformInfo(cl_platform_id, param_name, buffer, null));

            return memUTF8(buffer, bytes - 1);
        }

    }
}
