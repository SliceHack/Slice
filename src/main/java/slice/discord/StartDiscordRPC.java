package slice.discord;

import com.sliceclient.anticheat.SliceAC;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.arikia.dev.drpc.DiscordUser;
import net.arikia.dev.drpc.callbacks.ReadyCallback;
import slice.Slice;
import slice.api.API;
import slice.api.IRC;
import slice.util.LoggerUtil;

public class StartDiscordRPC {
    private long timestamp = 0;
    private boolean isRunning = true;

    public void start() {
        this.timestamp = System.currentTimeMillis();
//        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(discordUser -> {
//            LoggerUtil.addTerminalMessage("Welcome " + discordUser.username + "#" + discordUser.discriminator);
//        }).build();
//
//        DiscordRPC.discordInitialize("984300399534170113", handlers, true);
//
//        new Thread(() -> {
//            while (isRunning) {
//                DiscordRPC.discordRunCallbacks();
//            }
//        }).start();
    }

    public void setPresence(String line1, String line2) {
        DiscordRichPresence.Builder presence = new DiscordRichPresence.Builder(line2);
        presence.setBigImage("slice", Slice.VERSION);
        presence.setDetails(line1);
        presence.setStartTimestamps(timestamp);

        DiscordRPC.discordUpdatePresence(presence.build());
    }

    public void shutdown() {
        this.isRunning = false;
        DiscordRPC.discordShutdown();
    }


}