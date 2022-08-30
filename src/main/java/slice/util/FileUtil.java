package slice.util;

import lombok.experimental.UtilityClass;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.zip.ZipInputStream;

/**
 * Utility class for file operations.
 *
 * @author Nick
 * */
@UtilityClass
public class FileUtil {

    /***
     * Extracts a zip file to a directory.
     *
     * @param outDir The directory to extract to.
     * */
    public static void extractZip(ZipInputStream zipIn, File outDir) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(outDir.toPath()));
            byte[] bytesIn = new byte[4096];
            int read = 0;
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
            bos.close();
        } catch (Exception ignored){}
    }

    /**
     * Returns a ZipInputStream for a zip file.
     *
     * @param file The zip file to read.
     * */
    public static ZipInputStream getZipInputStream(File file) {
        try {
            return new ZipInputStream(Files.newInputStream(file.toPath()));
        } catch (Exception ignored) {}
        return null;
    }
}
