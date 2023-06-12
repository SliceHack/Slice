package com.sliceclient.ultralight.js.binding;

import de.florianmichael.viamcp.gui.GuiProtocolSelector;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.util.Session;
import slice.Slice;
import slice.manager.ModuleManager;
import slice.module.Module;
import slice.setting.Setting;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.ModeValue;
import slice.setting.settings.NumberValue;
import slice.ultralight.ViewMainMenu;
import slice.util.LoggerUtil;
import slice.util.account.LoginUtil;
import slice.util.account.microsoft.MicrosoftAccount;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@SuppressWarnings("unused")
public class SliceJsClientBinding {

    @Getter private final Minecraft mc = Minecraft.getMinecraft();
    @Getter private final ModuleManager moduleManager = Slice.INSTANCE.getModuleManager();

    public void realLoginMicrosoft(String email, String password) {
        LoginUtil.loginMicrosoft(email, password);
    }
    public void loginMicrosoft(String email, String password) {
        LoginUtil.loginMicrosoftNoSetSession(email, password);
    }
    public void loginCracked(String username) {
        LoginUtil.loginOffline(username);
    }

    public void displayMainMenu() { mc.displayGuiScreen(new ViewMainMenu()); }
    public void displayMultiplayerMenu() { mc.displayGuiScreen(new GuiMultiplayer(mc.currentScreen)); }
    public void displayVersion() { mc.displayGuiScreen(new GuiProtocolSelector(mc.currentScreen)); }
    public void displaySinglePlayerMenu() { mc.displayGuiScreen(new GuiSelectWorld(mc.currentScreen)); }
    public void displayOptions() { mc.displayGuiScreen(new GuiOptions(mc.currentScreen, mc.gameSettings)); }
    public void displayAlt() {
        MicrosoftAccount account = LoginUtil.loginFromWebView(true);

        if(account != null) {

            try(BufferedWriter writer = new BufferedWriter(new FileWriter(Slice.INSTANCE.getLastSessionFile(), true))) {
                writer.write(account.getProfile().getName() + ":" + account.getRefreshToken());
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        /*mc.displayGuiScreen(new ViewAltManager());*/
    }
    public void closeGui() { mc.displayGuiScreen(null); }

    public void exit() {
        mc.shutdownMinecraftApplet();
    }

    public void setEnabled(String moduleName, boolean enabled) {
        ModuleManager moduleManager = Slice.INSTANCE.getModuleManager();
        Module module = moduleManager.getModule(moduleName);

        if(module != null && module.isEnabled() != enabled) {
            module.toggle();
        }
    }

    public boolean isEnabled(String moduleName) {
        ModuleManager moduleManager = Slice.INSTANCE.getModuleManager();
        Module module = moduleManager.getModule(moduleName);

        if(module != null) {
            return module.isEnabled();
        }

        return false;
    }

    public void setValue(String moduleName, String settingName, Object value) {
        ModuleManager moduleManager = Slice.INSTANCE.getModuleManager();
        Module module = moduleManager.getModule(moduleName);

        if(module == null) return;

        Setting setting = module.getSetting(settingName);

        if(setting == null) return;

        switch (setting.getTypeName()) {
            case "BooleanValue":
                BooleanValue booleanValue = (BooleanValue) setting;
                booleanValue.setValue((boolean) value);
                break;
            case "NumberValue":
                NumberValue numberValue = (NumberValue) setting;
                numberValue.setValue((double) value);
                break;
            case "ModeValue":
                ModeValue modeValue = (ModeValue) setting;
                modeValue.setValue((String) value);
                break;
        }
    }

    public void log(Object object) {
        LoggerUtil.addTerminalMessage(object.toString());
    }

    public String getFaceUrl() {
        return String.format("https://crafatar.com/avatars/%s?size=100&overlay=true", mc.getSession().getPlayerID());
    }

    public String getAccountType() {
        return ((mc.getSession().getSessionType() == Session.Type.MOJANG) && !mc.getSession().getToken().isEmpty()) ? "Premium" : "Cracked";
    }


}
