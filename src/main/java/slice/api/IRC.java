package slice.api;

import io.socket.client.IO;
import io.socket.client.Socket;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.play.server.S02PacketChat;
import slice.Slice;
import slice.event.events.EventPacket;
import slice.event.events.EventSwitchAccount;
import slice.util.LoggerUtil;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * The class used for handling the Socket connection
 *
 * @author Nick & Dylan
 */
@Getter @Setter
public class IRC {

    /** API url */
    private static final String API_URL = "https://api.sliceclient.com/";

    private Socket socket;
    private SocketEvents socketEvents;

    private List<String> list = new ArrayList<>();


    /***
     * Connect to an IRC server.
     * */
    public IRC() {
        try {
            IO.Options options = IO.Options.builder().build();
            socket = IO.socket(URI.create(API_URL), options);
            socketEvents = new SocketEvents(socket);

            socket.on("addMessage", (args) -> LoggerUtil.addMessage(String.valueOf(args[0])));

            socket.connect();
        } catch (Exception ignored){}
    }

    /**
     * Sends a message to the socket server.
     *
     * @parma message The message to send to the server.
     * */
    public void sendMessage(String message) {
        if(!socket.connected()) {
            LoggerUtil.addMessage("Not connected to the server!");
            return;
        }

        socket.emit("message", message);
    }

    /**
     * Broadcasts a message to the server.
     * */
    public void broadcastMessage(String message) {
        if(!socket.connected()) {
            LoggerUtil.addMessage("Not connected to the server!");
            return;
        }

        socket.emit("broadcast", message);
    }

    /**
     * handles the chat message event.
     *
     * @param e The event to send.
     * */
    public void onMessage(EventPacket ep, S02PacketChat e) {
        if(!socket.connected())
            return;

        ep.setCancelled(true);
        String message = e.getChatComponent().getFormattedText();
        for(String s : list) {
            message = Slice.INSTANCE.replaceUsername(s.split(":")[0], s.split(":")[1], message);
        }
        LoggerUtil.addMessageNoPrefix(message);
    }

    /**
     * changes the users logged in account on the server.
     *
     * @parma event The event to switch to.
     * */
    public void accountSwitch(EventSwitchAccount event) {
        if(!socket.connected())
            return;

        try {
            socket.emit("setUsername", event.getUsername(), event.getLastSession().getUsername(), Slice.INSTANCE.discordName, Slice.INSTANCE.discordID);
        } catch (Exception ignored) {}
    }
}
