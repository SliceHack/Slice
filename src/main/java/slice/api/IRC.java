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
    private static final String API_URL = "http://localhost:3001";

    private Socket socket;

    /***
     * Connect to an IRC server.
     * */
    public IRC() {
        try {
            IO.Options options = IO.Options.builder().build();
            socket = IO.socket(URI.create(API_URL), options);

            socket.on("newMessage", (args) -> {
                String discordName = (String) args[0];
                String message = (String) args[1];

                LoggerUtil.addIRCMessage(discordName, message);
            });

            socket.emit("connected", Slice.INSTANCE.discordName); // we need this for the username
            socket.connect();
        } catch (Exception ignored){}
    }

    public void sendMessage(String message) {
        if(!socket.connected())
            return;

        socket.emit("message", message);
    }

}
