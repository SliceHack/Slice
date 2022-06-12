package slice.api;

import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import org.json.JSONObject;
import slice.Slice;
import slice.util.HardwareUtil;
import slice.util.LoggerUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

/**
 * Client API communication
 *
 * @author Nick & Dylan
 * **/
public class API {

    /** API url */
    private static final String API_URL = "https://api.sliceclient.com/";

    /**
     * Checks if a user is authenticated with the server
     * **/
    public static void sendAuthRequest() {
        try {
            URL url = new URL(API_URL + "checkAuth/" + HardwareUtil.getHardwareID());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.connect();
            JSONObject json = new JSONObject(Objects.requireNonNull(readResponse(connection)));

            boolean success = json.getBoolean("status");
            if(!success) {
                LoggerUtil.addTerminalMessage("[Slice] Authentication failed");
                Minecraft.getMinecraft().crashed(new CrashReport("Authentication failed", new Exception("Authentication failed")));
                System.exit(-1);
            }
            Slice.INSTANCE.id = HardwareUtil.getHardwareID();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Reads the response from the server
     *
     * @param connection the connection to the server
     * **/
    public static String readResponse(HttpURLConnection connection) {
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
