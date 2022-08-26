package slice.clickgui;

import net.minecraft.client.Minecraft;
import org.cef.ccbluex.AllTimeGuiView;
import org.cef.ccbluex.GuiView;
import org.cef.ccbluex.Page;
import slice.Slice;
import slice.module.Module;
import slice.setting.Setting;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.ModeValue;
import slice.setting.settings.NumberValue;
import slice.util.LoggerUtil;

import java.io.File;

public class HTMLGui extends AllTimeGuiView {

    public HTMLGui() {
        super(new Page(new File(Minecraft.getMinecraft().mcDataDir, "Slice\\html\\gui\\clickgui\\index.html")));
    }

    /**
     * Prevent Cef from destroying
     *
     * @see GuiView
     * */
    @Override
    public void onGuiClosed() {
        setVisible(false);
    }


    /**
     * When the gui is opened
     *
     * @see net.minecraft.client.gui.GuiScreen
     * */
    @Override
    public void initGui() {
        setVisible(true);
    }

    /**
     * Adds a module to the ClickGui iframe
     * (using javascript)
     *
     * @param module the module
     * */
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

    /**
     * runs javascript on the ClickGui's iframe
     *
     * @param js the javascript to execute on the frame
     * */
    public void runOnIFrame(String js) {
        javascript("document.querySelector(\"iframe\").contentWindow." + js);
    }

    /**
     * Executes javascript
     *
     * @param js the javascript
     * */
    private void javascript(String js) {
        getCefBrowser().executeJavaScript(js, null, 0);
    }

    /**
     * Sets the frame to be visible or not
     *
     * @param visible the visibility of the ClickGui
     * */
    public void setVisible(boolean visible) {
        javascript("document.querySelector(\"iframe\").style.visibility = \"" + (!visible ? "hidden" : "visible") + "\";");
    }

    /**
     * Inits the modules
     * */
    public void queryInit() {
        for(Module module : Slice.INSTANCE.getModuleManager().getModules()) addModule(module);
    }

    public void setEnabled(String module, Boolean enabled) {
        runOnIFrame("setEnabled(\"" + module + "\", " + enabled + ")");
    }

    public void setHidden(Module module, Setting setting, boolean hidden) {
        /*runOnIFrame("hideSetting(\"" + module.getName() + "\", \"" + setting.getName() + "\", " + !hidden + ")");*/
    }

    public void updateBooleanValue(Module module, BooleanValue booleanValue, boolean value) {
        runOnIFrame("updateBooleanValue(\"" + module.getName() + "\", \"" + booleanValue.getName() + "\", " + value + ")");
    }
}
