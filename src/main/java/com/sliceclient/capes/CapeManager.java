package com.sliceclient.capes;

import lombok.*;
import org.json.*;

import java.io.*;
import java.net.*;

@AllArgsConstructor
@Getter @Setter
public class CapeManager {

    private final String baseUrl;

    public String getCape(@NonNull String username) {
        try {
            URL url = new URL(String.format("%s/cape/%s", baseUrl, username));
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            
            if(jsonObject.has("error")) return null;

            return jsonObject.getString("url");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
