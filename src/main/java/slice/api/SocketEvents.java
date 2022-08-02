package slice.api;

import io.socket.client.Socket;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import org.json.JSONArray;
import slice.Slice;
import slice.util.HardwareUtil;
import slice.util.LoggerUtil;

import java.util.ArrayList;
import java.util.Arrays;
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

        socket.on("connected", (args) -> runConnected());

        socket.on("usernameSet", (args) -> {
            try {
                JSONArray array = (JSONArray) args[0];
                List<String> list = new ArrayList<>();

                for (int i = 0; i < array.length(); i++) {
                    String name = array.getString(i);
                    list.add(name);
                }
                Slice.INSTANCE.getIrc().setList(list);
                LoggerUtil.addMessage(list.toString());
            } catch (Exception e) {
                assert args[0] instanceof String;
                String s = (String) args[0];
                s = s.replace("[", "").replace("]", "").replace("\"", "").replace(",", "\n");
                String[] lines = s.split("\n");
                List<String> list = new ArrayList<>(Arrays.asList(lines));
                LoggerUtil.addMessage(list.toString());
                Slice.INSTANCE.getIrc().setList(list);
            }
        });

        socket.on("ircConnection", (args) -> {
            String discordName = (String) args[0];

            if(discordName == null)
                return;

            LoggerUtil.addMessage(discordName + " has connected");
        });

        socket.on("ircDisconnection", (args) -> {
            String discordName = (String) args[0];

            if(discordName == null)
                return;

            LoggerUtil.addMessage(discordName + " has disconnected");
        });

        socket.on("disconnected", (args) -> LoggerUtil.addMessage("Disconnected from the server"));

        new Thread(() -> {
            while(true) onKeepAlive();
        }).start();
    }

    /**
     * Runs when the socket is connected.
     * */
    public void runConnected() {
        socket.emit("connected", Slice.INSTANCE.discordName, Minecraft.getMinecraft().getSession().getUsername(), HardwareUtil.getHardwareID());
    }

    /**
     * onKeepAlive()
     * */
    public void onKeepAlive() {
        try {
            Thread.sleep(5000L);
            socket.emit("keepAlive");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}