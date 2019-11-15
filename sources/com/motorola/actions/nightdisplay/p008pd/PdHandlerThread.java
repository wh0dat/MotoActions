package com.motorola.actions.nightdisplay.p008pd;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import com.motorola.actions.nightdisplay.common.PlatformAccess;
import com.motorola.actions.nightdisplay.p009pi.PIEvent;
import com.motorola.actions.nightdisplay.p009pi.PINightDisplayController;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.nightdisplay.pd.PdHandlerThread */
class PdHandlerThread extends HandlerThread {
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(PdHandlerThread.class);
    private static final String THREAD_NAME = "NightDisplayThread";
    private InternalHandler mHandler;
    /* access modifiers changed from: private */
    public final PINightDisplayController mPINightDisplayController;

    /* renamed from: com.motorola.actions.nightdisplay.pd.PdHandlerThread$InternalHandler */
    private class InternalHandler extends Handler {
        InternalHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            try {
                PdHandlerThread.this.mPINightDisplayController.onEvent((PIEvent) message.obj);
            } catch (Throwable th) {
                PdHandlerThread.LOGGER.mo11960e("Exception processing event", th);
            }
        }
    }

    PdHandlerThread(PlatformAccess platformAccess) {
        super(THREAD_NAME);
        this.mPINightDisplayController = new PINightDisplayController(platformAccess);
    }

    public void start() {
        if (getLooper() == null) {
            super.start();
            this.mHandler = new InternalHandler(getLooper());
        }
    }

    /* access modifiers changed from: 0000 */
    public void event(PIEvent pIEvent) {
        this.mHandler.sendMessage(Message.obtain(this.mHandler, pIEvent.getEvent().ordinal(), pIEvent));
    }
}
