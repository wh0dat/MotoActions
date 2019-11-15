package com.motorola.actions.discovery.fdn.attentivedisplay;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings.System;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.attentivedisplay.util.ScreenTimeoutControl;
import com.motorola.actions.utils.MALogger;

class ScreenTimeoutSettingObserver extends ContentObserver {
    private static final MALogger LOGGER = new MALogger(ScreenTimeoutSettingObserver.class);
    private final AttentiveDisplayDiscoveryEventsCallback mCallback;
    private int mLastScreenOffTimeoutSet = -1;

    ScreenTimeoutSettingObserver(Handler handler, AttentiveDisplayDiscoveryEventsCallback attentiveDisplayDiscoveryEventsCallback) {
        super(handler);
        this.mCallback = attentiveDisplayDiscoveryEventsCallback;
    }

    public void start() {
        LOGGER.mo11957d("Starting settings observer");
        Uri uriFor = System.getUriFor("screen_off_timeout");
        this.mLastScreenOffTimeoutSet = ScreenTimeoutControl.getScreenTimeout(ActionsApplication.getAppContext());
        ActionsApplication.getAppContext().getContentResolver().registerContentObserver(uriFor, false, this);
    }

    public void stop() {
        LOGGER.mo11957d("Stopping settings observer");
        ActionsApplication.getAppContext().getContentResolver().unregisterContentObserver(this);
    }

    public void onChange(boolean z, Uri uri) {
        int screenTimeout = ScreenTimeoutControl.getScreenTimeout(ActionsApplication.getAppContext());
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Screen Off Timeout changed to ");
        sb.append(screenTimeout);
        mALogger.mo11957d(sb.toString());
        if (this.mLastScreenOffTimeoutSet == -1 || screenTimeout <= this.mLastScreenOffTimeoutSet || this.mCallback == null) {
            this.mLastScreenOffTimeoutSet = screenTimeout;
            return;
        }
        LOGGER.mo11957d("Screen Off Timeout was increased");
        this.mCallback.run(2);
    }
}
