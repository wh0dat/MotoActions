package com.motorola.actions.quickscreenshot.instrumentation;

import com.motorola.actions.checkin.CheckinAlarm;
import com.motorola.actions.quickscreenshot.ScreenshotReceiver.ScreenshotReceiverCallback;
import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.utils.MotoResearchUtils;
import com.motorola.actions.utils.MotoSurveyUtils;

public class QuickScreenshotInstrumentation {
    /* access modifiers changed from: private */
    public static QuickScreenshotAnalytics getQuickScreenshotAnalytics() {
        return (QuickScreenshotAnalytics) CheckinAlarm.getInstance().getAnalytics(QuickScreenshotAnalytics.class);
    }

    public static ScreenshotReceiverCallback getInstrumentationCallback() {
        return new ScreenshotReceiverCallback() {
            public void run(boolean z) {
                if (z) {
                    QuickScreenshotInstrumentation.getQuickScreenshotAnalytics().recordQuickScreenshotEvent();
                } else {
                    QuickScreenshotInstrumentation.getQuickScreenshotAnalytics().recordComboKeysScreenshotEvent();
                }
                if (MotorolaSettings.isEnhancedScreenshot()) {
                    MotoSurveyUtils.sendIntent(MotoResearchUtils.GESTURE_TYPE_ENHANCED_SCREENSHOT);
                }
            }
        };
    }
}
