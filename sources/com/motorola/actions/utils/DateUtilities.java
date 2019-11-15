package com.motorola.actions.utils;

import android.annotation.SuppressLint;
import android.text.format.DateFormat;
import com.motorola.actions.ActionsApplication;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateUtilities {
    @SuppressLint({"SimpleDateFormat"})
    private static SimpleDateFormat getAndroidDateFormat() {
        return new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), DateFormat.is24HourFormat(ActionsApplication.getAppContext()) ? "HH:mm" : "hh:mm a"));
    }

    public static String getAndroidFormattedTime(Calendar calendar) {
        return getAndroidDateFormat().format(calendar.getTime());
    }

    public static String getHourMinuteFormattedTime(int i) {
        Calendar instance = Calendar.getInstance();
        int i2 = i / 60;
        instance.set(11, i2);
        instance.set(12, i - (i2 * 60));
        return getAndroidFormattedTime(instance);
    }
}
