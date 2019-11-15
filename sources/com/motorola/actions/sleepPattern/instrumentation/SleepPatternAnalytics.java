package com.motorola.actions.sleepPattern.instrumentation;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.checkin.BaseAnalytics;
import com.motorola.actions.checkin.CheckinDatastore;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.reflect.CheckinEventProxy;
import com.motorola.actions.settings.updater.NDSettingsUpdater;
import com.motorola.actions.sleepPattern.SleepPatternService;
import com.motorola.actions.utils.MALogger;

public class SleepPatternAnalytics extends BaseAnalytics implements SleepPatternAnalyticsAccess {
    private static final String CHECKIN_TAG = "MOT_SLEEP_PATTERN";
    private static final String DAILY_CHECKIN_VERSION = "1.1";
    private static final String DATASTORE_NAME = "actions_sp";
    private static final String KEY_DAILY_PRE_PROCESS_WEEKEND_RETIRE = "p_wkd_r";
    private static final String KEY_DAILY_PRE_PROCESS_WEEKEND_WAKEUP = "p_wkd_w";
    private static final String KEY_DAILY_PRE_PROCESS_WEEK_RETIRE = "p_wk_r";
    private static final String KEY_DAILY_PRE_PROCESS_WEEK_WAKEUP = "p_wk_w";
    private static final MALogger LOGGER = new MALogger(SleepPatternAnalytics.class);

    public String getDatastoreName() {
        return DATASTORE_NAME;
    }

    public SleepPatternAnalytics(Context context) {
        super(context, CHECKIN_TAG, DAILY_CHECKIN_VERSION);
        addDatastore(DATASTORE_NAME);
    }

    /* access modifiers changed from: protected */
    public void populateDailyCheckinEvent(CheckinEventProxy checkinEventProxy, String str, long j) {
        LOGGER.mo11957d("SleepPattern daily checkin");
        CheckinDatastore checkinDatastore = (CheckinDatastore) this.mDatastores.get(DATASTORE_NAME);
        CommonCheckinAttributes.addCommonDailyAttributes(checkinEventProxy, isFeatureEnabled(), NDSettingsUpdater.getInstance().getEnabledSource(FeatureKey.SLEEP_PATTERN.getEnableDefaultState()));
        setOptionalStringAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_PRE_PROCESS_WEEK_RETIRE);
        setOptionalStringAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_PRE_PROCESS_WEEK_WAKEUP);
        setOptionalStringAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_PRE_PROCESS_WEEKEND_RETIRE);
        setOptionalStringAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_PRE_PROCESS_WEEKEND_WAKEUP);
    }

    /* access modifiers changed from: protected */
    public boolean isFeatureSupported() {
        return SleepPatternService.isFeatureSupported();
    }

    public boolean isFeatureEnabled() {
        return SleepPatternService.isServiceEnabled();
    }

    public synchronized void recordPreProcessing(SparseArray<String> sparseArray) {
        for (int i = 0; i < sparseArray.size(); i++) {
            Integer valueOf = Integer.valueOf(sparseArray.keyAt(i));
            String str = (String) sparseArray.get(valueOf.intValue());
            if (!TextUtils.isEmpty(str)) {
                ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).setStringValue(mapKey(valueOf), str);
            }
        }
    }

    private String mapKey(Integer num) {
        switch (num.intValue()) {
            case 1:
                return KEY_DAILY_PRE_PROCESS_WEEK_RETIRE;
            case 2:
                return KEY_DAILY_PRE_PROCESS_WEEK_WAKEUP;
            case 3:
                return KEY_DAILY_PRE_PROCESS_WEEKEND_RETIRE;
            case 4:
                return KEY_DAILY_PRE_PROCESS_WEEKEND_WAKEUP;
            default:
                String str = "";
                LOGGER.mo11963w("Undefined key");
                return str;
        }
    }
}
