package slice.script.lang.logger;

import slice.util.LoggerUtil;

/**
 * Console logger for the scripting language.
 *
 * @author Nick
 * */
public class Console {

    /**
     * Logs a message to the chat.
     *
     * @param message The message to log.
     */
    public static void log(Object message) {
        LoggerUtil.addMessage(message + "");
    }

    /**
     * Logs an error to the chat.
     *
     * @param message The message to log.
     */
    public static void error(Object message) {
        LoggerUtil.addMessage(message + "");
    }

    /**
     * Logs a warning to the chat.
     *
     * @param message The message to log.
     */
    public static void warn(Object message) {
        LoggerUtil.addMessage("[WARNING] " + message);
    }

}
