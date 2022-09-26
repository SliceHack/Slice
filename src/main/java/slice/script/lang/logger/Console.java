package slice.script.lang.logger;

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
        System.out.println(message);
    }

    /**
     * Logs an error to the chat.
     *
     * @param message The message to log.
     */
    public static void error(Object message) {
        System.err.println(message);
    }

    /**
     * Logs a warning to the chat.
     *
     * @param message The message to log.
     */
    public static void warn(Object message) {
        System.out.println("[WARNING] " + message);
    }

}
