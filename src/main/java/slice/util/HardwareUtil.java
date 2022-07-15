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
        String username = System.getProperty("user.name"),
                version = System.getProperty("os.version"),
                osArch = System.getProperty("os.arch"),
                osVersion = System.getProperty("os.version"),
                osName = System.getProperty("os.name"),
                dir = System.getProperty("user.home"),

                cpuVendor = System.getenv("PROCESSOR_IDENTIFIER"),
                cpuName = System.getenv("PROCESSOR_ARCHITECTURE"),
                cpuArch = System.getenv("PROCESSOR_ARCHITECTURE"),
                cpuVersion = System.getenv("PROCESSOR_LEVEL"),
                memory = System.getenv("PROCESSOR_LEVEL");

        String toEncrypt = username + version + osArch + osVersion + osName + dir + cpuVendor + cpuName + cpuArch + cpuVersion + memory;
        byte[] encodedBytes = Base64.getEncoder().encode(toEncrypt.getBytes());
        return new String(encodedBytes);
    }
}
