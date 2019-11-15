package com.motorola.actions.discovery.fdn.attentivedisplay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.attentivedisplay.AttentiveDisplayService;
import com.motorola.actions.attentivedisplay.MovementDetectManager;
import com.motorola.actions.attentivedisplay.MovementDetectManager.MovementChangeListener;
import com.motorola.actions.utils.MALogger;

public class DiscoveryConditionsListener extends BroadcastReceiver implements MovementChangeListener {
    private static final String ACTION_SCREEN_BRIGHT = "com.motorola.server.power.ACTION_SCREEN_BRIGHT";
    private static final String ACTION_SCREEN_DIM = "com.motorola.server.power.ACTION_SCREEN_DIM";
    public static final String KEY_NUMBER_OF_SCREENS_DIM_OUT = "number_of_screens_dim_out";
    private static final MALogger LOGGER = new MALogger(DiscoveryConditionsListener.class);
    private static final int MOVEMENT_DETECTION_DELAY = 1000;
    private static final int NUMBER_OF_SCREEN_DIM_OUT_FOR_FDN = 3;
    private final AttentiveDisplayDiscoveryEventsCallback mCallback;
    private MovementDetectManager mMovementDetectManager;
    private boolean mOnDimming;
    private boolean mRegistered;
    private ScreenTimeoutSettingObserver mScreenTimeoutSettingObserver;

    public DiscoveryConditionsListener(AttentiveDisplayDiscoveryEventsCallback attentiveDisplayDiscoveryEventsCallback) {
        this.mCallback = attentiveDisplayDiscoveryEventsCallback;
    }

    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            char c = 65535;
            int hashCode = action.hashCode();
            if (hashCode != -2053349210) {
                if (hashCode == -272692676 && action.equals(ACTION_SCREEN_DIM)) {
                    c = 0;
                }
            } else if (action.equals(ACTION_SCREEN_BRIGHT)) {
                c = 1;
            }
            switch (c) {
                case 0:
                    this.mOnDimming = true;
                    return;
                case 1:
                    if (this.mOnDimming && incrementScreenDimOutCounter() == 3) {
                        this.mMovementDetectManager = new MovementDetectManager(context, this);
                        this.mMovementDetectManager.start(false);
                        new Handler().postDelayed(new DiscoveryConditionsListener$$Lambda$0(this), 1000);
                    }
                    this.mOnDimming = false;
                    return;
                default:
                    this.mOnDimming = false;
                    return;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onReceive$0$DiscoveryConditionsListener() {
        if (this.mMovementDetectManager != null) {
            this.mMovementDetectManager.stop();
            this.mMovementDetectManager = null;
        }
    }

    public void onMovementChange(boolean z) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onMovementChange() - isMoving: ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        if (z) {
            this.mCallback.run(1);
            if (this.mMovementDetectManager != null) {
                this.mMovementDetectManager.stop();
                this.mMovementDetectManager = null;
            }
        }
    }

    public void register() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("register - mRegistered: ");
        sb.append(this.mRegistered);
        mALogger.mo11957d(sb.toString());
        Context appContext = ActionsApplication.getAppContext();
        if (!this.mRegistered && AttentiveDisplayService.isFeatureSupported(appContext)) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ACTION_SCREEN_DIM);
            intentFilter.addAction(ACTION_SCREEN_BRIGHT);
            appContext.registerReceiver(this, intentFilter);
            if (this.mScreenTimeoutSettingObserver == null) {
                this.mScreenTimeoutSettingObserver = new ScreenTimeoutSettingObserver(new Handler(), this.mCallback);
                this.mScreenTimeoutSettingObserver.start();
            }
            this.mRegistered = true;
        }
    }

    public void unregister() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("unregister - mRegistered: ");
        sb.append(this.mRegistered);
        mALogger.mo11957d(sb.toString());
        Context appContext = ActionsApplication.getAppContext();
        if (this.mRegistered && AttentiveDisplayService.isFeatureSupported(appContext)) {
            try {
                if (this.mScreenTimeoutSettingObserver != null) {
                    this.mScreenTimeoutSettingObserver.stop();
                    this.mScreenTimeoutSettingObserver = null;
                }
                appContext.unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                LOGGER.mo11960e("Unable to unregister screen dim out receiver.", e);
            } catch (Throwable th) {
                this.mRegistered = false;
                throw th;
            }
            this.mRegistered = false;
        }
    }

    private int incrementScreenDimOutCounter() {
        int i = SharedPreferenceManager.getInt(KEY_NUMBER_OF_SCREENS_DIM_OUT, 0);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("incrementScreenDimOutCounter: Dim out counter =");
        sb.append(String.valueOf(i));
        mALogger.mo11957d(sb.toString());
        if (i >= 3) {
            return i;
        }
        int i2 = i + 1;
        SharedPreferenceManager.putInt(KEY_NUMBER_OF_SCREENS_DIM_OUT, i2);
        return i2;
    }
}
