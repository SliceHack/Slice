package slice.ultralight;

import com.sliceclient.ultralight.Page;
import com.sliceclient.ultralight.listener.SliceLoadListener;
import com.sliceclient.ultralight.view.ViewNoGui;
import com.sun.istack.internal.NotNull;
import lombok.Getter;
import net.minecraft.entity.EntityLivingBase;
import slice.Slice;
import slice.event.data.EventInfo;
import slice.event.events.EventGuiRender;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.util.MoveUtil;

import java.util.ArrayList;
import java.util.List;

public class ViewHUD extends ViewNoGui {

    @Getter
    private final List<Module> arrayList = new ArrayList<>();

    public ViewHUD() {
        super(Page.of(String.format("https://assets.sliceclient.com/hud/index.html?name=%s&version=%s&discord=%s", Slice.NAME, Slice.VERSION, Slice.INSTANCE.getDiscordName())));
    }

    @Override
    public void init() {
        super.init();

        view.getView().setLoadListener(
                new SliceLoadListener() {

                    @Override
                    public void onFinishLoading(long frameId, boolean isMainFrame, String url) {
                        view.eval("document.querySelector(\".text\").innerHTML = \"" + Slice.NAME + " | " + Slice.VERSION + " | " + Slice.INSTANCE.discordName + "\";");
                        view.eval("document.querySelector(\".box\").style.width = (document.querySelector(\".text\").offsetWidth)+ 50 + \"px\";");

                        view.eval("document.body.insertAdjacentHTML('beforeend', '<iframe src=\"arraylist/index.html\" frameborder=\"0\"></iframe>');");

                        Slice.INSTANCE.getModuleManager().getModules().stream().filter(Module::isEnabled).forEach((module) -> addModule(module));
                    }

                }
        );
    }

    public void addModule(@NotNull Module module) {
        arrayList.add(module);
        view.eval(String.format("addToArrayList(\"%s\");", module.getMode() != null ? module.getName() + " " + module.getMode().getValue() : module.getName()));
    }

    public void removeModule(@NotNull Module module) {
        arrayList.remove(module);
        view.eval(String.format("removeFromArrayList(\"%s\");", module.getMode() != null ? module.getName() + " " + module.getMode().getValue() : module.getName()));
    }

    public void renameFromArrayList(String oldName, String newName) {
        view.eval(String.format("renameFromArrayList(\"%s\", \"%s\");", oldName, newName));
    }
}
