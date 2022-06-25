package slice.util;

import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import org.apache.logging.log4j.LogManager;

/**
 * Utility class for logging messages to the minecraft chat.
 *
 * @author Dylan
 */
@UtilityClass
public class LoggerUtil {

    /**
     * Logs a message to the minecraft chat.
     *
     * @param message The message to log.
     */
    public static void addMessage(String message) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(
                "§cSlice §7» " + message.replace("&", "§")
        ));
    }

    /**
     * Logs a message to the terminal.
     *
     * @param message The message to log.
     */
    public static void addTerminalMessage(String message) {
        LogManager.getLogger("Slice").info(message);
    }

    /**
     * Adds an irc message to chat.
     *
     * @param user The username
     * @param message The message
     */
    public static void addIRCMessage(String user, String message) {
        try {
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(
                    "§6IRC §7» §5" + user + "§r: " + message.replace("&", "§").replace("§k","")
            ));
        } catch(Exception ignored){}
    }
}
