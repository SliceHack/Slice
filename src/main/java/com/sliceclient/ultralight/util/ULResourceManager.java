package com.sliceclient.ultralight.util;

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

@UtilityClass
public class ULResourceManager {

    private static final String  LIBRARY_VERSION = "b8daecd";

    public static final File ultraLightDir = new File(Minecraft.getMinecraft().mcDataDir, "Slice\\ultralight");
    public static final File binDir = new File(ultraLightDir, "bin");
    public static final File resourceDir = new File(ultraLightDir, "resources");

    @SuppressWarnings("all")
    public static void loadUltralight() throws URISyntaxException, UltralightLoadException, IOException {
        Thread thread = new Thread(() -> {
            try {
                File VERSION = new File(ultraLightDir, "VERSION");
                if(VERSION.exists() && VERSION.isFile()){
                    String version = new String(Files.readAllBytes(VERSION.toPath()));
                    if(version.equals(LIBRARY_VERSION)){
                        return;
                    }
                }
                if(binDir.exists() && binDir.isDirectory()){
                    FileUtils.deleteDirectory(binDir);
                }

                binDir.mkdirs();

                if(resourceDir.exists() && resourceDir.isDirectory()){
                    FileUtils.deleteDirectory(resourceDir);
                }

                resourceDir.mkdirs();

                String os;
                String osName = System.getProperty("os.name").toLowerCase();
                if (osName.contains("win")) os = "win";
                else if (osName.contains("mac")) os = "mac";
                else os = "linux";

                URL url = new URL(String.format("https://ultralight-sdk.sfo2.cdn.digitaloceanspaces.com/ultralight-sdk-%s-%s-x64.7z", LIBRARY_VERSION, os));

                File file = new File(ultraLightDir, "ultralight-sdk.7z");

                System.out.println(file.getAbsolutePath());
                System.out.println(url);

                if(file.exists() && file.isFile()) file.delete();
                if(file.getParentFile().exists()) file.getParentFile().mkdirs();
                file.createNewFile();

                HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
                long completeFileSize = httpConnection.getContentLength();
                try(InputStream inputStream = url.openStream();
                    FileOutputStream fos = new FileOutputStream(file);
                    CountingInputStream cis = new CountingInputStream(inputStream)){

                    byte[] buffer = new byte[4096];
                    int len;
                    while ((len = cis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);

                        int percent = (int) (cis.getBytesRead() * 100 / completeFileSize);
                        System.out.println("Download:  " + percent + "%");
                    }
                }

                try(SevenZFile sevenZFile = new SevenZFile(file)){
                    SevenZArchiveEntry entry;
                    while ((entry = sevenZFile.getNextEntry()) != null){
                        if(entry.getName().startsWith("bin/")){
                            File dest = new File(binDir, entry.getName().substring(4));

                            if(dest.exists() && dest.isFile()) dest.delete();

                            if(dest.getParentFile().exists())
                                dest.getParentFile().mkdirs();
                            dest.createNewFile();
                            try(FileOutputStream fos = new FileOutputStream(dest)){
                                byte[] buffer = new byte[4096];
                                int len;
                                while ((len = sevenZFile.read(buffer)) > 0) {
                                    fos.write(buffer, 0, len);
                                }
                            }

                        }

                        if (entry.getName().startsWith("resources/")) {
                            File dest = new File(resourceDir, entry.getName().substring(10));
                            if(dest.exists() && dest.isFile()) dest.delete();

                            if(dest.getParentFile().exists())
                                dest.getParentFile().mkdirs();
                            dest.createNewFile();
                            try(FileOutputStream fos = new FileOutputStream(dest)){
                                byte[] buffer = new byte[4096];
                                int len;
                                while ((len = sevenZFile.read(buffer)) > 0) {
                                    fos.write(buffer, 0, len);
                                }
                            }
                        }
                    }
                }
                file.delete();
                UltralightJava.extractNativeLibrary(binDir.toPath());
                UltralightGPUDriverNativeUtil.extractNativeLibrary(binDir.toPath());

                Files.write(VERSION.toPath(), LIBRARY_VERSION.getBytes(), StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);
            }catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }


}
