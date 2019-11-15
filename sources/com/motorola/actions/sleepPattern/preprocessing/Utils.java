package com.motorola.actions.sleepPattern.preprocessing;

import com.motorola.actions.utils.MALogger;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class Utils {
    public static final int END_INDEX = 1;
    public static final String KEY_PREVIEW_BASE = "sleep_pattern_preview_";
    private static final MALogger LOGGER = new MALogger(Utils.class);
    public static final String RETIRE = "retire";
    public static final int START_INDEX = 0;
    public static final String WAKEUP = "wakeup";
    public static final int WEEKEND_RETIRE = 3;
    public static final int WEEKEND_WAKEUP = 4;
    public static final int WEEK_RETIRE = 1;
    public static final int WEEK_WAKEUP = 2;

    @Retention(RetentionPolicy.SOURCE)
    public @interface SleepPatternPreviewType {
    }

    public static List<Calendar> getIntervalSetForCalendar(Calendar calendar) {
        Calendar calendar2 = (Calendar) calendar.clone();
        Calendar calendar3 = (Calendar) calendar.clone();
        if (calendar.get(11) > 12) {
            calendar3.add(5, 1);
        } else {
            calendar2.add(5, -1);
        }
        calendar2.set(11, 18);
        calendar2.set(12, 0);
        calendar2.set(13, 0);
        calendar3.set(11, 12);
        calendar3.set(12, 0);
        calendar3.set(13, 0);
        LinkedList linkedList = new LinkedList();
        linkedList.add(calendar2);
        linkedList.add(calendar3);
        return linkedList;
    }

    public static Calendar calculateStartTime(Calendar calendar) {
        Calendar calendar2 = (Calendar) calendar.clone();
        return !isValidAlarmInterval(calendar) ? setBaseTime(calendar2) : calendar2;
    }

    public static Calendar setBaseTime(Calendar calendar) {
        Calendar calendar2 = (Calendar) calendar.clone();
        calendar2.set(11, 18);
        calendar2.set(12, 0);
        calendar2.set(13, 0);
        return calendar2;
    }

    public static boolean isWeekend(Calendar calendar) {
        if ((calendar.get(7) == 6 && calendar.get(11) >= 12) || calendar.get(7) == 7) {
            return true;
        }
        if (calendar.get(7) != 1 || calendar.get(11) >= 18) {
            return false;
        }
        return true;
    }

    public static boolean isValidAlarmInterval(Calendar calendar) {
        boolean z = calendar.get(11) >= 18 || calendar.get(11) < 12;
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isValidAlarmInterval: ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        return z;
    }
}
