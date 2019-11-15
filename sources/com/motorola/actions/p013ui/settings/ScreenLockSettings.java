package com.motorola.actions.p013ui.settings;

import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.motorola.actions.C0504R;
import com.motorola.actions.p013ui.ActionsBaseActivity;
import com.motorola.actions.p013ui.tutorial.VideoListener;
import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.ui.settings.ScreenLockSettings */
public class ScreenLockSettings extends ActionsBaseActivity {
    private static final MALogger LOGGER = new MALogger(ScreenLockSettings.class);
    private VideoListener mVideoListener;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0504R.layout.activity_screen_lock_settings);
        setupButton();
        setupVideo();
        setTextTitle(C0504R.string.onenav_tutorial_turn_off_screen_title);
        setTextInfo(C0504R.string.onenav_tutorial_turn_off_screen_description);
    }

    public void onPause() {
        super.onPause();
        if (this.mVideoListener != null) {
            this.mVideoListener.stopVideo();
        }
    }

    public void onResume() {
        super.onResume();
        if (this.mVideoListener != null) {
            this.mVideoListener.startVideo();
        }
    }

    private void setupButton() {
        LOGGER.mo11957d("setupButton");
        Button button = (Button) findViewById(C0504R.C0506id.rightBtn);
        button.setText(C0504R.string.got_it);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ScreenLockSettings.this.finish();
            }
        });
        ((TextView) findViewById(C0504R.C0506id.leftBtn)).setText(C0504R.string.turn_it_off);
        ((FrameLayout) findViewById(C0504R.C0506id.frame_left_button)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ScreenLockSettings.this.deactivateFPSLockAndFinish();
            }
        });
    }

    private void setupVideo() {
        this.mVideoListener = new VideoListener(getApplicationContext(), (TextureView) findViewById(C0504R.C0506id.video), C0504R.raw.one_nav_tutorial_turn_off_screen);
    }

    /* access modifiers changed from: protected */
    public void setTextTitle(int i) {
        ((TextView) findViewById(C0504R.C0506id.text_title)).setText(i);
    }

    /* access modifiers changed from: protected */
    public void setTextInfo(int i) {
        TextView textView = (TextView) findViewById(C0504R.C0506id.text_info);
        textView.setText(i);
        textView.setVisibility(0);
    }

    /* access modifiers changed from: private */
    public void deactivateFPSLockAndFinish() {
        MotorolaSettings.setFingerPrintLockScreen(false);
        finish();
    }
}
