package slice.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
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

    public static void downloadFile(String url, Path toPath) {
        try {
            URL website = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) website.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Files.copy(inputStream, toPath);
        } catch (Exception ignored){}
    }
}
