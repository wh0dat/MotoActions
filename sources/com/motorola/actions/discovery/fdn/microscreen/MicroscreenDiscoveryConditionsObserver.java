package com.motorola.actions.discovery.fdn.microscreen;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.utils.DisplayUtils;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.MotoAppUtils;
import com.motorola.actions.utils.RunningTasksUtils;
import com.motorola.actions.utils.SetupObserver;

class MicroscreenDiscoveryConditionsObserver extends ContentObserver {
    private static final MALogger LOGGER = new MALogger(MicroscreenDiscoveryConditionsObserver.class);
    private static final int VISIBILITY_MASK = 6148;
    private ActionsActivityWatcher mActionsActivityWatcher;
    /* access modifiers changed from: private */
    public boolean mIsPhoneOnCall;
    private TelephonyListener mTelephonyListener;

    private static class ActionsActivityWatcher implements ActivityLifecycleCallbacks {
        private int mNumRunningActivities;

        public void onActivityCreated(Activity activity, Bundle bundle) {
        }

        public void onActivityDestroyed(Activity activity) {
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        public void onActivityStarted(Activity activity) {
        }

        public void onActivityStopped(Activity activity) {
        }

        private ActionsActivityWatcher() {
        }

        /* access modifiers changed from: 0000 */
        public boolean isMotoActionsInForeground() {
            return this.mNumRunningActivities > 0;
        }

        public void onActivityResumed(Activity activity) {
            this.mNumRunningActivities++;
        }

        public void onActivityPaused(Activity activity) {
            if (this.mNumRunningActivities > 0) {
                this.mNumRunningActivities--;
            }
        }
    }

    private class TelephonyListener extends PhoneStateListener {
        final TelephonyManager mTelephonyManager = ((TelephonyManager) ActionsApplication.getAppContext().getSystemService("phone"));

        TelephonyListener() {
        }

        public void onCallStateChanged(int i, String str) {
            super.onCallStateChanged(i, str);
            MicroscreenDiscoveryConditionsObserver microscreenDiscoveryConditionsObserver = MicroscreenDiscoveryConditionsObserver.this;
            boolean z = true;
            if (i != 1) {
                z = false;
            }
            microscreenDiscoveryConditionsObserver.mIsPhoneOnCall = z;
        }

        public void register() {
            this.mTelephonyManager.listen(this, 32);
        }

        public void unregister() {
            this.mTelephonyManager.listen(this, 0);
        }
    }

    MicroscreenDiscoveryConditionsObserver(Handler handler) {
        super(handler);
    }

    public void onChange(boolean z, Uri uri) {
        boolean isForegroundAppPackageName = RunningTasksUtils.isForegroundAppPackageName(ActionsApplication.getAppContext(), MotoAppUtils.MOTO_PACKAGE_NAME);
        int systemViewVisibility = MotorolaSettings.getSystemViewVisibility();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onChange, visibility=");
        sb.append(systemViewVisibility);
        mALogger.mo11957d(sb.toString());
        if ((systemViewVisibility & VISIBILITY_MASK) != 0) {
            MALogger mALogger2 = LOGGER;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("onChange(). SystemViewVisibility=");
            sb2.append(systemViewVisibility);
            mALogger2.mo11957d(sb2.toString());
            if (DisplayUtils.isInPortrait() && SetupObserver.isSetupFinished() && this.mActionsActivityWatcher != null && !this.mActionsActivityWatcher.isMotoActionsInForeground() && !isForegroundAppPackageName && !this.mIsPhoneOnCall) {
                LOGGER.mo11957d("...and is in portrait ans setup finished and actions not in foreground.");
                DiscoveryManager.getInstance().onFDNEvent(FeatureKey.MICROSCREEN);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void register() {
        LOGGER.mo11957d("register");
        ActionsApplication.getAppContext().getContentResolver().registerContentObserver(MotorolaSettings.getSystemViewVisibilityUri(), false, this);
        if (this.mActionsActivityWatcher == null) {
            this.mActionsActivityWatcher = new ActionsActivityWatcher();
        }
        if (this.mTelephonyListener == null) {
            this.mTelephonyListener = new TelephonyListener();
            this.mTelephonyListener.register();
        }
        ((Application) ActionsApplication.getAppContext()).registerActivityLifecycleCallbacks(this.mActionsActivityWatcher);
    }

    /* access modifiers changed from: 0000 */
    public void unregister() {
        LOGGER.mo11957d("unregister");
        ActionsApplication.getAppContext().getContentResolver().unregisterContentObserver(this);
        Application application = (Application) ActionsApplication.getAppContext();
        if (this.mActionsActivityWatcher != null) {
            application.unregisterActivityLifecycleCallbacks(this.mActionsActivityWatcher);
            this.mActionsActivityWatcher = null;
        }
        if (this.mTelephonyListener != null) {
            this.mTelephonyListener.unregister();
            this.mTelephonyListener = null;
        }
    }
}
