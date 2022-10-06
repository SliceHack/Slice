package com.sliceclient.script.classloader;

import com.sliceclient.script.SliceScript;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * This file loads the slice script into the classpath then calls the onEnable method.
 *
 * @author Nick
 * @since 2022/10/5
 * */
@Getter @Setter
public class ScriptLoader {

    private File file;
    private ScriptDescriptionFile description;

    public ScriptLoader(File file) {
        this.file = file;
        load();
    }

    public void load() {
        if(!file.getName().endsWith(".jar")) throw new RuntimeException("File is not a jar");

        this.description = getDescription();

        if(description.getVariables().get("main") == null) throw new RuntimeException("main is not defined in script.json");

        try(ScriptClassLoader loader = new ScriptClassLoader(description.getVariables().get("main"), file, getClass().getClassLoader())) {
            Class<?> scriptClass = loader.loadClass(description.getVariables().get("main")).asSubclass(SliceScript.class);
            SliceScript script = (SliceScript) scriptClass.newInstance();
            script.onStartup();
            Runtime.getRuntime().addShutdownHook(new Thread(script::onShutdown));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load script", e);
        }
    }

    public ScriptDescriptionFile getDescription() {
        JarFile jar = null;
        InputStream stream = null;

        try {
            jar = new JarFile(file);
            JarEntry entry = jar.getJarEntry("script.json");

            if (entry == null) throw new FileNotFoundException("Jar does not contain script.json");

            stream = jar.getInputStream(entry);

            return new ScriptDescriptionFile(stream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load script.json", e);
        } finally {
            try {
                assert jar != null;
                assert stream != null;

                jar.close();
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void reload() {
        load();
    }
}
