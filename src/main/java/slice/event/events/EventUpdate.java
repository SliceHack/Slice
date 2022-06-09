package slice.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import slice.event.Event;

@Getter @Setter
@AllArgsConstructor
public class EventUpdate extends Event {
    private double x, y, z;
    private float yaw, pitch;
    private boolean onGround, pre;

    public void setYaw(float yaw) {
        this.yaw = yaw;
        Minecraft.getMinecraft().thePlayer.rotationYawHead = yaw;
        Minecraft.getMinecraft().thePlayer.renderYawOffset = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
        Minecraft.getMinecraft().thePlayer.rotationPitchHead = pitch;
    }
}
