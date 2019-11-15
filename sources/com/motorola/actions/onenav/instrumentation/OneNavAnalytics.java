package com.motorola.actions.onenav.instrumentation;

import android.content.Context;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.checkin.BaseAnalytics;
import com.motorola.actions.checkin.CheckinData;
import com.motorola.actions.checkin.CheckinDatastore;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.onenav.OneNavHelper;
import com.motorola.actions.reflect.CheckinEventProxy;
import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.settings.updater.OneNavSettingsUpdater;
import com.motorola.actions.utils.Constants;
import com.motorola.actions.utils.MALogger;

public class OneNavAnalytics extends BaseAnalytics {
    private static final String CHECKIN_TAG = "MOT_NAV_ONE";
    private static final String DAILY_CHECKIN_VERSION = "1.3";
    private static final String DATASTORE_NAME = "actions_nav_one";
    private static final String INSTANCE_CHECKIN_VERSION = "1.2";
    private static final String KEY_DAILY_COUNT_ONE_NAV_DISABLED_EVENTS = "n_dis";
    private static final String KEY_DAILY_COUNT_ONE_NAV_ENABLED_EVENTS = "n_en";
    private static final String KEY_DAILY_ONE_NAV_TUTORIAL_STEP = "ts";
    private static final String KEY_DAILY_REQUIRED_ONE_NAV_DIRECTION = "dir";
    private static final String KEY_DAILY_REQUIRED_ONE_NAV_DIRECTION_DEFAULT = "d";
    private static final String KEY_DAILY_REQUIRED_ONE_NAV_DIRECTION_REVERSED = "r";
    private static final String KEY_DAILY_REQUIRED_ONE_NAV_VIBRATION_ENABLED = "vib_en";
    private static final String KEY_INSTANCE_ENABLED = "en";
    static final String KEY_INSTANCE_FIRST_DISABLE_MOTO_APP = "fstOff_m";
    static final String KEY_INSTANCE_FIRST_DISABLE_SETTINGS = "fstOff_s";
    static final String KEY_INSTANCE_FIRST_ENABLE_MOTO_APP = "fstOn_m";
    static final String KEY_INSTANCE_FIRST_ENABLE_SETTINGS = "fstOn_s";
    private static final String KEY_INSTANCE_ONE_NAV_DISCOVERY_FULL_NO_THANKS = "full_nth";
    private static final String KEY_INSTANCE_ONE_NAV_DISCOVERY_FULL_TRY_IT = "full_try";
    private static final String KEY_INSTANCE_ONE_NAV_DISCOVERY_HALF_DISMISS = "half_ml";
    private static final String KEY_INSTANCE_ONE_NAV_DISCOVERY_HALF_LEARN_MORE = "half_mor";
    private static final String KEY_INSTANCE_ONE_NAV_DISCOVERY_HINT_CLICK = "hint_clk";
    private static final String KEY_INSTANCE_ONE_NAV_DISCOVERY_HINT_SHOW = "hint_sh";
    private static final MALogger LOGGER = new MALogger(OneNavAnalytics.class);
    static final int MAX_COUNT_ENABLE_DISABLE = 250;

    public String getDatastoreName() {
        return DATASTORE_NAME;
    }

    public OneNavAnalytics(Context context) {
        super(context, CHECKIN_TAG, DAILY_CHECKIN_VERSION);
        addDatastore(DATASTORE_NAME);
    }

    public void updateDailyInformation() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).setStringValue(KEY_DAILY_ONE_NAV_TUTORIAL_STEP, SharedPreferenceManager.getString(OneNavInstrumentation.LAST_TUTORIAL_STEP_CODE_PREFERENCE_KEY, ""));
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).setBooleanValue(KEY_DAILY_REQUIRED_ONE_NAV_VIBRATION_ENABLED, MotorolaSettings.getOneNavVibrationSetting());
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).setStringValue(KEY_DAILY_REQUIRED_ONE_NAV_DIRECTION, MotorolaSettings.getOneNavBackSwipeDirection(0) == 0 ? KEY_DAILY_REQUIRED_ONE_NAV_DIRECTION_DEFAULT : KEY_DAILY_REQUIRED_ONE_NAV_DIRECTION_REVERSED);
    }

    /* access modifiers changed from: protected */
    public void populateDailyCheckinEvent(CheckinEventProxy checkinEventProxy, String str, long j) {
        CommonCheckinAttributes.addCommonDailyAttributes(checkinEventProxy, isFeatureEnabled(), OneNavSettingsUpdater.getInstance().getEnabledSource(FeatureKey.ONE_NAV.getEnableDefaultState()));
        CheckinDatastore datastore = getDatastore(str);
        setOptionalIntAttribute(datastore, checkinEventProxy, KEY_DAILY_COUNT_ONE_NAV_ENABLED_EVENTS);
        setOptionalIntAttribute(datastore, checkinEventProxy, KEY_DAILY_COUNT_ONE_NAV_DISABLED_EVENTS);
        setOptionalStringAttribute(datastore, checkinEventProxy, KEY_DAILY_ONE_NAV_TUTORIAL_STEP);
        checkinEventProxy.setValue(KEY_DAILY_REQUIRED_ONE_NAV_VIBRATION_ENABLED, datastore.getBooleanValue(KEY_DAILY_REQUIRED_ONE_NAV_VIBRATION_ENABLED));
        checkinEventProxy.setValue(KEY_DAILY_REQUIRED_ONE_NAV_DIRECTION, datastore.getStringValue(KEY_DAILY_REQUIRED_ONE_NAV_DIRECTION));
    }

    /* access modifiers changed from: protected */
    public boolean isFeatureSupported() {
        return OneNavHelper.isOneNavPresent();
    }

    public boolean isFeatureEnabled() {
        return OneNavHelper.isOneNavPresent() && OneNavHelper.isOneNavEnabled();
    }

    /* access modifiers changed from: 0000 */
    public int getDailyEnabledDisabledCount(boolean z) {
        if (z) {
            return ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).getIntValue(KEY_DAILY_COUNT_ONE_NAV_ENABLED_EVENTS);
        }
        return ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).getIntValue(KEY_DAILY_COUNT_ONE_NAV_DISABLED_EVENTS);
    }

    private synchronized CheckinData createCheckinDataBase() {
        CheckinData checkinData;
        checkinData = new CheckinData(Constants.INSTRUMENTATION_INSTANCE_TAG, INSTANCE_CHECKIN_VERSION, System.currentTimeMillis());
        CommonCheckinAttributes.addApkVer(checkinData);
        return checkinData;
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordInstanceOneNavEnableStatusChanged(boolean z) {
        CheckinData createCheckinDataBase = createCheckinDataBase();
        createCheckinDataBase.setValue("en", z ? "1" : "0");
        publishCheckinData(createCheckinDataBase);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordInstanceOneNavFirstEnableSettings() {
        LOGGER.mo11957d("recordInstanceOneNavFirstEnableSettings");
        CheckinData createCheckinDataBase = createCheckinDataBase();
        createCheckinDataBase.setValue(KEY_INSTANCE_FIRST_ENABLE_SETTINGS, true);
        publishCheckinData(createCheckinDataBase);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordInstanceOneNavFirstEnableMotoApp() {
        LOGGER.mo11957d("recordInstanceOneNavFirstEnableMotoApp");
        CheckinData createCheckinDataBase = createCheckinDataBase();
        createCheckinDataBase.setValue(KEY_INSTANCE_FIRST_ENABLE_MOTO_APP, true);
        publishCheckinData(createCheckinDataBase);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordInstanceOneNavFirstDisableSettings() {
        LOGGER.mo11957d("recordInstanceOneNavFirstDisableSettings");
        CheckinData createCheckinDataBase = createCheckinDataBase();
        createCheckinDataBase.setValue(KEY_INSTANCE_FIRST_DISABLE_SETTINGS, true);
        publishCheckinData(createCheckinDataBase);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordInstanceOneNavFirstDisableMotoApp() {
        LOGGER.mo11957d("recordInstanceOneNavFirstDisableMotoApp");
        CheckinData createCheckinDataBase = createCheckinDataBase();
        createCheckinDataBase.setValue(KEY_INSTANCE_FIRST_DISABLE_MOTO_APP, true);
        publishCheckinData(createCheckinDataBase);
    }

    /* access modifiers changed from: 0000 */
    public void recordOneNavEnableDailyEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_COUNT_ONE_NAV_ENABLED_EVENTS);
    }

    /* access modifiers changed from: 0000 */
    public void recordOneNavDisableDailyEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_COUNT_ONE_NAV_DISABLED_EVENTS);
    }

    /* access modifiers changed from: 0000 */
    public void recordOneNavEnableVibrationDailyEvent(boolean z) {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).setBooleanValue(KEY_DAILY_REQUIRED_ONE_NAV_VIBRATION_ENABLED, z);
    }

    /* access modifiers changed from: 0000 */
    public void recordOneNavSwitchDirectionDailyEvent(boolean z) {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).setStringValue(KEY_DAILY_REQUIRED_ONE_NAV_DIRECTION, z ? KEY_DAILY_REQUIRED_ONE_NAV_DIRECTION_DEFAULT : KEY_DAILY_REQUIRED_ONE_NAV_DIRECTION_REVERSED);
    }

    /* access modifiers changed from: 0000 */
    public void recordOneNavDiscoveryShowHint() {
        LOGGER.mo11957d("recordOneNavDiscoveryShowHint");
        CheckinData createCheckinDataBase = createCheckinDataBase();
        createCheckinDataBase.setValue(KEY_INSTANCE_ONE_NAV_DISCOVERY_HINT_SHOW, true);
        publishCheckinData(createCheckinDataBase);
    }

    /* access modifiers changed from: 0000 */
    public void recordOneNavDiscoveryClickHint() {
        LOGGER.mo11957d("recordOneNavDiscoveryClickHint");
        CheckinData createCheckinDataBase = createCheckinDataBase();
        createCheckinDataBase.setValue(KEY_INSTANCE_ONE_NAV_DISCOVERY_HINT_CLICK, true);
        publishCheckinData(createCheckinDataBase);
    }

    /* access modifiers changed from: 0000 */
    public void recordOneNavDiscoveryDismissHalfSheet() {
        LOGGER.mo11957d("recordOneNavDiscoveryDismissHalfSheet");
        CheckinData createCheckinDataBase = createCheckinDataBase();
        createCheckinDataBase.setValue(KEY_INSTANCE_ONE_NAV_DISCOVERY_HALF_DISMISS, true);
        publishCheckinData(createCheckinDataBase);
    }

    /* access modifiers changed from: 0000 */
    public void recordOneNavDiscoveryLearnMoreHalfSheet() {
        LOGGER.mo11957d("recordOneNavDiscoveryLearnMoreHalfSheet");
        CheckinData createCheckinDataBase = createCheckinDataBase();
        createCheckinDataBase.setValue(KEY_INSTANCE_ONE_NAV_DISCOVERY_HALF_LEARN_MORE, true);
        publishCheckinData(createCheckinDataBase);
    }

    /* access modifiers changed from: 0000 */
    public void recordOneNavDiscoveryDismissFullSheet() {
        LOGGER.mo11957d("recordOneNavDiscoveryDismissFullSheet");
        CheckinData createCheckinDataBase = createCheckinDataBase();
        createCheckinDataBase.setValue(KEY_INSTANCE_ONE_NAV_DISCOVERY_FULL_NO_THANKS, true);
        publishCheckinData(createCheckinDataBase);
    }

    /* access modifiers changed from: 0000 */
    public void recordOneNavDiscoveryTryItOutFullSheet() {
        LOGGER.mo11957d("recordOneNavDiscoveryTryItOutFullSheet");
        CheckinData createCheckinDataBase = createCheckinDataBase();
        createCheckinDataBase.setValue(KEY_INSTANCE_ONE_NAV_DISCOVERY_FULL_TRY_IT, true);
        publishCheckinData(createCheckinDataBase);
    }
}
