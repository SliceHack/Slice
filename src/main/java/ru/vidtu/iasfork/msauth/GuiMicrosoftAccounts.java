package ru.vidtu.iasfork.msauth;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.resources.I18n;

/**
 * Lists previously saved Microsoft accounts, so they can be re-logged-in to
 * without repeating the browser OAuth flow, or removed.
 */
public class GuiMicrosoftAccounts extends GuiScreen {
	private final GuiScreen prev;
	private int selectedIndex = 0;
	private GuiMicrosoftAccounts.List list;
	private GuiButton login;
	private GuiButton remove;

	public GuiMicrosoftAccounts(GuiScreen prev) {
		this.prev = prev;
	}

	@Override
	public void initGui() {
		this.buttonList.clear();
		this.list = new GuiMicrosoftAccounts.List(this.mc);
		this.buttonList.add(new GuiButton(0, this.width / 2 - 154, this.height - 28, 100, 20, "Add Account"));
		this.buttonList.add(login = new GuiButton(1, this.width / 2 - 50, this.height - 28, 100, 20, "Login"));
		this.buttonList.add(remove = new GuiButton(2, this.width / 2 + 54, this.height - 28, 100, 20, "Remove"));
		this.buttonList.add(new GuiButton(3, this.width / 2 - 50, this.height - 52, 100, 20, I18n.format("gui.cancel")));
		updateButtons();
	}

	private void updateButtons() {
		boolean hasAccounts = !MicrosoftAccount.msaccounts.isEmpty();
		login.enabled = hasAccounts;
		remove.enabled = hasAccounts;
	}

	@Override
	public void handleMouseInput() throws java.io.IOException {
		super.handleMouseInput();
		this.list.handleMouseInput();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.list.drawScreen(mouseX, mouseY, partialTicks);
		this.drawCenteredString(this.fontRendererObj, "Microsoft Accounts", this.width / 2, 4, -1);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (!button.enabled) {
			return;
		}

		if (button.id == 0) {
			this.mc.displayGuiScreen(new MSAuthScreen(this));
		} else if (button.id == 1) {
			login();
		} else if (button.id == 2) {
			remove();
		} else if (button.id == 3) {
			this.mc.displayGuiScreen(this.prev);
		}
	}

	private void login() {
		if (!MicrosoftAccount.msaccounts.isEmpty()) {
			MicrosoftAccount.msaccounts.get(this.selectedIndex).login();
		}
	}

	private void remove() {
		if (!MicrosoftAccount.msaccounts.isEmpty()) {
			MicrosoftAccount.msaccounts.remove(this.selectedIndex);
			MicrosoftAccount.save(this.mc);
			if (this.selectedIndex > 0) {
				this.selectedIndex--;
			}
			updateButtons();
		}
	}

	class List extends GuiSlot {
		public List(Minecraft mcIn) {
			super(mcIn, GuiMicrosoftAccounts.this.width, GuiMicrosoftAccounts.this.height, 32, GuiMicrosoftAccounts.this.height - 64, 14);
		}

		@Override
		protected int getSize() {
			return MicrosoftAccount.msaccounts.size();
		}

		@Override
		protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
			GuiMicrosoftAccounts.this.selectedIndex = slotIndex;

			if (isDoubleClick) {
				GuiMicrosoftAccounts.this.login();
			}
		}

		@Override
		protected boolean isSelected(int slotIndex) {
			return slotIndex == GuiMicrosoftAccounts.this.selectedIndex;
		}

		@Override
		protected int getContentHeight() {
			return MicrosoftAccount.msaccounts.size() * 14;
		}

		@Override
		protected void drawBackground() {
			GuiMicrosoftAccounts.this.drawDefaultBackground();
		}

		@Override
		protected void drawSlot(int entryID, int x, int y, int listWidth, int mouseXIn, int mouseYIn) {
			MicrosoftAccount data = MicrosoftAccount.msaccounts.get(entryID);
			GuiMicrosoftAccounts.this.drawString(GuiMicrosoftAccounts.this.fontRendererObj, data.alias(), x + 2, y + 1, 16777215);
		}
	}
}
