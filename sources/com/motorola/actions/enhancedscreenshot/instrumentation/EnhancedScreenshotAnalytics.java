package com.motorola.actions.enhancedscreenshot.instrumentation;

import android.content.Context;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.checkin.BaseAnalytics;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.enhancedscreenshot.EnhancedScreenshotService;
import com.motorola.actions.reflect.CheckinEventProxy;
import com.motorola.actions.settings.updater.EnhancedScreenshotSettingsUpdater;

public class EnhancedScreenshotAnalytics extends BaseAnalytics {
    private static final String CHECKIN_TAG = "MOT_ENHANCED_SCREENSHOT";
    private static final String DAILY_CHECKIN_VERSION = "1.0";
    private static final String DATASTORE_NAME = "actions_enhanced";

    public String getDatastoreName() {
        return DATASTORE_NAME;
    }

    public EnhancedScreenshotAnalytics(Context context) {
        super(context, CHECKIN_TAG, "1.0");
        addDatastore(DATASTORE_NAME);
    }

    /* access modifiers changed from: protected */
    public void populateDailyCheckinEvent(CheckinEventProxy checkinEventProxy, String str, long j) {
        CommonCheckinAttributes.addCommonDailyAttributes(checkinEventProxy, isFeatureEnabled(), EnhancedScreenshotSettingsUpdater.getInstance().getEnabledSource(FeatureKey.ENHANCED_SCREENSHOT.getEnableDefaultState()));
    }

    /* access modifiers changed from: protected */
    public boolean isFeatureSupported() {
        return EnhancedScreenshotService.isFeatureSupported();
    }

    public boolean isFeatureEnabled() {
        return EnhancedScreenshotService.isServiceEnabled();
    }
}
