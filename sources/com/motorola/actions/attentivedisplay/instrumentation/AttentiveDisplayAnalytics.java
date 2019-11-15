package com.motorola.actions.attentivedisplay.instrumentation;

import android.content.Context;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.attentivedisplay.AttentiveDisplayService;
import com.motorola.actions.attentivedisplay.util.ScreenTimeoutControl;
import com.motorola.actions.checkin.BaseAnalytics;
import com.motorola.actions.checkin.CheckinDatastore;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.p013ui.settings.AttentiveDisplaySettingsFragment;
import com.motorola.actions.reflect.CheckinEventProxy;
import com.motorola.actions.settings.updater.AttentiveDisplaySettingsUpdater;
import com.motorola.actions.utils.MALogger;

public class AttentiveDisplayAnalytics extends BaseAnalytics {
    private static final String CHECKIN_TAG = "MOT_AD_STATS";
    private static final String CHECKIN_VERSION = "1.3";
    private static final String DATASTORE_NAME = "actions_ad";
    private static final String KEY_DAILY_CAMERA_ON_TIME_ABORTED = "cota";
    private static final String KEY_DAILY_CAMERA_ON_TIME_EXTENDED = "cote";
    private static final String KEY_DAILY_CAMERA_ON_TIME_NOT_EXTENDED = "cotn";
    private static final String KEY_DAILY_CAMERA_STARTED_ABORTED = "csa";
    private static final String KEY_DAILY_CAMERA_STARTED_NOT_EXTENDED = "csn";
    private static final String KEY_DAILY_FALSE_SCREEN_DIM_NO_FACE = "fsdnf";
    private static final String KEY_DAILY_FALSE_SCREEN_DIM_NO_OBJECT = "fsdno";
    private static final String KEY_DAILY_FALSE_SCREEN_OFF_NO_FACE = "fsonf";
    private static final String KEY_DAILY_FALSE_SCREEN_OFF_NO_OBJECT = "fsono";
    private static final String KEY_DAILY_GO_TO_SLEEP = "gtse";
    private static final String KEY_DAILY_OBJECT_DETECTION_STARTED_ABORTED = "odsa";
    private static final String KEY_DAILY_OBJECT_DETECTION_STARTED_NOT_EXTENDED = "odsn";
    private static final String KEY_DAILY_OBJECT_DETECTION_TIME_ABORTED = "odta";
    private static final String KEY_DAILY_OBJECT_DETECTION_TIME_EXTENDED = "odte";
    private static final String KEY_DAILY_OBJECT_DETECTION_TIME_NOT_EXTENDED = "odtn";
    private static final String KEY_DAILY_SCREEN_BRIGHT = "sb";
    private static final String KEY_DAILY_SCREEN_BRIGHT_TIME_SAVED = "sbtsm";
    private static final String KEY_DAILY_SCREEN_DIM = "sd";
    private static final String KEY_DAILY_SCREEN_DIM_TIME_SAVED = "sdtsm";
    private static final String KEY_DAILY_SCREEN_OFF = "soff";
    private static final String KEY_DAILY_SCREEN_ON = "son";
    private static final String KEY_DAILY_SCREEN_PRE_DIM = "spd";
    private static final String KEY_DAILY_SESSIONS_ABORTED = "sa";
    private static final String KEY_DAILY_SESSIONS_ABORTED_ERROR = "sae";
    private static final String KEY_DAILY_SESSIONS_ABORTED_TIMEOUT = "sat";
    private static final String KEY_DAILY_SESSIONS_ABORTED_USER_ACTIVITY = "sau";
    private static final String KEY_DAILY_SESSIONS_EXTENDED = "se";
    private static final String KEY_DAILY_SESSIONS_NOT_EXTENDED = "sn";
    private static final String KEY_DAILY_SESSIONS_SHORTENED = "ss";
    private static final String KEY_DAILY_TIMEOUT = "t";
    private static final MALogger LOGGER = new MALogger(AttentiveDisplayAnalytics.class);

    public String getDatastoreName() {
        return DATASTORE_NAME;
    }

    public AttentiveDisplayAnalytics(Context context) {
        super(context, CHECKIN_TAG, CHECKIN_VERSION);
        addDatastore(DATASTORE_NAME);
    }

    public void populateDailyCheckinEvent(CheckinEventProxy checkinEventProxy, String str, long j) {
        CheckinDatastore checkinDatastore = (CheckinDatastore) this.mDatastores.get(DATASTORE_NAME);
        CommonCheckinAttributes.addCommonDailyAttributes(checkinEventProxy, isFeatureEnabled(), AttentiveDisplaySettingsUpdater.getInstance().getEnabledSource(FeatureKey.ATTENTIVE_DISPLAY.getEnableDefaultState()));
        setOptionalBooleanAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_GO_TO_SLEEP);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, "t");
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_SCREEN_ON);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_SCREEN_OFF);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_SCREEN_BRIGHT);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_SCREEN_DIM);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_SCREEN_PRE_DIM);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_SESSIONS_SHORTENED);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_SESSIONS_EXTENDED);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_SESSIONS_NOT_EXTENDED);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_SESSIONS_ABORTED);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_SESSIONS_ABORTED_ERROR);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_SESSIONS_ABORTED_USER_ACTIVITY);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_SESSIONS_ABORTED_TIMEOUT);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_FALSE_SCREEN_DIM_NO_FACE);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_FALSE_SCREEN_DIM_NO_OBJECT);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_FALSE_SCREEN_OFF_NO_FACE);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_FALSE_SCREEN_OFF_NO_OBJECT);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_SCREEN_BRIGHT_TIME_SAVED);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_SCREEN_DIM_TIME_SAVED);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_CAMERA_ON_TIME_EXTENDED);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_CAMERA_ON_TIME_NOT_EXTENDED);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_CAMERA_ON_TIME_ABORTED);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_CAMERA_STARTED_NOT_EXTENDED);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_CAMERA_STARTED_ABORTED);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_OBJECT_DETECTION_TIME_EXTENDED);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_OBJECT_DETECTION_TIME_NOT_EXTENDED);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_OBJECT_DETECTION_TIME_ABORTED);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_OBJECT_DETECTION_STARTED_NOT_EXTENDED);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_OBJECT_DETECTION_STARTED_ABORTED);
    }

    public boolean isFeatureSupported() {
        return AttentiveDisplayService.isFeatureSupported(ActionsApplication.getAppContext());
    }

    public boolean isFeatureEnabled() {
        return AttentiveDisplayService.isFeatureSupported(ActionsApplication.getAppContext()) && AttentiveDisplaySettingsFragment.isStayOnEnabled();
    }

    public void updateDailyInformation() {
        updateDailyTimeoutMillis();
        updateDailyGoToSleep();
    }

    private synchronized void updateDailyGoToSleep() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).setBooleanValue(KEY_DAILY_GO_TO_SLEEP, AttentiveDisplaySettingsFragment.isGoToSleepEnabled());
    }

    private synchronized void updateDailyTimeoutMillis() {
        int screenTimeout = ScreenTimeoutControl.getScreenTimeout(ActionsApplication.getAppContext());
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("timeout = ");
        sb.append(screenTimeout);
        mALogger.mo11957d(sb.toString());
        if (screenTimeout < 0) {
            screenTimeout = 0;
        }
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).setIntValue("t", screenTimeout);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailyScreenOnEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_SCREEN_ON);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailyScreenOffEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_SCREEN_OFF);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailyScreenBrightEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_SCREEN_BRIGHT);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailyScreenDimEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_SCREEN_DIM);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailyScreenPreDimEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_SCREEN_PRE_DIM);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailySessionsShortenedEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_SESSIONS_SHORTENED);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailySessionsExtendedEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_SESSIONS_EXTENDED);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailySessionsNotExtendedEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_SESSIONS_NOT_EXTENDED);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailySessionsAbortedEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_SESSIONS_ABORTED);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailySessionsAbortedErrorEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_SESSIONS_ABORTED_ERROR);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailySessionsAbortedUserActivityEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_SESSIONS_ABORTED_USER_ACTIVITY);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailySessionsAbortedTimoutEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_SESSIONS_ABORTED_TIMEOUT);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailyFalseScreenDimNoFaceEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_FALSE_SCREEN_DIM_NO_FACE);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailyFalseScreenDimNoObjectEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_FALSE_SCREEN_DIM_NO_OBJECT);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailyFalseScreenOffNoFaceEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_FALSE_SCREEN_OFF_NO_FACE);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailyFalseScreenOffNoObjectEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_FALSE_SCREEN_OFF_NO_OBJECT);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailyScreenBrightTimeSavedMillis(int i) {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).addToIntValue(KEY_DAILY_SCREEN_BRIGHT_TIME_SAVED, i);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailyScreenDimTimeSavedMillis(int i) {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).addToIntValue(KEY_DAILY_SCREEN_DIM_TIME_SAVED, i);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailyCameraOnTimeExtendedMillis(int i) {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).addToIntValue(KEY_DAILY_CAMERA_ON_TIME_EXTENDED, i);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailyCameraOnTimeNotExtendedMillis(int i) {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).addToIntValue(KEY_DAILY_CAMERA_ON_TIME_NOT_EXTENDED, i);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailyCameraOnTimeAbortedMillis(int i) {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).addToIntValue(KEY_DAILY_CAMERA_ON_TIME_ABORTED, i);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailyCameraStartedNotExtendedEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_CAMERA_STARTED_NOT_EXTENDED);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailyCameraStartedAbortedEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_CAMERA_STARTED_ABORTED);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailyObjectDetectionTimeExtendedMillis(int i) {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).addToIntValue(KEY_DAILY_OBJECT_DETECTION_TIME_EXTENDED, i);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailyObjectDetectionTimeNotExtendedMillis(int i) {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).addToIntValue(KEY_DAILY_OBJECT_DETECTION_TIME_NOT_EXTENDED, i);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailyObjectDetectionTimeAbortedMillis(int i) {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).addToIntValue(KEY_DAILY_OBJECT_DETECTION_TIME_ABORTED, i);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailyObjectDetectionStartedNotExtendedEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_OBJECT_DETECTION_STARTED_NOT_EXTENDED);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordDailyObjectDetectionStartedAbortedEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_OBJECT_DETECTION_STARTED_ABORTED);
    }
}
