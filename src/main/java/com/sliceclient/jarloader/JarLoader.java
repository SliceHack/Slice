package com.sliceclient.jarloader;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Loads jars into the classpath runtime.
 *
 * @author Nick
 * @version 1.0
 * @since 10/5/2022
 * */
@UtilityClass
public class JarLoader {

    /**
     * Loads a jar into the classpath runtime.
     *
     * @param jarPath The path to the jar.
     * */
    public void loadJar(String jarPath) {
        try {
            File file = new File(jarPath);
            URL url = file.toURI().toURL();
            URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(classLoader, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
