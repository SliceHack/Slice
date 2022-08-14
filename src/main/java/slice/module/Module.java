package slice.module;

import fr.lavache.anime.Animate;
import fr.lavache.anime.Easing;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import slice.Slice;
import slice.cef.RequestHandler;
import slice.event.events.EventUpdate;
import slice.gui.hud.legacy.HUD;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.module.modules.render.Interface;
import slice.notification.Notification;
import slice.notification.NotificationManager;
import slice.notification.Type;
import slice.setting.Setting;
import slice.setting.settings.ModeValue;
import slice.util.Timer;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of the base of every module
 *
 * @author Nick
 */
@Getter @Setter
public class Module {

    /* Module fields */
    protected Minecraft mc = Minecraft.getMinecraft();
    protected Timer timer = new Timer();

    /* info */
    private ModuleInfo info = getClass().getAnnotation(ModuleInfo.class);

    /* Module Data */
    public String name, description;
    public Category category;
    public int key;

    private boolean enabled;

    private List<Setting> settings = new ArrayList<>();

    /** ArrayList animation */
    private Animate animate;

    public Module() {
        if(info == null) return;

        this.name = info.name();
        this.category = info.category();
        this.description = info.description();
        this.key = info.key();
        this.init();

        animate = new Animate();
        animate.setEase(Easing.BACK_IN);
        animate.setReversed(true);
    }

    public void init() {}

    public void toggle() {
        enabled = !enabled;
        if(enabled) startOnEnable();
        else startOnDisable();
        try {
            HUD.smoothArrayListHUD.onToggle(this);
            Slice.INSTANCE.getSaver().save();
        } catch (Exception ignored){}

        Interface interfaceModule = (Interface) Slice.INSTANCE.getModuleManager().getModule(Interface.class);
        if(interfaceModule.getToggleNotifications().getValue()) {
            NotificationManager.queue(new Notification(Type.INFO, enabled ? "Enabled " + name : "Disabled " + name, 2));
        }

        if(enabled) RequestHandler.addToArrayList(getMode() != null ? name + " " + getMode().getValue() : name);
        else RequestHandler.removeFromArrayList(getMode() != null ? name + " " + getMode().getValue() : name);
    }

    public void startOnEnable() {
        Slice.INSTANCE.getEventManager().register(this);
        onEnable();
    }

    public void startOnDisable() {
        Slice.INSTANCE.getEventManager().unregister(this);
        onDisable();
    }

    public void onEnable() {}
    public void onDisable() {}

    /**
     * Gets a setting by name
     * @param name the name of the setting
     *             - if the setting is not found, null is returned
     * */
    public Setting getSetting(String name) {
        return settings.stream().filter(setting -> setting.getName().replace(" ", "").equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    /**
     * Gets the modules mode
     * */
    public ModeValue getMode() {
        return settings.stream().filter(setting -> (setting instanceof ModeValue && setting.getName().equalsIgnoreCase("mode"))).map(setting -> (ModeValue) setting).findFirst().orElse(null);
    }

    /**
     * Damages the player using fake fall damage
     * */
    public void damage(float fallDamage) {
        mc.thePlayer.sendQueue.addToSendNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + fallDamage, mc.thePlayer.posZ, false));
        mc.thePlayer.sendQueue.addToSendNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
        mc.thePlayer.sendQueue.addToSendNoEvent(new C03PacketPlayer(true));
    }

    /**
     * Client onUpdate method
     * without being enabled
     * */
    public void onUpdateNoToggle(EventUpdate event) {}

    /** Animation */
    public void updateAnimation() {
        animate.update();
    }

    public void setMaxAnimation(float max) {
        animate.setMax(max);
    }

    public void setMinAnimation(float min) {
        animate.setMin(min);
    }

    public void setSpeedAnimation(float speed) {
        animate.setSpeed(speed);
    }

    public float getAnimationValue() {
        return animate.getValue();
    }

    public void setAnimationValue(float value) {
        animate.setValue(value);
    }

    public void setAnimationEasingValue(Easing easing) {
        animate.setEase(easing);
    }

    public void setReversedAnimation(boolean reversed) {
        animate.setReversed(reversed);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if(enabled) RequestHandler.addToArrayList(getMode() != null ? name + " " + getMode().getValue() : name);
        else RequestHandler.removeFromArrayList(getMode() != null ? name + " " + getMode().getValue() : name);
    }
}
