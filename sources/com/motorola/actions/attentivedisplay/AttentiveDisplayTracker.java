package com.motorola.actions.attentivedisplay;

import android.content.SharedPreferences;
import android.os.SystemClock;
import com.motorola.actions.SharedPreferenceManager;

public final class AttentiveDisplayTracker {
    private static final String PREFERENCES_TRACKER = "attentivedisplay_tracker";
    private static final String PREFERENCE_INACTIVITY_LAST_TIME = "inactivity_last_time";
    private static final String PREFERENCE_INACTIVITY_START_TIME = "inactivity_start_time";
    private static AttentiveDisplayTracker sInstance;
    private long mInactivityLastTime;
    private long mInactivityStartTime;
    private SharedPreferences mPreferences = SharedPreferenceManager.getSharedPreferences(PREFERENCES_TRACKER);

    private AttentiveDisplayTracker() {
        if (this.mPreferences != null) {
            this.mInactivityStartTime = this.mPreferences.getLong(PREFERENCE_INACTIVITY_START_TIME, 0);
            this.mInactivityLastTime = this.mPreferences.getLong(PREFERENCE_INACTIVITY_LAST_TIME, 0);
        }
    }

    public static synchronized AttentiveDisplayTracker getInstance() {
        AttentiveDisplayTracker attentiveDisplayTracker;
        synchronized (AttentiveDisplayTracker.class) {
            if (sInstance == null) {
                sInstance = new AttentiveDisplayTracker();
            }
            attentiveDisplayTracker = sInstance;
        }
        return attentiveDisplayTracker;
    }

    public void resetInactivityTimeout() {
        this.mInactivityStartTime = SystemClock.uptimeMillis();
        this.mInactivityLastTime = this.mInactivityStartTime;
        if (this.mPreferences != null) {
            this.mPreferences.edit().putLong(PREFERENCE_INACTIVITY_START_TIME, this.mInactivityStartTime).putLong(PREFERENCE_INACTIVITY_LAST_TIME, this.mInactivityLastTime).apply();
        }
    }

    public long getInactivityStartTime() {
        return this.mInactivityStartTime;
    }

    public long getInactivityLastTime() {
        return this.mInactivityLastTime;
    }

    public void setInactivityLastTime(long j) {
        this.mInactivityLastTime = j;
        if (this.mPreferences != null) {
            this.mPreferences.edit().putLong(PREFERENCE_INACTIVITY_LAST_TIME, this.mInactivityLastTime).apply();
        }
    }
}
