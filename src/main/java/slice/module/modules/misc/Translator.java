package slice.module.modules.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import org.json.JSONObject;
import slice.Slice;
import slice.api.API;
import slice.event.Event;
import slice.event.events.*;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.util.HardwareUtil;
import slice.util.LoggerUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import static slice.api.API.readResponse;

@ModuleInfo(name = "Translator", description = "Translates a language", category = Category.MISC)
public class Translator extends Module {

    public void onEvent(Event event) {
//        if(event instanceof EventChatMessage) {
//            EventChatMessage e = (EventChatMessage) event;
//            String text = translate(e.getChatComponent().getFormattedText());
//            e.setChatComponent(new ChatComponentText(text));
//            e.setCancelled(true);
//
//            LoggerUtil.addTerminalMessage(e.getFormattedMessage());
//            mc.ingameGUI.getChatGUI().printChatMessage(e.getChatComponent());
//        }
    }

    public String translate(String text) {
        try {
            URL url = new URL("http://translate.google.cn/translate_a/single?client=gtx&dt=t&dj=1&ie=UTF-8&sl=auto&tl=" + "en_us" + "&q=" + formatWeb(text));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.connect();

            StringBuilder response = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            JSONObject json = new JSONObject(response.toString());
            JSONObject json2 = json.getJSONArray("sentences").getJSONObject(0);
            return json2.getString("trans");
        } catch (Exception ignored) {}
        return null;
    }

    public String formatWeb(String text) {
        return text.replace(" ", "%20")
                .replace("§", "%C2%A7");
    }
}
