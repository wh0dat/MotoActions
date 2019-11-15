package com.motorola.actions.p013ui.settings;

import android.content.Context;
import com.motorola.actions.utils.MALogger;
import java.util.ArrayList;

/* renamed from: com.motorola.actions.ui.settings.PermissionsManager */
public final class PermissionsManager {
    private static final MALogger LOGGER = new MALogger(PermissionsManager.class);
    private static final String[] PERMISSIONS_FLASH_ON_CHOP = {"android.permission.CAMERA"};
    public static final int PERMISSIONS_FLASH_ON_CHOP_REQUEST_CODE = 1;
    private static PermissionsManager sInstance;

    private PermissionsManager() {
    }

    public static PermissionsManager getInstance() {
        if (sInstance == null) {
            sInstance = new PermissionsManager();
        }
        return sInstance;
    }

    public ArrayList<String> getNotGrantedPermissions(Context context, int i) {
        String[] strArr;
        LOGGER.mo11957d("checkPermissions");
        ArrayList<String> arrayList = new ArrayList<>();
        if (i != 1) {
            LOGGER.mo11957d("Discard permission request.");
        } else {
            for (String str : PERMISSIONS_FLASH_ON_CHOP) {
                if (context.checkSelfPermission(str) != 0) {
                    arrayList.add(str);
                }
            }
        }
        return arrayList;
    }
}
