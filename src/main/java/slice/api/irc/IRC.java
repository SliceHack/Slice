package slice.api.irc;

import io.socket.client.IO;
import io.socket.client.Socket;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S02PacketChat;
import slice.Slice;
import slice.api.irc.event.SocketEvents;
import slice.event.events.EventPacket;
import slice.event.events.EventSwitchAccount;
import slice.util.LoggerUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
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
    private static final String API_URL = "https://api.sliceclient.com";

    private Socket socket;
    private SocketEvents socketEvents;

    private List<String> list = new ArrayList<>();


    /***
     * Connect to an IRC server.
     * */
    public IRC() {
        try {
            Slice.INSTANCE.connecting = true;
            IO.Options options = IO.Options.builder().build();
            socket = IO.socket(URI.create(API_URL), options);
            socketEvents = new SocketEvents(socket);

            socket.on("addMessage", (args) -> LoggerUtil.addMessage(args[0] + ""));

            socket.connect();
        } catch (Exception ignored){}
    }

    /**
     * Gets the username in the server
     * */
    public String getUser() {
        for(String s : list) {

            String[] split = s.split(":");

            if(split[0].equals(Minecraft.getMinecraft().getSession().getUsername()) && !split[1].equalsIgnoreCase("undefined")) {
                return s;
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * Add server message
     * */
    public void addServerMessage(String message) {
        socket.emit("serverMessage", message);
    }

    /**
     * Sends a message to the socket server.
     *
     * @parma message The message to send to the server.
     * */
    public void sendMessage(String message) {
        if(!socket.connected()) {
            LoggerUtil.addMessage("Not connected to the server.");
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

        try {
            socket.emit("setUsername", event.getUsername(), event.getLastSession().getUsername());
        } catch (Exception ignored) {}
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
     * Keeps the socket connection alive.
     * */
    public void onKeepAlive() {
        if(!socket.connected()) {
            return;
        }
        socket.emit("keepAlive", "keepAlive");
    }

}
