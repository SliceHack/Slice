package slice.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import slice.event.Event;

@Getter @Setter
@AllArgsConstructor
public class EventRenderEntityModel extends Event {
    private RendererLivingEntity<? extends EntityLivingBase> renderer;
    public ModelBase model;
    private EntityLivingBase entity;
    private float partialTicks;
    private float maxRenderDistance;
    public float p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor;
}
