package com.motorola.actions.reflect;

import android.content.Context;
import com.motorola.actions.utils.MALogger;

public class ContextPrivateProxy {
    private static final MALogger LOGGER = new MALogger(ContextPrivateProxy.class);
    public static final String MODALITY_SERVICE;

    static {
        String str = null;
        try {
            str = (String) Context.class.getDeclaredField("MODALITY_SERVICE").get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            LOGGER.mo11964w("Reflection error", e);
        }
        MODALITY_SERVICE = str;
    }
}
