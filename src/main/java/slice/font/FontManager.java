package slice.font;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;

@Getter
@SuppressWarnings("all")
public class FontManager {

    private final HashMap<String, TTFFontRenderer> fonts = new HashMap<>();

    private void createFont(String name, int size) {
        Font font;

        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("minecraft", "slice/fonts/" + name + ".ttf")).getInputStream();
            font = Font.createFont(0, is);
            fonts.put(name + size, new TTFFontRenderer(font.deriveFont(Font.PLAIN, size)));
            font = font.deriveFont(Font.PLAIN, size);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", Font.PLAIN, +10);
        }
    }
    public TTFFontRenderer getFont(String name, int size) {
        if(fonts.get(name + size) == null) {
            createFont(name, size);
            return getDefaultFont();
        }

        return fonts.get(name + size);
    }

    public TTFFontRenderer getRobotoFont(int size) {
        return getFont("Roboto-Regular", size);
    }

    public TTFFontRenderer getRobotoFont() {
        return getFont("Roboto-Regular", 16);
    }

    public TTFFontRenderer getArialFont(int size) {
        if(fonts.get("Arial" + size) == null) {
            fonts.put("Arial" + size, new TTFFontRenderer(new Font("Arial", Font.PLAIN, size)));
        }
        return fonts.get("Arial" + size);
    }

    private TTFFontRenderer getDefaultFont() {
        if(fonts.get("default") == null)
            fonts.put("default", new TTFFontRenderer(new Font("default", Font.PLAIN, +10)));

        return fonts.get("default");
    }

    
}
