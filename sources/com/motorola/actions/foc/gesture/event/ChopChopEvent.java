package com.motorola.actions.foc.gesture.event;

import com.motorola.actions.ActionsApplication;
import com.motorola.actions.foc.gesture.SensorHubChopRecognizer;
import com.motorola.actions.foc.gesture.event.listener.ChopEventListener;

public class ChopChopEvent implements IEventSource {
    private final ChopEventListener mChopEventListener;
    private final SensorHubChopRecognizer mChopRecognizer = new SensorHubChopRecognizer(ActionsApplication.getAppContext());

    public ChopChopEvent(ChopEventListener chopEventListener) {
        this.mChopEventListener = chopEventListener;
        start();
    }

    public void start() {
        this.mChopRecognizer.startSampling();
        if (this.mChopEventListener != null) {
            this.mChopRecognizer.addChopListener(this.mChopEventListener);
        }
    }

    public void stop() {
        if (this.mChopEventListener != null) {
            this.mChopRecognizer.removeChopListener(this.mChopEventListener);
        }
        this.mChopRecognizer.stopSampling();
    }
}
