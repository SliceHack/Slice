package slice.api.irc.event;

import io.socket.client.Socket;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import slice.Slice;
import slice.util.LoggerUtil;

@Getter @Setter
public class SocketEvents {

    private Socket socket;

    public SocketEvents(Socket socket) {
        this.socket = socket;
        runOnEvent();
        runConnected();
    }

    private void runOnEvent() {
        socket.on("newMessage", (args) -> {
            String discordName = (String) args[0];
            String message = (String) args[1];

            LoggerUtil.addIRCMessage(discordName, message);
        });
    }

    private void runConnected() {
        socket.emit("connected", Slice.INSTANCE.discordName, Minecraft.getMinecraft().getSession().getUsername());
    }
}
