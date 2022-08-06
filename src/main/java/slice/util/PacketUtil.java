package slice.util;

import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

/**
 * PacketUtil
 *
 * @author Nick
 * */
@UtilityClass
public class PacketUtil {

    /**
     * Sends a packet to the server with no extra data.
     *
     * @param packet The packet to send.
     * */
    public static void sendPacketNoEvent(Packet<?> packet) {
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendNoEvent(packet);
    }

    /**
     * Sends a packet to the server.
     *
     * @param packet The packet to send.
     * **/
    public static void sendPacket(Packet<?> packet) {
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(packet);
    }

    /***
     * Checks if a packet is a movement packet.
     *
     * @param packet The packet to check.
     * */
    public boolean isMovementPacket(Packet<?> packet) {
        return packet instanceof C03PacketPlayer.C04PacketPlayerPosition
                || packet instanceof C03PacketPlayer.C05PacketPlayerLook
                || packet instanceof C03PacketPlayer.C06PacketPlayerPosLook;
    }
}
