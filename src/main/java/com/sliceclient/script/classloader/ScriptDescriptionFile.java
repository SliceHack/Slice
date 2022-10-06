package com.sliceclient.script.classloader;

import lombok.Getter;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This file is for script.json
 *
 * @author Nick
 * @since 2022/10/5
 */
@Getter
public class ScriptDescriptionFile {

    private final JSONObject json;
    private final Map<Object, String> variables;

    public ScriptDescriptionFile(InputStream stream) {
        variables = new HashMap<>();

        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read script.json", e);
        }

        json = new JSONObject(builder.toString());

        for(Map.Entry<String, Object> entry : json.toMap().entrySet()) {
            variables.put(entry.getKey(), entry.getValue().toString());
        }
    }
}
