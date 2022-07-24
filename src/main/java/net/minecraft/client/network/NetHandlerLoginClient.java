package net.minecraft.client.network;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.math.BigInteger;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.network.login.client.C01PacketEncryptionResponse;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.network.login.server.S01PacketEncryptionRequest;
import net.minecraft.network.login.server.S02PacketLoginSuccess;
import net.minecraft.network.login.server.S03PacketEnableCompression;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.CryptManager;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import slice.gui.main.MainMenu;

@SuppressWarnings("all")
public class NetHandlerLoginClient implements INetHandlerLoginClient
{
    private static final Logger logger = LogManager.getLogger();
    private final Minecraft mc;
    private final GuiScreen previousGuiScreen;
    private final NetworkManager networkManager;
    private GameProfile gameProfile;
    private ServerData data;
    private Session session;

    public NetHandlerLoginClient(NetworkManager networkManagerIn, Minecraft mcIn, GuiScreen p_i45059_3_)
    {
        this.networkManager = networkManagerIn;
        this.mc = mcIn;
        this.previousGuiScreen = p_i45059_3_;
        this.data = mcIn.getCurrentServerData();
        this.session = mcIn.getSession();
    }

    public NetHandlerLoginClient(String ip, Session session, NetworkManager networkManagerIn, GuiScreen previousGuiScreen) {
        this.networkManager = networkManagerIn;
        this.previousGuiScreen = previousGuiScreen;
        this.mc = Minecraft.getMinecraft();
        this.data = new ServerData("", ip, false);
        this.session = session;
    }

    public void handleEncryptionRequest(S01PacketEncryptionRequest packetIn)
    {
        final SecretKey secretkey = CryptManager.createNewSharedKey();
        String s = packetIn.getServerId();
        PublicKey publickey = packetIn.getPublicKey();
        String s1 = (new BigInteger(CryptManager.getServerIdHash(s, publickey, secretkey))).toString(16);

        if (data != null && data.isOnLAN())
        {
            try
            {
                getSessionService().joinServer(session.getProfile(), session.getToken(), s1);
            }
            catch (AuthenticationException var10)
            {
                logger.warn("Couldn\'t connect to auth servers but will continue to join LAN");
            }
        }
        else
        {
            try
            {
                getSessionService().joinServer(session.getProfile(), session.getToken(), s1);
            }
            catch (AuthenticationUnavailableException var7)
            {
                networkManager.closeChannel(new ChatComponentTranslation("disconnect.loginFailedInfo", new Object[] {new ChatComponentTranslation("disconnect.loginFailedInfo.serversUnavailable", new Object[0])}));
                return;
            }
            catch (InvalidCredentialsException var8)
            {
                networkManager.closeChannel(new ChatComponentTranslation("disconnect.loginFailedInfo", new Object[] {new ChatComponentTranslation("disconnect.loginFailedInfo.invalidSession", new Object[0])}));
                return;
            }
            catch (AuthenticationException authenticationexception)
            {
                networkManager.closeChannel(new ChatComponentTranslation("disconnect.loginFailedInfo", new Object[] {authenticationexception.getMessage()}));
                return;
            }
        }

        networkManager.sendPacket(new C01PacketEncryptionResponse(secretkey, publickey, packetIn.getVerifyToken()), new GenericFutureListener < Future <? super Void >> ()
        {
            public void operationComplete(Future <? super Void > p_operationComplete_1_) throws Exception
            {
                networkManager.enableEncryption(secretkey);
            }
        }, new GenericFutureListener[0]);
    }

    private MinecraftSessionService getSessionService()
    {
        return mc.getSessionService();
    }

    public void handleLoginSuccess(S02PacketLoginSuccess packetIn)
    {
        gameProfile = packetIn.getProfile();
        networkManager.setConnectionState(EnumConnectionState.PLAY);
        networkManager.setNetHandler(new NetHandlerPlayClient(mc, previousGuiScreen, networkManager, gameProfile));
    }

    /**
     * Invoked when disconnecting, the parameter is a ChatComponent describing the reason for termination
     */
    public void onDisconnect(IChatComponent reason)
    {
        mc.displayGuiScreen(new GuiDisconnected(previousGuiScreen, "connect.failed", reason));
    }

    public void handleDisconnect(S00PacketDisconnect packetIn)
    {
        networkManager.closeChannel(packetIn.func_149603_c());
    }

    public void handleEnableCompression(S03PacketEnableCompression packetIn)
    {
        if (!networkManager.isLocalChannel())
        {
            networkManager.setCompressionTreshold(packetIn.getCompressionTreshold());
        }
    }
}
