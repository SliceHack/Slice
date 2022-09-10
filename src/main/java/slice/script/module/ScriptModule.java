package slice.script.module;

import slice.Slice;
import slice.event.data.EventInfo;
import slice.event.events.*;
import slice.font.FontManager;
import slice.module.Module;
import slice.module.data.Category;
import slice.script.lang.Base;
import slice.script.lang.logger.Chat;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.ModeValue;
import slice.setting.settings.NumberValue;
import slice.util.*;

import javax.script.ScriptEngine;

@SuppressWarnings("unused")
public class ScriptModule extends Module {

    private final ScriptEngine engine;

    public ScriptModule(String name, String description, Category category, ScriptEngine engine, FontManager fontManager) {
        Base.putClassInEngine(engine, "Chat", Chat.class);
        Base.putClassInEngine(engine, "MoveUtil", MoveUtil.class);
        Base.putClassInEngine(engine, "KeyUtil", KeyUtil.class);
        Base.putClassInEngine(engine, "RenderUtil", RenderUtil.class);
        Base.putClassInEngine(engine, "RotationUtil", RotationUtil.class);
        Base.putClassInEngine(engine, "LoggerUtil", LoggerUtil.class);
        Base.putInEngine(engine,"timer", timer);

        this.name = name;
        this.description = description;
        this.category = category;
        this.engine = engine;
    }

    @Override
    public void onUpdateNoToggle(EventUpdate event) {
        engine.put("player", mc.thePlayer);
        engine.put("FontManager", Slice.INSTANCE.getFontManager());
        engine.put("timer", timer);
    }

    @Override
    public void init() {
        Base.callFunction(engine, "init");
        Base.putInEngine(engine, "module", this);
        Base.putInEngine(engine, "ModuleManager", Slice.INSTANCE.getModuleManager());
        Base.putInEngine(engine, "CommandManager", Slice.INSTANCE.getCommandManager());
        super.init();
    }

    @Override
    public void onEnable() {
        Base.callFunction(engine, "onEnable");
        super.onEnable();
    }

    @Override
    public void onDisable() {
        Base.callFunction(engine, "onDisable");
        super.onDisable();
    }

    @EventInfo
    public void onUpdate(EventUpdate e) {
        Base.callFunction(engine, "onUpdate", e);
    }

    @EventInfo
    public void onEvent3D(Event3D e) {
        Base.callFunction(engine, "onEvent3D", e);
    }

    @EventInfo
    public void onEventAttack(EventAttack e) {
        Base.callFunction(engine, "onEventAttack", e);
    }

    @EventInfo
    public void onEventChat(EventChat e) {
        Base.callFunction(engine, "onEventChat", e);
    }

    @EventInfo
    public void onEventChatMessage(EventChatMessage e) {
        Base.callFunction(engine, "onEventChatMessage", e);
    }

    @EventInfo
    public void onEventClientBrand(EventClientBrand e) {
        Base.callFunction(engine, "onEventClientBrand", e);
    }

    @EventInfo
    public void onEventClientTick(EventClientTick e) {
        Base.callFunction(engine, "onEventClientTick", e);
    }

    @EventInfo
    public void onEventEntityRender(EventEntityRender e) {
        Base.callFunction(engine, "onEventEntityRender", e);
    }

    @EventInfo
    public void onEventJump(EventJump e) {
        Base.callFunction(engine, "onEventJump", e);
    }

    @EventInfo
    public void onEventKey(EventKey e) {
        Base.callFunction(engine, "onEventKey", e);
    }

    @EventInfo
    public void onEventMouse(EventMouse e) {
        Base.callFunction(engine, "onEventMouse", e);
    }

    @EventInfo
    public void onEventPacket(EventPacket e) {
        Base.callFunction(engine, "onEventPacket", e);
    }

    @EventInfo
    public void onEventPlayerReach(EventPlayerReach e) {
        Base.callFunction(engine, "onEventPlayerReach", e);
    }

    @EventInfo
    public void onEventSafeWalk(EventSafeWalk e) {
        Base.callFunction(engine, "onEventSafeWalk", e);
    }

    @EventInfo
    public void onEventSlowDown(EventSlowDown e) {
        Base.callFunction(engine, "onEventSlowDown", e);
    }

    @EventInfo
    public void onEvent2D(Event2D e) {
        Base.callFunction(engine, "onEvent2D", e);
    }

    public BooleanValue registerSettingBoolean(String name, boolean value) {
        BooleanValue setting = new BooleanValue(name, value);
        getSettings().add(setting);
        return setting;
    }

    public ModeValue registerSettingMode(String name, String... modes) {
        if(modes.length == 0) return null;
        ModeValue mode = new ModeValue(name, modes[0], modes);
        getSettings().add(mode);
        return mode;
    }

    public NumberValue registerSettingNumber(String name, double min, double max, double value, NumberValue.Type type) {
        NumberValue setting = new NumberValue(name, min, max, value, type);
        getSettings().add(setting);
        return setting;
    }
}
