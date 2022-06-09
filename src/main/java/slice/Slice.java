package slice;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import org.lwjgl.input.Keyboard;
import slice.clickgui.ClickGui;
import slice.discord.StartDiscordRPC;
import slice.event.Event;
import slice.event.events.EventChat;
import slice.event.events.EventKey;
import slice.event.events.EventPacket;
import slice.file.Saver;
import slice.font.FontManager;
import slice.manager.CommandManager;
import slice.manager.ModuleManager;
import slice.manager.SettingsManager;
import slice.module.Module;
import slice.util.LoggerUtil;
import slice.util.ResourceUtil;
import slice.util.account.LoginUtil;

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
    private Saver saver;
    private final StartDiscordRPC discordRPC;

    /**
     * TODO:
        * - Fix ClickGui module opening
     * */
    Slice() {
        moduleManager = new ModuleManager();
        commandManager = new CommandManager(moduleManager);
        settingsManager = new SettingsManager(moduleManager);
        fontManager = new FontManager();
        clickGui = new ClickGui();
        saver = new Saver(moduleManager);
        discordRPC = new StartDiscordRPC();
        discordRPC.start();
    }

    public void stop() {
        saver.save();
    }

    /**
     * Where all events are handled
     *
     * @pamra event - the event to be handled
     * */
    public void onEvent(Event event) {
        if(event instanceof EventChat) {
            commandManager.handleChat((EventChat) event);
        }
        if(event instanceof EventKey) {
            EventKey e = (EventKey) event;
            if(e.getKey() == Keyboard.KEY_RSHIFT) Minecraft.getMinecraft().displayGuiScreen(clickGui);
            if (e.getKey() == Keyboard.KEY_PERIOD) Minecraft.getMinecraft().displayGuiScreen(new GuiChat("."));
            moduleManager.getModules().stream().filter(module -> module.getKey() == e.getKey()).forEach(Module::toggle); // key event
        }
        moduleManager.getModules().stream().filter(Module::isEnabled).forEach(module -> module.onEvent(event)); // Module events
    }


}