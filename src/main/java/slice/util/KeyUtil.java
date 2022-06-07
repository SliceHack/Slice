package slice.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

/**
 * Key Mappings are messed up so I made my own
 *
 * @author Nick
 * */
public class KeyUtil {

    public static KeyBinding keyBindLeft;
    public static KeyBinding keyBindBack;
    public static KeyBinding keyBindRight;
    public static KeyBinding keyBindJump;
    public static KeyBinding keyBindSneak;
    public static KeyBinding keyBindSprint;
    public static KeyBinding keyBindInventory;
    public static KeyBinding keyBindUseItem;
    public static KeyBinding keyBindDrop;
    public static KeyBinding keyBindAttack;
    public static KeyBinding keyBindPickBlock;
    public static KeyBinding keyBindChat;
    public static KeyBinding keyBindPlayerList;
    public static KeyBinding keyBindCommand;
    public static KeyBinding keyBindScreenshot;
    public static KeyBinding keyBindTogglePerspective;
    public static KeyBinding keyBindSmoothCamera;
    public static KeyBinding keyBindFullscreen;
    public static KeyBinding keyBindSpectatorOutlines;
    public static KeyBinding keyBindStreamStartStop;
    public static KeyBinding keyBindStreamPauseUnpause;
    public static KeyBinding keyBindStreamCommercials;
    public static KeyBinding keyBindStreamToggleMic;
    public static KeyBinding keyBindsHotBar;

    /**
     * Runs at the start of the game, so it does not crash
     * */
    public KeyUtil() {
        keyBindBack = Minecraft.getMinecraft().gameSettings.keyBindBack;
        keyBindLeft = Minecraft.getMinecraft().gameSettings.keyBindLeft;
        keyBindRight = Minecraft.getMinecraft().gameSettings.keyBindJump;
        keyBindJump = Minecraft.getMinecraft().gameSettings.keyBindSneak;
        keyBindSneak = Minecraft.getMinecraft().gameSettings.keyBindSprint;
        keyBindSprint = Minecraft.getMinecraft().gameSettings.keyBindSprint;
        keyBindInventory = Minecraft.getMinecraft().gameSettings.keyBindUseItem;
        keyBindUseItem = Minecraft.getMinecraft().gameSettings.keyBindDrop;
        keyBindDrop = Minecraft.getMinecraft().gameSettings.keyBindAttack;
        keyBindAttack = Minecraft.getMinecraft().gameSettings.keyBindPickBlock;
        keyBindPickBlock = Minecraft.getMinecraft().gameSettings.keyBindChat;
        keyBindChat = Minecraft.getMinecraft().gameSettings.keyBindPlayerList;
        keyBindPlayerList = Minecraft.getMinecraft().gameSettings.keyBindCommand;
        keyBindCommand = Minecraft.getMinecraft().gameSettings.keyBindScreenshot;
        keyBindScreenshot = Minecraft.getMinecraft().gameSettings.keyBindTogglePerspective;
        keyBindTogglePerspective = Minecraft.getMinecraft().gameSettings.keyBindSmoothCamera;
        keyBindSmoothCamera = Minecraft.getMinecraft().gameSettings.keyBindFullscreen;
        keyBindFullscreen = Minecraft.getMinecraft().gameSettings.keyBindSpectatorOutlines;
        keyBindSpectatorOutlines = Minecraft.getMinecraft().gameSettings.keyBindStreamStartStop;
        keyBindStreamStartStop = Minecraft.getMinecraft().gameSettings.keyBindStreamPauseUnpause;
        keyBindStreamPauseUnpause = Minecraft.getMinecraft().gameSettings.keyBindStreamCommercials;
        keyBindStreamCommercials = Minecraft.getMinecraft().gameSettings.keyBindStreamToggleMic;
        keyBindStreamToggleMic = Minecraft.getMinecraft().gameSettings.keyBindsHotbar;
        keyBindsHotBar = Minecraft.getMinecraft().gameSettings.keyBindsHotbar;
    }
}
