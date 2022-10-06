package com.sliceclient.jarloader.script;

import com.sliceclient.script.SliceScript;
import lombok.Getter;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * This file loads the script into the classpath then calls the onEnable method.
 *
 * @author Nick
 * @since 2022/10/5
 * */
@Getter
public final class ScriptClassLoader extends URLClassLoader {

    private final SliceScript script;

    public ScriptClassLoader(String classToStartFrom, File file, ClassLoader parent) throws MalformedURLException {
        super(new URL[] {file.toURI().toURL()}, parent);

        try {
            Class<?> jarClass = Class.forName(classToStartFrom, true, this);
            Class<? extends SliceScript> scriptClass = jarClass.asSubclass(SliceScript.class);
            script = scriptClass.newInstance();
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | ClassCastException ex) {
            throw new RuntimeException("Failed to load script class", ex);
        }
    }
}