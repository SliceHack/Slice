package com.sliceclient.api.util;

import lombok.experimental.UtilityClass;
import slice.util.LoggerUtil;

@UtilityClass
public class Chat {

    public static void print(String message){
        LoggerUtil.addMessage(message);
    }

}
