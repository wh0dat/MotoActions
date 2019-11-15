package com.motorola.actions.p013ui.tutorial.onenav.soft;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.onenav.OneNavHelper;
import com.motorola.actions.onenav.instrumentation.InstrumentationTutorialStep;
import com.motorola.actions.onenav.instrumentation.OneNavInstrumentation;
import com.motorola.actions.p013ui.ActionsBaseActivity;
import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.settings.updater.OneNavSettingsUpdater;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.ui.tutorial.onenav.soft.SoftOneNavOptinActivity */
public class SoftOneNavOptinActivity extends ActionsBaseActivity {
    private static final MALogger LOGGER = new MALogger(SoftOneNavOptinActivity.class);

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0504R.layout.activity_softonenav_optin);
        setupButtons(findViewById(C0504R.C0506id.parent_root));
    }

    private void setupButtons(View view) {
        View findViewById = view.findViewById(C0504R.C0506id.layout_cmd_single_button);
        View findViewById2 = view.findViewById(C0504R.C0506id.layout_cmd_two_buttons);
        if (OneNavHelper.isOneNavEnabled()) {
            setupSingleButton(view);
            if (findViewById2 != null) {
                findViewById2.setVisibility(8);
            }
            if (findViewById != null) {
                findViewById.setVisibility(0);
                return;
            }
            return;
        }
        setupLeftButton(view);
        setupRightButton(view);
        if (findViewById2 != null) {
            findViewById2.setVisibility(0);
        }
        if (findViewById != null) {
            findViewById.setVisibility(8);
        }
    }

    private void setupLeftButton(View view) {
        TextView textView = (TextView) view.findViewById(C0504R.C0506id.leftBtn);
        if (textView != null) {
            textView.setText(C0504R.string.no_thanks);
            ((FrameLayout) view.findViewById(C0504R.C0506id.frame_left_button)).setOnClickListener(new SoftOneNavOptinActivity$$Lambda$0(this));
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$setupLeftButton$0$SoftOneNavOptinActivity(View view) {
        LOGGER.mo11957d("onLeftButtonClicked");
        finish();
        OneNavInstrumentation.recordOneNavTutorialStep(InstrumentationTutorialStep.OFF);
    }

    private void setupRightButton(View view) {
        Button button = (Button) view.findViewById(C0504R.C0506id.rightBtn);
        if (button != null) {
            button.setText(C0504R.string.turn_it_on);
            button.setOnClickListener(new SoftOneNavOptinActivity$$Lambda$1(this));
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$setupRightButton$1$SoftOneNavOptinActivity(View view) {
        LOGGER.mo11957d("onRightButtonClicked");
        OneNavSettingsUpdater.getInstance().setEnabledSource(true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_TUTORIAL);
        updateOneNavAndExit(1);
        OneNavInstrumentation.recordOneNavTutorialStep(InstrumentationTutorialStep.ON);
    }

    private void setupSingleButton(View view) {
        Button button = (Button) view.findViewById(C0504R.C0506id.singleButton);
        if (button != null) {
            button.setText(C0504R.string.done);
            button.setOnClickListener(new SoftOneNavOptinActivity$$Lambda$2(this));
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$setupSingleButton$2$SoftOneNavOptinActivity(View view) {
        LOGGER.mo11957d("onSingleButtonClicked");
        finish();
    }

    private void updateOneNavAndExit(int i) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("updateOneNavAndExit - OneNav status: ");
        sb.append(i);
        mALogger.mo11957d(sb.toString());
        MALogger mALogger2 = LOGGER;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("updateOneNavAndExit - mIsOneNavEnabledBeforeTutorial: ");
        sb2.append(OneNavHelper.isOneNavEnabled());
        mALogger2.mo11957d(sb2.toString());
        boolean z = OneNavHelper.isOneNavEnabled() != (i == 1);
        MotorolaSettings.setOneNavEnabled(i, z);
        if (i == 1) {
            DiscoveryManager.getInstance().setDiscoveryStatus(FeatureKey.ONE_NAV, 0);
        }
        if (z) {
            getFDNSession().recordChange(FeatureKey.ONE_NAV);
        }
        finish();
    }
}
