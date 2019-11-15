package com.motorola.actions.approach.p006us;

import android.content.Context;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.checkin.BaseAnalytics;
import com.motorola.actions.checkin.CheckinDatastore;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.reflect.CheckinEventProxy;
import com.motorola.actions.settings.updater.ApproachSettingsUpdater;
import com.motorola.actions.utils.MALogger;
import java.util.Calendar;

/* renamed from: com.motorola.actions.approach.us.USAnalytics */
public class USAnalytics extends BaseAnalytics {
    private static final String CHECKIN_TAG = "MOT_ULTRASOUND";
    private static final String DAILY_CHECKIN_VERSION = "1.2";
    private static final String DATASTORE_NAME = "usservice";
    private static final String KEY_DAILY_MOTION_BEFORE_INTERFERENCE = "M_BI";
    private static final String KEY_DAILY_MOTION_NO_INTERFERENCE = "M_NI";
    private static final String KEY_DAILY_NO_MOTION_BEFORE_INTERFERENCE = "NM_BI";
    private static final String KEY_DAILY_NO_MOTION_NO_INTERFERENCE = "NM_NI";
    private static final String KEY_DEV_LAST_US_INST_RETRIEVE_TIME = "last_us_inst_retrieve_time";
    private static final MALogger LOGGER = new MALogger(USAnalytics.class);

    public String getDatastoreName() {
        return DATASTORE_NAME;
    }

    public USAnalytics(Context context) {
        super(context, CHECKIN_TAG, DAILY_CHECKIN_VERSION);
        addDatastore(DATASTORE_NAME);
        if (!this.mDatastores.containsKey(KEY_DEV_LAST_US_INST_RETRIEVE_TIME)) {
            setLastInstrumentationRetrieveTime();
        }
    }

    public synchronized void recordNoMotionNoInterference(int i) {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).addToIntValue(KEY_DAILY_NO_MOTION_NO_INTERFERENCE, i);
    }

    public synchronized void recordMotionNoInterference(int i) {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).addToIntValue(KEY_DAILY_MOTION_NO_INTERFERENCE, i);
    }

    public synchronized void recordNoMotionBeforeInterference(int i) {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).addToIntValue(KEY_DAILY_NO_MOTION_BEFORE_INTERFERENCE, i);
    }

    public synchronized void recordMotionBeforeInterference(int i) {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).addToIntValue(KEY_DAILY_MOTION_BEFORE_INTERFERENCE, i);
    }

    /* access modifiers changed from: protected */
    public void populateDailyCheckinEvent(CheckinEventProxy checkinEventProxy, String str, long j) {
        if (DATASTORE_NAME.equals(str)) {
            LOGGER.mo11957d("Beginning daily checkin for USAnalytics.");
            CommonCheckinAttributes.addCommonDailyAttributes(checkinEventProxy, isFeatureEnabled(), ApproachSettingsUpdater.getInstance().getEnabledSource(FeatureKey.APPROACH.getEnableDefaultState()));
            CheckinDatastore checkinDatastore = (CheckinDatastore) this.mDatastores.get(DATASTORE_NAME);
            checkinEventProxy.setValue(KEY_DAILY_NO_MOTION_NO_INTERFERENCE, checkinDatastore.getIntValue(KEY_DAILY_NO_MOTION_NO_INTERFERENCE));
            checkinEventProxy.setValue(KEY_DAILY_MOTION_NO_INTERFERENCE, checkinDatastore.getIntValue(KEY_DAILY_MOTION_NO_INTERFERENCE));
            checkinEventProxy.setValue(KEY_DAILY_NO_MOTION_BEFORE_INTERFERENCE, checkinDatastore.getIntValue(KEY_DAILY_NO_MOTION_BEFORE_INTERFERENCE));
            checkinEventProxy.setValue(KEY_DAILY_MOTION_BEFORE_INTERFERENCE, checkinDatastore.getIntValue(KEY_DAILY_MOTION_BEFORE_INTERFERENCE));
        }
    }

    /* access modifiers changed from: protected */
    public boolean isFeatureSupported() {
        return USService.isUSSupported();
    }

    public boolean isFeatureEnabled() {
        return USService.isServiceEnabled();
    }

    public synchronized void setLastInstrumentationRetrieveTime() {
        SharedPreferenceManager.putLong(KEY_DEV_LAST_US_INST_RETRIEVE_TIME, Calendar.getInstance().getTimeInMillis());
    }

    public synchronized long getLastInstrumentationRetrieveTime() {
        return SharedPreferenceManager.getLong(KEY_DEV_LAST_US_INST_RETRIEVE_TIME, 0);
    }
}
