package com.motorola.actions.quickscreenshot.instrumentation;

import android.content.Context;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.checkin.BaseAnalytics;
import com.motorola.actions.checkin.CheckinDatastore;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.quickscreenshot.QuickScreenshotHelper;
import com.motorola.actions.quickscreenshot.QuickScreenshotModel;
import com.motorola.actions.reflect.CheckinEventProxy;
import com.motorola.actions.settings.updater.QuickScreenshotSettingsUpdater;

public class QuickScreenshotAnalytics extends BaseAnalytics {
    private static final String CHECKIN_TAG = "MOT_QUICK_SCREENSHOT";
    private static final String DAILY_CHECKIN_VERSION = "1.1";
    private static final String DATASTORE_NAME = "actions_qs";
    private static final String KEY_DAILY_COMBO_SCREENSHOTS_COUNT = "n_combo";
    private static final String KEY_DAILY_QS_SCREENSHOTS_COUNT = "n_qs";

    public String getDatastoreName() {
        return DATASTORE_NAME;
    }

    public QuickScreenshotAnalytics(Context context) {
        super(context, CHECKIN_TAG, DAILY_CHECKIN_VERSION);
        addDatastore(DATASTORE_NAME);
    }

    /* access modifiers changed from: protected */
    public void populateDailyCheckinEvent(CheckinEventProxy checkinEventProxy, String str, long j) {
        CheckinDatastore checkinDatastore = (CheckinDatastore) this.mDatastores.get(DATASTORE_NAME);
        CommonCheckinAttributes.addCommonDailyAttributes(checkinEventProxy, isFeatureEnabled(), QuickScreenshotSettingsUpdater.getInstance().getEnabledSource(FeatureKey.QUICK_SCREENSHOT.getEnableDefaultState()));
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_QS_SCREENSHOTS_COUNT);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_COMBO_SCREENSHOTS_COUNT);
    }

    /* access modifiers changed from: protected */
    public boolean isFeatureSupported() {
        return QuickScreenshotHelper.isFeatureSupported();
    }

    public boolean isFeatureEnabled() {
        return QuickScreenshotModel.isServiceEnabled();
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordQuickScreenshotEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_QS_SCREENSHOTS_COUNT);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordComboKeysScreenshotEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_COMBO_SCREENSHOTS_COUNT);
    }
}
