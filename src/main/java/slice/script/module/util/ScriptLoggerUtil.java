package slice.script.module.util;

import slice.util.LoggerUtil;

public class ScriptLoggerUtil {

    public static ScriptLoggerUtil INSTANCE = new ScriptLoggerUtil();

    public void addMessage(String message) {
        LoggerUtil.addMessage(message);
    }


    public void addMessageNoPrefix(String message) {
        LoggerUtil.addMessageNoPrefix(message);
    }

}
