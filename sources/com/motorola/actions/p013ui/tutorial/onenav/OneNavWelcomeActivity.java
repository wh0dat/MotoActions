package com.motorola.actions.p013ui.tutorial.onenav;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.onenav.OneNavHelper;
import com.motorola.actions.p013ui.ActionsBaseActivity;
import com.motorola.actions.p013ui.tutorial.VideoListener;
import com.motorola.actions.utils.ActivityUtils;
import com.motorola.actions.utils.Constants;
import com.motorola.actions.utils.Device;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.ui.tutorial.onenav.OneNavWelcomeActivity */
public class OneNavWelcomeActivity extends ActionsBaseActivity {
    private static final MALogger LOGGER = new MALogger(OneNavWelcomeActivity.class);
    private VideoListener mVideoListener;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivityUtils.setFullscreenMode(this);
        setContentView((int) C0504R.layout.activity_welcome_onenav);
        SharedPreferenceManager.putBoolean(Constants.ONENAV_SCREEN_ALREADY_SHOWN, true);
        setupVideo();
        setupButton();
        setupDescription();
    }

    private void setupVideo() {
        LOGGER.mo11957d("setupVideo");
        this.mVideoListener = new VideoListener(ActionsApplication.getAppContext(), (TextureView) findViewById(C0504R.C0506id.welcome_video), Device.is2017UIDevice() ? C0504R.raw.one_nav_welcome : C0504R.raw.one_nav_welcome_slim);
    }

    private void setupButton() {
        LOGGER.mo11957d("setupButton");
        ((TextView) findViewById(C0504R.C0506id.leftBtn)).setText(C0504R.string.onenav_back_tutorial);
        Button button = (Button) findViewById(C0504R.C0506id.rightBtn);
        button.setText(C0504R.string.try_it_out);
        ((FrameLayout) findViewById(C0504R.C0506id.frame_left_button)).setOnClickListener(new OneNavWelcomeActivity$$Lambda$0(this));
        button.setOnClickListener(new OneNavWelcomeActivity$$Lambda$1(this));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$setupButton$0$OneNavWelcomeActivity(View view) {
        finish();
        overridePendingTransition(C0504R.anim.splash_slide_in_anim_reverse_set, C0504R.anim.splash_slide_out_anim_reverse_set);
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$setupButton$1$OneNavWelcomeActivity(View view) {
        Intent intent = new Intent(this, OneNavTutorialActivity.class);
        intent.putExtras(getIntent().getExtras());
        startActivity(intent);
        overridePendingTransition(C0504R.anim.splash_slide_in_anim_set, C0504R.anim.splash_slide_out_anim_set);
        finish();
    }

    private void setupDescription() {
        ((TextView) findViewById(C0504R.C0506id.welcome_text_description)).setText(OneNavHelper.getResourceForType(C0504R.string.onenav_welcome_description, C0504R.string.onenav_welcome_description_soft));
    }

    public void onPause() {
        super.onPause();
        this.mVideoListener.stopVideo();
    }

    public void onResume() {
        super.onResume();
        ActivityUtils.setSetupWizardMode(findViewById(C0504R.C0506id.welcome_root), this);
        if (this.mVideoListener != null) {
            this.mVideoListener.startVideo();
        }
    }

    public static void openWelcomeActivity(Activity activity, Bundle bundle) {
        Intent intent = new Intent(activity, OneNavWelcomeActivity.class);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }
}
