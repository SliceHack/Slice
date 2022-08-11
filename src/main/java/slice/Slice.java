package slice;

import lombok.Getter;
import me.friwi.jcefmaven.impl.progress.ConsoleProgressHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import org.cef.ccbluex.CefRenderManager;
import org.cef.ccbluex.Page;
import org.lwjgl.input.Keyboard;
import slice.api.API;
import slice.api.IRC;
import slice.cef.ViewNoGui;
import slice.clickgui.ClickGui;
import slice.command.commands.CommandPlugins;
import slice.discord.StartDiscordRPC;
import slice.event.data.EventInfo;
import slice.event.events.*;
import slice.event.manager.EventManager;
import slice.file.Saver;
import slice.font.FontManager;
import slice.gui.alt.manager.AltManager;
import slice.gui.hud.slice.HUD;
import slice.gui.main.NewMainMenu;
import slice.manager.CommandManager;
import slice.manager.ModuleManager;
import slice.manager.SettingsManager;
import slice.module.Module;
import slice.notification.NotificationManager;
import slice.script.manager.ScriptManager;
import slice.util.LoggerUtil;
import slice.util.ResourceUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* Main Class for the Client
*
* @author Nick & Dylan
*/
@Getter @SuppressWarnings("unused")
public enum Slice {
    INSTANCE;

    public static final String NAME = "Slice", VERSION = "1.0";

    /* managers */
    private final EventManager eventManager;
    private final ModuleManager moduleManager;
    private final CommandManager commandManager;
    private final SettingsManager settingsManager;
    private final FontManager fontManager;
    private final ScriptManager scriptManager;

    private NotificationManager notificationManager;

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

    /** KillAura target for target hud */
    public EntityLivingBase target;

    private HUD hud;

    /** html */
    private final CefRenderManager cefRenderManager;
    private final List<ViewNoGui> html = new ArrayList<>();

    /** other things */
    public int ping = 0, players = 0;
    private final String date;


    Slice() {
        connecting = true;
        eventManager = new EventManager();
        cefRenderManager = new CefRenderManager(eventManager);
        cefRenderManager.initialize(new ConsoleProgressHandler());
        moduleManager = new ModuleManager();
        fontManager = new FontManager();
        scriptManager = new ScriptManager(moduleManager, fontManager);
        commandManager = new CommandManager(moduleManager);
        settingsManager = new SettingsManager(moduleManager);
        clickGui = new ClickGui();
        saver = new Saver(moduleManager);
        discordRPC = new StartDiscordRPC();
        discordRPC.start();
        API.sendAuthRequest(irc);
        eventManager.register(this);

        date = (new SimpleDateFormat("dd/MM/yyyy")).format(new Date());

        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    /**
     * Calls when minecraft is initialized and ready to be used.
     * */
    public void init() {
        notificationManager = new NotificationManager();

        File sliceDir = new File(Minecraft.getMinecraft().mcDataDir, "Slice"), sliceHTML = new File(sliceDir, "html"), sliceHUD = new File(sliceHTML, "hud");
        File html = new File(sliceHUD, "index.html"), css = new File(sliceHUD, "styles.css");


        extractHTML(sliceHUD, "/slice/html/hud");
        extractHTML(new File(sliceHUD, "TargetHUD"), "slice/html/hud/targethud");
        extractHTML(new File(sliceHUD, "SessionHUD"), "/slice/html/hud/sessionhud");
        extractHTML(new File(sliceHUD, "Notification"), "/slice/html/hud/notification");

        this.html.add(new ViewNoGui(new Page("file:///" + html.getAbsolutePath() + "?name=" + NAME + "&version=" + VERSION + "&discord=" + discordName)));
    }

    @SuppressWarnings("all")
    public void extractHTML(File computerPath, String path) {
        File sliceDir = new File(Minecraft.getMinecraft().mcDataDir, "Slice"),
                sliceHTML = new File(sliceDir, "html"),
                sliceHUD = new File(sliceHTML, "hud");

        if(!sliceHTML.exists()) sliceHTML.mkdirs();

        File html = new File(computerPath, "index.html"), css = new File(computerPath, "styles.css");

        if(!html.exists() || !css.exists()) {
            ResourceUtil.extractResource(path + "/index.html", html.toPath());
            ResourceUtil.extractResource(path + "/styles.css", css.toPath());

            if(sliceHUD.getParentFile().exists()) sliceHUD.mkdirs();
        }
    }

    /**
     * Called when the client is stopped
     * */
    public void stop() {
        irc.getSocket().disconnect();
        connecting = false;
        saver.save();
        html.forEach(ViewNoGui::destroy);
    }

    @EventInfo
    public void onUpdate(EventUpdate e) {
        html.forEach((html) -> html.onResize(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight));
        for(Module module : moduleManager.getModules()) {

            if(!module.isEnabled() && eventManager.isRegistered(module)) {
                eventManager.unregister(module);
            }

            if(module.isEnabled() && !eventManager.isRegistered(module)) {
                eventManager.register(module);
            }
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


        if(Minecraft.getMinecraft().currentScreen != null) {

            if(Minecraft.getMinecraft().currentScreen instanceof AltManager) this.altManager = (AltManager) Minecraft.getMinecraft().currentScreen;
            else this.altManager = null;
        }

        CommandPlugins plugins = ((CommandPlugins) commandManager.getCommand("plugins"));
        plugins.onUpdate();

        moduleManager.getModules().forEach(module -> module.onUpdateNoToggle(e));
    }

    @EventInfo
    public void onPacket(EventPacket e) {
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

    @EventInfo
    public void onChat(EventChat e) {
        String message = e.getMessage();
        commandManager.handleChat(e);

        if(irc == null)
            return;

        if(message.startsWith("#")) {
            message = message.substring(1).replaceFirst(" ","");
            irc.sendMessage(message);
            e.setCancelled(true);
        }
    }

    @EventInfo
    public void onGuiRender(EventGuiRender e) {
        if(Minecraft.getMinecraft().gameSettings.showDebugInfo) return;
        html.forEach((html) -> {
            if(html.isInit()) html.draw(e);
            else html.init();
        });
    }

    @EventInfo
    public void switchAccount(EventSwitchAccount e) {
        if(irc == null)
            return;

        irc.accountSwitch(e);
    }

    @EventInfo
    public void onKey(EventKey e) {
        if(e.getKey() == Keyboard.KEY_RSHIFT) Minecraft.getMinecraft().displayGuiScreen(clickGui);
        if (e.getKey() == Keyboard.KEY_PERIOD) Minecraft.getMinecraft().displayGuiScreen(new GuiChat("."));
        moduleManager.getModules().stream().filter(module -> module.getKey() == e.getKey()).forEach(Module::toggle); // key event
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