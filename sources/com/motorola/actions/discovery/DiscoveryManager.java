package com.motorola.actions.discovery;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.discovery.fdn.FDNManager;
import com.motorola.actions.discovery.fdn.NotificationReceiver;
import com.motorola.actions.discovery.fdn.attentivedisplay.AttentiveDisplayFDNManager;
import com.motorola.actions.discovery.fdn.foc.FlashOnChopFDNManager;
import com.motorola.actions.discovery.fdn.ftm.FlipToDNDFDNManager;
import com.motorola.actions.discovery.fdn.lts.LiftToSilenceFDNManager;
import com.motorola.actions.discovery.fdn.mediacontrol.MediaControlFDNManager;
import com.motorola.actions.discovery.fdn.microscreen.MicroscreenFDNManager;
import com.motorola.actions.discovery.fdn.nightdisplay.NightDisplayFDNManager;
import com.motorola.actions.discovery.fdn.nightdisplay.sleeppattern.SleepPatternFDNManager;
import com.motorola.actions.discovery.fdn.onenav.SoftOneNavFDNManager;
import com.motorola.actions.discovery.fdn.p007qc.QuickCaptureFDNManager;
import com.motorola.actions.discovery.fdn.quickscreenshot.QuickScreenshotFDNManager;
import com.motorola.actions.discovery.highlight.FeatureHighlightKey;
import com.motorola.actions.discovery.highlight.FeatureHighlightManager;
import com.motorola.actions.discovery.highlight.onenav.SoftOneNavHighlightManager;
import com.motorola.actions.ftm.FlipToMuteService;
import com.motorola.actions.onenav.ScreenLockNotificationManager;
import com.motorola.actions.p013ui.ActionsBaseActivity;
import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.settings.provider.ActionsSettingsProvider;
import com.motorola.actions.utils.Constants;
import com.motorola.actions.utils.DemoModeUtils;
import com.motorola.actions.utils.Device;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.MotoAppUtils;
import com.motorola.actions.utils.MotoDisplayUtils;
import com.motorola.actions.utils.RunningTasksUtils;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class DiscoveryManager {
    private static final int DISCOVERY_HIGHLIGHTED = 1;
    public static final int DISCOVERY_IGNORED = 0;
    private static final Set<FeatureKey> DISCOVERY_IGNORE_LIST = EnumSet.of(FeatureKey.ATTENTIVE_DISPLAY);
    public static final String DISCOVERY_STATUS_PREFERENCE = "discovery_status_preference";
    private static final long FIVE_DAYS_IN_MILLIS = TimeUnit.DAYS.toMillis(5);
    private static final long FOUR_DAYS_IN_MILLIS = TimeUnit.DAYS.toMillis(4);
    private static final int GENERIC_TRIGGER_ID = 0;
    public static final String KEY_FDN_DELAY_MODE = "key_fdn_delay_mode";
    public static final String KEY_TIME_SINCE_ACTIVATION = "key_time_since_activation";
    private static final MALogger LOGGER = new MALogger(DiscoveryManager.class);
    private static final int SEVEN_DAYS = 7;
    private EnumMap<FeatureHighlightKey, FeatureHighlightManager> mAllHighlightManagers;
    private EnumMap<FeatureKey, FDNManager> mAllManagers;
    private final EnumMap<FeatureKey, FDNManager> mFDNManagers = new EnumMap<>(FeatureKey.class);
    private final EnumMap<FeatureHighlightKey, FeatureHighlightManager> mFeatureDiscoveryManagers = new EnumMap<>(FeatureHighlightKey.class);
    private boolean mHasOneFDNLaunched;
    private NotificationReceiver mNotificationReceiver;
    private long mPreviousElapsed;

    public enum FDNDelayMode {
        NORMAL,
        DEBUG
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DiscoveryManager INSTANCE = new DiscoveryManager();

        private SingletonHolder() {
        }
    }

    public EnumMap<FeatureKey, FDNManager> getAllFDNManagers() {
        if (this.mAllManagers == null) {
            this.mAllManagers = new EnumMap<>(FeatureKey.class);
            this.mAllManagers.put(FeatureKey.QUICK_CAPTURE, new QuickCaptureFDNManager());
            this.mAllManagers.put(FeatureKey.QUICK_SCREENSHOT, new QuickScreenshotFDNManager());
            this.mAllManagers.put(FeatureKey.FLASH_ON_CHOP, new FlashOnChopFDNManager());
            this.mAllManagers.put(FeatureKey.MICROSCREEN, new MicroscreenFDNManager());
            this.mAllManagers.put(FeatureKey.PICKUP_TO_STOP_RINGING, new LiftToSilenceFDNManager());
            this.mAllManagers.put(FeatureKey.FLIP_TO_DND, new FlipToDNDFDNManager());
            this.mAllManagers.put(FeatureKey.ATTENTIVE_DISPLAY, new AttentiveDisplayFDNManager());
            this.mAllManagers.put(FeatureKey.NIGHT_DISPLAY, new NightDisplayFDNManager());
            this.mAllManagers.put(FeatureKey.ONE_NAV, new SoftOneNavFDNManager());
            this.mAllManagers.put(FeatureKey.MEDIA_CONTROL, new MediaControlFDNManager());
            this.mAllManagers.put(FeatureKey.SLEEP_PATTERN, new SleepPatternFDNManager());
        }
        return this.mAllManagers;
    }

    private EnumMap<FeatureHighlightKey, FeatureHighlightManager> getAllFeatureDiscoveryManagers() {
        if (this.mAllHighlightManagers == null) {
            this.mAllHighlightManagers = new EnumMap<>(FeatureHighlightKey.class);
            this.mAllHighlightManagers.put(FeatureHighlightKey.SOFTONENAV, new SoftOneNavHighlightManager());
        }
        return this.mAllHighlightManagers;
    }

    private boolean isMotoExperiencesForeground() {
        final Context appContext = ActionsApplication.getAppContext();
        String topTaskPackageName = RunningTasksUtils.getTopTaskPackageName();
        C05411 r2 = new ArrayList<String>() {
            {
                add(MotoAppUtils.MOTO_PACKAGE_NAME);
                add(appContext.getPackageName());
                add(MotoDisplayUtils.MOTO_DISPLAY_PACKAGE_NAME);
                add("com.motorola.motokey");
                add(FlipToMuteService.AOV_PACKAGE);
            }
        };
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isMotoExperiencesForeground(): topTask = ");
        sb.append(topTaskPackageName);
        mALogger.mo11957d(sb.toString());
        return !TextUtils.isEmpty(topTaskPackageName) && r2.contains(topTaskPackageName);
    }

    private boolean isValidTrigger(FDNManager fDNManager, int i) {
        boolean shouldIgnoreFdnDaysCount = fDNManager.shouldIgnoreFdnDaysCount(i);
        boolean z = false;
        if (shouldIgnoreFdnDaysCount || getDaysSinceActivation() > 7) {
            boolean isTopTaskPackageName = RunningTasksUtils.isTopTaskPackageName(ActionsApplication.getAppContext().getPackageName());
            boolean z2 = SharedPreferenceManager.getBoolean(ActionsBaseActivity.KEY_ACTIONS_ACTIVITY_FOCUS, false);
            if (!isMotoExperiencesForeground() || (!z2 && isTopTaskPackageName)) {
                z = true;
            }
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("isValidTrigger(): isMotoActionsInForeground = ");
            sb.append(isTopTaskPackageName);
            sb.append(", hasFocus = ");
            sb.append(z2);
            sb.append(", is valid = ");
            sb.append(z);
            mALogger.mo11957d(sb.toString());
        } else {
            LOGGER.mo11957d("isValidTrigger: false - Not valid period to trigger FDNs");
        }
        return z;
    }

    private void setElapsedTimeSinceActivation(long j) {
        SharedPreferenceManager.putLong(KEY_TIME_SINCE_ACTIVATION, j);
    }

    private void incrementTimeSinceActivation(long j) {
        long elapsedTimeSinceActivationInMs = getElapsedTimeSinceActivationInMs();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("incrementTimeSinceActivation - current days saved: ");
        sb.append(TimeUnit.MILLISECONDS.toDays(elapsedTimeSinceActivationInMs));
        sb.append(" - incrementTime: ");
        sb.append(j);
        mALogger.mo11957d(sb.toString());
        if (getFDNDelayMode() != FDNDelayMode.NORMAL.ordinal()) {
            j = FOUR_DAYS_IN_MILLIS;
        }
        long j2 = elapsedTimeSinceActivationInMs + j;
        MALogger mALogger2 = LOGGER;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("incrementTimeSinceActivation - incrementing days: ");
        sb2.append(TimeUnit.MILLISECONDS.toDays(j));
        sb2.append(" - Total: ");
        sb2.append(TimeUnit.MILLISECONDS.toDays(j2));
        mALogger2.mo11957d(sb2.toString());
        setElapsedTimeSinceActivation(j2);
    }

    private void showPreviousFDN() {
        for (Entry entry : this.mFDNManagers.entrySet()) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("showPreviousFDN for ");
            sb.append(entry.getKey());
            mALogger.mo11957d(sb.toString());
            ((FDNManager) entry.getValue()).showFDNAgain();
        }
    }

    public static DiscoveryManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void onFDNEvent(FeatureKey featureKey) {
        onFDNEvent(featureKey, 0);
    }

    public void onFDNEvent(FeatureKey featureKey, int i) {
        if (this.mFDNManagers.containsKey(featureKey)) {
            FDNManager fDNManager = (FDNManager) this.mFDNManagers.get(featureKey);
            if (this.mHasOneFDNLaunched || !isValidTrigger(fDNManager, i)) {
                fDNManager.onInvalidTrigger();
                return;
            }
            setDiscoveryStatus(featureKey, 1);
            fDNManager.onEvent();
        }
    }

    public void onHighlightEvent(FeatureHighlightKey featureHighlightKey) {
        if (this.mFeatureDiscoveryManagers.containsKey(featureHighlightKey)) {
            ((FeatureHighlightManager) this.mFeatureDiscoveryManagers.get(featureHighlightKey)).onEvent();
        }
    }

    public void reset() {
        for (Entry entry : getAllFDNManagers().entrySet()) {
            ((FDNManager) entry.getValue()).resetFDN();
            ((FDNManager) entry.getValue()).setFDNDismissed();
        }
        for (Entry entry2 : getAllFeatureDiscoveryManagers().entrySet()) {
            ((FeatureHighlightManager) entry2.getValue()).resetHighlight();
            ((FeatureHighlightManager) entry2.getValue()).setHighlightReady(false);
        }
    }

    public void registerFDNListeners() {
        if (!DemoModeUtils.isDemoModeEnabled()) {
            for (Entry entry : getAllFDNManagers().entrySet()) {
                if (((FDNManager) entry.getValue()).registerTriggerReceiver() || ((FDNManager) entry.getValue()).isFDNVisible()) {
                    MALogger mALogger = LOGGER;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Registering FDN manager:");
                    sb.append(entry.getKey());
                    mALogger.mo11957d(sb.toString());
                    this.mFDNManagers.put((Enum) entry.getKey(), entry.getValue());
                }
            }
            for (Entry entry2 : getAllFeatureDiscoveryManagers().entrySet()) {
                if (((FeatureHighlightManager) entry2.getValue()).registerTriggerReceiver()) {
                    MALogger mALogger2 = LOGGER;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Registering FD manager:");
                    sb2.append(entry2.getKey());
                    mALogger2.mo11957d(sb2.toString());
                    this.mFeatureDiscoveryManagers.put((Enum) entry2.getKey(), entry2.getValue());
                }
            }
        }
    }

    public void unregisterFDNListeners() {
        LOGGER.mo11957d("unregisterFDNListeners");
        for (Entry value : this.mFDNManagers.entrySet()) {
            ((FDNManager) value.getValue()).unregisterTriggerReceiver();
        }
        for (Entry value2 : this.mFeatureDiscoveryManagers.entrySet()) {
            ((FeatureHighlightManager) value2.getValue()).unregisterTriggerReceiver();
        }
    }

    public void cancelFDN(FeatureKey featureKey) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("cancelFDN: ");
        sb.append(featureKey.toString());
        mALogger.mo11957d(sb.toString());
        if (this.mFDNManagers.containsKey(featureKey)) {
            ((FDNManager) this.mFDNManagers.get(featureKey)).cancelFDN();
            ((FDNManager) this.mFDNManagers.get(featureKey)).setFDNDismissed();
            this.mFDNManagers.remove(featureKey);
        }
    }

    public void cancelHighlight(FeatureHighlightKey featureHighlightKey) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("cancelHighlight: ");
        sb.append(featureHighlightKey.toString());
        mALogger.mo11957d(sb.toString());
        if (this.mFeatureDiscoveryManagers.containsKey(featureHighlightKey)) {
            ((FeatureHighlightManager) this.mFeatureDiscoveryManagers.get(featureHighlightKey)).cancelHighlight();
            this.mFeatureDiscoveryManagers.remove(featureHighlightKey);
        }
    }

    public void registerReceiver(FeatureKey featureKey) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("registerReceiver: ");
        sb.append(featureKey);
        mALogger.mo11957d(sb.toString());
        if (this.mNotificationReceiver == null) {
            this.mNotificationReceiver = new NotificationReceiver();
        }
        this.mNotificationReceiver.register(featureKey);
    }

    public void unregisterReceiver(FeatureKey featureKey) {
        if (this.mNotificationReceiver != null) {
            this.mNotificationReceiver.unregister(featureKey);
        }
    }

    public int getDiscoveryStatus(FeatureKey featureKey) {
        int i = SharedPreferenceManager.getInt(DISCOVERY_STATUS_PREFERENCE, featureKey.name(), featureKey == FeatureKey.NIGHT_DISPLAY ? 1 : 0);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("getDiscoveryStatus: ");
        sb.append(featureKey.toString());
        sb.append(", value = ");
        sb.append(i);
        mALogger.mo11957d(sb.toString());
        return i;
    }

    public void setDiscoveryStatus(FeatureKey featureKey, int i) {
        FeatureKey[] values;
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("setDiscoveryStatus: ");
        sb.append(featureKey.toString());
        sb.append(" - Status: ");
        sb.append(i);
        mALogger.mo11957d(sb.toString());
        if (!DISCOVERY_IGNORE_LIST.contains(featureKey)) {
            if (i == 1) {
                for (FeatureKey featureKey2 : FeatureKey.values()) {
                    if (!(featureKey2 == featureKey || getDiscoveryStatus(featureKey2) == 0)) {
                        SharedPreferenceManager.putInt(DISCOVERY_STATUS_PREFERENCE, featureKey2.name(), 0);
                        ActionsSettingsProvider.notifyChange(featureKey2);
                    }
                }
            }
            if (getDiscoveryStatus(featureKey) != i) {
                SharedPreferenceManager.putInt(DISCOVERY_STATUS_PREFERENCE, featureKey.name(), i);
                ActionsSettingsProvider.notifyChange(featureKey);
                return;
            }
            return;
        }
        MALogger mALogger2 = LOGGER;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("setDiscoveryStatus: ignoring: ");
        sb2.append(featureKey.toString());
        mALogger2.mo11957d(sb2.toString());
    }

    public void start() {
        LOGGER.mo11957d("start");
        SharedPreferenceManager.putBoolean(ActionsBaseActivity.KEY_ACTIONS_ACTIVITY_FOCUS, false);
        registerFDNListeners();
        showPreviousFDN();
        if (MotorolaSettings.fingerPrintLockScreenExists() && !Device.hasBackFPSSensor() && !Device.hasSideFPSSensor() && !SharedPreferenceManager.getBoolean(Constants.FPS_LOCK_SCREEN_SHOWN, false)) {
            ((ActionsApplication) ActionsApplication.getAppContext()).registerActivityLifecycleCallbacks(ScreenLockNotificationManager.getInstance());
        }
        this.mPreviousElapsed = SystemClock.elapsedRealtime();
    }

    private long getDaysSinceActivation() {
        long days = TimeUnit.MILLISECONDS.toDays(getElapsedTimeSinceActivationInMs());
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("getDaysSinceActivation: ");
        sb.append(days);
        mALogger.mo11957d(sb.toString());
        return days;
    }

    private long getElapsedTimeSinceActivationInMs() {
        return SharedPreferenceManager.getLong(KEY_TIME_SINCE_ACTIVATION, 0);
    }

    public void handleFDNDelayCount() {
        LOGGER.mo11957d("handleFDNDelayCount");
        long elapsedRealtime = SystemClock.elapsedRealtime();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("handleFDNDelayCount - currentElapsedTime: ");
        sb.append(elapsedRealtime);
        mALogger.mo11957d(sb.toString());
        long j = elapsedRealtime - this.mPreviousElapsed;
        this.mPreviousElapsed = elapsedRealtime;
        incrementTimeSinceActivation(j);
    }

    public void restartFDNDelay() {
        LOGGER.mo11957d("restartFDNDelay");
        if (getDaysSinceActivation() > 6) {
            LOGGER.mo11957d("restartFDNDelay - setting delay count to 5 days.");
            setElapsedTimeSinceActivation(FIVE_DAYS_IN_MILLIS);
        }
        this.mPreviousElapsed = SystemClock.elapsedRealtime();
    }

    public int getFDNDelayMode() {
        return SharedPreferenceManager.getInt(KEY_FDN_DELAY_MODE, FDNDelayMode.NORMAL.ordinal());
    }

    public void setFDNLaunchedFlag(boolean z) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("setFDNLaunchedFlag: ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        this.mHasOneFDNLaunched = z;
    }
}
