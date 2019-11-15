package com.motorola.actions.p013ui.tutorial.lts;

import android.app.Fragment;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.motorola.actions.C0504R;
import com.motorola.actions.p013ui.tutorial.CountdownSpinner;
import com.motorola.actions.reflect.SensorPrivateProxy;
import com.motorola.actions.utils.EventOnlySensorListener;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.ui.tutorial.lts.LiftToSilenceIntroTutorialFragment */
public class LiftToSilenceIntroTutorialFragment extends Fragment {
    private static final int COUNTDOWN_REFRESH_MS = 50;
    private static final long COUNTDOWN_TIME_MS = 8000;
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(LiftToSilenceIntroTutorialFragment.class);
    /* access modifiers changed from: private */
    public LiftToSilenceTutorialActivity mActivity;
    private ImageButton mButtonCountdown;
    /* access modifiers changed from: private */
    public long mCountdown = COUNTDOWN_TIME_MS;
    /* access modifiers changed from: private */
    public CountdownSpinner mCountdownSpinner;
    private SensorEventListener mFlatDownSensorListener = new EventOnlySensorListener() {
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == SensorPrivateProxy.TYPE_FLAT_DOWN) {
                if (sensorEvent.values[0] == 0.0f) {
                    LiftToSilenceIntroTutorialFragment.this.mOnTableFlatDown = false;
                } else {
                    LiftToSilenceIntroTutorialFragment.this.mOnTableFlatDown = true;
                }
                LiftToSilenceIntroTutorialFragment.this.updateText();
            }
        }
    };
    private SensorEventListener mFlatUpSensorListener = new EventOnlySensorListener() {
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == SensorPrivateProxy.TYPE_FLAT_UP) {
                if (sensorEvent.values[0] == 0.0f) {
                    LiftToSilenceIntroTutorialFragment.this.mOnTableFlatUp = false;
                } else {
                    LiftToSilenceIntroTutorialFragment.this.mOnTableFlatUp = true;
                }
                LiftToSilenceIntroTutorialFragment.this.updateText();
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean mOnTableFlatDown = false;
    /* access modifiers changed from: private */
    public boolean mOnTableFlatUp = false;
    private View mRootView;
    /* access modifiers changed from: private */
    public Runnable mRunCountdown = new Runnable() {
        public void run() {
            if (LiftToSilenceIntroTutorialFragment.this.mActivity != null) {
                boolean z = LiftToSilenceIntroTutorialFragment.this.mOnTableFlatDown || LiftToSilenceIntroTutorialFragment.this.mOnTableFlatUp;
                if (LiftToSilenceIntroTutorialFragment.this.mCountdown > 0) {
                    if (z || LiftToSilenceIntroTutorialFragment.this.mCountdown >= 4000) {
                        LiftToSilenceIntroTutorialFragment.this.mCountdown = LiftToSilenceIntroTutorialFragment.this.mCountdown - 50;
                        LiftToSilenceIntroTutorialFragment.this.mCountdownSpinner.setProgress(LiftToSilenceIntroTutorialFragment.COUNTDOWN_TIME_MS - LiftToSilenceIntroTutorialFragment.this.mCountdown);
                        LiftToSilenceIntroTutorialFragment.this.mSpinnerView.invalidate();
                        LiftToSilenceIntroTutorialFragment.this.mSpinnerView.postDelayed(LiftToSilenceIntroTutorialFragment.this.mRunCountdown, 50);
                        return;
                    }
                    LiftToSilenceIntroTutorialFragment.this.mActivity.showFailureNotPutOnTableFragment();
                } else if (z) {
                    LiftToSilenceIntroTutorialFragment.this.mActivity.showDemoFragment();
                } else {
                    LiftToSilenceIntroTutorialFragment.this.mActivity.showFailureNotPutOnTableFragment();
                }
            } else {
                LiftToSilenceIntroTutorialFragment.LOGGER.mo11963w("Activity invalid.");
            }
        }
    };
    private SensorManager mSensorManager;
    /* access modifiers changed from: private */
    public View mSpinnerView;

    /* access modifiers changed from: private */
    public void updateText() {
        if (this.mOnTableFlatDown || this.mOnTableFlatUp) {
            changeTextItems(C0504R.string.lts_enabled, C0504R.string.lts_motion_description2);
        } else {
            changeTextItems(C0504R.string.lts_place_on_table, C0504R.string.lts_motion_description1);
        }
    }

    private void changeTextItems(int i, int i2) {
        if (this.mRootView != null) {
            ((TextView) this.mRootView.findViewById(C0504R.C0506id.text_title)).setText(i);
            ((TextView) this.mRootView.findViewById(C0504R.C0506id.text_info)).setText(i2);
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mRootView = layoutInflater.inflate(C0504R.layout.fragment_demo_phone_intro, viewGroup, false);
        this.mActivity = (LiftToSilenceTutorialActivity) getActivity();
        this.mSensorManager = (SensorManager) this.mActivity.getSystemService("sensor");
        changeTextItems(C0504R.string.lts_place_on_table, C0504R.string.lts_motion_description1);
        this.mCountdownSpinner = new CountdownSpinner(getActivity(), COUNTDOWN_TIME_MS);
        this.mButtonCountdown = (ImageButton) this.mRootView.findViewById(C0504R.C0506id.button_countdown);
        this.mButtonCountdown.setColorFilter(this.mActivity.getColor(C0504R.color.parasailing_500));
        addListenerOnButton();
        this.mSpinnerView = this.mRootView.findViewById(C0504R.C0506id.spinner_view);
        this.mSpinnerView.setBackground(this.mCountdownSpinner);
        return this.mRootView;
    }

    private void addListenerOnButton() {
        this.mButtonCountdown.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                LiftToSilenceIntroTutorialFragment.this.getActivity().onBackPressed();
            }
        });
    }

    public void onResume() {
        super.onResume();
        this.mCountdown = COUNTDOWN_TIME_MS;
        this.mSpinnerView.postDelayed(this.mRunCountdown, 0);
        this.mOnTableFlatUp = false;
        this.mOnTableFlatDown = false;
        this.mSensorManager.registerListener(this.mFlatUpSensorListener, this.mSensorManager.getDefaultSensor(SensorPrivateProxy.TYPE_FLAT_UP), 3);
        this.mSensorManager.registerListener(this.mFlatDownSensorListener, this.mSensorManager.getDefaultSensor(SensorPrivateProxy.TYPE_FLAT_DOWN), 3);
    }

    public void onPause() {
        super.onPause();
        this.mSensorManager.unregisterListener(this.mFlatUpSensorListener);
        this.mSensorManager.unregisterListener(this.mFlatDownSensorListener);
        this.mSpinnerView.removeCallbacks(this.mRunCountdown);
    }
}
