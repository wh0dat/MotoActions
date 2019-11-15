package com.motorola.actions.onenav.instrumentation;

import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.onenav.OneNavHelper;
import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.utils.MALogger;

public final class SoftOneNavInstrumentationObserver extends ContentObserver {
    public static final String KEY_SOFT_ONENAV_LAST_STATE = "soft_onenav_last_state";
    private static final MALogger LOGGER = new MALogger(SoftOneNavInstrumentationObserver.class);
    private static SoftOneNavInstrumentationObserver sSoftOneNavInstrumentationObserver;

    private SoftOneNavInstrumentationObserver(Handler handler) {
        super(handler);
    }

    public static synchronized void registerObserver() {
        synchronized (SoftOneNavInstrumentationObserver.class) {
            boolean z = MotorolaSettings.getSoftOneNavDiscovery() == 0;
            if (OneNavHelper.isSoftOneNav() && z && sSoftOneNavInstrumentationObserver == null) {
                LOGGER.mo11957d("Start observing SoftOneNav Discovery status");
                sSoftOneNavInstrumentationObserver = new SoftOneNavInstrumentationObserver(new Handler(Looper.getMainLooper()));
                ActionsApplication.getAppContext().getContentResolver().registerContentObserver(MotorolaSettings.getSoftOneNavDiscoveryUri(), false, sSoftOneNavInstrumentationObserver);
            }
        }
    }

    public static synchronized void unregisterObserver() {
        synchronized (SoftOneNavInstrumentationObserver.class) {
            LOGGER.mo11957d("Stop observing SoftOneNav Discovery status");
            if (sSoftOneNavInstrumentationObserver != null) {
                ActionsApplication.getAppContext().getContentResolver().unregisterContentObserver(sSoftOneNavInstrumentationObserver);
                sSoftOneNavInstrumentationObserver = null;
            }
        }
    }

    public void onChange(boolean z) {
        int i = SharedPreferenceManager.getInt(KEY_SOFT_ONENAV_LAST_STATE, 0);
        int softOneNavDiscovery = MotorolaSettings.getSoftOneNavDiscovery();
        SharedPreferenceManager.putInt(KEY_SOFT_ONENAV_LAST_STATE, softOneNavDiscovery);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Discovery status changed to: ");
        sb.append(softOneNavDiscovery);
        sb.append(" , lastDiscoveryStatus = ");
        sb.append(i);
        mALogger.mo11957d(sb.toString());
        if (softOneNavDiscovery != i) {
            switch (softOneNavDiscovery) {
                case 1:
                    OneNavInstrumentation.recordOneNavDiscoveryShowHint();
                    return;
                case 2:
                    if (i == 4) {
                        OneNavInstrumentation.recordOneNavDiscoveryDismissHalfSheet();
                    }
                    unregisterObserver();
                    return;
                case 3:
                    if (i == 4) {
                        OneNavInstrumentation.recordOneNavDiscoveryLearnMoreHalfSheet();
                    }
                    unregisterObserver();
                    return;
                case 4:
                    OneNavInstrumentation.recordOneNavDiscoveryClickHint();
                    return;
                default:
                    return;
            }
        }
    }
}
