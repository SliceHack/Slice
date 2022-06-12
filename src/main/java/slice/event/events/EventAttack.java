package slice.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.Entity;
import slice.event.Event;
import slice.module.Module;
import slice.module.data.ModuleInfo;

@Getter @Setter
@AllArgsConstructor
public class EventAttack extends Event {
    private Entity entity;
}
