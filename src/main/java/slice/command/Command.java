package slice.command;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import slice.command.data.CommandInfo;
import slice.util.LoggerUtil;
import slice.util.Timer;

@Getter @Setter
public abstract class Command {

    /** Command fields */
    protected Minecraft mc = Minecraft.getMinecraft();
    protected EntityPlayerSP player = mc.thePlayer;
    protected WorldClient world = mc.theWorld;
    protected Timer timer = new Timer();

    /* CommandInfo */
    private CommandInfo info = getClass().getAnnotation(CommandInfo.class);

    private String name, description;
    private String[] aliases;

    public Command() {
        if(info == null) {
            throw new IllegalArgumentException("Command class must have @CommandInfo annotation");
        }
        this.name = info.name();
        this.description = info.description();
        this.aliases = info.aliases();
    }

    public void init() {}

    public void addMessage(String message) {
        LoggerUtil.addMessage(message);
    }

    public abstract boolean handle(String name, String[] args);

    public void addAlias(String alias) {
        String[] newAliases = new String[aliases.length + 1];
        for (int i = 0; i < aliases.length; i++) {
            newAliases[i] = aliases[i];
        }
        newAliases[newAliases.length - 1] = alias;
        this.aliases = newAliases;
    }
}
