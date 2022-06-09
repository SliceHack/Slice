package slice.discord;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import slice.util.LoggerUtil;

public class StartDiscordRPC {

    public void start() {
        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler((user) -> {
            LoggerUtil.addTerminalMessage("Discord: " + user.username + "#" + user.discriminator);
            System.exit(0);
        }).build();

        DiscordRPC.discordInitialize("984300399534170113", handlers, true);
        updateDiscord("Slice Client", "Haxor Client");

        new Thread(DiscordRPC::discordRunCallbacks);

    }

    public void updateDiscord(String line1, String line2) {
        DiscordRichPresence rich = new DiscordRichPresence.Builder(line1).setDetails(line2).build();
        DiscordRPC.discordUpdatePresence(rich);
    }

}
