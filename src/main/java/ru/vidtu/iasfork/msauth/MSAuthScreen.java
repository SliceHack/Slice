package ru.vidtu.iasfork.msauth;

import com.github.mrebhan.ingameaccountswitcher.MR;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import the_fireplace.ias.gui.GuiAccountSelector;
import the_fireplace.iasencrypt.EncryptionTools;

/**
 * @author ru.vidtu (IAS fork), ported to MCP naming
 */
public class MSAuthScreen extends GuiScreen implements MSAuthHandler {
	public static final String[] symbols = new String[]{"▃ ▄ ▅ ▆ ▇ █ ▇ ▆ ▅ ▄ ▃", "_ ▃ ▄ ▅ ▆ ▇ █ ▇ ▆ ▅ ▄", "_ _ ▃ ▄ ▅ ▆ ▇ █ ▇ ▆ ▅", "_ _ _ ▃ ▄ ▅ ▆ ▇ █ ▇ ▆", "_ _ _ _ ▃ ▄ ▅ ▆ ▇ █ ▇", "_ _ _ _ _ ▃ ▄ ▅ ▆ ▇ █", "_ _ _ _ ▃ ▄ ▅ ▆ ▇ █ ▇", "_ _ _ ▃ ▄ ▅ ▆ ▇ █ ▇ ▆", "_ _ ▃ ▄ ▅ ▆ ▇ █ ▇ ▆ ▅", "_ ▃ ▄ ▅ ▆ ▇ █ ▇ ▆ ▅ ▄", "▃ ▄ ▅ ▆ ▇ █ ▇ ▆ ▅ ▄ ▃", "▄ ▅ ▆ ▇ █ ▇ ▆ ▅ ▄ ▃ _", "▅ ▆ ▇ █ ▇ ▆ ▅ ▄ ▃ _ _", "▆ ▇ █ ▇ ▆ ▅ ▄ ▃ _ _ _", "▇ █ ▇ ▆ ▅ ▄ ▃ _ _ _ _", "█ ▇ ▆ ▅ ▄ ▃ _ _ _ _ _", "▇ █ ▇ ▆ ▅ ▄ ▃ _ _ _ _", "▆ ▇ █ ▇ ▆ ▅ ▄ ▃ _ _ _", "▅ ▆ ▇ █ ▇ ▆ ▅ ▄ ▃ _ _", "▄ ▅ ▆ ▇ █ ▇ ▆ ▅ ▄ ▃ _"};
	private static final ResourceLocation DEMO_BG = new ResourceLocation("textures/gui/demo_background.png");
	public GuiScreen prev;
	public List<String> text = new ArrayList<String>();
	public boolean endTask = false;
	public int tick;
	public final boolean add;
	public boolean cancelButton = true;

	public MSAuthScreen(GuiScreen prev) {
		this.prev = prev;
		this.add = true;
		AuthSys.start(this);
	}

	public MSAuthScreen(GuiScreen prev, String token, String refresh) {
		this.prev = prev;
		this.add = false;
		AuthSys.start(token, refresh, this);
	}

	@Override
	public void initGui() {
		GuiButton btn = new GuiButton(0, this.width / 2 - 50, (this.height + 114) / 2, 100, 20, I18n.format("gui.cancel"));
		btn.enabled = this.cancelButton;
		this.buttonList.add(btn);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == 0) {
			this.mc.displayGuiScreen(this.prev);
		}
	}

	@Override
	public void setWorldAndResolution(Minecraft mc, int width, int height) {
		this.prev.setWorldAndResolution(mc, width, height);
		super.setWorldAndResolution(mc, width, height);
	}

	@Override
	public void updateScreen() {
		++this.tick;
		this.buttonList.get(0).enabled = this.cancelButton;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float delta) {
		this.drawDefaultBackground();
		if (this.prev != null) {
			this.prev.drawScreen(0, 0, delta);
		}

		drawRect(0, 0, this.width, this.height, Integer.MIN_VALUE);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(DEMO_BG);
		this.drawTexturedModalRect((this.width - 248) / 2, (this.height - 166) / 2, 0, 0, 248, 166);
		String title = I18n.format("ias.msauth.title");
		this.fontRendererObj.drawString(title, this.width / 2 - this.fontRendererObj.getStringWidth(title) / 2, (this.height - 156) / 2, -16777216);

		for (int i = 0; i < this.text.size(); ++i) {
			String line = this.text.get(i);
			this.fontRendererObj.drawString(line, this.width / 2 - this.fontRendererObj.getStringWidth(line) / 2, this.height / 2 + i * 10 - this.text.size() * 5, -13290187);
		}

		if (!this.endTask) {
			String spinner = symbols[this.tick % symbols.length];
			this.fontRendererObj.drawString(spinner, this.width / 2 - this.fontRendererObj.getStringWidth(spinner) / 2, this.height - 10, -26368);
		}

		super.drawScreen(mouseX, mouseY, delta);
	}

	@Override
	public void onGuiClosed() {
		AuthSys.stop();
		this.prev.onGuiClosed();
		super.onGuiClosed();
	}

	@Override
	public void setState(String key) {
		Minecraft mc = Minecraft.getMinecraft();
		mc.addScheduledTask(() -> this.text = mc.fontRendererObj.listFormattedStringToWidth(I18n.format(key), 240));
	}

	@Override
	public void error(Throwable t) {
		this.cancelButton = true;
		Minecraft mc = Minecraft.getMinecraft();
		mc.addScheduledTask(() -> {
			this.endTask = true;
			if (t instanceof AuthSys.MicrosoftAuthException) {
				this.text = mc.fontRendererObj.listFormattedStringToWidth(EnumChatFormatting.DARK_RED + I18n.format("ias.msauth.error", t.getMessage()), 240);
			} else {
				this.text = mc.fontRendererObj.listFormattedStringToWidth(EnumChatFormatting.DARK_RED + I18n.format("ias.msauth.error", t.toString()), 240);
			}
		});
	}

	@Override
	public void success(String name, String uuid, String token, String refresh) {
		Minecraft mc = Minecraft.getMinecraft();
		mc.addScheduledTask(() -> {
			if (this.add) {
				MicrosoftAccount.msaccounts.add(new MicrosoftAccount(name, EncryptionTools.encode(token), EncryptionTools.encode(refresh)));
				MicrosoftAccount.save(mc);
				mc.displayGuiScreen(new GuiAccountSelector());
			} else {
				try {
					MR.setSession(new Session(name, uuid, token, "mojang"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				mc.displayGuiScreen(null);
			}
		});
	}

	@Override
	public void cancellble(boolean b) {
		this.cancelButton = b;
	}
}
