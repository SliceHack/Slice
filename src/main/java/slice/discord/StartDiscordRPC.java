package slice.discord;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import slice.util.LoggerUtil;

public class StartDiscordRPC {
    long timestamp = 0;

    public void start() {
        timestamp = System.currentTimeMillis();

        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler((user) -> {
            System.out.println("Welcome " + user.username + "#" + user.discriminator);
        }).build();

        new Thread(DiscordRPC::discordRunCallbacks).start();

        DiscordRPC.discordInitialize("984300399534170113", handlers, true);
    }

    public void updateDiscord(String line1, String line2) {
        DiscordRichPresence presence = new DiscordRichPresence.Builder(line2)
                .setBigImage("large", "")
                .setDetails(line1)
                .setStartTimestamps(timestamp)
                .build();

        DiscordRPC.discordUpdatePresence(presence);
    }

}