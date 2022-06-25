package slice.api;

import io.socket.client.IO;
import io.socket.client.Socket;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Session;
import slice.Slice;
import slice.event.events.EventSwitchAccount;
import slice.util.HardwareUtil;
import slice.util.LoggerUtil;

import java.net.URI;

@Getter
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

            socket.emit("connected", Slice.INSTANCE.discordName, Minecraft.getMinecraft().getSession().getUsername()); // we need this for the username
            socket.connect();
        } catch (Exception ignored){}
    }

    /**
     * Sends a message to the socket server.
     *
     * @parma message The message to send to the server.
     * */
    public void sendMessage(String message) {
        if(!socket.connected())
            return;

        socket.emit("message", message);
    }

    /**
     * changes the users logged in account on the server.
     *
     * @parma event The event to switch to.
     * */
    public void accountSwitch(EventSwitchAccount event) {
        if(!socket.connected())
            return;

        socket.emit("setUsername", event.getUsername());
    }

}
