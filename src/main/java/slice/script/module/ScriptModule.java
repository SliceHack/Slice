package slice.script.module;

import slice.event.data.EventInfo;
import slice.event.events.*;
import slice.module.Module;
import slice.module.data.Category;
import slice.script.lang.Base;
import slice.script.lang.logger.Chat;
import slice.script.lang.util.ScriptKeyUtil;
import slice.script.lang.util.ScriptMoveUtil;

import javax.script.ScriptEngine;

@SuppressWarnings("unused")
public class ScriptModule extends Module {

    private final ScriptEngine engine;

    public ScriptModule(String name, String description, Category category, ScriptEngine engine) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.engine = engine;
        engine.put("chat", Chat.INSTANCE);
        engine.put("MoveUtil", ScriptMoveUtil.INSTANCE);
        engine.put("KeyUtil", ScriptKeyUtil.INSTANCE);
        engine.put("timer", timer);
        init();
    }

    @Override
    public void onUpdateNoToggle(EventUpdate event) {
        engine.put("player", mc.thePlayer);
    }

    @Override
    public void init() {
        Base.callFunction(engine, "init");
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

}
