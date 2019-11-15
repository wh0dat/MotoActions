package com.motorola.actions.foc.gesture.event;

import com.motorola.actions.ActionsApplication;
import com.motorola.actions.foc.gesture.FlashOnChopTimeout;
import com.motorola.actions.foc.gesture.event.listener.TimeoutEventListener;
import java.util.concurrent.TimeUnit;

public class TimeoutEvent implements IEventSource {
    private static final String ACTION_TIMEOUT_FLASHOFF = "foc.alarm_timeout";
    private static final long TIMEOUT_FLASSHOFF_MILLIS_TO_FIRE = TimeUnit.MINUTES.toMillis(5);
    private final Runnable mFlashlightOffTimeout = new Runnable() {
        public void run() {
            if (TimeoutEvent.this.mTimeoutEventListener != null) {
                TimeoutEvent.this.mTimeoutEventListener.onTimeout();
            }
        }
    };
    private FlashOnChopTimeout mTimeOutFlashOff;
    /* access modifiers changed from: private */
    public final TimeoutEventListener mTimeoutEventListener;

    public TimeoutEvent(TimeoutEventListener timeoutEventListener) {
        this.mTimeoutEventListener = timeoutEventListener;
        start();
    }

    public void start() {
        setFlashlightTimeout();
    }

    public void stop() {
        if (this.mTimeOutFlashOff != null) {
            this.mTimeOutFlashOff.cancel(ActionsApplication.getAppContext());
        }
        this.mTimeOutFlashOff = null;
    }

    private void setFlashlightTimeout() {
        if (this.mTimeOutFlashOff != null) {
            this.mTimeOutFlashOff.cancel(ActionsApplication.getAppContext());
        }
        FlashOnChopTimeout flashOnChopTimeout = new FlashOnChopTimeout(ActionsApplication.getAppContext(), this.mFlashlightOffTimeout, TIMEOUT_FLASSHOFF_MILLIS_TO_FIRE, ACTION_TIMEOUT_FLASHOFF);
        this.mTimeOutFlashOff = flashOnChopTimeout;
    }
}
