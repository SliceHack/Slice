package slice.script.lang.logger;

import slice.util.LoggerUtil;

/**
 * Chat logger for the scripting language.
 *
 * @author Nick
 * */
public class Chat {

    /** The Chat instance */
    public static Chat INSTANCE = new Chat();

    /**
     * Adds a message to the chat.
     * */
    public void addMessage(String message) {
        LoggerUtil.addMessage(message);
    }
}
