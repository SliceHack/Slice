package slice.module.modules.render;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import slice.Slice;
import slice.event.Event;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.ModeValue;
import slice.util.LoggerUtil;
import slice.util.Timer;

import java.text.SimpleDateFormat;
import java.util.Date;

@ModuleInfo(name = "Interface", description = "The Interface of Slice", category = Category.RENDER)
@Getter @Setter
public class Interface extends Module {

    BooleanValue clearChat = new BooleanValue("Transparent Chat", true);
    BooleanValue fontChat = new BooleanValue("Font Chat", true);
    BooleanValue toggleNotifications = new BooleanValue("Toggle Notifications", false);
    ModeValue fontChatMode = new ModeValue("Font Mode", "Poppins", "Poppins", "Arial");


    public void onUpdateNoToggle(EventUpdate event) {
        fontChatMode.setHidden(!fontChat.getValue());

        double milliseconds = System.currentTimeMillis() - Slice.INSTANCE.startTime;
        int seconds = (int) (milliseconds / 1000),
                minutes = seconds / 60,
                hours = minutes / 60,
                days = hours / 24,
                seconds1 = (int) ((Slice.INSTANCE.totalTime + milliseconds) / 1000),
                minutes1 = seconds / 60,
                hours1 = minutes / 60,
                days1 = hours / 24;

        seconds %= 60;
        minutes %= 60;
        hours %= 24;

        seconds1 %= 60;
        minutes1 %= 60;
        hours1 %= 24;

        Slice.INSTANCE.playTime = days + "d " + hours + "h " + minutes + "m " + seconds + "s";
        Slice.INSTANCE.totalPlayTime = days1 + "d " + hours1 + "h " + minutes1 + "m " + seconds1 + "s";
    }

    public void onEnable() {
        setEnabled(false);
    }
}
