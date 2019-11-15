package com.motorola.actions.p013ui.tutorial.p015qc;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.p010qc.FeatureManager;
import com.motorola.actions.p010qc.QuickDrawHelper;
import com.motorola.actions.p013ui.tutorial.TutorialFragment;
import com.motorola.actions.reflect.SensorPrivateProxy;
import com.motorola.actions.utils.DisplayUtils;

/* renamed from: com.motorola.actions.ui.tutorial.qc.QuickDrawTutorialFragment */
public class QuickDrawTutorialFragment extends TutorialFragment {
    private static final String ACTION_SUCCESS = "com.motorola.actions.tutorial.QUICKDRAW_SUCCESS";
    private static final int MAX_TIME_TO_TWIST_MS = 8000;
    private final SensorEventListener mGestureListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == SensorPrivateProxy.TYPE_CAMERA_ACTIVATE && QuickDrawTutorialFragment.this.getActivity() != null) {
                FeatureManager.sendIntentToCamera(new Intent(QuickDrawTutorialFragment.ACTION_SUCCESS));
            }
        }
    };
    private boolean mPreviousEnableState;
    private final Runnable mTimeout = new QuickDrawTutorialFragment$$Lambda$0(this);

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$new$0$QuickDrawTutorialFragment() {
        if (getContext() != null) {
            setTextTitle(C0504R.string.camera_qd_timeout_title);
            setTextInfo(C0504R.string.camera_qd_timeout_info);
            setBulletInfo(getContext().getResources().getStringArray(C0504R.array.camera_qd_tutorial_tip_list));
        }
    }

    /* access modifiers changed from: protected */
    public FeatureKey getFeatureKey() {
        return FeatureKey.QUICK_CAPTURE;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        setTextTitle(C0504R.string.camera_qd_title);
        setTextInfo(C0504R.string.camera_qd_info);
        hideButtons();
        return onCreateView;
    }

    public void onPause() {
        super.onPause();
        if (!this.mPreviousEnableState) {
            QuickDrawHelper.setEnabled(false);
        }
        unregisterGestureListener();
        this.mRootView.removeCallbacks(this.mTimeout);
    }

    public void onResume() {
        super.onResume();
        this.mPreviousEnableState = QuickDrawHelper.isEnabled();
        if (!this.mPreviousEnableState) {
            QuickDrawHelper.setEnabled(true);
        }
        this.mRootView.postDelayed(this.mTimeout, 8000);
        registerGestureListener();
    }

    /* access modifiers changed from: protected */
    public int getVideoResId() {
        return DisplayUtils.getScreenWidth(getActivity()) <= 540 ? C0504R.raw.twist_lowres : C0504R.raw.twist;
    }

    private void registerGestureListener() {
        SensorManager sensorManager = (SensorManager) getActivity().getSystemService("sensor");
        sensorManager.registerListener(this.mGestureListener, sensorManager.getDefaultSensor(SensorPrivateProxy.TYPE_CAMERA_ACTIVATE), 2);
    }

    private void unregisterGestureListener() {
        ((SensorManager) getActivity().getSystemService("sensor")).unregisterListener(this.mGestureListener);
    }
}
