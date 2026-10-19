package the_fireplace.ias.events;

import com.github.mrebhan.ingameaccountswitcher.tools.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import slice.Slice;
import slice.event.data.EventInfo;
import slice.event.events.EventActionPerformed;
import slice.event.events.EventClientTick;
import slice.event.events.EventGUIInit;
import the_fireplace.ias.gui.GuiAccountSelector;
import the_fireplace.ias.gui.GuiButtonWithImage;

/**
 * @author The_Fireplace
 */
public class ClientEvents {

    public static ClientEvents INSTANCE = new ClientEvents();

    public static void register() {
        Slice.INSTANCE.getEventManager().register(ClientEvents.INSTANCE);
    }

	@EventInfo
	public void guiEvent(EventGUIInit event){
		GuiScreen gui = event.getScreen();
		if(gui instanceof GuiMainMenu){
			event.getButtonList().add(new GuiButtonWithImage(20, gui.width / 2 + 104, (gui.height / 4 + 48) + 72 + 12, 20, 20, ""));
		}
	}

	@EventInfo
	public void onClick(EventActionPerformed event){
		if(event.getScreen() instanceof GuiMainMenu && event.getButton().id == 20){
			if(Config.getInstance() == null){
				Config.load();
			}
			Minecraft.getMinecraft().displayGuiScreen(new GuiAccountSelector());
		}
	}

	@EventInfo
	public void onTick(EventClientTick t) {
		GuiScreen screen = Minecraft.getMinecraft().currentScreen;

		if (screen instanceof GuiMainMenu) {
			screen.drawCenteredString(
                    Minecraft.getMinecraft().fontRendererObj,
                    "You are currently logged in as " + Minecraft.getMinecraft().getSession().getUsername()+".",
                    screen.width / 2,
                    screen.height / 4 + 48 + 72 + 12 + 22,
                    0xFFCC8888
            );
		} else if(screen instanceof GuiMultiplayer && Minecraft.getMinecraft().getSession().getToken().equals("0")){
            screen.drawCenteredString(
                    Minecraft.getMinecraft().fontRendererObj,
                    "You are currently in offline mode. You can only join offline mode servers.",
                    screen.width / 2,
                    10,
                    16737380
            );
		}
	}

    // probably don't need the code below we will see though
	/*@SubscribeEvent
	public void configChanged(ConfigChangedEvent event){
		if(event.modID.equals(Reference.MODID)){
			IAS.syncConfig();
		}
	}*/

}
