package slice.module.modules.misc;

import net.minecraft.entity.player.EntityPlayer;
import slice.event.data.EventInfo;
import slice.event.events.EventHandleChat;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.BooleanValue;

@ModuleInfo(name = "Minehut", description = "Minehut Utility", category = Category.MISC)
public class Minehut extends Module {

    BooleanValue ads = new BooleanValue("Disable ADS", true);
    BooleanValue fakeAds = new BooleanValue("Disable fake ADS", true);
    BooleanValue lobbyJoins = new BooleanValue("Disable lobby join messages", true);
    BooleanValue voting = new BooleanValue("Disable voting messages", true);

    @EventInfo
    public void onHandleChat(EventHandleChat e) {
        String[] args = e.getS02PacketChat().getChatComponent().getUnformattedText().split(" ");
        String str = e.getS02PacketChat().getChatComponent().getUnformattedText();

        if (args[0].equalsIgnoreCase("[AD]") && (args[2].equalsIgnoreCase("/join") || args[3].equalsIgnoreCase("/join"))) e.setCancelled(ads.getValue());
        if ((args[1].equalsIgnoreCase("ad") || args[1].equalsIgnoreCase("join")) || args[2].equalsIgnoreCase("ad") || args[2].equalsIgnoreCase("join")) e.setCancelled(fakeAds.getValue());
        if ((args.length == 5) && str.contains("joined your lobby.")) e.setCancelled(lobbyJoins.getValue());
        if (str.contains("just got credits for voting at https://minecraftservers.org/vote/443456 Use /voting for info.")) e.setCancelled(voting.getValue());

        e.setChatVisibility(EntityPlayer.EnumChatVisibility.HIDDEN);
    }
}
