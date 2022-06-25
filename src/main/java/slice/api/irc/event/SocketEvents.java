package slice.api.irc.event;

import io.socket.client.Socket;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import org.json.JSONArray;
import slice.Slice;
import slice.util.LoggerUtil;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class SocketEvents {

    private Socket socket;

    public SocketEvents(Socket socket) {
        this.socket = socket;
        this.runOnEvent();
        this.runConnected();
    }

    private void runOnEvent() {
        socket.on("newMessage", (args) -> {
            String discordName = (String) args[0];
            String message = (String) args[1];

            LoggerUtil.addIRCMessage(args[2] + " §c(§b" + discordName + "§c)§r", message);
        });

        socket.on("users", (args) -> {
            String message = (String) args[0];
            JSONArray users = (JSONArray) args[1];

            List<String> strings = new ArrayList<>();
            for(int i = 0; i < users.length(); i++) {
                strings.add(users.getString(i));
            }

            for(String user : strings) {
                String[] split = user.split(":");
                String username = split[0];
                String discordName = split[1];
                LoggerUtil.addMessageNoPrefix(Slice.INSTANCE.replaceUsername(username, discordName, message));
                return;
            }
            LoggerUtil.addMessageNoPrefix(message);

        });

        socket.on("ircConnection", (args) -> {
            String discordName = (String) args[0];
            LoggerUtil.addMessage(discordName + " has connected");
        });

        socket.on("ircDisconnection", (args) -> {
            String discordName = (String) args[0];
            LoggerUtil.addMessage(discordName + " has disconnected");
        });

        socket.on("keepAlive", (args) -> Slice.INSTANCE.getIrc().onKeepAlive()); // keep connection alive
    }

    public void runConnected() {
        socket.emit("connected", Slice.INSTANCE.discordName, Minecraft.getMinecraft().getSession().getUsername());
    }
}