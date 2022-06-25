package slice;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.input.Keyboard;
import slice.api.API;
import slice.api.IRC;
import slice.clickgui.ClickGui;
import slice.discord.StartDiscordRPC;
import slice.event.Event;
import slice.event.events.EventChat;
import slice.event.events.EventKey;
import slice.event.events.EventPacket;
import slice.event.events.EventUpdate;
import slice.file.Saver;
import slice.font.FontManager;
import slice.manager.CommandManager;
import slice.manager.ModuleManager;
import slice.manager.SettingsManager;
import slice.module.Module;
import slice.module.modules.misc.Translator;

/**
* Main Class for the Client
*
* @author Nick
*/
@Getter
public enum Slice {
    INSTANCE;

    public static String NAME = "Slice", VERSION = "1.0";

    /* managers */
    private final ModuleManager moduleManager;
    private final CommandManager commandManager;
    private final SettingsManager settingsManager;
    private final FontManager fontManager;

    /* data */
    private final ClickGui clickGui;
    private final Saver saver;
    private final StartDiscordRPC discordRPC;

    /** Server */
    public IRC irc;

    /** discord */
    public String discordName, discordID, discordDiscriminator;

    Slice() {
        API.sendAuthRequest();
        moduleManager = new ModuleManager();
        commandManager = new CommandManager(moduleManager);
        settingsManager = new SettingsManager(moduleManager);
        fontManager = new FontManager();
        clickGui = new ClickGui();
        saver = new Saver(moduleManager);
        discordRPC = new StartDiscordRPC();
        discordRPC.start();
    }

    /**
     * Called when the client is stopped
     * */
    public void stop() {
        saver.save();
    }

    /**
     * Where all events are handled
     *
     * @pamra event - the event to be handled
     * */
    public void onEvent(Event event) {
        if(event instanceof EventPacket) {
            EventPacket e = (EventPacket) event;
            Packet<?> packet = e.getPacket();
            if(packet instanceof S02PacketChat) {
                if(moduleManager.getModule(Translator.class).isEnabled())
                    return;

                S02PacketChat chat = (S02PacketChat) packet;
                String message = chat.getChatComponent().getFormattedText();
                event.setCancelled(true);
                message = replaceUsername(message);
                Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(message));
            }
        }
        if(event instanceof EventChat) {
            EventChat e = (EventChat) event;
            String message = e.getMessage();
            commandManager.handleChat(e);

            if(irc == null)
                return;

            if(message.startsWith("#")) {
                message = message.substring(1).replaceFirst(" ","");
                irc.sendMessage(message);
                event.setCancelled(true);
            }
        }

        if(event instanceof EventUpdate) {
            moduleManager.getModules().forEach(module -> module.onUpdate((EventUpdate) event));
        }

        if(event instanceof EventKey) {
            EventKey e = (EventKey) event;
            if(e.getKey() == Keyboard.KEY_RSHIFT) Minecraft.getMinecraft().displayGuiScreen(clickGui);
            if (e.getKey() == Keyboard.KEY_PERIOD) Minecraft.getMinecraft().displayGuiScreen(new GuiChat("."));
            moduleManager.getModules().stream().filter(module -> module.getKey() == e.getKey()).forEach(Module::toggle); // key event
        }
        moduleManager.getModules().stream().filter(Module::isEnabled).forEach(module -> module.onEvent(event)); // Module events
    }

    /**
     * Replaces the username in the message with the username of the players discord account
     * @param message - the message to be replaced
     **/
    public String replaceUsername(String message) {
        if(discordName == null)
            return message;

        String lastColor = "";
        for(int i = message.length() - 1; i >= 0; i--) {
            if(message.charAt(i) == '§') {
                lastColor = message.substring(i, i + 2);
                break;
            }
        }
        return message.replaceAll(Minecraft.getMinecraft().getSession().getUsername(),  Minecraft.getMinecraft().getSession().getUsername() + " §c(§b" + discordName + "§c)" + lastColor);
    }


}