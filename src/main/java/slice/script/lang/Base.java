package slice.script.lang;

import jdk.internal.dynalink.beans.StaticClass;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptUtils;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import org.json.JSONObject;
import slice.Slice;
import slice.module.data.Category;
import slice.script.lang.logger.Chat;
import slice.script.lang.logger.Console;
import slice.setting.settings.NumberValue;
import slice.util.*;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import java.util.Set;

/**
 * Utility class for setting up the scripting language.
 *
 * @author Nick
 */
@UtilityClass
public class Base {

    /**
     * Starts the script engine.
     *
     * @param engine The script engine to manage.
     * */
    public static void setup(ScriptEngine engine) {
        engine.setBindings(engine.createBindings(), ScriptContext.ENGINE_SCOPE);
        engine.put("mc", Minecraft.getMinecraft());
        putClassInEngine(engine, "console", Console.class);
        putClassInEngine(engine,"System", System.class);
        putClassInEngine(engine,"Category", Category.class);
        putClassInEngine(engine, "Type", NumberValue.Type.class);
        putClassInEngine(engine, "Math", Math.class);
        putClassInEngine(engine, "Chat", Chat.class);
        putClassInEngine(engine, "MoveUtil", MoveUtil.class);
        putClassInEngine(engine, "KeyUtil", KeyUtil.class);
        putClassInEngine(engine, "RenderUtil", RenderUtil.class);
        putClassInEngine(engine, "RotationUtil", RotationUtil.class);
        putClassInEngine(engine, "LoggerUtil", LoggerUtil.class);
        putClassInEngine(engine, "PacketUtil", PacketUtil.class);

        try {
            engine.eval("function fetch(url) { " +
                    "var URL = Java.type('java.net.URL');" +
                    "var BufferedReader = Java.type('java.io.BufferedReader');" +
                    "var InputStreamReader = Java.type('java.io.InputStreamReader');" +
                    "var url = new URL(url);" +
                    "var con = url.openConnection();" +
                    "con.setRequestProperty('User-Agent', 'Mozilla/5.0');" +
                    "con.setRequestProperty('Connection', 'keep-alive');" +
                    "var reader = new BufferedReader(new InputStreamReader(con.getInputStream()));" +
                    "var line = '';" +
                    "var content = '';" +
                    "while ((line = reader.readLine()) != null) {" +
                    "content += line;" +
                    "}" +
                    "return content;" +
                    "}");
        } catch (Exception ignored){}
    }

    public static String formatJavaScriptLine(String line) {
        // arrow functions
        if(line.contains("=>")) {
            line = line.replaceFirst("\\(\\s*\\)\\s*=>\\s*\\{", "function() {");
            line = line.replaceFirst("\\(\\s*([a-zA-Z0-9_]+)\\s*\\)\\s*=>\\s*\\{", "function($1) {");
            line = line.replaceFirst("\\(\\s*([a-zA-Z0-9_]+)\\s*(,\\s*[a-zA-Z0-9_]+\\s*)*\\)\\s*=>\\s*\\{", "function($1) {");
            line = line.replaceFirst("\\(\\s*\\)\\s*=>\\s*([a-zA-Z0-9_]+)", "function() { $1; }");
            line = line.replaceFirst("\\(\\s*([a-zA-Z0-9_]+)\\s*\\)\\s*=>\\s*([a-zA-Z0-9_]+)", "function($1) { $2; }");
            line = line.replaceFirst("\\(\\s*([a-zA-Z0-9_]+)\\s*(,\\s*[a-zA-Z0-9_]+\\s*)*\\)\\s*=>\\s*([a-zA-Z0-9_]+)", "function($1) { $3; }");
        }

        // import statements
        if(line.contains("import")) {
            line = line.replaceAll("import\\s+([a-zA-Z0-9_]+)\\s+from\\s+'([a-zA-Z0-9_.]+)'", "var $1 = Java.type(\"$2\");");
            line = line.replaceAll("import\\s+([a-zA-Z0-9_]+)\\s+from\\s+\"([a-zA-Z0-9_.]+)\"", "var $1 = Java.type(\"$2\");");
            line = line.replaceAll("import\\s+([a-zA-Z0-9_]+)\\s+from\\s+([a-zA-Z0-9_.]+)", "var $1 = Java.type(\"$2\");");
        }

        return line;
    }

    /**
     * Sets a variable in the script engine.
     *
     * @param engine The script engine.
     * @param name The name of the variable.
     * @param value The value of the variable.
     * */
    public static void putInEngine(ScriptEngine engine, String name, Object value) {
        engine.put(name, value);
    }

    public static void putClassInEngine(ScriptEngine engine, String name, Class<?> clazz) {
        engine.put(name, StaticClass.forClass(clazz));
    }
    /**
     * Gets a variable from the script engine.
     *
     * @param engine The script engine.
     * @param name The name of the variable.
     * @return The variable.
     * */
    public static Object getVariable(ScriptEngine engine, String name) {
        try {
            return engine.eval(name);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Sets a variable in the script engine.
     *
     * @param engine The script engine.
     * @param name The name of the variable.
     * */
    public static int getInt(ScriptEngine engine, String name) {
        return (Integer)getVariable(engine, name);
    }

    /**
     * Gets a double from the script engine.
     *
     * @param engine The script engine.
     * @param name The name of the variable.
     * */
    public static double getDouble(ScriptEngine engine, String name) {
        return (Double)getVariable(engine, name);
    }

    /**
     * Gets a boolean from the script engine.
     *
     * @param engine The script engine.
     * @param name The name of the variable.
     * */
    public static boolean getBoolean(ScriptEngine engine, String name) {
        return (Boolean)getVariable(engine, name);
    }

    /**
     * Gets a string from the script engine.
     *
     * @param engine The script engine.
     * @param name The name of the variable.
     * */
    public static String getString(ScriptEngine engine, String name) {
        return (String)getVariable(engine, name);
    }

    /**
     * Checks if a script has a function.
     *
     * @param engine The script engine.
     * @param name The name of the variable.
     */
    public boolean hasFunction(ScriptEngine engine, String name) {
        try {
            Bindings bind = engine.getBindings(ScriptContext.ENGINE_SCOPE);
            Set<String> allAttributes = bind.keySet();
            for (String attr : allAttributes) {
                if (!("function".equals(engine.eval("typeof " + attr)))) continue;

                if (!attr.equals(name)) continue;

                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * Calls a function in the script engine.
     *
     * @param engine The script engine.
     * @param name The name of the function.
     * @param args The arguments to pass to the function.
     * */
    public void callFunction(ScriptEngine engine, String name, Object... args) {
        Invocable inv = (Invocable) engine;

        if(!hasFunction(engine, name))
            return;

        try {
            inv.invokeFunction(name, args);
        } catch (Exception e) {
            LoggerUtil.addMessage(e.getMessage());
        }
    }

    /**
     * Checks if a script has a variable.
     *
     * @param engine The script engine.
     * @param name The name of the variable.
     */
    public boolean hasVariable(ScriptEngine engine, String name) {
        try {
            return engine.eval(name) != null;
        } catch (Exception e) {
            return false;
        }
    }

}
