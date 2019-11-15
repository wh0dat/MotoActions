package com.motorola.actions.foc.gesture.service;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

final /* synthetic */ class FlashOnChopService$$Lambda$0 implements OnSharedPreferenceChangeListener {
    private final FlashOnChopService arg$1;

    FlashOnChopService$$Lambda$0(FlashOnChopService flashOnChopService) {
        this.arg$1 = flashOnChopService;
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
        this.arg$1.lambda$new$0$FlashOnChopService(sharedPreferences, str);
    }
}
