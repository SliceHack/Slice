package slice.util;

import com.labymedia.ultralight.UltralightJava;
import lombok.experimental.UtilityClass;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

/**
 * Gets Resource Locations from Strings.
 *
 * @author Nick
*/
@UtilityClass
public class ResourceUtil {

    /**
     * Gets a Resource Location from a String.
     * */
    public static ResourceLocation getResource(String resource) {
        return new ResourceLocation("slice/" + resource);
    }

    /**
     * Extracts a Resource from a String.
     * */
    private static boolean extractResource(String resourcePath, Path targetFile) throws IOException {
        InputStream stream = UltralightJava.class.getResourceAsStream(resourcePath);
        Throwable throwable = null;

        try {
            if (stream == null) return false;

            Path targetDir = targetFile.getParent();
            if (!Files.isDirectory(targetDir)) Files.createDirectories(targetDir);

            Files.copy(stream, targetFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (Throwable e) {
            throwable = e;
            throw e;
        } finally {
            if (stream != null) {
                if (throwable != null) {
                    try {
                        stream.close();
                    } catch (Throwable var13) {
                        throwable.addSuppressed(var13);
                    }
                } else {
                    stream.close();
                }
            }

        }

        return true;
    }
}
