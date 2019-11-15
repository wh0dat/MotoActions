package com.motorola.actions.foc.gesture.util;

import android.content.Context;
import android.content.Intent;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.p001v4.content.LocalBroadcastManager;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.foc.gesture.event.TorchEvent;
import com.motorola.actions.foc.gesture.receiver.FlashOnChopForegroundReceiver;

public class FlashOnChopUtils {
    public static void playFlashlightOnFeedback() {
        vibratePhone(new long[]{0, 200, 100, 400});
    }

    public static void playFlashlightOffFeedback() {
        vibratePhone(new long[]{0, 300});
    }

    private static void vibratePhone(long[] jArr) {
        Vibrator vibrator = (Vibrator) ActionsApplication.getAppContext().getSystemService("vibrator");
        if (vibrator != null) {
            vibrator.vibrate(VibrationEffect.createWaveform(jArr, -1));
        }
    }

    public static void sendToForeground(boolean z) {
        Context appContext = ActionsApplication.getAppContext();
        Intent intent = new Intent();
        intent.setPackage(appContext.getPackageName());
        intent.setAction(z ? FlashOnChopForegroundReceiver.FLASH_ON_CHOP_TO_FOREGROUND : FlashOnChopForegroundReceiver.FLASH_ON_CHOP_TO_BACKGROUND);
        LocalBroadcastManager.getInstance(appContext).sendBroadcast(intent);
    }

    public static void sendMsgTorchModeTutorial(boolean z, boolean z2) {
        Intent intent = new Intent();
        intent.setPackage(ActionsApplication.getAppContext().getPackageName());
        intent.setAction(z ? TorchEvent.ACTION_TUTORIAL_TORCH_ON : TorchEvent.ACTION_TUTORIAL_TORCH_OFF);
        intent.putExtra(TorchEvent.EXTRA_FLASHLIGHT_FROM_ACTIONS, z2);
        LocalBroadcastManager.getInstance(ActionsApplication.getAppContext()).sendBroadcast(intent);
    }
}
