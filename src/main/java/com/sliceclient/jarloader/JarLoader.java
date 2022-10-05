package com.sliceclient.jarloader;

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
public class JarLoader {

    /**
     * Loads a jar into the classpath runtime.
     *
     * @param jarPath The path to the jar.
     * @return true if the jar was loaded successfully, false otherwise.
     * */
    protected boolean loadJar(String jarPath) {
        try {
            File file = new File(jarPath);
            URL url = file.toURI().toURL();
            URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(classLoader, url);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
