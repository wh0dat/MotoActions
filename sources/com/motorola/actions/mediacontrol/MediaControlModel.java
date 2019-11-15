package com.motorola.actions.mediacontrol;

import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.settings.provider.ActionsSettingsProvider;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemMediaControl;
import com.motorola.actions.utils.MALogger;

public class MediaControlModel {
    private static final String KEY_ENABLED = "media_control_enabled";
    private static final MALogger LOGGER = new MALogger(MediaControlModel.class);

    public static void setServiceEnabled(boolean z) {
        SharedPreferenceManager.putBoolean(KEY_ENABLED, z);
        ActionsSettingsProvider.notifyChange(ContainerProviderItemMediaControl.TABLE_NAME);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("setServiceEnabled = ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
    }

    public static boolean isServiceEnabled() {
        boolean z = SharedPreferenceManager.getBoolean(KEY_ENABLED, FeatureKey.MEDIA_CONTROL.getEnableDefaultState());
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isServiceEnabled = ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        return z;
    }

    public static void setMusicMode(boolean z) {
        MotorolaSettings.setMusicMode(z ? 1 : 0);
    }
}
