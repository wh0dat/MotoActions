package com.motorola.actions.lts.instrumentation;

import android.content.Context;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.checkin.BaseAnalytics;
import com.motorola.actions.checkin.CheckinData;
import com.motorola.actions.checkin.CheckinDatastore;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.lts.LiftToSilenceService;
import com.motorola.actions.reflect.CheckinEventProxy;
import com.motorola.actions.settings.updater.LTSSettingsUpdater;
import com.motorola.actions.utils.Constants;

public class LiftToSilenceAnalytics extends BaseAnalytics {
    private static final String CHECKIN_TAG = "MOT_LIFT_TO_SILENCE";
    private static final String DAILY_CHECKIN_VERSION = "1.4";
    private static final String DATASTORE_NAME = "actions_lts";
    private static final String INSTANCE_CHECKIN_VERSION = "1.3";
    private static final String KEY_DAILY_COUNT_LTS = "nltse";
    private static final String KEY_DAILY_COUNT_LTS_CLOSED_LID = "nltse_cl";
    private static final String KEY_INSTANCE_DELAY_TO_SILENCE_CALL = "delay";
    private static final String KEY_INSTANCE_DELAY_TO_SILENCE_CALL_CLOSED_LID = "delay_cl";
    private static final String KEY_INSTANCE_SILENCED_CALL_CAUSE = "sbl";
    private static final String KEY_INSTANCE_SILENCED_CALL_CAUSE_CLOSED_LID = "sbl_cl";
    private static final String KEY_INSTANCE_STATE_SCREEN_PRIOR_CALL = "sos";
    private static final String KEY_INSTANCE_STATE_SCREEN_PRIOR_CALL_CLOSED_LID = "sos_cl";
    private static final String KEY_INSTANCE_TIME_FROM_INCOMING_TO_ANSWER = "ttam";
    private static final String KEY_INSTANCE_TIME_FROM_INCOMING_TO_ANSWER_CLOSED_LID = "ttam_cl";

    public String getDatastoreName() {
        return DATASTORE_NAME;
    }

    public LiftToSilenceAnalytics(Context context) {
        super(context, CHECKIN_TAG, DAILY_CHECKIN_VERSION);
        addDatastore(DATASTORE_NAME);
    }

    public synchronized void recordRingingEndedAfterSilenced(String str, boolean z, int i, int i2) {
        CheckinData checkinData = new CheckinData(Constants.INSTRUMENTATION_INSTANCE_TAG, INSTANCE_CHECKIN_VERSION, System.currentTimeMillis());
        if (str != null && i2 > 0) {
            checkinData.setValue(KEY_INSTANCE_SILENCED_CALL_CAUSE, str);
            checkinData.setValue(KEY_INSTANCE_DELAY_TO_SILENCE_CALL, i2);
        }
        checkinData.setValue(KEY_INSTANCE_STATE_SCREEN_PRIOR_CALL, z ? "on" : "off");
        checkinData.setValue(KEY_INSTANCE_TIME_FROM_INCOMING_TO_ANSWER, i);
        CommonCheckinAttributes.addApkVer(checkinData);
        publishCheckinData(checkinData);
    }

    public synchronized void recordRingingEndedAfterSilencedClosedLid(String str, boolean z, int i, int i2) {
        CheckinData checkinData = new CheckinData(Constants.INSTRUMENTATION_INSTANCE_TAG, INSTANCE_CHECKIN_VERSION, System.currentTimeMillis());
        if (str != null && i2 > 0) {
            checkinData.setValue(KEY_INSTANCE_SILENCED_CALL_CAUSE_CLOSED_LID, str);
            checkinData.setValue(KEY_INSTANCE_DELAY_TO_SILENCE_CALL_CLOSED_LID, i2);
        }
        checkinData.setValue(KEY_INSTANCE_STATE_SCREEN_PRIOR_CALL_CLOSED_LID, z ? "on" : "off");
        checkinData.setValue(KEY_INSTANCE_TIME_FROM_INCOMING_TO_ANSWER_CLOSED_LID, i);
        CommonCheckinAttributes.addApkVer(checkinData);
        publishCheckinData(checkinData);
    }

    /* access modifiers changed from: protected */
    public void populateDailyCheckinEvent(CheckinEventProxy checkinEventProxy, String str, long j) {
        CheckinDatastore checkinDatastore = (CheckinDatastore) this.mDatastores.get(DATASTORE_NAME);
        CommonCheckinAttributes.addCommonDailyAttributes(checkinEventProxy, isFeatureEnabled(), LTSSettingsUpdater.getInstance().getEnabledSource(FeatureKey.PICKUP_TO_STOP_RINGING.getEnableDefaultState()));
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_COUNT_LTS);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_COUNT_LTS_CLOSED_LID);
    }

    /* access modifiers changed from: protected */
    public boolean isFeatureSupported() {
        return LiftToSilenceService.isFeatureSupported();
    }

    public void recordLiftToSilenceDailyEvents() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_COUNT_LTS);
    }

    public void recordLiftToSilenceDailyEventsClosedLid() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_COUNT_LTS_CLOSED_LID);
    }

    public boolean isFeatureEnabled() {
        return LiftToSilenceService.isLiftToSilenceEnabled();
    }
}
