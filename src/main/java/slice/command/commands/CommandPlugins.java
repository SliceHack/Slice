package slice.command.commands;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S3APacketTabComplete;
import slice.command.Command;
import slice.command.data.CommandInfo;
import slice.event.events.EventPacket;
import slice.util.LoggerUtil;

import java.util.ArrayList;
import java.util.List;

@CommandInfo(name = "plugins", description = "Finds plugins the server is using")
public class CommandPlugins extends Command {

    public boolean searching = false;
    private boolean sendMessage = false;

    public boolean handle(String name, String[] args) {
        mc.thePlayer.sendQueue.addToSendNoEvent(new C14PacketTabComplete("/"));
        searching = true;
        return true;
    }

    public void onUpdate() {
        if(!sendMessage)
            return;

        if(timer.hasReached(200L)) {
            sendMessage = false;
            timer.reset();
        }

    }

    public void onPacketReceive(EventPacket event) {
        Packet<?> packet = event.getPacket();

        if(!searching)
            return;

        if (mc.isSingleplayer()) {
            if(!sendMessage) {
                addMessage("There is no plugins in SinglePlayer Dumbass");
                searching = false;
                sendMessage = true;
            }
            return;
        }

        if(packet instanceof S3APacketTabComplete) {
            S3APacketTabComplete s3a = (S3APacketTabComplete) packet;
            String[] commands = s3a.func_149630_c();

            List<String> plugins = new ArrayList<>();
            for (String command : commands) {

                if(command.contains(":")) {
                    String plugin = command.split(":")[0].replaceAll("/", "");

                    if(!plugin.equalsIgnoreCase("Spigot")
                            && !plugin.equalsIgnoreCase("Bukkit")
                            && !plugin.equalsIgnoreCase("Paper")
                            && !plugin.equalsIgnoreCase("Minecraft")
                            && !plugin.equalsIgnoreCase("PurPur")
                            && !plugin.equalsIgnoreCase("PufferFish")) { // removes things that are most likely not plugins


                        plugin = plugin.substring(0, 1).toUpperCase() + plugin.substring(1);


                        if(plugin.contains("-")) {
                            String[] split = plugin.split("-");
                            plugin = split[0] + "-" + split[1].substring(0, 1).toUpperCase() + split[1].substring(1);
                        }

                        if (!plugins.contains(plugin)) {
                            plugins.add(plugin);
                        }
                    }
                }
            }

            if (plugins.isEmpty()) {
                if(!sendMessage) {
                    addMessage("Failed to detect plugins");
                    sendMessage = true;
                    searching = false;
                }
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Plugins (").append(plugins.size()).append("): ");
            for (String plugin : plugins) {
                sb.append("§a").append(plugin).append("§r").append(", ");
            }
            if (!sendMessage) {
                LoggerUtil.addMessageNoPrefix(sb.toString());
                searching = false;
                sendMessage = true;
            }
        }
    }
}
