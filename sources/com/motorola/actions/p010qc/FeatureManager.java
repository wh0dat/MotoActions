package com.motorola.actions.p010qc;

import android.content.Context;
import android.content.Intent;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.MotoCameraUtils;
import com.motorola.actions.utils.ServiceUtils;

/* renamed from: com.motorola.actions.qc.FeatureManager */
public final class FeatureManager {
    private static final MALogger LOGGER = new MALogger(FeatureManager.class);

    public static void start(Context context) {
        if (!SharedPreferenceManager.contains(QuickCaptureService.KEY_ENABLED)) {
            LOGGER.mo11957d("No ACTIONS_QC_ENABLED key");
            SharedPreferenceManager.putBoolean(QuickCaptureService.KEY_ENABLED, QuickCaptureConfig.isSupported() && QuickDrawHelper.isEnabled());
        }
        if (QuickCaptureConfig.isSupported()) {
            ServiceUtils.startServiceSafe(QuickCaptureService.createIntent(context));
        }
    }

    public static void stop(Context context) {
        context.stopService(QuickCaptureService.createIntent(context));
    }

    public static void sendIntentToCamera(Intent intent) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("sendIntentToCamera - action: ");
        sb.append(intent != null ? intent.getAction() : "null");
        mALogger.mo11957d(sb.toString());
        if (intent != null) {
            intent.setPackage(MotoCameraUtils.CAMERA_ONE_PACKAGE);
            ActionsApplication.getAppContext().sendBroadcast(intent);
            intent.setPackage(MotoCameraUtils.CAMERA_TWO_PACKAGE);
            ActionsApplication.getAppContext().sendBroadcast(intent);
        }
    }
}
