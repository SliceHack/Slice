package slice.util.account;

import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.litarvan.openauth.microsoft.model.response.MinecraftProfile;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import slice.Slice;
import slice.util.LoggerUtil;
import slice.util.account.microsoft.MicrosoftAccount;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Proxy;
import java.util.concurrent.CompletableFuture;

/**
 * Utility class to log in to accounts
 *
 * @author Nick
 * */
@UtilityClass
public class LoginUtil {

    /**
     * Logins in using email and password.
     * */
    public static Session loginMojang(String email, String password) {
        final YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service.createUserAuthentication(Agent.MINECRAFT);

        auth.setUsername(email);
        auth.setPassword(password);

        try {
            auth.logIn();
            return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Logins in using an altening token.
     * */
    public static Session loginAlteining(String token) {
        YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service.createUserAuthentication(Agent.MINECRAFT);

        auth.setUsername(token);
        auth.setPassword(token);

        try {
            auth.logIn();
        } catch (Exception ignored){}

        return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
    }

    /**
     * Logins user in using Microsoft Authenticator.
     */
    public static MicrosoftAccount loginMicrosoft(String email, String password) {
        try {
            MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
            MicrosoftAuthResult result = authenticator.loginWithCredentials(email, password);
            MinecraftProfile profile = result.getProfile();

            Session session = new Session(profile.getName(), profile.getId(), result.getAccessToken(), "mojang");
            Minecraft.getMinecraft().session = session;
            return new MicrosoftAccount(profile, session, result.getRefreshToken());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static MicrosoftAccount loginMicrosoftNoSetSession(String email, String password) {
        try {
            MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
            MicrosoftAuthResult result = authenticator.loginWithCredentials(email, password);
            MinecraftProfile profile = result.getProfile();
            Session session = new Session(profile.getName(), profile.getId(), result.getAccessToken(), "mojang");
            return new MicrosoftAccount(profile, session, result.getRefreshToken());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static MicrosoftAccount loginFromWebView(boolean changeSession) {
        try {
            MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
            CompletableFuture<MicrosoftAuthResult> result = authenticator.loginWithAsyncWebview();

            MicrosoftAuthResult authResult = result.get();
            MinecraftProfile profile = authResult.getProfile();

            Session session = new Session(profile.getName(), profile.getId(), authResult.getAccessToken(), "mojang");

            if(changeSession) {
                Minecraft.getMinecraft().session = session;
            }

            return new MicrosoftAccount(profile, session, authResult.getRefreshToken());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static MicrosoftAccount loginWithRefreshToken(String token, boolean changeSession) {
        try {
            MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
            MicrosoftAuthResult result = authenticator.loginWithRefreshToken(token);
            MinecraftProfile profile = result.getProfile();

            Session session = new Session(profile.getName(), profile.getId(), result.getAccessToken(), "mojang");

            if(changeSession) {
                Minecraft.getMinecraft().session = session;
            }

            return new MicrosoftAccount(profile, session, result.getRefreshToken());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void askLoginWithSessionFile(File sessionFile) {

        if (sessionFile.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(sessionFile));
                String line = reader.readLine();

                String username = line.split(":")[0];
                String refreshToken = line.split(":")[1];

                int confirmDialog = JOptionPane.showConfirmDialog(
                        null,
                        String.format("Would you like to login with your session file? (%s)", username),
                        "Login",
                        JOptionPane.YES_NO_OPTION
                );

                if(confirmDialog == JOptionPane.YES_OPTION) {
                    LoginUtil.loginWithRefreshToken(refreshToken, true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Logs user in as offline. (cracked account)
     */
    public static Session loginOffline(String username) {
        return Minecraft.getMinecraft().session = new Session(username, "0", "0", "legacy");
    }
}
