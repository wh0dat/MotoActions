package com.motorola.actions.p010qc.instrumentation;

import android.content.Context;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.checkin.BaseAnalytics;
import com.motorola.actions.checkin.CheckinDatastore;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.p010qc.QuickCaptureConfig;
import com.motorola.actions.p010qc.QuickDrawHelper;
import com.motorola.actions.reflect.CheckinEventProxy;
import com.motorola.actions.settings.updater.QCSettingsUpdater;

/* renamed from: com.motorola.actions.qc.instrumentation.QuickCaptureAnalytics */
public class QuickCaptureAnalytics extends BaseAnalytics {
    private static final String CHECKIN_TAG = "MOT_QUICK_CAPTURE";
    private static final String DAILY_CHECKIN_VERSION = "1.3";
    private static final String DATASTORE_NAME = "actions_qc";
    private static final String KEY_DAILY_NUMBER_OF_DAILY_TOGGLES = "num_tog";
    private static final String KEY_DAILY_TOTAL_RAW_QCS_DETECTED = "trqc";
    private static final String KEY_DAILY_TOTAL_RAW_QCS_DETECTED_CLOSED_LID = "trqc_cl";

    public String getDatastoreName() {
        return DATASTORE_NAME;
    }

    public QuickCaptureAnalytics(Context context) {
        super(context, CHECKIN_TAG, DAILY_CHECKIN_VERSION);
        addDatastore(DATASTORE_NAME);
    }

    /* access modifiers changed from: protected */
    public void populateDailyCheckinEvent(CheckinEventProxy checkinEventProxy, String str, long j) {
        CheckinDatastore checkinDatastore = (CheckinDatastore) this.mDatastores.get(DATASTORE_NAME);
        CommonCheckinAttributes.addCommonDailyAttributes(checkinEventProxy, isFeatureEnabled(), QCSettingsUpdater.getInstance().getEnabledSource(FeatureKey.QUICK_CAPTURE.getEnableDefaultState()));
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_NUMBER_OF_DAILY_TOGGLES);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_TOTAL_RAW_QCS_DETECTED);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_TOTAL_RAW_QCS_DETECTED_CLOSED_LID);
    }

    /* access modifiers changed from: protected */
    public boolean isFeatureSupported() {
        return QuickCaptureConfig.isSupported();
    }

    public boolean isFeatureEnabled() {
        return QuickDrawHelper.isEnabled();
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordQuickCaptureEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_TOTAL_RAW_QCS_DETECTED);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordQuickCaptureEventClosedLid() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_TOTAL_RAW_QCS_DETECTED_CLOSED_LID);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailyToggleEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_NUMBER_OF_DAILY_TOGGLES);
    }
}
