package slice.ultralight;

import com.labymedia.ultralight.javascript.JavascriptContextLock;
import com.sliceclient.ultralight.Page;
import com.sliceclient.ultralight.listener.SliceLoadListener;
import com.sliceclient.ultralight.view.AllTimeGuiView;
import slice.Slice;
import slice.module.Module;
import slice.setting.Setting;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.ModeValue;
import slice.setting.settings.NumberValue;

import java.io.File;
import java.util.Set;

public class ViewClickGui extends AllTimeGuiView {

    private boolean firstOpen = true;

    public ViewClickGui() {
        super(new Page(new File(
                "C:\\Users\\Nick\\VSCode\\assets.sliceclient.com\\clickgui\\index.html"
        )));
    }

    @Override
    public void initGui() {
        if(this.firstOpen) {
            Slice.INSTANCE.getModuleManager().getModules().forEach(ViewClickGui.this::addModule);
            this.firstOpen = false;
        }
    }

    @Override
    public void mouseClicked(int x, int y, int mouseButton) {
        JavascriptContextLock lock = view.getView().lockJavascriptContext();
        lock.getContext().garbageCollect();
        super.mouseClicked(x, y, mouseButton);
    }

    public void updateSettings(Module module) {
        for(Setting setting : module.getSettings()) {
            if(setting instanceof BooleanValue) {
                runOnIFrame(String.format(
                        "updateSetting(\"%s\", \"%s\", \"%s\", \"%s\")", module.getName(), setting.getName(), "BooleanValue", ((BooleanValue) setting).getValue()
                        )
                );
            }

            if(setting instanceof ModeValue) {
                runOnIFrame(String.format(
                        "updateSetting(\"%s\", \"%s\", \"%s\", \"%s\")", module.getName(), setting.getName(), "ModeValue", ((ModeValue) setting).getValue()
                        )
                );
            }

            if(setting instanceof NumberValue) {
                runOnIFrame(String.format(
                        "updateSetting(\"%s\", \"%s\", \"%s\", \"%s\")", module.getName(), setting.getName(), "NumberValue", ((NumberValue) setting).getValue()
                        )
                );
            }
        }
    }

    public void addModule(Module module) {
        runOnIFrame("addModule(\"" + module.getCategory().getName() + "\", \"" + module.getName() + "\")");

        for(Setting setting : module.getSettings()) {

            if(setting instanceof BooleanValue) {
                BooleanValue bv = (BooleanValue) setting;
                runOnIFrame("addSettingToModule(\"" + module.getName() + "\", \"BooleanValue\", \"" + bv.getName() + "\"," + "\"" + bv.getValue() + "\"" + ")");
            }

            if(setting instanceof NumberValue) {
                NumberValue nv = (NumberValue) setting;
                runOnIFrame("addSettingToModule(\"" + module.getName() + "\", \"NumberValue\", \"" + nv.getName() + "\"," + "\"" + nv.getValue() + "\"," + nv.getMin() + ", " + nv.getMax() + ")");
            }

            if(setting instanceof ModeValue) {
                ModeValue mv = (ModeValue) setting;

                StringBuilder args = new StringBuilder();
                args.append("\"").append(module.getName()).append("\"").append(",");
                args.append("\"").append("ModeValue").append("\"").append(",");
                args.append("\"").append(mv.getName()).append("\"").append(",");
                args.append("\"").append(mv.getValue()).append("\"").append(",");
                int index = 0;
                for(String s : mv.getValues()) {
                    index++;
                    if(index == mv.getValues().length) args.append("\"").append(s).append("\"");
                    else args.append("\"").append(s).append("\"").append(",");
                }
                runOnIFrame("addSettingToModule(" + args + ")");
            }
            if (module.isEnabled()) {
                setEnabled(module.getName(), true);
            }
        }
    }

    public void setEnabled(String name, boolean enabled) {
        runOnIFrame("setEnabled(\"" + name + "\", " + enabled + ")");
    }

    public void runOnIFrame(String js) {
        view.eval("document.querySelector(\"iframe\").contentWindow." + js);
    }
}
