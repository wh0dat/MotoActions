package com.motorola.actions.approach;

import android.content.Context;
import com.motorola.actions.ServiceStartReason;
import com.motorola.actions.approach.p005ir.IRService;
import com.motorola.actions.approach.p006us.USService;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.ServiceUtils;

public final class FeatureManager {
    private static final MALogger LOGGER = new MALogger(FeatureManager.class);

    public static void start(Context context, ServiceStartReason serviceStartReason) {
        if (IRService.isIRSupported()) {
            ServiceUtils.startServiceSafe(IRService.createIntent(context));
        }
        switch (serviceStartReason) {
            case BOOT:
            case REPLACEMENT:
            case USER_SWITCH_FOREGROUND:
                USService.toggleService(context, USService.isServiceEnabled());
                return;
            case ENTER_SETTINGS:
                return;
            default:
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown start reason at IRServiceLauncher:");
                sb.append(serviceStartReason);
                mALogger.mo11959e(sb.toString());
                return;
        }
    }

    public static void stop(Context context) {
        LOGGER.mo11957d("stop : Approach");
        if (IRService.isIRSupported()) {
            context.stopService(IRService.createIntent(context));
        } else {
            USService.toggleService(context, false);
        }
    }
}
