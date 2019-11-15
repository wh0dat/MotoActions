package com.motorola.actions.ftm.instrumentation;

import android.app.NotificationManager;
import android.app.NotificationManager.Policy;
import android.text.TextUtils;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.checkin.CheckinAlarm;
import com.motorola.actions.utils.DisplayUtils;
import com.motorola.actions.utils.MALogger;
import java.util.ArrayList;

public class FlipToMuteInstrumentation {
    private static final MALogger LOGGER = new MALogger(FlipToMuteInstrumentation.class);
    private static final int POLICY_CATEGORY_CALLS = 8;
    private static final int POLICY_CATEGORY_EVENTS = 2;
    private static final int POLICY_CATEGORY_MESSAGES = 4;
    private static final int POLICY_CATEGORY_REMINDERS = 1;
    private static final int POLICY_CATEGORY_REPEAT_CALLERS = 16;

    public static void recordTotalDuration(int i) {
        if (DisplayUtils.isLidClosed()) {
            getFlipToMuteAnalytics().recordDailyTotalDurationClosedLid(i);
        } else {
            getFlipToMuteAnalytics().recordDailyTotalDuration(i);
        }
    }

    public static void publishEvents(int i) {
        NotificationManager notificationManager = (NotificationManager) ActionsApplication.getAppContext().getSystemService("notification");
        if (notificationManager != null) {
            try {
                String buildPolicyState = buildPolicyState(notificationManager.getNotificationPolicy());
                if (DisplayUtils.isLidClosed()) {
                    getFlipToMuteAnalytics().publishAfterEventEndedClosedLid(buildPolicyState, i);
                } else {
                    getFlipToMuteAnalytics().publishAfterEventEnded(buildPolicyState, i);
                }
            } catch (SecurityException e) {
                LOGGER.mo11959e(e.toString());
            }
        } else {
            LOGGER.mo11959e("Could not retrieve notification manager.");
        }
    }

    public static void recordOptionSelected(int i) {
        FlipToMuteAnalytics flipToMuteAnalytics = getFlipToMuteAnalytics();
        switch (i) {
            case 2:
                flipToMuteAnalytics.recordDailyPriorityOnlySelected();
                return;
            case 3:
                flipToMuteAnalytics.recordDailyTotalSilenceSelected();
                return;
            case 4:
                flipToMuteAnalytics.recordDailyAlarmsOnlySelected();
                return;
            default:
                LOGGER.mo11961i("No valid option selected to record.");
                return;
        }
    }

    private static FlipToMuteAnalytics getFlipToMuteAnalytics() {
        return (FlipToMuteAnalytics) CheckinAlarm.getInstance().getAnalytics(FlipToMuteAnalytics.class);
    }

    private static String buildPolicyState(Policy policy) {
        int i = policy.priorityCategories;
        ArrayList arrayList = new ArrayList();
        if ((i & 8) != 0) {
            arrayList.add("CALLS");
        }
        if ((i & 2) != 0) {
            arrayList.add("EVENTS");
        }
        if ((i & 4) != 0) {
            arrayList.add("MESSAGES");
        }
        if ((i & 1) != 0) {
            arrayList.add("REMINDERS");
        }
        if ((i & 16) != 0) {
            arrayList.add("REPEAT_CALLERS");
        }
        return TextUtils.join(",", arrayList);
    }
}
