package com.motorola.actions.p013ui.settings;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.nightdisplay.NightDisplayService;
import com.motorola.actions.p013ui.ActionsBaseActivity;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.ui.settings.NightDisplaySettingsActivity */
public class NightDisplaySettingsActivity extends ActionsBaseActivity {
    private static final MALogger LOGGER = new MALogger(NightDisplaySettingsActivity.class);

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        LOGGER.mo11957d("Creating NightDisplaySettingsActivity");
        updateEnabledStatus();
        startActivity(SettingsActivity.getFeatureLaunchIntent(FeatureKey.NIGHT_DISPLAY.ordinal()));
        finish();
    }

    public static void updateEnabledStatus() {
        boolean isFeatureSupported = NightDisplayService.isFeatureSupported();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("updateEnabledStatus: ");
        sb.append(isFeatureSupported);
        mALogger.mo11957d(sb.toString());
        Context appContext = ActionsApplication.getAppContext();
        appContext.getPackageManager().setComponentEnabledSetting(new ComponentName(appContext, NightDisplaySettingsActivity.class.getName()), isFeatureSupported ? 1 : 2, 1);
    }
}
