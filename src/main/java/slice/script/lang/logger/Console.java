package slice.script.lang.logger;

/**
 * Console logger for the scripting language.
 *
 * @author Nick
 * */
public class Console {

    /** The logger instance */
    public static Console INSTANCE = new Console();

    /**
     * Logs a message to the chat.
     *
     * @param message The message to log.
     */
    public void log(Object message) {
        System.out.println(message);
    }

    /**
     * Logs an error to the chat.
     *
     * @param message The message to log.
     */
    public void error(Object message) {
        System.err.println(message);
    }

    /**
     * Logs a warning to the chat.
     *
     * @param message The message to log.
     */
    public void warn(Object message) {
        System.out.println("[WARNING] " + message);
    }

}
