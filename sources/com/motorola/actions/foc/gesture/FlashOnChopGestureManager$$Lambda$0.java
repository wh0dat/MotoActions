package com.motorola.actions.foc.gesture;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

final /* synthetic */ class FlashOnChopGestureManager$$Lambda$0 implements OnSharedPreferenceChangeListener {
    private final FlashOnChopGestureManager arg$1;

    FlashOnChopGestureManager$$Lambda$0(FlashOnChopGestureManager flashOnChopGestureManager) {
        this.arg$1 = flashOnChopGestureManager;
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
        this.arg$1.lambda$new$0$FlashOnChopGestureManager(sharedPreferences, str);
    }
}
