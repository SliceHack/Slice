package slice.module.modules.misc;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import slice.event.data.EventInfo;
import slice.event.events.EventClientBrand;
import slice.event.events.EventPacket;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.ModeValue;

import java.io.ByteArrayOutputStream;
import java.nio.Buffer;

@SuppressWarnings("all") @ModuleInfo(name = "Spoofer", description = "Spoofs your client brand", category = Category.MISC)
public class Spoofer extends Module {

    ModeValue mode = new ModeValue("Mode", "Lunar", "Lunar", "Geyser", "Forge");
    @EventInfo
    public void onPacket(EventPacket e) {
        Packet<?> p = e.getPacket();

        if(p instanceof C17PacketCustomPayload) {
            C17PacketCustomPayload packet = (C17PacketCustomPayload) p;

            if(packet.channel.equalsIgnoreCase("MC|Brand")) {
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                ByteBuf message = Unpooled.buffer();

                switch (mode.getValue()) {
                    case "Lunar":
                        message.writeBytes(("lunarclient:" + lunarSha()).getBytes());
                        break;
                    case "Forge":
                        message.writeBytes("fml".getBytes());
                        break;
                    case "Geyser":
                        message.writeBytes("GeyserMC".getBytes());
                        break;

                }
            }

            switch (mode.getValue()) {
                case "Forge": {
                    packet.data = packetBuffer("FML", true);
                }
                case "Geyser": {
                    packet.data = packetBuffer("Geyser", false);
                    break;
                }
                case "Lunar": {
                    packet.data = packetBuffer("llunarclient:" + lunarSha(), false);
                    break;
                }
            }
        }

    }

    public String lunarSha() {
        String chars = "abcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            int index = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
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
