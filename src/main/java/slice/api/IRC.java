package slice.api;

import io.socket.client.IO;
import io.socket.client.Socket;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import slice.Slice;
import slice.util.HardwareUtil;
import slice.util.LoggerUtil;

import java.net.URI;

public class IRC {

    /** API url */
    private static final String API_URL = "https://api.sliceclient.com/irc/";
    private static final String IRC_PATH = "chat";

    private Socket socket;

    /***
     * Connect to an IRC server.
     * */
    public IRC() {
        try {
            IO.Options options = IO.Options.builder().setPath(IRC_PATH + "/").build();
            socket = IO.socket(URI.create(API_URL), options);

            socket.on("newMessage", (args) -> {
                String discordName = (String) args[0];
                String message = (String) args[1];

                LoggerUtil.addTerminalMessage(discordName + ": " + message);
            });

            socket.emit("connected", Slice.INSTANCE.discordName); // we need this for the username
            socket.connect();
        } catch (Exception ignored){}
    }

    private void sendMessage(String message) {
        if(!socket.connected())
            return;

        socket.emit("message", message);
    }

}
