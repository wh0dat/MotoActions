package com.motorola.actions.discovery.fdn.instrumentation;

import android.content.SharedPreferences;
import android.text.TextUtils;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.checkin.CheckinAlarm;
import com.motorola.actions.utils.MALogger;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DiscoveryInstrumentation {
    static final String APPROACH = "APPROACH";
    protected static final String ATTENTIVE_DISPLAY = "AD";
    public static final String FDN_CHANGED_FEATURES = "fdn_changed_features";
    protected static final String FLASH_ON_CHOP = "FOC";
    protected static final String FLIP_TO_DND = "FTDND";
    private static final MALogger LOGGER = new MALogger(DiscoveryInstrumentation.class);
    static final String MEDIA_CONTROL = "MC";
    protected static final String MICROSCREEN = "MS";
    protected static final String NIGHT_DISPLAY = "ND";
    static final String NOT_VALID = "NOT_VAL";
    protected static final String ONE_NAV = "ONE_NAV";
    protected static final String PICKUP_TO_STOP_RINGING = "LTS";
    protected static final String QUICK_CAPTURE = "QC";
    static final String QUICK_SCREENSHOT = "QS";
    private static final String RISE_TO_EAR = "RTE";

    @Retention(RetentionPolicy.SOURCE)
    @interface FeatureName {
    }

    private static DiscoveryAnalytics getDiscoveryAnalytics() {
        DiscoveryAnalytics discoveryAnalytics = (DiscoveryAnalytics) CheckinAlarm.getInstance().getAnalytics(DiscoveryAnalytics.class);
        return discoveryAnalytics == null ? new DiscoveryAnalytics(ActionsApplication.getAppContext()) : discoveryAnalytics;
    }

    private static synchronized void recordFDNEnroll(int i, String str) {
        synchronized (DiscoveryInstrumentation.class) {
            DiscoveryAnalytics discoveryAnalytics = getDiscoveryAnalytics();
            String featureName = getFeatureName(i);
            if (!TextUtils.equals(featureName, NOT_VALID)) {
                discoveryAnalytics.publishInstanceMotFDN(featureName, str);
            } else {
                LOGGER.mo11957d("Clicked FDN for unknown feature.");
            }
        }
    }

    public static synchronized void recordShowFDN(int i) {
        synchronized (DiscoveryInstrumentation.class) {
            recordFDNEnroll(i, "show");
        }
    }

    public static synchronized void recordDismissSwipeFDN(int i) {
        synchronized (DiscoveryInstrumentation.class) {
            recordFDNEnroll(i, "swipe");
        }
    }

    public static synchronized void recordDismissNoThanksFDN(int i) {
        synchronized (DiscoveryInstrumentation.class) {
            recordFDNEnroll(i, "no_thks");
        }
    }

    public static synchronized void recordClickFDN(int i) {
        synchronized (DiscoveryInstrumentation.class) {
            recordFDNEnroll(i, "click");
        }
    }

    public static synchronized void recordTryItNowFDN(int i) {
        synchronized (DiscoveryInstrumentation.class) {
            recordFDNEnroll(i, "try_it");
        }
    }

    public static synchronized void recordChangeFDN(int i) {
        synchronized (DiscoveryInstrumentation.class) {
            DiscoveryAnalytics discoveryAnalytics = getDiscoveryAnalytics();
            String featureName = getFeatureName(i);
            SharedPreferences sharedPreferences = SharedPreferenceManager.getSharedPreferences(FDN_CHANGED_FEATURES);
            if (sharedPreferences != null) {
                if (TextUtils.equals(featureName, NOT_VALID)) {
                    LOGGER.mo11957d("Changed FDN for unknown feature.");
                } else if (!sharedPreferences.contains(featureName)) {
                    discoveryAnalytics.publishInstanceMotFDN(featureName, "change");
                    sharedPreferences.edit().putBoolean(featureName, true).apply();
                } else {
                    MALogger mALogger = LOGGER;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Record change for feature ");
                    sb.append(featureName);
                    sb.append(" already recorded.");
                    mALogger.mo11957d(sb.toString());
                }
            }
        }
    }

    private static String getFeatureName(int i) {
        switch (FeatureKey.getFeatureKey(i)) {
            case QUICK_CAPTURE:
                return QUICK_CAPTURE;
            case APPROACH:
                return APPROACH;
            case FLASH_ON_CHOP:
                return "FOC";
            case RISE_TO_EAR:
                return RISE_TO_EAR;
            case ATTENTIVE_DISPLAY:
                return ATTENTIVE_DISPLAY;
            case MICROSCREEN:
                return MICROSCREEN;
            case PICKUP_TO_STOP_RINGING:
                return PICKUP_TO_STOP_RINGING;
            case FLIP_TO_DND:
                return FLIP_TO_DND;
            case NIGHT_DISPLAY:
                return NIGHT_DISPLAY;
            case ONE_NAV:
                return ONE_NAV;
            case QUICK_SCREENSHOT:
                return QUICK_SCREENSHOT;
            case MEDIA_CONTROL:
                return MEDIA_CONTROL;
            default:
                return NOT_VALID;
        }
    }
}
