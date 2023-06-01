package com.sliceclient.ultralight;

import com.labymedia.ultralight.UltralightJava;
import com.labymedia.ultralight.UltralightLoadException;
import com.labymedia.ultralight.gpu.UltralightGPUDriverNativeUtil;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.utils.CountingInputStream;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
@UtilityClass
public class ResourceManager {

    private static final String  LIBRARY_VERSION = "b8daecd";

    public static final File ultraLightDir = new File(Minecraft.getMinecraft().mcDataDir, "Slice\\ultralight"),
            binDir = new File(ultraLightDir, "bin"),
            resourceDir = new File(ultraLightDir, "resources"),
            cacheDir = new File(ultraLightDir, "cache");

    private static List<Integer> printedValues = new ArrayList<>();

    public static void downloadUltralight() throws URISyntaxException, UltralightLoadException, IOException {
        try {
            File VERSION = new File(ultraLightDir, "VERSION");
            if (VERSION.exists() && VERSION.isFile()) {
                String version = new String(Files.readAllBytes(VERSION.toPath()));
                if (version.equals(LIBRARY_VERSION)) {
                    return;
                }
            }
            if (binDir.exists() && binDir.isDirectory()) {
                FileUtils.deleteDirectory(binDir);
            }

            binDir.mkdirs();

            if (resourceDir.exists() && resourceDir.isDirectory()) {
                FileUtils.deleteDirectory(resourceDir);
            }

            resourceDir.mkdirs();

            if (cacheDir.exists() && cacheDir.isDirectory()) {
                FileUtils.deleteDirectory(cacheDir);
            }

            cacheDir.mkdirs();

            String os;
            String osName = System.getProperty("os.name").toLowerCase();
            if (osName.contains("win")) os = "win";
            else if (osName.contains("mac")) os = "mac";
            else os = "linux";

            URL url = new URL(String.format("https://ultralight-sdk.sfo2.cdn.digitaloceanspaces.com/ultralight-sdk-%s-%s-x64.7z", LIBRARY_VERSION, os));

            File file = new File(ultraLightDir, "ultralight-sdk.7z");

            System.out.println(file.getAbsolutePath());
            System.out.println(url);

            if (file.exists() && file.isFile()) file.delete();
            if (file.getParentFile().exists()) file.getParentFile().mkdirs();
            file.createNewFile();

            HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
            long completeFileSize = httpConnection.getContentLength();
            try (InputStream inputStream = url.openStream();
                 FileOutputStream fos = new FileOutputStream(file);
                 CountingInputStream cis = new CountingInputStream(inputStream)) {

                byte[] buffer = new byte[4096];
                int len;
                while ((len = cis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);

                    int percent = (int) (cis.getBytesRead() * 100 / completeFileSize);
                    if(!printedValues.contains(percent)) {
                        System.out.println("Download:  " + percent + "%");
                    }
                    printedValues.add(percent);

                }
            }

            try (SevenZFile sevenZFile = new SevenZFile(file)) {
                SevenZArchiveEntry entry;
                while ((entry = sevenZFile.getNextEntry()) != null) {
                    if (entry.getName().startsWith("bin/")) {
                        File dest = new File(binDir, entry.getName().substring(4));
                        if (dest.exists() && dest.isFile()) {
                            dest.delete();
                        }
                        if (dest.getParentFile().exists() || dest.getParentFile().mkdirs()) {
                            try (FileOutputStream fos = new FileOutputStream(dest);
                                 BufferedOutputStream bos = new BufferedOutputStream(fos)) {
                                byte[] buffer = new byte[1024];
                                int len;
                                while ((len = sevenZFile.read(buffer)) > 0) {
                                    bos.write(buffer, 0, len);
                                }
                            }
                        }
                    } else if (entry.getName().startsWith("resources/")) {
                        File dest = new File(resourceDir, entry.getName().substring(10));
                        if (dest.exists() && dest.isFile()) {
                            dest.delete();
                        }
                        if (dest.getParentFile().exists() || dest.getParentFile().mkdirs()) {
                            try (FileOutputStream fos = new FileOutputStream(dest);
                                 BufferedOutputStream bos = new BufferedOutputStream(fos)) {
                                byte[] buffer = new byte[1024];
                                int len;
                                while ((len = sevenZFile.read(buffer)) > 0) {
                                    bos.write(buffer, 0, len);
                                }
                            }
                        }
                    }
                }
            }

            file.delete();

            UltralightJava.extractNativeLibrary(binDir.toPath());
            UltralightGPUDriverNativeUtil.extractNativeLibrary(binDir.toPath());

            Files.write(VERSION.toPath(), LIBRARY_VERSION.getBytes(), StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}