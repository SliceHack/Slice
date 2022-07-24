package slice.module.modules.misc;

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

    public boolean send(String str) {
        String[] args = str.split(" ");
        boolean cancel = false;

        if (args[0].equalsIgnoreCase("§r§d[AD]") && (args[2].equalsIgnoreCase("/join") || args[3].equalsIgnoreCase("/join"))) {
           cancel = ads.getValue();
        }

        if ((args[1].equalsIgnoreCase("ad") || args[1].equalsIgnoreCase("join")) || args[2].equalsIgnoreCase("ad") || args[2].equalsIgnoreCase("join")) {
            cancel = fakeAds.getValue();
        }

        if ((args.length == 5) && str.contains("§3joined your lobby.§r")) {
            cancel = lobbyJoins.getValue();
        }

        if (str.contains("just got credits for voting at §r§ehttps://minecraftservers.org/vote/443456§r§6! Use §r§e/voting§r§6 for info.§r")) {
            cancel = voting.getValue();
        }
        return cancel;
    }
}
