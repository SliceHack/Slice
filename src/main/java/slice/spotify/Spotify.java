package slice.spotify;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import slice.Slice;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

@Getter @Setter
public class Spotify {

    private File file = new File(Minecraft.getMinecraft().mcDataDir, "Slice\\Spotify\\token.txt");
    private String token;

    public Spotify() {
        requestToken();

        if(!file.exists())
            return;

        this.token = getToken();
    }

    public void requestToken() {
        try {
            URL url = new URL("https://accounts.spotify.com/api/token");
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);
            connection.getOutputStream().write("grant_type=client_credentials&client_id=0fe2432ca9ab4ee9bec21bd25a58c020&client_secret=d93f2ceffffc45d6b1f84e3b548ee0c6".getBytes());
            System.out.println("Spotify: " + connection.getInputStream().read());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getToken() {

        if(!file.exists())
            return null;

        try(FileReader reader = new FileReader(file)) {
            StringBuilder sb = new StringBuilder();

            int line;
            while ((line = reader.read()) != -1) {
                sb.append((char) line);
            }
            return sb.toString();
        } catch (IOException ignored) {}
        return null;
    }

}
