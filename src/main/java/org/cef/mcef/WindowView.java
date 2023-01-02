package org.cef.mcef;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import org.cef.browser.CefBrowserCustom;
import org.cef.browser.ICefRenderer;
import org.cef.browser.lwjgl.CefRendererLwjgl;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import slice.Slice;
import slice.util.LoggerUtil;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

@Getter @Setter
@SuppressWarnings("all")
public class WindowView {
    protected final Minecraft mc = Minecraft.getMinecraft();

    float x = 30f;
    float y = 30f;
    boolean focus = false;

    float width = 140f;
    float height = 200f;
    int browserScale = 2;
    long lastFocusUpdate = System.currentTimeMillis();

    private boolean handleInput = false;
    private GuiTextField textField = new GuiTextField(0, mc.fontRendererObj, 0, 0, 0, 0);

    private boolean transparent = false;
    private boolean showTitle = true;
    private boolean drag = false;
    private float dragOffsetX = 0f;
    private float dragOffsetY = 0f;
    private boolean scale = false;

    private float minWidth = 70f;
    private float minHeight = 70f;

    private CefBrowserCustom cefBrowser;
    private ICefRenderer cefRenderer;

    public WindowView() {
        textField.setMaxStringLength(1000);
        cefRenderer = new CefRendererLwjgl(true);
        cefBrowser = new CefBrowserCustom(Slice.INSTANCE.getCefRenderManager().getCefClient(), "https://sliceclient.com", true, null, cefRenderer);
        cefBrowser.setCloseAllowed();
        cefBrowser.createImmediately();
        cefBrowser.setFocus(true);
        cefBrowser.wasResized_((int)(width * browserScale), (int)(height * browserScale));
    }

    public float coerceAtLeast(float value, float min) {
        return value < min ? min : value;
    }

    public int coerceAtLeast(int value, int min) {
        return value < min ? min : value;
    }

    /**
     * @param mouseX realMouseX - x
     */
    public void render(boolean fromChat, float mouseX, float mouseY, boolean mouseEvent) {
        if(drag) {
            x = (mouseX + x) - dragOffsetX;
            y = (mouseY + y) - dragOffsetY;
            drag = Mouse.isButtonDown(0);
        }
        if(scale) {
            width = coerceAtLeast(mouseX, minWidth);
            height = coerceAtLeast(mouseY, minHeight);
            scale = Mouse.isButtonDown(0);

            mc.fontRendererObj.drawString(width + "x" + height + " Scale " + browserScale, x + width, y + height - mc.fontRendererObj.FONT_HEIGHT, Color.WHITE.getRGB(), false);
            if (Mouse.hasWheel()) {
                int wheel = Mouse.getDWheel();
                if (wheel > 0) {
                    browserScale++;
                } else if (wheel < 0) {
                    browserScale--;
                }
                browserScale = coerceAtLeast(browserScale, 1);
            }
            cefBrowser.wasResized_((int)(width * browserScale), (int)(height * browserScale));
        }

        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0f);

        float titleHeight = mc.fontRendererObj.FONT_HEIGHT + 2f;

        if(handleInput) {
            if(!fromChat || mouseEvent) {
                textField.setText("");
                handleInput = false;
            }
            drawRect(0f, -titleHeight, width, 0f, Color.LIGHT_GRAY.getRGB());
            mc.fontRendererObj.drawString(getText1(), 2f, -titleHeight + 1f, Color.WHITE.getRGB(), false);
        }

        if(showTitle || fromChat) {
            // render title
            drawRect(0f, 0f, width, titleHeight, focus ? Color.LIGHT_GRAY.getRGB() : Color.GRAY.getRGB());
            mc.fontRendererObj.drawString(cefBrowser.isLoading() ?  "Loading..." : "Browser", 2f, 1f, Color.WHITE.getRGB(), false);
            mc.fontRendererObj.drawCenteredString("X", width - (titleHeight * 0.5f), 1f, Color.WHITE.getRGB());
            if(transparent) {
                drawRect(width - (titleHeight * 2), 0f, width - titleHeight, titleHeight, Color.WHITE.getRGB());
            }
            mc.fontRendererObj.drawCenteredString("T", width - (titleHeight * 1.5f), 1f, (transparent ?  Color.BLACK.getRGB() : Color.WHITE.getRGB()));
            if(!showTitle) {
               drawRect(width - (titleHeight * 3), 0f, width - (titleHeight * 2), titleHeight, Color.WHITE.getRGB());
            }
            mc.fontRendererObj.drawCenteredString("S", width - (titleHeight * 2.5f), 1f, (showTitle ? Color.BLACK.getRGB() :  Color.WHITE.getRGB()));


            // handle title events
            if(mouseEvent && inArea(mouseX, mouseY, 0f, 0f, width, titleHeight)) {
                focus = true;
                if(Mouse.getEventButton() == 0) {
                    if(inArea(mouseX, mouseY, width - (titleHeight * 2), 0f, width - titleHeight, titleHeight)) {
                        transparent = !transparent;
                    } else if(inArea(mouseX, mouseY, width - (titleHeight * 3), 0f, width - (titleHeight * 2), titleHeight)) {
                        showTitle = !showTitle;
                    } else if(inArea(mouseX, mouseY, width - titleHeight, 0f, width, titleHeight)) {
                        try {
                            finalize();
                        } catch (Throwable e) {
                            LoggerUtil.addMessage(e.getMessage());
                        }
                    } else {
                        drag = true;
                        dragOffsetX = mouseX;
                        dragOffsetY = mouseY;
                    }
                } else {
                    handleInput = true;
                    textField.setText(cefBrowser.getURL());
                }
            }
        }
        if (!transparent) {
            drawRect(0f, titleHeight, width, height, Color.WHITE.getRGB());
        }
        cefRenderer.render(0.0, titleHeight, width, height);
        if(fromChat) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
//            glColor(); // background is white, so we need something special

            GL11.glBegin(GL11.GL_POLYGON);
            GL11.glVertex2f(width - 5f, height);
            GL11.glVertex2f(width, height);
            GL11.glVertex2f(width, height - 5f);
            GL11.glEnd();

            GL11.glColor4f(1f, 1f, 1f, 1f);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
        }

        if (fromChat) {
            int mouseRX = (int)(mouseX * browserScale);
            int mouseRY = (int)((mouseY - titleHeight) * browserScale);
            if (inArea(mouseX, mouseY, 0f, titleHeight, width, height)) {
                cefBrowser.mouseMoved(mouseRX, mouseRY, 0);
                if (Mouse.hasWheel()) {
                    val wheel = Mouse.getDWheel();
                    if (wheel != 0) {
                        cefBrowser.mouseScrolled(mouseRX, mouseRY, GuiView.keyModifiers(0), 1, wheel);
                    }
                }
            }
            if(mouseEvent) {
                if(inArea(mouseX, mouseY, 0f, titleHeight, width, height)) {
                    focus = true;
                    if(Mouse.getEventButton() == 0 && inArea(mouseX, mouseY, width - 5f, height - 5f, width, height)) {
                        scale = true;
                    } else {
                        int mod = GuiView.mouseModifiers(GuiView.keyModifiers(0));
                        cefBrowser.mouseInteracted(mouseRX, mouseRY, mod, Mouse.getEventButton(), true, 1);
                        cefBrowser.mouseInteracted(mouseRX, mouseRY, mod, Mouse.getEventButton(), false, 1);
                    }
                } else if(!inArea(mouseX, mouseY, 0f, 0f, width, titleHeight)) {
                    focus = false;
                }
            }
        }

        GL11.glPopMatrix();
        lastFocusUpdate = System.currentTimeMillis();
    }

    public static boolean inArea(float x, float y, float ax1, float ay1, float ax2, float ay2) {
        return x >= ax1 && x <= ax2 && y >= ay1 && y <= ay2;
    }

    public String getText1() {
        if(!textField.getText().isEmpty()) {
            mc.fontRendererObj.trimStringToWidth(textField.getText(), (int) width, true);
            if (System.currentTimeMillis() % 1500 > 1000) {
                return "|";
            } else {
                return "";
            }
        }
        return "Input URL...";
    }

    public static void drawRect(final double x, final double y, final double x2, final double y2, final int color) {
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);

        glColor(color);
        glBegin(GL_QUADS);

        glVertex2d(x2, y);
        glVertex2d(x, y);
        glVertex2d(x, y2);
        glVertex2d(x2, y2);
        glEnd();

        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
    }

    public static void glColor(final Color color) {
        final float red = color.getRed() / 255F;
        final float green = color.getGreen() / 255F;
        final float blue = color.getBlue() / 255F;
        final float alpha = color.getAlpha() / 255F;

        GlStateManager.color(red, green, blue, alpha);
    }

    public static void glColor(final int hex) {
        final float alpha = (hex >> 24 & 0xFF) / 255F;
        final float red = (hex >> 16 & 0xFF) / 255F;
        final float green = (hex >> 8 & 0xFF) / 255F;
        final float blue = (hex & 0xFF) / 255F;

        GlStateManager.color(red, green, blue, alpha);
    }

}
