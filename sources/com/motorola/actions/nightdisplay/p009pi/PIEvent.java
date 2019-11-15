package com.motorola.actions.nightdisplay.p009pi;

import android.os.Bundle;
import java.util.Calendar;

/* renamed from: com.motorola.actions.nightdisplay.pi.PIEvent */
public class PIEvent {
    private final Bundle mBundle;
    private final Event mEvent;
    private final Calendar mTimeStamp;

    /* renamed from: com.motorola.actions.nightdisplay.pi.PIEvent$Event */
    public enum Event {
        INIT,
        SCREEN_ON,
        SCREEN_OFF,
        TIME_CHANGED,
        DESTROY,
        WAKE_UP_ALARM_UPDATE,
        CONFIGURATION_CHANGED,
        SERVICE_PAUSED,
        SERVICE_RESUMED,
        USER_CHANGED,
        PREVIEW_UPDATE,
        UNLOCKED,
        TIME_CONFIG_CHANGED
    }

    public PIEvent(Event event, Calendar calendar) {
        this.mTimeStamp = calendar;
        this.mEvent = event;
        this.mBundle = null;
    }

    public PIEvent(Event event) {
        this.mTimeStamp = Calendar.getInstance();
        this.mEvent = event;
        this.mBundle = null;
    }

    public PIEvent(Event event, Bundle bundle) {
        this.mTimeStamp = Calendar.getInstance();
        this.mEvent = event;
        this.mBundle = bundle;
    }

    public Calendar getTimeStamp() {
        return this.mTimeStamp;
    }

    public Event getEvent() {
        return this.mEvent;
    }

    public Bundle getBundle() {
        return this.mBundle;
    }
}
