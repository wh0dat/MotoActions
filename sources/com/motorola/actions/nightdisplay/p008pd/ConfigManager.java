package com.motorola.actions.nightdisplay.p008pd;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.nightdisplay.common.Persistence;
import com.motorola.actions.nightdisplay.p009pi.PIEvent;
import com.motorola.actions.nightdisplay.p009pi.PIEvent.Event;
import com.motorola.actions.sleepPattern.SleepPatternService;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.nightdisplay.pd.ConfigManager */
class ConfigManager implements OnSharedPreferenceChangeListener {
    private static final MALogger LOGGER = new MALogger(ConfigManager.class);
    private final PdHandlerThread mPdHandlerThread;

    ConfigManager(PdHandlerThread pdHandlerThread) {
        this.mPdHandlerThread = pdHandlerThread;
    }

    public void register() {
        SharedPreferenceManager.registerOnSharedPreferenceChangeListener(this);
    }

    public void unregister() {
        SharedPreferenceManager.unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onSharedPreferenceChanged: ");
        sb.append(str);
        mALogger.mo11957d(sb.toString());
        if (Persistence.KEY_NIGHT_DISPLAY_MODE.equals(str) || Persistence.KEY_NIGHT_DISPLAY_INITIATION_CUSTOM_HOUR.equals(str) || Persistence.KEY_NIGHT_DISPLAY_TERMINATION_CUSTOM_HOUR.equals(str) || (Persistence.getMode(SleepPatternService.getDefaultState()) == 4 && str.matches("sleep_pattern_preview_[1234]"))) {
            this.mPdHandlerThread.event(new PIEvent(Event.CONFIGURATION_CHANGED));
        }
    }
}
