package com.motorola.actions.nightdisplay.instrumentation;

import android.content.Context;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.checkin.BaseAnalytics;
import com.motorola.actions.checkin.CheckinDatastore;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.nightdisplay.NightDisplayService;
import com.motorola.actions.nightdisplay.common.Persistence;
import com.motorola.actions.reflect.CheckinEventProxy;
import com.motorola.actions.settings.updater.NDSettingsUpdater;
import com.motorola.actions.sleepPattern.SleepPatternService;
import com.motorola.actions.utils.MALogger;

public class NightDisplayAnalytics extends BaseAnalytics {
    private static final String CHECKIN_TAG = "MOT_NIGHT_DISPLAY";
    private static final String DAILY_CHECKIN_VERSION = "1.3";
    private static final String DATASTORE_NAME = "actions_nd";
    private static final String KEY_DAILY_BLF_ACTIVATED = "blf_act";
    private static final String KEY_DAILY_MODE_AUTOMATIC_TYPE = "p";
    private static final String KEY_DAILY_MODE_INITIATION_TYPE = "start_tp";
    private static final String KEY_DAILY_MODE_MANUAL_TYPE = "c";
    private static final String KEY_DAILY_MODE_TERMINATOR_TYPE = "end_tp";
    private static final MALogger LOGGER = new MALogger(NightDisplayAnalytics.class);

    public String getDatastoreName() {
        return DATASTORE_NAME;
    }

    public NightDisplayAnalytics(Context context) {
        super(context, CHECKIN_TAG, DAILY_CHECKIN_VERSION);
        addDatastore(DATASTORE_NAME);
    }

    /* access modifiers changed from: protected */
    public void populateDailyCheckinEvent(CheckinEventProxy checkinEventProxy, String str, long j) {
        LOGGER.mo11957d("Night Display daily checkin");
        CheckinDatastore checkinDatastore = (CheckinDatastore) this.mDatastores.get(DATASTORE_NAME);
        CommonCheckinAttributes.addCommonDailyAttributes(checkinEventProxy, isFeatureEnabled(), NDSettingsUpdater.getInstance().getEnabledSource(FeatureKey.NIGHT_DISPLAY.getEnableDefaultState()));
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_BLF_ACTIVATED);
        setOptionalStringAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_MODE_INITIATION_TYPE);
        setOptionalStringAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_MODE_TERMINATOR_TYPE);
    }

    /* access modifiers changed from: protected */
    public boolean isFeatureSupported() {
        return NightDisplayService.isFeatureSupported();
    }

    public boolean isFeatureEnabled() {
        return Persistence.isFeatureEnabled();
    }

    public void updateDailyInformation() {
        recordDailyModeInitialType();
        recordDailyModeTerminatorType();
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailyFilterActivated() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).setIntValue(KEY_DAILY_BLF_ACTIVATED, 1);
    }

    private synchronized void recordDailyModeInitialType() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).setStringValue(KEY_DAILY_MODE_INITIATION_TYPE, getInstrumentationNightDisplayType(Persistence.getMode(SleepPatternService.getDefaultState())));
    }

    private synchronized void recordDailyModeTerminatorType() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).setStringValue(KEY_DAILY_MODE_TERMINATOR_TYPE, getInstrumentationNightDisplayType(Persistence.getMode(SleepPatternService.getDefaultState())));
    }

    private String getInstrumentationNightDisplayType(int i) {
        String str = "";
        if (!isFeatureEnabled()) {
            return str;
        }
        if (i == 1) {
            return KEY_DAILY_MODE_MANUAL_TYPE;
        }
        if (i == 4) {
            return KEY_DAILY_MODE_AUTOMATIC_TYPE;
        }
        LOGGER.mo11957d("Unknown Night Display mode");
        return str;
    }
}
