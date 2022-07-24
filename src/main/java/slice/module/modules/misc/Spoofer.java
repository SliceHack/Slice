package slice.module.modules.misc;

import io.netty.buffer.Unpooled;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import slice.event.data.EventInfo;
import slice.event.events.EventPacket;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.ModeValue;

@SuppressWarnings("all") @ModuleInfo(name = "Spoofer", description = "Spoofs your client brand", category = Category.MISC)
public class Spoofer extends Module {

    ModeValue mode = new ModeValue("Mode", "Lunar", "Lunnar", "Geyser", "Forge");

    @EventInfo
    public void onPacket(EventPacket e) {
        Packet<?> p = e.getPacket();

        if(p instanceof C17PacketCustomPayload) {
            C17PacketCustomPayload packet = (C17PacketCustomPayload) p;

            switch (mode.getValue()) {
                case "Forge": {
                    packet.data = packetBuffer("FML", true);
                }
                case "Geyser": {
                    packet.data = packetBuffer("Geyser", false);
                    break;
                }
                case "Lunnar": {
                    packet.channel = "REGISTER";
                    packet.data = packetBuffer("Lunar-Client", false);
                    break;
                }
            }
        }
    }

    public PacketBuffer packetBuffer(String data, boolean string) {
        PacketBuffer packetBuffer;

        if (string) {
            packetBuffer = new PacketBuffer(Unpooled.buffer());
            packetBuffer.writeString(data);
            return packetBuffer;
        }
        packetBuffer = new PacketBuffer(Unpooled.wrappedBuffer(data.getBytes()));
        return packetBuffer;
    }
}
