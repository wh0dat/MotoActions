package com.motorola.actions.mediacontrol.instrumentation;

import com.motorola.actions.checkin.CheckinAlarm;

public class MediaControlInstrumentation {
    private static MediaControlAnalytics getMediaControlAnalytics() {
        return (MediaControlAnalytics) CheckinAlarm.getInstance().getAnalytics(MediaControlAnalytics.class);
    }

    public static synchronized void recordMusicChangeEvent() {
        synchronized (MediaControlInstrumentation.class) {
            getMediaControlAnalytics().recordMusicChangeEvent();
        }
    }
}
