package com.motorola.actions.reflect;

import android.os.UserHandle;
import com.motorola.actions.utils.MALogger;

public class UserHandlePrivateProxy {
    public static final UserHandle ALL;
    public static final UserHandle CURRENT;
    private static final MALogger LOGGER = new MALogger(UserHandlePrivateProxy.class);
    public static final UserHandle OWNER;
    public static final int OWNER_ID;

    static {
        UserHandle userHandle;
        int i;
        UserHandle userHandle2;
        UserHandle userHandle3 = null;
        try {
            Class cls = Class.forName("android.os.UserHandle");
            i = ((Integer) cls.getDeclaredField("USER_OWNER").get(null)).intValue();
            try {
                userHandle2 = (UserHandle) cls.getDeclaredField("OWNER").get(null);
                try {
                    userHandle = (UserHandle) cls.getDeclaredField("ALL").get(null);
                } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException unused) {
                    userHandle = null;
                    LOGGER.mo11963w("Reflection failed");
                    OWNER_ID = i;
                    OWNER = userHandle2;
                    ALL = userHandle;
                    CURRENT = userHandle3;
                }
                try {
                    userHandle3 = (UserHandle) cls.getDeclaredField("CURRENT").get(null);
                } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException unused2) {
                    LOGGER.mo11963w("Reflection failed");
                    OWNER_ID = i;
                    OWNER = userHandle2;
                    ALL = userHandle;
                    CURRENT = userHandle3;
                }
            } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException unused3) {
                userHandle2 = null;
                userHandle = null;
                LOGGER.mo11963w("Reflection failed");
                OWNER_ID = i;
                OWNER = userHandle2;
                ALL = userHandle;
                CURRENT = userHandle3;
            }
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException unused4) {
            userHandle = null;
            i = 0;
            userHandle2 = null;
            LOGGER.mo11963w("Reflection failed");
            OWNER_ID = i;
            OWNER = userHandle2;
            ALL = userHandle;
            CURRENT = userHandle3;
        }
        OWNER_ID = i;
        OWNER = userHandle2;
        ALL = userHandle;
        CURRENT = userHandle3;
    }
}
