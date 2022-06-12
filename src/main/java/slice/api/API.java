package slice.api;

import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import org.json.JSONObject;
import slice.util.HardwareUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class API {

    private static final String API_URL = "https://api.sliceclient.com/";

    public static void sendAuthRequest() {
        try {
            URL url = new URL(API_URL + "checkAuth/" + HardwareUtil.getHardwareID());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.getOutputStream().flush();
            connection.getOutputStream().close();

            JSONObject json = new JSONObject(Objects.requireNonNull(readResponse(connection)));
            boolean status = Boolean.parseBoolean(json.getString("status"));

            if(!status) {
                Minecraft.getMinecraft().crashed(new CrashReport("Not Authenticated", new Exception("Not Authenticated")));
                System.exit(-1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readResponse(HttpURLConnection connection) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (Exception ignored) {}
        return null;
    }
}
