package the_fireplace.ias.gui;

import com.github.mrebhan.ingameaccountswitcher.tools.alt.AltDatabase;
import net.minecraft.client.gui.GuiButton;
import ru.vidtu.iasfork.msauth.MSAuthScreen;
import the_fireplace.ias.account.ExtendedAccountData;

/**
 * The GUI where the alt is added
 * @author The_Fireplace
 * @author evilmidget38
 */
public class GuiAddAccount extends AbstractAccountGui {

	public GuiAddAccount()
	{
		super("Add Account");
	}

	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.add(new GuiButton(4, this.width / 2 - 75, 30, 150, 20, "Login with Microsoft"));
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.enabled && button.id == 4) {
			this.mc.displayGuiScreen(new MSAuthScreen(this));
		} else {
			super.actionPerformed(button);
		}
	}

	@Override
	public void complete()
	{
		AltDatabase.getInstance().getAlts().add(new ExtendedAccountData(getUsername(), getPassword(), getUsername()));
	}
}
