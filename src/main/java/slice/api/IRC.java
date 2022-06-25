package slice.api;

import io.socket.client.IO;
import io.socket.client.Socket;
import slice.util.HardwareUtil;

import java.net.URI;

public class IRC {

    /** API url */
    private static final String API_URL = "https://api.sliceclient.com/irc";
    private static final String IRC_PATH = "chat";

    /***
     * Connect to an IRC server.
     * */
    public IRC() {
        try {
            IO.Options options = IO.Options.builder().setPath("/" + IRC_PATH + "/").build();
            Socket socket = IO.socket(URI.create(API_URL), options);

            socket.connect();
        } catch (Exception ignored){}
    }

}
