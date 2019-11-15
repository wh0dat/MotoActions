package com.motorola.actions.reflect;

import android.view.KeyEvent;
import com.motorola.actions.utils.MALogger;

public class KeyEventPrivateProxy {
    public static final int KEYCODE_FPS_ONENAV_HOLD;
    public static final int KEYCODE_FPS_ONENAV_TAP;
    public static final int KEYCODE_FPS_ONENAV_UP;
    private static final MALogger LOGGER = new MALogger(KeyEventPrivateProxy.class);

    static {
        int i;
        int i2;
        int i3;
        try {
            i = KeyEvent.class.getDeclaredField("KEYCODE_FPS_ONENAV_UP").getInt(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            LOGGER.mo11964w("Reflection error. KEYCODE_FPS_ONENAV_UP", e);
            i = -1;
        }
        try {
            i2 = KeyEvent.class.getDeclaredField("KEYCODE_FPS_ONENAV_TAP").getInt(null);
        } catch (IllegalAccessException | NoSuchFieldException e2) {
            LOGGER.mo11964w("Reflection error. KEYCODE_FPS_ONENAV_TAP", e2);
            i2 = -1;
        }
        try {
            i3 = KeyEvent.class.getDeclaredField("KEYCODE_FPS_ONENAV_HOLD").getInt(null);
        } catch (IllegalAccessException | NoSuchFieldException e3) {
            LOGGER.mo11964w("Reflection error. KEYCODE_FPS_ONENAV_HOLD", e3);
            i3 = -1;
        }
        KEYCODE_FPS_ONENAV_UP = i;
        KEYCODE_FPS_ONENAV_TAP = i2;
        KEYCODE_FPS_ONENAV_HOLD = i3;
    }
}
