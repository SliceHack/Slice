package slice.util;

import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.litarvan.openauth.microsoft.model.response.MinecraftProfile;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.net.Proxy;

/**
 * Account Logger utility class
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
     * Logins user in using Microsoft Authenticator.
     */
    public static Session loginMicrosoft(String email, String password) {
        try {
            MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
            MicrosoftAuthResult result = authenticator.loginWithCredentials("email", "password");
            MinecraftProfile profile = result.getProfile();
            return Minecraft.getMinecraft().session = new Session(profile.getName(), profile.getId(), result.getAccessToken(), "mojang");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Logs user in as offline. (cracked account)
     */
    public static Session loginOffline(String username) {
        return Minecraft.getMinecraft().session = new Session(username, "0", "0", "legacy");
    }
}
