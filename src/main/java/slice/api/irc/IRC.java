package slice.api.irc;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S02PacketChat;
import slice.Slice;
import slice.api.irc.event.SocketEvents;
import slice.event.events.EventChat;
import slice.event.events.EventChatMessage;
import slice.event.events.EventPacket;
import slice.event.events.EventSwitchAccount;
import slice.util.LoggerUtil;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The class used for handling the Socket connection
 *
 * @author Nick & Dylan
 */
@Getter @Setter
public class IRC {

    /** API url */
    private static final String API_URL = "http://d24b-173-88-170-62.ngrok.io/";

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

            socket.on("addMessage", (args) -> {
                LoggerUtil.addMessage(args[0] + "");
            });

            connect();
        } catch (Exception ignored){}
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

        String message = e.getChatComponent().getUnformattedText();
        for(String s : list) {
            if (message.contains(s)) {
                LoggerUtil.addMessageNoPrefix(Slice.INSTANCE.replaceUsername(s.split(":")[0], s.split("1")[1], message));
                return;
            }
            return;
        }
    }

    /**
     * Keeps the socket connection alive.
     * */
    public void onKeepAlive() {
        if(!socket.connected())
            return;

        socket.emit("keepAlive", "keepAlive");
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

        if(socket.connected())
            return;

        LoggerUtil.addMessage("Failed to connect to the server!");
    }
}