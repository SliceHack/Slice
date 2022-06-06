package slice.util;

import lombok.experimental.UtilityClass;
import net.minecraft.util.ResourceLocation;

@UtilityClass
public class ResourceUtil {

    public static ResourceLocation getResource(String resource) {
        return new ResourceLocation("slice/" + resource);
    }
}
