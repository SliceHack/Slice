package slice.api.irc;

import io.socket.client.IO;
import io.socket.client.Socket;
import lombok.Getter;
import slice.api.irc.event.SocketEvents;
import slice.event.events.EventSwitchAccount;
import slice.util.LoggerUtil;

import java.net.URI;

@Getter
public class IRC {

    /** API url */
    private static final String API_URL = "http://localhost:3001";

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
            LoggerUtil.addMessage("Not connected to the IRC server.");
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

}
