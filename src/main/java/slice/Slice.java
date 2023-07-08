package slice;

import com.sliceclient.anticheat.SliceAC;
import com.sliceclient.capes.CapeManager;
import com.sliceclient.ultralight.UltraLightEngine;
import com.sliceclient.ultralight.view.ViewNoGui;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import org.lwjgl.input.Keyboard;
import slice.api.API;
import slice.api.IRC;
import slice.command.Command;
import slice.legacy.clickgui.ClickGui;
import slice.command.commands.CommandPlugins;
import slice.discord.StartDiscordRPC;
import slice.event.data.EventInfo;
import slice.event.events.*;
import slice.event.manager.EventManager;
import slice.file.Saver;
import slice.font.FontManager;
import slice.gui.hud.slice.HUD;
import slice.manager.CommandManager;
import slice.manager.ModuleManager;
import slice.manager.SettingsManager;
import slice.module.Module;
import slice.notification.NotificationManager;
import slice.script.manager.ScriptManager;
import slice.script.module.ScriptModule;
import slice.spotify.Spotify;
import slice.ultralight.ViewHUD;
import slice.util.account.LoginUtil;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Main Class for the Client
 *
 * @author Nick & Dylan
 */
@Getter
@SuppressWarnings("unused")
public enum Slice {
    INSTANCE;

    public static final String NAME = "Slice", VERSION = "1.0";

    /* managers */
    private final EventManager eventManager;
    private final ModuleManager moduleManager;
    private final CommandManager commandManager;
    private final FontManager fontManager;

    private SettingsManager settingsManager;
    private ScriptManager scriptManager;
    private NotificationManager notificationManager;
    private CapeManager capeManager;

    /* data */
    private final ClickGui legacyClickGui;

    private Saver saver;
    private final StartDiscordRPC discordRPC;

    /**
     * Server
     */
    public IRC irc;

    /**
     * discord
     */
    public String discordName, discordID, discordDiscriminator;

    /**
     * server
     */
    public float serverYaw, serverPitch, serverLastYaw, serverLastPitch;
    public double serverX, serverLastX, serverY, serverLastY, serverZ, serverLastZ;

    /**
     * MainMenu
     */
    public int mainIndex;

    /**
     * for irc reconnecting
     */
    public boolean connecting;

    /**
     * KillAura target for target hud
     */
    public EntityLivingBase target;

    /**
     * anticheat
     */
    public SliceAC anticheat;

    private HUD hud;

    /**
     * html
     */
    private UltraLightEngine ultraLightEngine;
    private ViewHUD viewHUD;

    private final List<ViewNoGui> html = new ArrayList<>();

    /**
     * other things
     */
    public int ping = 0, players = 0, seconds = 0, minutes = 0, hours = 0;
    public final long startTime;
    public long totalTime;
    public String playTime, totalPlayTime;
    public String currentEmail, currentPassword;

    private final String date;

    @Getter
    private final File lastSessionFile = new File(Minecraft.getMinecraft().mcDataDir, "Slice/microsoftAuth.txt");

    Slice() {
        connecting = true;
        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        fontManager = new FontManager();
        commandManager = new CommandManager(moduleManager);
        legacyClickGui = new ClickGui();

        LoginUtil.askLoginWithSessionFile(lastSessionFile);

        discordRPC = new StartDiscordRPC();
        discordRPC.start();

        API.sendAuthRequest(irc);

        date = (new SimpleDateFormat("MM/dd/yyyy")).format(new Date());

        File totalTimeFile = new File(Minecraft.getMinecraft().mcDataDir, "Slice/totalTime.txt");
        if (!totalTimeFile.exists()) saveTotalTime(0L);
        totalTime = loadTotalTime();
        startTime = System.currentTimeMillis();

        eventManager.register(this);

        new Spotify();
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    /**
     * TODO: Fix buggy ass clickgui
     */
    public void init() {
        notificationManager = new NotificationManager();
        capeManager = new CapeManager("https://api.sliceclient.com");
        anticheat = SliceAC.INSTANCE;

        viewHUD = new ViewHUD();

        scriptManager = new ScriptManager(moduleManager, fontManager);
        settingsManager = new SettingsManager(moduleManager);
        saver = new Saver(moduleManager);

        commandManager.commands.forEach(Command::init);

        moduleManager.getModules().stream().filter(module -> module instanceof ScriptModule).forEach(Module::init);
    }

    /**
     * Called when the client is stopped
     */
    public void stop() {
        totalTime += System.currentTimeMillis() - startTime;
        irc.getSocket().disconnect();
        connecting = false;
        saver.save();
        saveTotalTime();
    }

    @EventInfo
    public void onUpdate(EventUpdate e) {
        for (Module module : moduleManager.getModules()) {

            if (!module.isEnabled() && eventManager.isRegistered(module)) eventManager.unregister(module);
            if (module.isEnabled() && !eventManager.isRegistered(module)) eventManager.register(module);
        }
        players = Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap().size();

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

        CommandPlugins plugins = ((CommandPlugins) commandManager.getCommand("plugins"));
        plugins.onUpdate();

        moduleManager.getModules().forEach(module -> module.onUpdateNoToggle(e));
        moduleManager.getModules().forEach(module -> module.onUpdateNoToggle(e));
    }

    @EventInfo
    public void onPacket(EventPacket e) {
        Packet<?> packet = e.getPacket();

        CommandPlugins plugins = ((CommandPlugins) commandManager.getCommand("plugins"));
        if (plugins.searching) {
            plugins.onPacketReceive(e);
        }

        if (packet instanceof S02PacketChat) {
            S02PacketChat s02 = (S02PacketChat) packet;

            irc.onMessage(e, s02);
        }
    }

    @EventInfo
    public void onChat(EventChat e) {
        String message = e.getMessage();
        commandManager.handleChat(e);

        if (irc == null)
            return;

        if (message.startsWith("#")) {
            message = message.substring(1).replaceFirst(" ", "");
            irc.sendMessage(message);
            e.setCancelled(true);
        }
    }

    @EventInfo
    public void switchAccount(EventSwitchAccount e) {
        if (irc == null)
            return;

        irc.accountSwitch(e);
    }

    @EventInfo
    public void onKey(EventKey e) {
        if (e.getKey() == Keyboard.KEY_PERIOD) Minecraft.getMinecraft().displayGuiScreen(new GuiChat("."));
        moduleManager.getModules().stream().filter(module -> module.getKey() == e.getKey()).forEach(Module::toggle); // key event
    }

    /**
     * Replaces the username in the message with the username of the players discord account
     *
     * @param message - the message to be replaced
     **/
    public String replaceUsername(String username, String discordName, String message) {
        if (discordName == null)
            return message;

        String lastColor = "";
        for (int i = message.length() - 1; i >= 0; i--) {
            if (message.charAt(i) == '§') {
                lastColor = message.substring(i, i + 2);
                break;
            }
        }
        return message.replaceAll(username, username + " §c(§b" + discordName + "§c)" + lastColor);
    }

    public long loadTotalTime() {
        File file = new File(Minecraft.getMinecraft().mcDataDir, "Slice/totalTime.txt");
        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                reader.close();
                return Long.parseLong(line);
            } catch (IOException e) {
                return System.currentTimeMillis() - startTime;
            }
        }

        return System.currentTimeMillis() - startTime;
    }

    public void saveTotalTime() {
        saveTotalTimeValue(totalTime);
    }

    public void saveTotalTime(long value) {
        saveTotalTimeValue(value);
    }

    private void saveTotalTimeValue(long value) {
        File file = new File(Minecraft.getMinecraft().mcDataDir, "Slice/totalTime.txt");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(value + "");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}