package com.sliceclient.ultralight.util;

import com.labymedia.ultralight.input.UltralightKey;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class UltralightKeyMapper {
    private static final Map<Integer, UltralightKey> keyMap = new ConcurrentHashMap<>();

    static {
        keyMap.put(57, UltralightKey.SPACE);
        keyMap.put(40, UltralightKey.OEM_7);
        keyMap.put(51, UltralightKey.OEM_COMMA);
        keyMap.put(12, UltralightKey.OEM_MINUS);
        keyMap.put(52, UltralightKey.OEM_PERIOD);
        keyMap.put(53, UltralightKey.OEM_2);
        keyMap.put(11, UltralightKey.NUM_0);
        keyMap.put(2, UltralightKey.NUM_1);
        keyMap.put(3, UltralightKey.NUM_2);
        keyMap.put(4, UltralightKey.NUM_3);
        keyMap.put(5, UltralightKey.NUM_4);
        keyMap.put(6, UltralightKey.NUM_5);
        keyMap.put(7, UltralightKey.NUM_6);
        keyMap.put(8, UltralightKey.NUM_7);
        keyMap.put(9, UltralightKey.NUM_8);
        keyMap.put(10, UltralightKey.NUM_9);
        keyMap.put(39, UltralightKey.OEM_1);
        keyMap.put(13, UltralightKey.OEM_PLUS);
        keyMap.put(30, UltralightKey.A);
        keyMap.put(48, UltralightKey.B);
        keyMap.put(46, UltralightKey.C);
        keyMap.put(32, UltralightKey.D);
        keyMap.put(18, UltralightKey.E);
        keyMap.put(33, UltralightKey.F);
        keyMap.put(34, UltralightKey.G);
        keyMap.put(35, UltralightKey.H);
        keyMap.put(23, UltralightKey.I);
        keyMap.put(36, UltralightKey.J);
        keyMap.put(37, UltralightKey.K);
        keyMap.put(38, UltralightKey.L);
        keyMap.put(50, UltralightKey.M);
        keyMap.put(49, UltralightKey.N);
        keyMap.put(24, UltralightKey.O);
        keyMap.put(25, UltralightKey.P);
        keyMap.put(16, UltralightKey.Q);
        keyMap.put(19, UltralightKey.R);
        keyMap.put(31, UltralightKey.S);
        keyMap.put(20, UltralightKey.T);
        keyMap.put(22, UltralightKey.U);
        keyMap.put(47, UltralightKey.V);
        keyMap.put(17, UltralightKey.W);
        keyMap.put(45, UltralightKey.X);
        keyMap.put(21, UltralightKey.Y);
        keyMap.put(44, UltralightKey.Z);
        keyMap.put(26, UltralightKey.OEM_4);
        keyMap.put(43, UltralightKey.OEM_5);
        keyMap.put(27, UltralightKey.OEM_6);
        keyMap.put(41, UltralightKey.OEM_3);
        keyMap.put(1, UltralightKey.ESCAPE);
        keyMap.put(28, UltralightKey.RETURN);
        keyMap.put(15, UltralightKey.TAB);
        keyMap.put(14, UltralightKey.BACK);
        keyMap.put(210, UltralightKey.INSERT);
        keyMap.put(211, UltralightKey.DELETE);
        keyMap.put(205, UltralightKey.RIGHT);
        keyMap.put(203, UltralightKey.LEFT);
        keyMap.put(208, UltralightKey.DOWN);
        keyMap.put(200, UltralightKey.UP);
        keyMap.put(201, UltralightKey.PRIOR);
        keyMap.put(209, UltralightKey.NEXT);
        keyMap.put(199, UltralightKey.HOME);
        keyMap.put(207, UltralightKey.END);
        keyMap.put(58, UltralightKey.CAPITAL);
        keyMap.put(70, UltralightKey.SCROLL);
        keyMap.put(69, UltralightKey.NUMLOCK);
        keyMap.put(183, UltralightKey.SNAPSHOT);
        keyMap.put(197, UltralightKey.PAUSE);
        keyMap.put(59, UltralightKey.F1);
        keyMap.put(60, UltralightKey.F2);
        keyMap.put(61, UltralightKey.F3);
        keyMap.put(62, UltralightKey.F4);
        keyMap.put(63, UltralightKey.F5);
        keyMap.put(64, UltralightKey.F6);
        keyMap.put(65, UltralightKey.F7);
        keyMap.put(66, UltralightKey.F8);
        keyMap.put(67, UltralightKey.F9);
        keyMap.put(68, UltralightKey.F10);
        keyMap.put(87, UltralightKey.F11);
        keyMap.put(88, UltralightKey.F12);
        keyMap.put(100, UltralightKey.F13);
        keyMap.put(101, UltralightKey.F14);
        keyMap.put(102, UltralightKey.F15);
        //f16-f24 unmapped
        keyMap.put(82, UltralightKey.NUMPAD0);
        keyMap.put(79, UltralightKey.NUMPAD1);
        keyMap.put(80, UltralightKey.NUMPAD2);
        keyMap.put(81, UltralightKey.NUMPAD3);
        keyMap.put(75, UltralightKey.NUMPAD4);
        keyMap.put(76, UltralightKey.NUMPAD5);
        keyMap.put(77, UltralightKey.NUMPAD6);
        keyMap.put(71, UltralightKey.NUMPAD7);
        keyMap.put(72, UltralightKey.NUMPAD8);
        keyMap.put(73, UltralightKey.NUMPAD9);
        keyMap.put(83, UltralightKey.DECIMAL);
        keyMap.put(181, UltralightKey.DIVIDE);
        keyMap.put(55, UltralightKey.MULTIPLY);
        keyMap.put(74, UltralightKey.SUBTRACT);
        keyMap.put(78, UltralightKey.ADD);
        keyMap.put(54, UltralightKey.SHIFT); // right shift
        keyMap.put(42, UltralightKey.SHIFT); //left shift
        keyMap.put(29, UltralightKey.CONTROL); // left control
        keyMap.put(157, UltralightKey.CONTROL); // right control
        keyMap.put(56, UltralightKey.MENU); // left alt
        keyMap.put(184, UltralightKey.MENU); // right alt
        keyMap.put(219, UltralightKey.LWIN);
        keyMap.put(220, UltralightKey.RWIN);
    }

    public enum KeyType {
        CHAR,
        ACTION;
    }

    public static KeyType getKeyType(int key) {
        switch (key) {
            case 14: // backspace
            case 28: //enter
                return KeyType.ACTION;
            default:
                return KeyType.CHAR;
        }


    }

    public static UltralightKey getKey(int key) {
        return keyMap.getOrDefault(key, UltralightKey.UNKNOWN);
    }
}
