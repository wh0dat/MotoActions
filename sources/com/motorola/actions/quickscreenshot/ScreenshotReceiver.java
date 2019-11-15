package com.motorola.actions.quickscreenshot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.utils.MALogger;

public class ScreenshotReceiver extends BroadcastReceiver {
    private static final String ACTION_SHOW_FDN = "com.motorola.actions.quickScreenshot.ACTION_SHOW_FDN";
    private static final String IS_QUICK_SCREENSHOT_TYPE_FLAG = "com.motorola.actions.IS_QUICK_SCREENSHOT_TYPE";
    private static final MALogger LOGGER = new MALogger(ScreenshotReceiver.class);
    private static final String MEDIA_CONTROL_PERMISSION = "android.permission.MEDIA_CONTENT_CONTROL";
    private final ScreenshotReceiverCallback mCallback;
    private boolean mRegistered;

    public interface ScreenshotReceiverCallback {
        void run(boolean z);
    }

    public ScreenshotReceiver(ScreenshotReceiverCallback screenshotReceiverCallback) {
        this.mCallback = screenshotReceiverCallback;
    }

    public void onReceive(Context context, Intent intent) {
        if (intent != null && ACTION_SHOW_FDN.equals(intent.getAction())) {
            LOGGER.mo11957d("ACTION_SHOW_FDN intent");
            this.mCallback.run(intent.getBooleanExtra(IS_QUICK_SCREENSHOT_TYPE_FLAG, false));
        }
    }

    public void register() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("register - mRegistered: ");
        sb.append(this.mRegistered);
        mALogger.mo11957d(sb.toString());
        if (!this.mRegistered && QuickScreenshotHelper.isFeatureSupported()) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ACTION_SHOW_FDN);
            ActionsApplication.getAppContext().registerReceiver(this, intentFilter, MEDIA_CONTROL_PERMISSION, null);
            this.mRegistered = true;
        }
    }

    public void unregister() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("unregister - mRegistered: ");
        sb.append(this.mRegistered);
        mALogger.mo11957d(sb.toString());
        if (this.mRegistered && QuickScreenshotHelper.isFeatureSupported()) {
            try {
                ActionsApplication.getAppContext().unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                LOGGER.mo11960e("Unable to unregister Screenshot detection receiver.", e);
            } catch (Throwable th) {
                this.mRegistered = false;
                throw th;
            }
            this.mRegistered = false;
        }
    }
}
