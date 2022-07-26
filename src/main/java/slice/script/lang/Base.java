package slice.script.lang;

import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import slice.script.lang.logger.Console;
import slice.util.MathUtil;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import java.util.HashSet;
import java.util.List;
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
        engine.put("console", Console.INSTANCE);
        engine.put("mc", Minecraft.getMinecraft());
        engine.put("Math", slice.script.lang.math.Math.INSTANCE);
        engine.put("sys_out", System.out);
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

    /**
     * Gets a variable from the script engine.
     *
     * @param engine The script engine.
     * @param name The name of the variable.
     * @return The variable.
     * */
    public static Object getVariable(ScriptEngine engine, String name) {
        return engine.get(name);
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
                if ("function".equals(engine.eval("typeof " + attr))) {
                    if (attr.equals(name)) {
                        return true;
                    }
                }
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
        } catch (Exception ignored){}
    }

    /**
     * Checks if a script has a variable.
     *
     * @param engine The script engine.
     * @param name The name of the variable.
     */
    public boolean hasVariable(ScriptEngine engine, String name) {
        try {
            Bindings bind = engine.getBindings(ScriptContext.ENGINE_SCOPE);
            Set<String> allAttributes = bind.keySet();
            for (String attr : allAttributes) {
                if (!("function".equals(engine.eval("typeof " + attr)))) {
                    if (attr.equals(name)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
