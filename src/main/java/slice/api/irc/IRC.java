package slice.api.irc;

import io.socket.client.IO;
import io.socket.client.Socket;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S02PacketChat;
import slice.api.irc.event.SocketEvents;
import slice.event.events.EventPacket;
import slice.event.events.EventSwitchAccount;
import slice.util.LoggerUtil;

import java.net.URI;

/**
 * The class used for handling the Socket connection
 *
 * @author Nick & Dylan
 */
@Getter
public class IRC {

    /** API url */
    private static final String API_URL = "https://api.sliceclient.com/";

    private Socket socket;
    private SocketEvents socketEvents;

    /***
     * Connect to an IRC server.
     * */
    public IRC() {
        try {
            IO.Options options = IO.Options.builder().build();
            socket = IO.socket(URI.create(API_URL), options);
            socketEvents = new SocketEvents(socket);
            connect();
        } catch (Exception ignored){}
    }

    /**
     * Sends a message to the socket server.
     *
     * @parma message The message to send to the server.
     * */
    public void sendMessage(String message) {
        if(!socket.connected()) {
            LoggerUtil.addMessage("Not connected to the server.");
            connect();
            return;
        }

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

    /**
     * sends a packet to the server.
     *
     * @param e The event to send.
     * @param s02 The chat packet to send.
     * */
    public void onMessage(EventPacket e, S02PacketChat s02) {
        if(!socket.connected())
            return;

        socket.emit("onS02", s02.getChatComponent().getFormattedText());
    }

    /**
     * Connects to the server.
     * */
    private void connect() {
        if(socket.connected())
            return;

        socket.connect();

        if(Minecraft.getMinecraft().thePlayer == null && Minecraft.getMinecraft().theWorld == null)
            return;

        if(socket.connected()) {
            LoggerUtil.addMessage("Connected to the server!");
            return;
        }
        LoggerUtil.addMessage("Failed to connect to the server!");
    }
}
