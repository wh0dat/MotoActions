package com.motorola.actions.foc.gesture.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Handler;
import android.os.IBinder;
import android.support.p001v4.content.LocalBroadcastManager;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.foc.gesture.FlashOnChopGestureManager;
import com.motorola.actions.foc.gesture.SensorHubChopRecognizer;
import com.motorola.actions.foc.gesture.receiver.FlashOnChopForegroundReceiver;
import com.motorola.actions.foc.gesture.util.FlashOnChopUtils;
import com.motorola.actions.p013ui.tutorial.foc.FlashOnChopTutorialActivity;
import com.motorola.actions.utils.MALogger;

public class FlashOnChopService extends Service {
    public static final String KEY_ENABLED = "actions_foc_enabled";
    public static final String KEY_TURN_ON_BY_PERMISSIONS_GRANTED = "actions_foc_turn_on_by_permission_granted";
    private static final MALogger LOGGER = new MALogger(FlashOnChopService.class);
    private final BroadcastReceiver mFOCTutorialReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (context != null && intent != null) {
                String action = intent.getAction();
                if (FlashOnChopTutorialActivity.ACTION_FOC_TUTORIAL_ENTER.equals(action)) {
                    FlashOnChopService.this.handleEnterTutorial();
                } else if (FlashOnChopTutorialActivity.ACTION_FOC_TUTORIAL_LEAVE.equals(action)) {
                    FlashOnChopService.this.registerForegroundReceiver();
                }
            }
        }
    };
    private FlashOnChopForegroundReceiver mFlashOnChopForegroundReceiver;
    private FlashOnChopGestureManager mFlashOnChopGestureManager;
    private boolean mIsFeatureEnabled;
    private boolean mIsTutorialReceiverRegistered = false;
    private final OnSharedPreferenceChangeListener mSharedPreferenceListener = new FlashOnChopService$$Lambda$0(this);

    public IBinder onBind(Intent intent) {
        return null;
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$new$0$FlashOnChopService(SharedPreferences sharedPreferences, String str) {
        checkSharedPreference(str);
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, FlashOnChopService.class);
    }

    public static boolean isFeatureSupported(Context context) {
        return SensorHubChopRecognizer.isRecognizerAvailable(context);
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        LOGGER.mo11957d("onStartCommand");
        if (!isFeatureSupported(this)) {
            stopSelf();
            return 2;
        }
        checkSharedPreference(KEY_ENABLED);
        SharedPreferenceManager.registerOnSharedPreferenceChangeListener(this.mSharedPreferenceListener);
        if (this.mFlashOnChopGestureManager != null && FlashOnChopGestureManager.isFlashlightOn()) {
            this.mFlashOnChopGestureManager.sendMessage(2);
        }
        registerForegroundReceiver();
        registerTutorialReceiver();
        return 1;
    }

    public void onDestroy() {
        LOGGER.mo11957d("onDestroy");
        SharedPreferenceManager.unregisterOnSharedPreferenceChangeListener(this.mSharedPreferenceListener);
        unregisterForegroundReceiver();
        unregisterTutorialReceiver();
        stopForeground(true);
        if (this.mFlashOnChopGestureManager != null) {
            this.mFlashOnChopGestureManager.stop();
        }
        super.onDestroy();
    }

    /* access modifiers changed from: private */
    public void registerForegroundReceiver() {
        if (this.mFlashOnChopForegroundReceiver == null) {
            this.mFlashOnChopForegroundReceiver = new FlashOnChopForegroundReceiver(this);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(FlashOnChopForegroundReceiver.FLASH_ON_CHOP_TO_FOREGROUND);
            intentFilter.addAction(FlashOnChopForegroundReceiver.FLASH_ON_CHOP_TO_BACKGROUND);
            LocalBroadcastManager.getInstance(this).registerReceiver(this.mFlashOnChopForegroundReceiver, intentFilter);
            LOGGER.mo11957d("registerReceiver(mFlashOnChopForegroundReceiver)");
        }
    }

    private void unregisterForegroundReceiver() {
        if (this.mFlashOnChopForegroundReceiver != null) {
            try {
                LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mFlashOnChopForegroundReceiver);
                this.mFlashOnChopForegroundReceiver = null;
                LOGGER.mo11957d("unregisterReceiver(mFlashOnChopForegroundReceiver)");
            } catch (IllegalArgumentException e) {
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("unregisterReceiver(mFlashOnChopForegroundReceiver): ");
                sb.append(e.getMessage());
                mALogger.mo11959e(sb.toString());
            }
        }
    }

    private void registerTutorialReceiver() {
        if (!this.mIsTutorialReceiverRegistered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(FlashOnChopTutorialActivity.ACTION_FOC_TUTORIAL_ENTER);
            intentFilter.addAction(FlashOnChopTutorialActivity.ACTION_FOC_TUTORIAL_LEAVE);
            LocalBroadcastManager.getInstance(this).registerReceiver(this.mFOCTutorialReceiver, intentFilter);
            LOGGER.mo11957d("registerReceiver(mFOCTutorialReceiver)");
            this.mIsTutorialReceiverRegistered = true;
        }
    }

    private void unregisterTutorialReceiver() {
        if (this.mFOCTutorialReceiver != null) {
            try {
                LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mFOCTutorialReceiver);
                this.mIsTutorialReceiverRegistered = false;
                LOGGER.mo11957d("unregisterReceiver(mFOCTutorialReceiver)");
            } catch (IllegalArgumentException e) {
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("unregisterReceiver(mFOCTutorialReceiver): ");
                sb.append(e.getMessage());
                mALogger.mo11959e(sb.toString());
            }
        }
    }

    private void updateChopMotionEnabled() {
        if (this.mIsFeatureEnabled) {
            if (this.mFlashOnChopGestureManager == null) {
                this.mFlashOnChopGestureManager = new FlashOnChopGestureManager(this);
            }
            this.mFlashOnChopGestureManager.start();
        } else if (this.mFlashOnChopGestureManager != null) {
            this.mFlashOnChopGestureManager.stop();
        }
    }

    private void checkSharedPreference(String str) {
        if (KEY_ENABLED.equals(str)) {
            this.mIsFeatureEnabled = SharedPreferenceManager.getBoolean(KEY_ENABLED, FeatureKey.FLASH_ON_CHOP.getEnableDefaultState());
            updateChopMotionEnabled();
        }
    }

    public static boolean isServiceEnabled(Context context) {
        if (isFeatureSupported(context)) {
            return SharedPreferenceManager.getBoolean(KEY_ENABLED, FeatureKey.FLASH_ON_CHOP.getEnableDefaultState());
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void handleEnterTutorial() {
        FlashOnChopUtils.sendToForeground(false);
        unregisterForegroundReceiver();
        if (this.mFlashOnChopGestureManager != null) {
            new Handler().postDelayed(new FlashOnChopService$$Lambda$1(this), 150);
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$handleEnterTutorial$1$FlashOnChopService() {
        this.mFlashOnChopGestureManager.sendMessage(2);
    }
}
