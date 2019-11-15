package com.motorola.actions.microScreen.instrumentation;

import android.content.Context;
import android.text.TextUtils;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.checkin.BaseAnalytics;
import com.motorola.actions.checkin.CheckinData;
import com.motorola.actions.checkin.CheckinDatastore;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.microScreen.MicroScreenService;
import com.motorola.actions.reflect.CheckinEventProxy;
import com.motorola.actions.settings.updater.MicroScreenSettingsUpdater;
import com.motorola.actions.utils.Constants;

public class MicroScreenAnalytics extends BaseAnalytics {
    private static final String CHECKIN_TAG = "MOT_MICROSCREEN";
    private static final String DAILY_CHECKIN_VERSION = "1.3";
    private static final String DATASTORE_NAME = "actions_microscreen";
    private static final String INSTANCE_CHECKIN_VERSION = "1.2";
    private static final String KEY_DAILY_NUMBER_OF_ACTIVATIONS = "n_act";
    private static final String KEY_DAILY_NUMBER_OF_ACTIVATIONS_WITH_ONENAV = "n_act_1n";
    private static final String KEY_DAILY_TOTAL_DURATION = "t_dur";
    private static final String KEY_INSTANCE_ACTIVATION_METHOD = "lr";
    static final String KEY_INSTANCE_ACTIVATION_METHOD_CENTER = "C";
    static final String KEY_INSTANCE_ACTIVATION_METHOD_LEFT = "L";
    static final String KEY_INSTANCE_ACTIVATION_METHOD_RIGHT = "R";
    private static final String KEY_INSTANCE_APP_PKG_IN_VIEW = "pkg";
    private static final String KEY_INSTANCE_DURATION_EVENT = "dur";
    private static final String KEY_INSTANCE_EXIT_METHOD = "em";

    public String getDatastoreName() {
        return DATASTORE_NAME;
    }

    public MicroScreenAnalytics(Context context) {
        super(context, CHECKIN_TAG, DAILY_CHECKIN_VERSION);
        addDatastore(DATASTORE_NAME);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordTotalDuration(int i) {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).addToIntValue(KEY_DAILY_TOTAL_DURATION, i);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordActivationWithOneNavDailyEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_NUMBER_OF_ACTIVATIONS_WITH_ONENAV);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordActivationDailyEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_NUMBER_OF_ACTIVATIONS);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordMicroScreenOff(int i, String str, String str2, String str3) {
        CheckinData checkinData = new CheckinData(Constants.INSTRUMENTATION_INSTANCE_TAG, INSTANCE_CHECKIN_VERSION, System.currentTimeMillis());
        checkinData.setValue(KEY_INSTANCE_DURATION_EVENT, i);
        checkinData.setValue(KEY_INSTANCE_APP_PKG_IN_VIEW, str);
        if (!TextUtils.isEmpty(str3)) {
            checkinData.setValue(KEY_INSTANCE_EXIT_METHOD, str3);
        }
        if (!TextUtils.isEmpty(str2)) {
            checkinData.setValue(KEY_INSTANCE_ACTIVATION_METHOD, str2);
        }
        CommonCheckinAttributes.addApkVer(checkinData);
        publishCheckinData(checkinData);
    }

    /* access modifiers changed from: protected */
    public void populateDailyCheckinEvent(CheckinEventProxy checkinEventProxy, String str, long j) {
        CheckinDatastore checkinDatastore = (CheckinDatastore) this.mDatastores.get(DATASTORE_NAME);
        CommonCheckinAttributes.addCommonDailyAttributes(checkinEventProxy, isFeatureEnabled(), MicroScreenSettingsUpdater.getInstance().getEnabledSource(FeatureKey.MICROSCREEN.getEnableDefaultState()));
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_TOTAL_DURATION);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_NUMBER_OF_ACTIVATIONS);
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_NUMBER_OF_ACTIVATIONS_WITH_ONENAV);
    }

    /* access modifiers changed from: protected */
    public boolean isFeatureSupported() {
        return MicroScreenService.isFeatureSupported();
    }

    public boolean isFeatureEnabled() {
        return MicroScreenService.isServiceEnabled();
    }
}
