package com.motorola.actions.p013ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.p004v7.app.AppCompatActivity;
import com.motorola.actions.C0504R;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.discovery.fdn.FDNSession;

/* renamed from: com.motorola.actions.ui.ActionsBaseActivity */
public abstract class ActionsBaseActivity extends AppCompatActivity {
    public static final String KEY_ACTIONS_ACTIVITY_FOCUS = "activity_focus";
    private final FDNSession mFDNSession = new FDNSession();

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getFDNSession().enterSession(getIntent());
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getFDNSession().enterSession(intent);
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        SharedPreferenceManager.putBoolean(KEY_ACTIONS_ACTIVITY_FOCUS, z);
    }

    /* access modifiers changed from: protected */
    public FDNSession getFDNSession() {
        return this.mFDNSession;
    }

    /* access modifiers changed from: protected */
    public void animateSlideIn() {
        overridePendingTransition(C0504R.anim.splash_slide_in_anim_set, C0504R.anim.splash_slide_out_anim_set);
    }

    /* access modifiers changed from: protected */
    public void animateSlideInReverse() {
        overridePendingTransition(C0504R.anim.splash_slide_in_anim_reverse_set, C0504R.anim.splash_slide_out_anim_reverse_set);
    }
}
