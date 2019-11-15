package com.motorola.actions.foc.instrumentation;

import android.content.Context;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.checkin.BaseAnalytics;
import com.motorola.actions.checkin.CheckinDatastore;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.foc.gesture.service.FlashOnChopService;
import com.motorola.actions.reflect.CheckinEventProxy;
import com.motorola.actions.settings.updater.FOCSettingsUpdater;

public class FlashOnChopAnalytics extends BaseAnalytics {
    private static final String CHECKIN_TAG = "MOT_GESTURES";
    private static final String DAILY_CHECKIN_EVENT_NAME = "ChopChopDailyStats";
    private static final String DAILY_CHECKIN_VERSION = "1.8";
    private static final String DATASTORE_NAME = "actions_foc";
    private static final String KEY_CC_FLASHLIGHT_ON_0_TO_15_SECS = "cc_on_0_15s";
    private static final String KEY_CC_FLASHLIGHT_ON_0_TO_15_SECS_CLOSED_LID = "cc_on_0_15s_cl";
    private static final String KEY_CC_FLASHLIGHT_ON_15_TO_30_SECS = "cc_on_15_30s";
    private static final String KEY_CC_FLASHLIGHT_ON_15_TO_30_SECS_CLOSED_LID = "cc_on_15_30s_cl";
    private static final String KEY_CC_FLASHLIGHT_ON_30_TO_60_SECS = "cc_on_30_60s";
    private static final String KEY_CC_FLASHLIGHT_ON_30_TO_60_SECS_CLOSED_LID = "cc_on_30_60s_cl";
    private static final String KEY_CC_FLASHLIGHT_ON_60_TO_120_SECS = "cc_on_60_120s";
    private static final String KEY_CC_FLASHLIGHT_ON_60_TO_120_SECS_CLOSED_LID = "cc_on_60_120s_cl";
    private static final String KEY_CC_FLASHLIGHT_ON_OVER_120_SECS = "cc_on_120s";
    private static final String KEY_CC_FLASHLIGHT_ON_OVER_120_SECS_CLOSED_LID = "cc_on_120s_cl";
    private static final String KEY_CC_FLASHLIGHT_TURNED_ON = "cc_on";
    private static final String KEY_CC_FLASHLIGHT_TURNED_ON_CLOSED_LID = "cc_on_cl";
    private static final String KEY_CC_ON_BY_CAMERA_MANAGER = "cc_on_CM";
    private static final String KEY_CC_ON_BY_CAMERA_MANAGER_CLOSED_LID = "cc_on_CM_cl";
    private static final String KEY_CC_ON_BY_FLASHLIGHTCONTROLLER = "cc_on_FC";
    private static final String KEY_CC_ON_BY_FLASHLIGHTCONTROLLER_CLOSED_LID = "cc_on_FC_cl";
    private static final String KEY_CHECKIN_PERIOD_SECS = "time_s";
    private static final String KEY_FLASHLIGHT_ON_0_TO_15_SECS = "fl_on_0_15s";
    private static final String KEY_FLASHLIGHT_ON_15_TO_30_SECS = "fl_on_15_30s";
    private static final String KEY_FLASHLIGHT_ON_30_TO_60_SECS = "fl_on_30_60s";
    private static final String KEY_FLASHLIGHT_ON_60_TO_120_SECS = "fl_on_60_120s";
    private static final String KEY_FLASHLIGHT_ON_OVER_120_SECS = "fl_on_120s";
    private static final String KEY_FLASHLIGHT_TIMED_OUT = "timeout";
    private static final String KEY_FLASHLIGHT_TIMED_OUT_CLOSED_LID = "timeout_cl";
    private static final String KEY_FLASHLIGHT_TURNED_ON = "fl_on";
    private static final String KEY_GYRO_THRESHOLD_TRIGGERED = "g_conflicts";
    private static final String KEY_GYRO_THRESHOLD_TRIGGERED_CLOSED_LID = "g_conflicts_cl";
    private static final String KEY_NUMBER_OF_DAILY_TOGGLES = "num_tog";
    private static final String KEY_NUMBER_OF_DAILY_TOGGLES_CLOSED_LID = "num_tog_cl";
    private static final String KEY_TIME_ENABLED_SECS = "en_s";
    private static final String KEY_TOTAL_CHOPS_DETECTED = "total_chops";
    private static final String KEY_TOTAL_CHOPS_DETECTED_CLOSED_LID = "total_chops_cl";
    private static final int MAX_CHECKIN_SECS = 172800;

    public String getDatastoreName() {
        return DATASTORE_NAME;
    }

    public FlashOnChopAnalytics(Context context) {
        super(context, CHECKIN_TAG, DAILY_CHECKIN_EVENT_NAME, DAILY_CHECKIN_VERSION);
        addDatastore(DATASTORE_NAME);
    }

    /* access modifiers changed from: protected */
    public void populateDailyCheckinEvent(CheckinEventProxy checkinEventProxy, String str, long j) {
        CommonCheckinAttributes.addCommonDailyAttributes(checkinEventProxy, isFeatureEnabled(), FOCSettingsUpdater.getInstance().getEnabledSource(FeatureKey.FLASH_ON_CHOP.getEnableDefaultState()));
        CheckinDatastore checkinDatastore = (CheckinDatastore) this.mDatastores.get(DATASTORE_NAME);
        checkinEventProxy.setValue(KEY_CHECKIN_PERIOD_SECS, BaseAnalytics.capValue(j / 1000, 172800));
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_NUMBER_OF_DAILY_TOGGLES);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_TOTAL_CHOPS_DETECTED);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_GYRO_THRESHOLD_TRIGGERED);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_FLASHLIGHT_TIMED_OUT);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_CC_FLASHLIGHT_TURNED_ON);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_CC_FLASHLIGHT_ON_0_TO_15_SECS);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_CC_FLASHLIGHT_ON_15_TO_30_SECS);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_CC_FLASHLIGHT_ON_30_TO_60_SECS);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_CC_FLASHLIGHT_ON_60_TO_120_SECS);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_CC_FLASHLIGHT_ON_OVER_120_SECS);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_FLASHLIGHT_TURNED_ON);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_FLASHLIGHT_ON_0_TO_15_SECS);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_FLASHLIGHT_ON_15_TO_30_SECS);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_FLASHLIGHT_ON_30_TO_60_SECS);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_FLASHLIGHT_ON_60_TO_120_SECS);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_FLASHLIGHT_ON_OVER_120_SECS);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_CC_ON_BY_CAMERA_MANAGER);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_CC_ON_BY_FLASHLIGHTCONTROLLER);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_NUMBER_OF_DAILY_TOGGLES_CLOSED_LID);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_TOTAL_CHOPS_DETECTED_CLOSED_LID);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_GYRO_THRESHOLD_TRIGGERED_CLOSED_LID);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_FLASHLIGHT_TIMED_OUT_CLOSED_LID);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_CC_FLASHLIGHT_TURNED_ON_CLOSED_LID);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_CC_FLASHLIGHT_ON_0_TO_15_SECS_CLOSED_LID);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_CC_FLASHLIGHT_ON_15_TO_30_SECS_CLOSED_LID);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_CC_FLASHLIGHT_ON_30_TO_60_SECS_CLOSED_LID);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_CC_FLASHLIGHT_ON_60_TO_120_SECS_CLOSED_LID);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_CC_FLASHLIGHT_ON_OVER_120_SECS_CLOSED_LID);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_CC_ON_BY_CAMERA_MANAGER_CLOSED_LID);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_CC_ON_BY_FLASHLIGHTCONTROLLER_CLOSED_LID);
        boolean isRecordingTimeEnabledSecs = FlashOnChopInstrumentation.isRecordingTimeEnabledSecs();
        if (isRecordingTimeEnabledSecs) {
            FlashOnChopInstrumentation.stopRecordTimeEnabledSecs();
        }
        checkinEventProxy.setValue(KEY_TIME_ENABLED_SECS, BaseAnalytics.capValue(checkinDatastore.getIntValue(KEY_TIME_ENABLED_SECS), (int) MAX_CHECKIN_SECS));
        if (isRecordingTimeEnabledSecs) {
            FlashOnChopInstrumentation.startRecordTimeEnabledSecs();
        }
    }

    /* access modifiers changed from: protected */
    public boolean isFeatureSupported() {
        return FlashOnChopService.isFeatureSupported(this.mContext);
    }

    public boolean isFeatureEnabled() {
        return FlashOnChopService.isServiceEnabled(this.mContext);
    }

    public synchronized void recordChopEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_TOTAL_CHOPS_DETECTED);
    }

    public synchronized void recordChopEventClosedLid() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_TOTAL_CHOPS_DETECTED_CLOSED_LID);
    }

    public synchronized void recordGyroThresholdTriggeredEvents(int i) {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).addToIntValue(KEY_GYRO_THRESHOLD_TRIGGERED, i);
    }

    public synchronized void recordGyroThresholdTriggeredEventsClosedLid(int i) {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).addToIntValue(KEY_GYRO_THRESHOLD_TRIGGERED_CLOSED_LID, i);
    }

    public synchronized void recordFlashlightTimedOutEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_FLASHLIGHT_TIMED_OUT);
    }

    public synchronized void recordFlashlightTimedOutEventClosedLid() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_FLASHLIGHT_TIMED_OUT_CLOSED_LID);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordFlashlightOnEvent(boolean z) {
        if (z) {
            try {
                ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_CC_FLASHLIGHT_TURNED_ON);
            } catch (Throwable th) {
                throw th;
            }
        } else {
            ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_FLASHLIGHT_TURNED_ON);
        }
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordFlashlightOnEventClosedLid() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_CC_FLASHLIGHT_TURNED_ON_CLOSED_LID);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordFlashlightOn0To15Secs(boolean z) {
        if (z) {
            try {
                ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_CC_FLASHLIGHT_ON_0_TO_15_SECS);
            } catch (Throwable th) {
                throw th;
            }
        } else {
            ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_FLASHLIGHT_ON_0_TO_15_SECS);
        }
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordFlashlightOn0To15SecsClosedLid() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_CC_FLASHLIGHT_ON_0_TO_15_SECS_CLOSED_LID);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordFlashlightOn15To30Secs(boolean z) {
        if (z) {
            try {
                ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_CC_FLASHLIGHT_ON_15_TO_30_SECS);
            } catch (Throwable th) {
                throw th;
            }
        } else {
            ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_FLASHLIGHT_ON_15_TO_30_SECS);
        }
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordFlashlightOn15To30SecsClosedLid() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_CC_FLASHLIGHT_ON_15_TO_30_SECS_CLOSED_LID);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordFlashlightOn30To60Secs(boolean z) {
        if (z) {
            try {
                ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_CC_FLASHLIGHT_ON_30_TO_60_SECS);
            } catch (Throwable th) {
                throw th;
            }
        } else {
            ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_FLASHLIGHT_ON_30_TO_60_SECS);
        }
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordFlashlightOn30To60SecsClosedLid() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_CC_FLASHLIGHT_ON_30_TO_60_SECS_CLOSED_LID);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordFlashlightOn60To120Secs(boolean z) {
        if (z) {
            try {
                ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_CC_FLASHLIGHT_ON_60_TO_120_SECS);
            } catch (Throwable th) {
                throw th;
            }
        } else {
            ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_FLASHLIGHT_ON_60_TO_120_SECS);
        }
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordFlashlightOn60To120SecsClosedLid() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_CC_FLASHLIGHT_ON_60_TO_120_SECS_CLOSED_LID);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordFlashlightOnOver120Secs(boolean z) {
        if (z) {
            try {
                ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_CC_FLASHLIGHT_ON_OVER_120_SECS);
            } catch (Throwable th) {
                throw th;
            }
        } else {
            ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_FLASHLIGHT_ON_OVER_120_SECS);
        }
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordFlashlightOnOver120SecsClosedLid() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_CC_FLASHLIGHT_ON_OVER_120_SECS_CLOSED_LID);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordCameraManagerEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_CC_ON_BY_CAMERA_MANAGER);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordCameraManagerEventClosedLid() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_CC_ON_BY_CAMERA_MANAGER_CLOSED_LID);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordFlashlightControllerEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_CC_ON_BY_FLASHLIGHTCONTROLLER);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordFlashlightControllerEventClosedLid() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_CC_ON_BY_FLASHLIGHTCONTROLLER_CLOSED_LID);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordTimeEnabledSecs(int i) {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).addToIntValue(KEY_TIME_ENABLED_SECS, i);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailyToggleEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_NUMBER_OF_DAILY_TOGGLES);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailyToggleEventClosedLid() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_NUMBER_OF_DAILY_TOGGLES_CLOSED_LID);
    }
}
