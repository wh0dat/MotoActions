package com.motorola.actions.ltu.instrumentation;

import android.content.Context;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.checkin.BaseAnalytics;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.ltu.LiftToUnlockHelper;
import com.motorola.actions.reflect.CheckinEventProxy;
import com.motorola.actions.settings.updater.LiftToUnlockSettingsUpdater;

public class LiftToUnlockAnalytics extends BaseAnalytics {
    private static final String CHECKIN_TAG = "MOT_LIFT_TO_UNLOCK";
    private static final String DAILY_CHECKIN_VERSION = "1.0";
    private static final String DATASTORE_NAME = "actions_ltu";

    public String getDatastoreName() {
        return DATASTORE_NAME;
    }

    public LiftToUnlockAnalytics(Context context) {
        super(context, CHECKIN_TAG, "1.0");
        addDatastore(DATASTORE_NAME);
    }

    /* access modifiers changed from: protected */
    public void populateDailyCheckinEvent(CheckinEventProxy checkinEventProxy, String str, long j) {
        CommonCheckinAttributes.addCommonDailyAttributes(checkinEventProxy, isFeatureEnabled(), LiftToUnlockSettingsUpdater.getInstance().getEnabledSource(FeatureKey.LIFT_TO_UNLOCK.getEnableDefaultState()));
    }

    /* access modifiers changed from: protected */
    public boolean isFeatureSupported() {
        return LiftToUnlockHelper.isFeatureSupported();
    }

    public boolean isFeatureEnabled() {
        return LiftToUnlockHelper.isEnabled();
    }
}
