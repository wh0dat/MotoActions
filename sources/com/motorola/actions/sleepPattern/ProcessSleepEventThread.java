package com.motorola.actions.sleepPattern;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import com.motorola.actions.nightdisplay.common.Persistence;
import com.motorola.actions.sleepPattern.preprocessing.Utils;
import com.motorola.actions.utils.MALogger;
import java.util.Calendar;
import javax.inject.Inject;

public final class ProcessSleepEventThread extends HandlerThread {
    private static final MALogger LOGGER = new MALogger(ProcessSleepEventThread.class);
    private static final String THREAD_NAME = "ProcessSleepEventThread";
    /* access modifiers changed from: private */
    public AccelerometerReceiver mAccelerometerReceiver;
    private ActivityRecognitionManager mActivityRecognitionManager;
    private Handler mHandler;
    /* access modifiers changed from: private */
    public SleepPatternProcessing mSleepPatternProcessing;

    private final class InternalHandler extends Handler {
        private InternalHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            if (message.what == MessageType.INIT.ordinal()) {
                ProcessSleepEventThread.this.mSleepPatternProcessing.processSleepPattern(false);
            } else if (message.what == MessageType.READ_ACCEL.ordinal()) {
                ProcessSleepEventThread.this.startAccelData();
            } else if (message.what == MessageType.SHUT_DOWN.ordinal()) {
                ProcessSleepEventThread.this.mAccelerometerReceiver.shutDown();
            }
        }
    }

    private enum MessageType {
        INIT,
        READ_ACCEL,
        SHUT_DOWN
    }

    @Inject
    public ProcessSleepEventThread(SleepPatternProcessing sleepPatternProcessing, AccelerometerReceiver accelerometerReceiver, ActivityRecognitionManager activityRecognitionManager) {
        super(THREAD_NAME);
        this.mSleepPatternProcessing = sleepPatternProcessing;
        this.mAccelerometerReceiver = accelerometerReceiver;
        this.mActivityRecognitionManager = activityRecognitionManager;
    }

    public void start() {
        if (getLooper() == null) {
            super.start();
            this.mHandler = new InternalHandler(getLooper());
            LOGGER.mo11957d("start()");
        }
        this.mHandler.sendMessage(Message.obtain(this.mHandler, MessageType.INIT.ordinal()));
    }

    /* access modifiers changed from: 0000 */
    public void processReadAccelEvent() {
        LOGGER.mo11957d("processReadAccelEvent");
        this.mHandler.sendMessage(Message.obtain(this.mHandler, MessageType.READ_ACCEL.ordinal()));
    }

    /* access modifiers changed from: 0000 */
    public void shutDown() {
        this.mHandler.sendMessage(Message.obtain(this.mHandler, MessageType.SHUT_DOWN.ordinal()));
    }

    /* access modifiers changed from: private */
    public void startAccelData() {
        Calendar instance = Calendar.getInstance();
        boolean isActivityRecognitionEnabled = Persistence.isActivityRecognitionEnabled();
        if (Utils.isValidAlarmInterval(instance)) {
            LOGGER.mo11957d("Start activity recognition data gathering");
            if (isActivityRecognitionEnabled) {
                LOGGER.mo11957d("Activity Recognition API selected as source");
                this.mAccelerometerReceiver.shutDown();
                this.mActivityRecognitionManager.startActivityData(this.mHandler);
                return;
            }
            LOGGER.mo11957d("Accelerometer selected as source");
            this.mActivityRecognitionManager.shutDown();
            this.mAccelerometerReceiver.startAccelData(this.mHandler);
            return;
        }
        if (isActivityRecognitionEnabled) {
            this.mActivityRecognitionManager.shutDown();
        } else {
            this.mAccelerometerReceiver.shutDown();
        }
        this.mSleepPatternProcessing.processSleepPattern(true);
    }
}
