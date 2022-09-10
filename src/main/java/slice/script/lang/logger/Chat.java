package slice.script.lang.logger;

import slice.util.LoggerUtil;

/**
 * Chat logger for the scripting language.
 *
 * @author Nick
 * */
public class Chat {

    /**
     * Adds a message to the chat.
     * */
    public static void addMessage(String message) {
        LoggerUtil.addMessage(message);
    }

    /**
     * @see #addMessage(String)
     * */
    public static void print(String message) {
        addMessage(message);
    }
}
