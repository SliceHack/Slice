package slice.api;

import java.net.URL;

public class IRC {

    /** API url */
    private static final String API_URL = "https://api.sliceclient.com/irc";

    /***
     * Connect to an IRC server.
     * */
    public IRC() {
        try {
            URL url = new URL(API_URL);
        } catch (Exception ignored){}
    }

}
