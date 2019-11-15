package com.motorola.actions.reflect;

import com.motorola.actions.utils.MALogger;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TransitionProxy {
    private static final MALogger LOGGER = new MALogger(TransitionProxy.class);
    private static final Method METHOD_GET_NEW_STATE;
    private Object mInstance;

    static {
        Method method;
        try {
            method = Class.forName("com.motorola.slpc.Transition").getDeclaredMethod("getNewState", new Class[0]);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            LOGGER.mo11964w("Reflection error", e);
            method = null;
        }
        METHOD_GET_NEW_STATE = method;
    }

    public TransitionProxy(Object obj) {
        this.mInstance = obj;
    }

    public int getNewState() {
        try {
            return ((Integer) METHOD_GET_NEW_STATE.invoke(this.mInstance, new Object[0])).intValue();
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.mo11965w(e);
            return 0;
        }
    }
}
