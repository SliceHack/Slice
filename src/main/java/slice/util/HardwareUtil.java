package slice.util;

import lombok.experimental.UtilityClass;

import java.util.Base64;

/**
 *  Utility class to compute the hardware ID of the computer.
 *
 *  @author Nick
 * */
@UtilityClass
public class HardwareUtil {

    /**
     * Gets the hardware ID of the computer.
     * */
    public static String getHardwareID() {
        String username = System.getProperty("user.name");
        String version = System.getProperty("os.version");
        String osArch = System.getProperty("os.arch");

        String osVersion = System.getProperty("os.version");
        String osName = System.getProperty("os.name");
        String dir = System.getProperty("user.home");

        String cpuVendor = System.getenv("PROCESSOR_IDENTIFIER");
        String cpuName = System.getenv("PROCESSOR_ARCHITECTURE");
        String cpuArch = System.getenv("PROCESSOR_ARCHITECTURE");
        String cpuVersion = System.getenv("PROCESSOR_LEVEL");
        String memory = System.getenv("PROCESSOR_LEVEL");

        String toEncrypt = username + version + osArch + osVersion + osName + dir + cpuVendor + cpuName + cpuArch + cpuVersion + memory;
        byte[] encodedBytes = Base64.getEncoder().encode(toEncrypt.getBytes());
        return new String(encodedBytes);
    }
}
