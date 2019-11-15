package com.motorola.actions.ftm;

import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.utils.MALogger;

public class FlipToMuteHelper {
    private static final MALogger LOGGER = new MALogger(FlipToMuteService.class);
    private static final boolean VIBRATION_DEFAULT_VALUE = true;

    public static boolean isVibrationEnabled() {
        boolean z = SharedPreferenceManager.contains(FlipToMuteConstants.KEY_ENABLED) ? SharedPreferenceManager.getBoolean(FlipToMuteConstants.KEY_FTM_VIBRATION, false) : true;
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isVibrationOn: ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        return z;
    }
}
