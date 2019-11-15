package com.motorola.actions.p013ui.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.motorola.actions.C0504R;
import com.motorola.actions.p013ui.ActionsBaseActivity;

/* renamed from: com.motorola.actions.ui.tutorial.TutorialActivity */
public abstract class TutorialActivity extends ActionsBaseActivity {
    protected boolean mIsCurrentEnabled;
    protected FrameLayout mLayoutLeftBtn;
    protected Button mRightButton;
    protected Button mSingleButton;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0504R.layout.activity_tutorial);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i2 == -1) {
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void setupButtons(boolean z, boolean z2) {
        boolean z3;
        if (z && z2) {
            setupOptinSingleButton();
        } else if (!z || z2) {
            setupNextButton();
        } else {
            setupOptinTwoButtons();
            z3 = true;
            this.mIsCurrentEnabled = !z3;
        }
        z3 = false;
        this.mIsCurrentEnabled = !z3;
    }

    private void setupOptinSingleButton() {
        ((FrameLayout) findViewById(C0504R.C0506id.frame_left_button)).setVisibility(8);
        Button button = (Button) findViewById(C0504R.C0506id.rightBtn);
        button.setText(C0504R.string.done);
        button.setOnClickListener(new TutorialActivity$$Lambda$0(this));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$setupOptinSingleButton$0$TutorialActivity(View view) {
        finish();
    }

    /* access modifiers changed from: protected */
    public void setupOptinTwoButtons() {
        ((TextView) findViewById(C0504R.C0506id.leftBtn)).setText(C0504R.string.maybe_later);
        this.mLayoutLeftBtn = (FrameLayout) findViewById(C0504R.C0506id.frame_left_button);
        this.mLayoutLeftBtn.setVisibility(0);
        this.mRightButton = (Button) findViewById(C0504R.C0506id.rightBtn);
        this.mRightButton.setText(C0504R.string.turn_it_on);
    }

    /* access modifiers changed from: protected */
    public void setupNextButton() {
        ((FrameLayout) findViewById(C0504R.C0506id.frame_left_button)).setVisibility(8);
        this.mSingleButton = (Button) findViewById(C0504R.C0506id.rightBtn);
        this.mSingleButton.setText(C0504R.string.actions_tutorial_next);
    }
}
