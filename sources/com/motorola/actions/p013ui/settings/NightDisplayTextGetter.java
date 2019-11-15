package com.motorola.actions.p013ui.settings;

import android.content.Context;
import android.support.p001v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.nightdisplay.common.Persistence;
import com.motorola.actions.nightdisplay.common.SleepPatternAccess;
import com.motorola.actions.sleepPattern.preprocessing.Utils;
import com.motorola.actions.utils.DateUtilities;
import java.util.Calendar;

/* renamed from: com.motorola.actions.ui.settings.NightDisplayTextGetter */
class NightDisplayTextGetter {
    private static int getFallbackTextId(boolean z) {
        if (z) {
            return C0504R.string.night_display_fallback_sleep_both_info;
        }
        return 0;
    }

    NightDisplayTextGetter() {
    }

    static String getFallbackText(boolean z) {
        int fallbackTextId = getFallbackTextId(z);
        return fallbackTextId != 0 ? ActionsApplication.getAppContext().getString(fallbackTextId) : "";
    }

    static String getStartText(Calendar calendar, int i) {
        StringBuilder sb = new StringBuilder();
        if (i == 1) {
            sb.append(DateUtilities.getHourMinuteFormattedTime(Persistence.getInitialTimeInMinutes()));
        } else if (i == 4) {
            sb.append(getTextSleepBased(calendar, Utils.RETIRE));
        }
        return sb.toString();
    }

    static String getEndText(Calendar calendar, int i) {
        StringBuilder sb = new StringBuilder();
        if (i == 1) {
            sb.append(DateUtilities.getHourMinuteFormattedTime(Persistence.getTerminationTimeInMinutes()));
        } else if (i == 4) {
            sb.append(getTextSleepBased(calendar, Utils.WAKEUP));
        }
        return sb.toString();
    }

    private static String getTextSleepBased(Calendar calendar, String str) {
        return DateUtilities.getHourMinuteFormattedTime(SleepPatternAccess.getSleepPatternTimeInMinutes(calendar, str));
    }

    static Spanned getCustomTimeSpannedText(String str, int i) {
        Context appContext = ActionsApplication.getAppContext();
        return Html.fromHtml(appContext.getString(C0504R.string.night_display_custom_time, new Object[]{String.valueOf(ContextCompat.getColor(appContext, i)), str}), 0);
    }
}
