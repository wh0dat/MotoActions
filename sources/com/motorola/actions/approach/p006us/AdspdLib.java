package com.motorola.actions.approach.p006us;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import com.motorola.actions.utils.Device;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.approach.us.AdspdLib */
public class AdspdLib {
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(AdspdLib.class);
    private static HandlerThread sHandlerThread;
    private static Handler sWorker;

    /* renamed from: com.motorola.actions.approach.us.AdspdLib$2 */
    static /* synthetic */ class C05212 {
        static final /* synthetic */ int[] $SwitchMap$com$motorola$actions$approach$us$AdspdLib$MessageType = new int[MessageType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        static {
            /*
                com.motorola.actions.approach.us.AdspdLib$MessageType[] r0 = com.motorola.actions.approach.p006us.AdspdLib.MessageType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$motorola$actions$approach$us$AdspdLib$MessageType = r0
                int[] r0 = $SwitchMap$com$motorola$actions$approach$us$AdspdLib$MessageType     // Catch:{ NoSuchFieldError -> 0x0014 }
                com.motorola.actions.approach.us.AdspdLib$MessageType r1 = com.motorola.actions.approach.p006us.AdspdLib.MessageType.START_ULTRASOUND     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = $SwitchMap$com$motorola$actions$approach$us$AdspdLib$MessageType     // Catch:{ NoSuchFieldError -> 0x001f }
                com.motorola.actions.approach.us.AdspdLib$MessageType r1 = com.motorola.actions.approach.p006us.AdspdLib.MessageType.STOP_ULTRASOUND     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = $SwitchMap$com$motorola$actions$approach$us$AdspdLib$MessageType     // Catch:{ NoSuchFieldError -> 0x002a }
                com.motorola.actions.approach.us.AdspdLib$MessageType r1 = com.motorola.actions.approach.p006us.AdspdLib.MessageType.RETRIEVE_INSTRUMENTATION     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.motorola.actions.approach.p006us.AdspdLib.C05212.<clinit>():void");
        }
    }

    /* renamed from: com.motorola.actions.approach.us.AdspdLib$InstrumentationData */
    private static class InstrumentationData {
        public int mMotionBeforeInterference;
        public int mMotionNoInterference;
        public int mNoMotionBeforeInterference;
        public int mNoMotionNoInterference;

        private InstrumentationData() {
        }
    }

    /* renamed from: com.motorola.actions.approach.us.AdspdLib$MessageType */
    enum MessageType {
        START_ULTRASOUND,
        STOP_ULTRASOUND,
        RETRIEVE_INSTRUMENTATION
    }

    /* renamed from: com.motorola.actions.approach.us.AdspdLib$UltrasoundConfigListener */
    public interface UltrasoundConfigListener {

        /* renamed from: com.motorola.actions.approach.us.AdspdLib$UltrasoundConfigListener$Result */
        public enum Result {
            Success,
            ConnectionFailure,
            ControlError
        }

        void onFinishUltrasoundConfig(String str, Result result);
    }

    /* renamed from: com.motorola.actions.approach.us.AdspdLib$UltrasoundInstrumentationListener */
    public interface UltrasoundInstrumentationListener extends UltrasoundConfigListener {
        void onInstrumentationRetrieve(int i, int i2, int i3, int i4);
    }

    /* access modifiers changed from: private */
    public static native boolean native_adspd_ultrasound_connect();

    /* access modifiers changed from: private */
    public static native void native_adspd_ultrasound_disconnect();

    /* access modifiers changed from: private */
    public static native InstrumentationData native_adspd_ultrasound_retrieve_instrumentation();

    /* access modifiers changed from: private */
    public static native boolean native_adspd_ultrasound_start();

    /* access modifiers changed from: private */
    public static native boolean native_adspd_ultrasound_stop();

    static {
        try {
            if (!Device.isIbisDevice() && !Device.isGoldenEagleDevice() && !Device.isHoudiniDevice()) {
                if (!Device.isVertexDevice()) {
                    LOGGER.mo11957d("Using HIDL adsp implementation.");
                    System.loadLibrary("adspd");
                    System.loadLibrary("actions_adspd_hidl");
                    setupWorker();
                }
            }
            LOGGER.mo11957d("Using socket adsp implementation.");
            System.loadLibrary("adspd_socket");
            System.loadLibrary("actions_adspd_socket");
            setupWorker();
        } catch (UnsatisfiedLinkError e) {
            LOGGER.mo11960e("Failed to find native AdspdLib library", e);
        }
    }

    public static void startUltrasound(UltrasoundConfigListener ultrasoundConfigListener) {
        if (sWorker != null) {
            sWorker.sendMessage(sWorker.obtainMessage(MessageType.START_ULTRASOUND.ordinal(), ultrasoundConfigListener));
        }
    }

    public static void stopUltrasound(UltrasoundConfigListener ultrasoundConfigListener) {
        if (sWorker != null) {
            sWorker.sendMessage(sWorker.obtainMessage(MessageType.STOP_ULTRASOUND.ordinal(), ultrasoundConfigListener));
        }
    }

    public static void retrieveInstrumentation(UltrasoundInstrumentationListener ultrasoundInstrumentationListener) {
        if (sWorker != null) {
            sWorker.sendMessage(sWorker.obtainMessage(MessageType.RETRIEVE_INSTRUMENTATION.ordinal(), ultrasoundInstrumentationListener));
        }
    }

    private static void setupWorker() {
        sHandlerThread = new HandlerThread("stm worker thread");
        sHandlerThread.start();
        sWorker = new Handler(sHandlerThread.getLooper()) {
            public void handleMessage(Message message) {
                MessageType messageType = MessageType.values()[message.what];
                MALogger access$000 = AdspdLib.LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("MessageType=");
                sb.append(messageType);
                access$000.mo11957d(sb.toString());
                String messageType2 = messageType.toString();
                try {
                    switch (C05212.$SwitchMap$com$motorola$actions$approach$us$AdspdLib$MessageType[messageType.ordinal()]) {
                        case 1:
                        case 2:
                            if (message.obj instanceof UltrasoundConfigListener) {
                                UltrasoundConfigListener ultrasoundConfigListener = (UltrasoundConfigListener) message.obj;
                                Result result = Result.Success;
                                if (!AdspdLib.native_adspd_ultrasound_connect()) {
                                    result = Result.ConnectionFailure;
                                } else if (messageType == MessageType.START_ULTRASOUND && !AdspdLib.native_adspd_ultrasound_start()) {
                                    result = Result.ControlError;
                                } else if (messageType == MessageType.STOP_ULTRASOUND && !AdspdLib.native_adspd_ultrasound_stop()) {
                                    result = Result.ControlError;
                                }
                                AdspdLib.native_adspd_ultrasound_disconnect();
                                ultrasoundConfigListener.onFinishUltrasoundConfig(messageType2, result);
                                break;
                            } else {
                                MALogger access$0002 = AdspdLib.LOGGER;
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("Unexpected type sent for INIT_ULTRASOUND: ");
                                sb2.append(message.obj.getClass().getName());
                                access$0002.mo11959e(sb2.toString());
                                return;
                            }
                            break;
                        case 3:
                            if (message.obj instanceof UltrasoundInstrumentationListener) {
                                UltrasoundInstrumentationListener ultrasoundInstrumentationListener = (UltrasoundInstrumentationListener) message.obj;
                                Result result2 = Result.Success;
                                if (!AdspdLib.native_adspd_ultrasound_connect()) {
                                    result2 = Result.ConnectionFailure;
                                } else {
                                    InstrumentationData access$500 = AdspdLib.native_adspd_ultrasound_retrieve_instrumentation();
                                    if (access$500 != null) {
                                        ultrasoundInstrumentationListener.onInstrumentationRetrieve(access$500.mNoMotionNoInterference, access$500.mMotionNoInterference, access$500.mNoMotionBeforeInterference, access$500.mMotionBeforeInterference);
                                    } else {
                                        result2 = Result.ControlError;
                                    }
                                }
                                AdspdLib.native_adspd_ultrasound_disconnect();
                                ultrasoundInstrumentationListener.onFinishUltrasoundConfig(messageType2, result2);
                                break;
                            } else {
                                MALogger access$0003 = AdspdLib.LOGGER;
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("Unexpected type sent for INIT_ULTRASOUND: ");
                                sb3.append(message.obj.getClass().getName());
                                access$0003.mo11959e(sb3.toString());
                                return;
                            }
                        default:
                            MALogger access$0004 = AdspdLib.LOGGER;
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append("Unexpected request: ");
                            sb4.append(message.what);
                            access$0004.mo11959e(sb4.toString());
                            break;
                    }
                } catch (Throwable th) {
                    MALogger access$0005 = AdspdLib.LOGGER;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("Exception talking to adspdlib, request: ");
                    sb5.append(message.what);
                    access$0005.mo11960e(sb5.toString(), th);
                }
            }
        };
    }
}
