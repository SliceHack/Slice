package slice.util;

import com.labymedia.ultralight.UltralightJava;
import com.labymedia.ultralight.UltralightLoadException;
import com.labymedia.ultralight.gpu.UltralightGPUDriverNativeUtil;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
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

    public static void copyFolder(Path source, Path target, CopyOption... options) throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Files.createDirectories(target.resolve(source.relativize(dir).toString()));
                System.out.println("Creating directory " + target.resolve(source.relativize(dir).toString()));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                Files.copy(file, target.resolve(source.relativize(file).toString()), options);
                System.out.println("Copying " + file + " to " + target.resolve(source.relativize(file).toString()));
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
