/*
package Root.Compute;

import org.lwjgl.*;
import org.lwjgl.opencl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.*;

import static Root.Compute.InfoUtil.*;
import static org.lwjgl.opencl.CL10.*;
import static org.lwjgl.system.MemoryStack.*;

public class GPU2 {

    private static ArrayList<Long> Platforms = null;

    private static long Platform = 0;
    private static CLCapabilities PlatformCapabilities = null;
    private static String PlatformID = null;





    public static void Main() {
        try (MemoryStack Stack = stackPush()) {
            Platforms = GetPlatformList(Stack);

            Platforms.sort((Platform1, Platform2) -> {
                //Prefer platforms that support GPUs
                boolean GPU1 = !GetDevices(Platform1, CL_DEVICE_TYPE_GPU).isEmpty();
                boolean GPU2 = !GetDevices(Platform2, CL_DEVICE_TYPE_GPU).isEmpty();

                int Comparator = GPU1 == GPU2 ? 0 : (GPU1 ? -1 : 1);

                if (Comparator != 0) { return Comparator; }

                return getPlatformInfoStringUTF8(Platform1, CL_PLATFORM_VENDOR).compareTo(getPlatformInfoStringUTF8(Platform2, CL_PLATFORM_VENDOR));
            });

            Platform = Platforms.get(0);
            PlatformCapabilities = CL.createPlatformCapabilities(Platform);
            PlatformID = getPlatformInfoStringUTF8(Platform, CL_PLATFORM_VENDOR);

            boolean CPU = false;
            boolean GPU = false;

            for (Long Device : GetDevices(Platform, CL_DEVICE_TYPE_ALL)) {
                long Type = getDeviceInfoLong(Device, CL_DEVICE_TYPE);

                if (Type == CL_DEVICE_TYPE_CPU) {
                    CPU = true;
                } else if (Type == CL_DEVICE_TYPE_GPU) {
                    GPU = true;
                }
            }

            System.out.println(CPU);
            System.out.println(GPU);

        }
    }
}
*/
