package com.motorola.actions.ftm.instrumentation;

import android.content.Context;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.checkin.BaseAnalytics;
import com.motorola.actions.checkin.CheckinData;
import com.motorola.actions.checkin.CheckinDatastore;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.ftm.FlipToMuteConstants;
import com.motorola.actions.ftm.FlipToMuteService;
import com.motorola.actions.reflect.CheckinEventProxy;
import com.motorola.actions.settings.updater.FTMSettingsUpdater;
import com.motorola.actions.utils.Constants;

public class FlipToMuteAnalytics extends BaseAnalytics {
    private static final String CHECKIN_TAG = "MOT_FLIP_TO_DND";
    private static final String DAILY_CHECKIN_VERSION = "1.4";
    private static final String DATASTORE_NAME = "actions_ftm";
    private static final String INSTANCE_CHECKIN_VERSION = "1.3";
    private static final String KEY_DAILY_ALARMS_ONLY_SELECTED = "ao";
    private static final String KEY_DAILY_PRIORITY_ONLY_SELECTED = "po";
    private static final String KEY_DAILY_TOTAL_DURATION = "t_dur";
    private static final String KEY_DAILY_TOTAL_DURATION_CLOSED_LID = "t_dur_cl";
    private static final String KEY_DAILY_TOTAL_SILENCE_SELECTED = "ts";
    private static final String KEY_INSTANCE_CHECKIN_DND_DURATION = "dur";
    private static final String KEY_INSTANCE_CHECKIN_DND_DURATION_CLOSED_LID = "dur_cl";
    private static final String KEY_INSTANCE_CHECKIN_DND_STATE = "st";
    private static final String KEY_INSTANCE_CHECKIN_DND_STATE_CLOSED_LID = "st_cl";

    public String getDatastoreName() {
        return DATASTORE_NAME;
    }

    public FlipToMuteAnalytics(Context context) {
        super(context, CHECKIN_TAG, DAILY_CHECKIN_VERSION);
        addDatastore(DATASTORE_NAME);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailyTotalDuration(int i) {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).addToIntValue(KEY_DAILY_TOTAL_DURATION, i);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailyTotalDurationClosedLid(int i) {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).addToIntValue(KEY_DAILY_TOTAL_DURATION_CLOSED_LID, i);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailyPriorityOnlySelected() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).setBooleanValue(KEY_DAILY_PRIORITY_ONLY_SELECTED, true);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailyAlarmsOnlySelected() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).setBooleanValue(KEY_DAILY_ALARMS_ONLY_SELECTED, true);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailyTotalSilenceSelected() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).setBooleanValue(KEY_DAILY_TOTAL_SILENCE_SELECTED, true);
    }

    /* access modifiers changed from: protected */
    public void populateDailyCheckinEvent(CheckinEventProxy checkinEventProxy, String str, long j) {
        CheckinDatastore checkinDatastore = (CheckinDatastore) this.mDatastores.get(DATASTORE_NAME);
        CommonCheckinAttributes.addCommonDailyAttributes(checkinEventProxy, isFeatureEnabled(), FTMSettingsUpdater.getInstance().getEnabledSource(FeatureKey.FLIP_TO_DND.getEnableDefaultState()));
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_TOTAL_DURATION);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_TOTAL_DURATION_CLOSED_LID);
        if (isFeatureEnabled()) {
            if (checkinDatastore.getBooleanValue(KEY_DAILY_PRIORITY_ONLY_SELECTED)) {
                checkinEventProxy.setValue(KEY_DAILY_PRIORITY_ONLY_SELECTED, 1);
            }
            if (checkinDatastore.getBooleanValue(KEY_DAILY_ALARMS_ONLY_SELECTED)) {
                checkinEventProxy.setValue(KEY_DAILY_ALARMS_ONLY_SELECTED, 1);
            }
            if (checkinDatastore.getBooleanValue(KEY_DAILY_TOTAL_SILENCE_SELECTED)) {
                checkinEventProxy.setValue(KEY_DAILY_TOTAL_SILENCE_SELECTED, 1);
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean isFeatureSupported() {
        return FlipToMuteService.isFeatureSupported();
    }

    public boolean isFeatureEnabled() {
        return FlipToMuteService.isServiceEnabled();
    }

    /* access modifiers changed from: 0000 */
    public synchronized void publishAfterEventEnded(String str, int i) {
        CheckinData checkinData = new CheckinData(Constants.INSTRUMENTATION_INSTANCE_TAG, INSTANCE_CHECKIN_VERSION, System.currentTimeMillis());
        checkinData.setValue(KEY_INSTANCE_CHECKIN_DND_STATE, str);
        checkinData.setValue(KEY_INSTANCE_CHECKIN_DND_DURATION, i);
        publishCheckinData(checkinData);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void publishAfterEventEndedClosedLid(String str, int i) {
        CheckinData checkinData = new CheckinData(Constants.INSTRUMENTATION_INSTANCE_TAG, INSTANCE_CHECKIN_VERSION, System.currentTimeMillis());
        checkinData.setValue(KEY_INSTANCE_CHECKIN_DND_STATE_CLOSED_LID, str);
        checkinData.setValue(KEY_INSTANCE_CHECKIN_DND_DURATION_CLOSED_LID, i);
        CommonCheckinAttributes.addApkVer(checkinData);
        publishCheckinData(checkinData);
    }

    public void updateDailyInformation() {
        FlipToMuteInstrumentation.recordOptionSelected(SharedPreferenceManager.getInt(FlipToMuteConstants.DND_OPTION_SELECTED, 4));
    }
}
