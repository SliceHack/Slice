package slice.command.commands;

import net.minecraft.client.Minecraft;
import slice.command.Command;
import slice.command.data.CommandInfo;

@CommandInfo(name = "vclip", description = "Clips a player in the y axis.")
public class CommandVClip extends Command {

    Minecraft mc = Minecraft.getMinecraft();

    public boolean handle(String name, String[] args) {
        try {
            if (args.length < 1) {
                addMessage("Usage: vclip <blocks>");
                return false;
            }
            double blocks = Double.parseDouble(args[0]);
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + blocks, mc.thePlayer.posZ);

            return true;
        } catch (Exception e) {
            addMessage("That was not a number. SMH.");
            return false;
        }
    }
}
