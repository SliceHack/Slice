package slice.command.commands;

import slice.Slice;
import slice.command.Command;
import slice.command.data.CommandInfo;
import slice.util.LoggerUtil;

@CommandInfo(name = "connect", description = "reconnects to the server", aliases = {"c"})
public class CommandConnect extends Command {

    @Override
    public boolean handle(String name, String[] args) {
        if(Slice.INSTANCE.getIrc().getSocket().connected()) Slice.INSTANCE.getIrc().getSocket().disconnect();
        Slice.INSTANCE.getIrc().getSocket().connect();
        new Thread(() -> {
            try {
                while (!Slice.INSTANCE.getIrc().getSocket().connected()) {}
                LoggerUtil.addMessage("Connected to the server!");
                Slice.INSTANCE.getIrc().getSocketEvents().runConnected();
            } catch (Exception ignored) {}
        }).start();
        return false;
    }
}
