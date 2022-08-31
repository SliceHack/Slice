package slice.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.EntityLivingBase;
import slice.event.Event;


@Getter @Setter
@AllArgsConstructor
public class EventEntityRender extends Event {
    private EntityLivingBase entity;
    private float partialTicks;
    private boolean pre;
}
