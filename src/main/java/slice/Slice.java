package slice;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import org.lwjgl.input.Keyboard;
import slice.api.API;
import slice.api.irc.IRC;
import slice.clickgui.ClickGui;
import slice.command.commands.CommandPlugins;
import slice.discord.StartDiscordRPC;
import slice.event.Event;
import slice.event.events.*;
import slice.file.Saver;
import slice.font.FontManager;
import slice.gui.alt.manager.AltManager;
import slice.gui.alt.manager.ui.AltManButton;
import slice.manager.CommandManager;
import slice.manager.ModuleManager;
import slice.manager.SettingsManager;
import slice.module.Module;

/**
* Main Class for the Client
*
* @author Nick & Dylan
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

    /** Alt Manager */
    private AltManager altManager;

    /** Server */
    public IRC irc;

    /** discord */
    public String discordName, discordID, discordDiscriminator;

    /** server */
    public float serverYaw, serverPitch, serverLastYaw, serverLastPitch;
    public double serverX, serverLastX, serverY, serverLastY, serverZ, serverLastZ;

    /** MainMenu */
    public int mainIndex;

    /** for irc reconnecting */
    public boolean connecting;
    /** killaura target for target hud */
    public EntityLivingBase target;

    Slice() {
        connecting = true;
        moduleManager = new ModuleManager();
        commandManager = new CommandManager(moduleManager);
        settingsManager = new SettingsManager(moduleManager);
        fontManager = new FontManager();
        clickGui = new ClickGui();
        saver = new Saver(moduleManager);
        discordRPC = new StartDiscordRPC();
        discordRPC.start();
        API.sendAuthRequest(irc);
    }

    /**
     * Called when the client is stopped
     * */
    public void stop() {
        connecting = false;
        saver.save();
    }

    /**
     * Where all events are handled
     *
     * @pamra event - the event to be handled
     * */
    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            EventUpdate e = (EventUpdate) event;

            serverLastYaw = Minecraft.getMinecraft().thePlayer.lastReportedYaw;
            serverLastPitch = Minecraft.getMinecraft().thePlayer.lastReportedPitch;
            serverLastX = Minecraft.getMinecraft().thePlayer.lastReportedPosX;
            serverLastY = Minecraft.getMinecraft().thePlayer.lastReportedPosY;
            serverLastZ = Minecraft.getMinecraft().thePlayer.lastReportedPosZ;

            serverYaw = e.getYaw();
            serverPitch = e.getPitch();
            serverX = e.getX();
            serverY = e.getY();
            serverZ = e.getZ();
        }

        if(event instanceof EventPacket) {
            EventPacket e = (EventPacket) event;
            Packet<?> packet = e.getPacket();

            CommandPlugins plugins = ((CommandPlugins) commandManager.getCommand("plugins"));
            if(plugins.searching) {
                plugins.onPacketReceive(e);
            }

            if(packet instanceof S02PacketChat) {
                S02PacketChat s02 = (S02PacketChat) packet;

                irc.onMessage(e, s02);
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

        if(event instanceof EventSwitchAccount) {
            if(irc == null)
                return;

            irc.accountSwitch((EventSwitchAccount) event);
        }

        if(event instanceof EventUpdate) {

            if(Minecraft.getMinecraft().currentScreen != null) {


                if(Minecraft.getMinecraft().currentScreen instanceof AltManager) this.altManager = (AltManager) Minecraft.getMinecraft().currentScreen;
                else this.altManager = null;
            }

            if(irc.getUser() == null) {
                irc.getSocket().disconnect();
            }

            if(!irc.getSocket().connected()) {
                irc.getSocket().connect();
            }

            CommandPlugins plugins = ((CommandPlugins) commandManager.getCommand("plugins"));
            plugins.onUpdate();

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
    public String replaceUsername(String username, String discordName, String message) {
        if(discordName == null)
            return message;

        String lastColor = "";
        for(int i = message.length() - 1; i >= 0; i--) {
            if(message.charAt(i) == '§') {
                lastColor = message.substring(i, i + 2);
                break;
            }
        }
        return message.replaceAll(username,  username + " §c(§b" + discordName + "§c)" + lastColor);
    }


}