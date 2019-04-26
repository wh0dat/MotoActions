/*
 * Copyright (c) 2017 The AOSP Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.moto.actions;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;

import java.util.List;
import java.util.LinkedList;

import com.moto.actions.actions.UpdatedStateNotifier;
import com.moto.actions.actions.CameraActivationSensor;
import com.moto.actions.actions.ChopChopSensor;
import com.moto.actions.actions.FlipToMute;
import com.moto.actions.actions.LiftToSilence;
import com.moto.actions.actions.ProximitySilencer;

import com.moto.actions.doze.DozePulseAction;
import com.moto.actions.doze.GlanceSensor;
import com.moto.actions.doze.ProximitySensor;
import com.moto.actions.doze.FlatUpSensor;
import com.moto.actions.doze.ScreenReceiver;
import com.moto.actions.doze.ScreenStateNotifier;
import com.moto.actions.doze.StowSensor;

public class MotoActionsService extends IntentService implements ScreenStateNotifier,
        UpdatedStateNotifier {
    private static final String TAG = "MotoActions";

    private final Context mContext;

    private final DozePulseAction mDozePulseAction;
    private final PowerManager mPowerManager;
    private final PowerManager.WakeLock mWakeLock;
    private final ScreenReceiver mScreenReceiver;
    private final SensorHelper mSensorHelper;

    private final List<ScreenStateNotifier> mScreenStateNotifiers = new LinkedList<ScreenStateNotifier>();
    private final List<UpdatedStateNotifier> mUpdatedStateNotifiers =
                        new LinkedList<UpdatedStateNotifier>();

    public MotoActionsService(Context context) {
        super("MotoActionservice");
        mContext = context;

        Log.d(TAG, "Starting");

        MotoActionsSettings MotoActionsSettings = new MotoActionsSettings(context, this);
        mSensorHelper = new SensorHelper(context);
        mScreenReceiver = new ScreenReceiver(context, this);

        mDozePulseAction = new DozePulseAction(context);
        mScreenStateNotifiers.add(mDozePulseAction);

        // Actionable sensors get screen on/off notifications
        mScreenStateNotifiers.add(new GlanceSensor(MotoActionsSettings, mSensorHelper, mDozePulseAction));
        mScreenStateNotifiers.add(new ProximitySensor(MotoActionsSettings, mSensorHelper, mDozePulseAction));
        mScreenStateNotifiers.add(new StowSensor(MotoActionsSettings, mSensorHelper, mDozePulseAction));
        mScreenStateNotifiers.add(new FlatUpSensor(MotoActionsSettings, mSensorHelper, mDozePulseAction));

        // Other actions that are always enabled
        mUpdatedStateNotifiers.add(new CameraActivationSensor(MotoActionsSettings, mSensorHelper));
        mUpdatedStateNotifiers.add(new ChopChopSensor(MotoActionsSettings, mSensorHelper));
        mUpdatedStateNotifiers.add(new ProximitySilencer(MotoActionsSettings, context, mSensorHelper));
        mUpdatedStateNotifiers.add(new FlipToMute(MotoActionsSettings, context, mSensorHelper));
        mUpdatedStateNotifiers.add(new LiftToSilence(MotoActionsSettings, context, mSensorHelper));

        mPowerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MotoActionsWakeLock");
        updateState();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    }

    @Override
    public void screenTurnedOn() {
            if (!mWakeLock.isHeld()) {
                mWakeLock.acquire();
            }
        for (ScreenStateNotifier screenStateNotifier : mScreenStateNotifiers) {
            screenStateNotifier.screenTurnedOn();
        }
    }

    @Override
    public void screenTurnedOff() {
            if (mWakeLock.isHeld()) {
                mWakeLock.release();
            }
        for (ScreenStateNotifier screenStateNotifier : mScreenStateNotifiers) {
            screenStateNotifier.screenTurnedOff();
        }
    }

    public void updateState() {
        if (mPowerManager.isInteractive()) {
            screenTurnedOn();
        } else {
            screenTurnedOff();
        }
        for (UpdatedStateNotifier notifier : mUpdatedStateNotifiers) {
            notifier.updateState();
        }
    }
}
