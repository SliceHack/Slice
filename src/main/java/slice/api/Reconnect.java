package slice.api;

import io.socket.client.Socket;
import lombok.AllArgsConstructor;
import slice.Slice;
import slice.util.LoggerUtil;

/**
 * Reconnects to the server.
 */
@AllArgsConstructor
public class Reconnect implements Runnable {

    public Socket socket;
    public IRC irc;

    /**
     * Runs the reconnection.
     */
    @Override
    public void run() {
        LoggerUtil.addMessage("Please wait while we reconnect you.");
        if(Slice.INSTANCE.getIrc().getSocket().connected()) Slice.INSTANCE.getIrc().getSocket().disconnect();
        Slice.INSTANCE.getIrc().getSocket().connect();
        new Thread(() -> {
            try {
                while (!socket.connected());
                Slice.INSTANCE.getIrc().getSocketEvents().runConnected();
            } catch (Exception ignored) {}
        }).start();
    }
}
