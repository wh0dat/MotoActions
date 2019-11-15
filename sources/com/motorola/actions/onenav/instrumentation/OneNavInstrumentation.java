package com.motorola.actions.onenav.instrumentation;

import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.checkin.CheckinAlarm;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.utils.MALogger;

public class OneNavInstrumentation {
    public static final String LAST_TUTORIAL_STEP_CODE_PREFERENCE_KEY = "one_nav_tutorial_last_step_code";
    private static final MALogger LOGGER = new MALogger(OneNavInstrumentation.class);

    private static OneNavAnalytics getOneNavAnalytics() {
        return (OneNavAnalytics) CheckinAlarm.getInstance().getAnalytics(OneNavAnalytics.class);
    }

    public static synchronized void recordOneNavEnableStatusChanged(boolean z) {
        synchronized (OneNavInstrumentation.class) {
            OneNavAnalytics oneNavAnalytics = getOneNavAnalytics();
            if (z) {
                LOGGER.mo11957d("record daily event for one nav enabled");
                oneNavAnalytics.recordOneNavEnableDailyEvent();
            } else {
                LOGGER.mo11957d("record daily event for one nav disabled");
                oneNavAnalytics.recordOneNavDisableDailyEvent();
            }
            if (oneNavAnalytics.getDailyEnabledDisabledCount(z) <= 250) {
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("record instance of one nav for enable status changed. isEnabled = ");
                sb.append(z);
                mALogger.mo11957d(sb.toString());
                oneNavAnalytics.recordInstanceOneNavEnableStatusChanged(z);
            } else {
                LOGGER.mo11963w("Not recording instance of one nav for enable status changed. #Instances exceeds limit");
            }
        }
    }

    public static synchronized void recordOneNavTutorialStep(InstrumentationTutorialStep instrumentationTutorialStep) {
        synchronized (OneNavInstrumentation.class) {
            InstrumentationTutorialStep fromCode = InstrumentationTutorialStep.fromCode(SharedPreferenceManager.getString(LAST_TUTORIAL_STEP_CODE_PREFERENCE_KEY, ""));
            int i = -1;
            if (fromCode != null) {
                i = fromCode.ordinal();
            }
            if (instrumentationTutorialStep.ordinal() > i) {
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("record daily event for one nav tutorial step. Tutorial step = ");
                sb.append(instrumentationTutorialStep.toString());
                mALogger.mo11957d(sb.toString());
                SharedPreferenceManager.putString(LAST_TUTORIAL_STEP_CODE_PREFERENCE_KEY, instrumentationTutorialStep.getCode());
            }
        }
    }

    public static synchronized void recordOneNavEnableVibrationDailyEvent(boolean z) {
        synchronized (OneNavInstrumentation.class) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("record OneNav vibration event, enable = ");
            sb.append(z);
            mALogger.mo11957d(sb.toString());
            getOneNavAnalytics().recordOneNavEnableVibrationDailyEvent(z);
        }
    }

    public static synchronized void recordOneNavSwitchDirectionDailyEvent(boolean z) {
        synchronized (OneNavInstrumentation.class) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("record OneNav switch direction event, isDefault = ");
            sb.append(z);
            mALogger.mo11957d(sb.toString());
            getOneNavAnalytics().recordOneNavSwitchDirectionDailyEvent(z);
        }
    }

    public static synchronized void recordOneNavFirstEnableEvent(boolean z, String str) {
        synchronized (OneNavInstrumentation.class) {
            if (CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_MOTO_APP.equals(str)) {
                if (z) {
                    if (!SharedPreferenceManager.contains("fstOn_m")) {
                        getOneNavAnalytics().recordInstanceOneNavFirstEnableMotoApp();
                        SharedPreferenceManager.putBoolean("fstOn_m", true);
                    }
                } else if (!SharedPreferenceManager.contains("fstOff_m")) {
                    getOneNavAnalytics().recordInstanceOneNavFirstDisableMotoApp();
                    SharedPreferenceManager.putBoolean("fstOff_m", true);
                }
            } else if (CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_SETTINGS.equals(str)) {
                if (z) {
                    if (!SharedPreferenceManager.contains("fstOn_s")) {
                        getOneNavAnalytics().recordInstanceOneNavFirstEnableSettings();
                        SharedPreferenceManager.putBoolean("fstOn_s", true);
                    }
                } else if (!SharedPreferenceManager.contains("fstOff_s")) {
                    getOneNavAnalytics().recordInstanceOneNavFirstDisableSettings();
                    SharedPreferenceManager.putBoolean("fstOff_s", true);
                }
            }
        }
    }

    public static synchronized void recordOneNavDiscoveryDismissHalfSheet() {
        synchronized (OneNavInstrumentation.class) {
            getOneNavAnalytics().recordOneNavDiscoveryDismissHalfSheet();
        }
    }

    public static synchronized void recordOneNavDiscoveryDismissFullSheet() {
        synchronized (OneNavInstrumentation.class) {
            getOneNavAnalytics().recordOneNavDiscoveryDismissFullSheet();
        }
    }

    public static synchronized void recordOneNavDiscoveryShowHint() {
        synchronized (OneNavInstrumentation.class) {
            getOneNavAnalytics().recordOneNavDiscoveryShowHint();
        }
    }

    public static synchronized void recordOneNavDiscoveryClickHint() {
        synchronized (OneNavInstrumentation.class) {
            getOneNavAnalytics().recordOneNavDiscoveryClickHint();
        }
    }

    public static synchronized void recordOneNavDiscoveryLearnMoreHalfSheet() {
        synchronized (OneNavInstrumentation.class) {
            getOneNavAnalytics().recordOneNavDiscoveryLearnMoreHalfSheet();
        }
    }

    public static synchronized void recordOneNavDiscoveryTryItOutFullSheet() {
        synchronized (OneNavInstrumentation.class) {
            getOneNavAnalytics().recordOneNavDiscoveryTryItOutFullSheet();
        }
    }
}
