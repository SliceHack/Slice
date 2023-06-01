package slice.ultralight;

import com.sliceclient.ultralight.Page;
import com.sliceclient.ultralight.view.AllTimeGuiView;

public class ViewMainMenu extends AllTimeGuiView {

    public ViewMainMenu() {
        super(Page.of("https://assets.sliceclient.com/mainmenu"));
    }

}
