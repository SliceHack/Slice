package slice.util;

import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import java.util.logging.Logger;


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
        Logger.getLogger("Slice").info(message);
    }

}
