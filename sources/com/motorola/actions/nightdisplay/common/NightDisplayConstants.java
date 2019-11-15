package com.motorola.actions.nightdisplay.common;

import java.util.concurrent.TimeUnit;

public class NightDisplayConstants {
    public static final String ACTION_UPDATE_WAKE_UP_ALARM = "action_update_wake_up_alarm";
    private static final int AUTOMATIC_TIME_BEGIN = 22;
    private static final int AUTOMATIC_TIME_END = 8;
    public static final long AUTOMATIC_TIME_MINUTE_BEGIN = TimeUnit.HOURS.toMinutes(22);
    public static final long AUTOMATIC_TIME_MINUTE_END = TimeUnit.HOURS.toMinutes(8);
    static final int AUTOMATIC_TIME_WINDOW_UPDATE = 30;
    public static final int MIN_COLOR_TEMP = 0;
    public static final int SLEEP_DELTA_IN_MINUTES = ((int) TimeUnit.HOURS.toMinutes(2));
    public static final int TYPE_AUTOMATIC = 4;
    public static final int TYPE_MANUAL = 1;
    public static final int WAKE_UP_ALARM_UPDATE_TIME = 12;
}
