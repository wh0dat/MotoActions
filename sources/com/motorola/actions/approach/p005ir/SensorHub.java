package com.motorola.actions.approach.p005ir;

import android.os.Handler;
import android.os.HandlerThread;
import com.motorola.actions.approach.p005ir.tuning.Tuning;
import com.motorola.actions.foc.config.FlashOnChopInterfaceConfig;
import com.motorola.actions.utils.Device;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.approach.ir.SensorHub */
public class SensorHub {
    private static final int GET_HUB_VERSION = 4;
    private static final int GET_MOTION_HISTORY = 6;
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(SensorHub.class);
    private static final long RETRY_DELAY_MS = 5000;
    private static final int SET_ALTERNATE_CONFIG = 3;
    private static final int SET_CHOP_CONFIG = 5;
    private static final int SET_IR_DISABLED = 1;
    private static final int SET_IR_TUNING = 2;
    private static final int SH_QC_NEW_VAR_VERSION = 37;
    private static HandlerThread sHandlerThread = null;
    /* access modifiers changed from: private */
    public static int sMotionHistory = -1;
    /* access modifiers changed from: private */
    public static int sVersion = -1;
    /* access modifiers changed from: private */
    public static Handler sWorker;

    /* renamed from: com.motorola.actions.approach.ir.SensorHub$FlashOnChopWrapper */
    private static class FlashOnChopWrapper {
        FlashOnChopInterfaceConfig mConfig;
        SensorHubResultListener mListener;

        private FlashOnChopWrapper() {
        }
    }

    /* renamed from: com.motorola.actions.approach.ir.SensorHub$SensorHubMotionHistoryListener */
    public interface SensorHubMotionHistoryListener {
        void onMotionHistory(int i);
    }

    /* renamed from: com.motorola.actions.approach.ir.SensorHub$SensorHubResultListener */
    public interface SensorHubResultListener {
        void onCommandComplete(boolean z);
    }

    /* renamed from: com.motorola.actions.approach.ir.SensorHub$SensorHubType */
    public enum SensorHubType {
        STML0xx,
        STM401,
        UNKNOWN
    }

    /* renamed from: com.motorola.actions.approach.ir.SensorHub$SensorHubVersionListener */
    public interface SensorHubVersionListener {
        void onGetVersion(int i);
    }

    /* access modifiers changed from: private */
    public static native int nativeGetIRConfigVersion();

    /* access modifiers changed from: private */
    public static native int nativeGetMotionHistory();

    /* access modifiers changed from: private */
    public static native int nativeGetMotionHistoryL4();

    private static native int nativeGetSHVersion();

    /* access modifiers changed from: private */
    public static native int nativeGetVersion();

    /* access modifiers changed from: private */
    public static native boolean nativeSetAlternateConfig();

    /* access modifiers changed from: private */
    public static native boolean nativeSetChopConfig(short[] sArr);

    /* access modifiers changed from: private */
    public static native boolean nativeSetChopL4Config(short[] sArr);

    /* access modifiers changed from: private */
    public static native boolean nativeSetIRDisabled(boolean z);

    private static native boolean nativeSetLiftL4Config(short[] sArr);

    private static native boolean nativeSetQCConfig(short[] sArr);

    private static native boolean nativeSetQCConfigWithNewVars(short[] sArr);

    static {
        try {
            System.loadLibrary("actions_irservice_stm");
            setupWorker();
        } catch (UnsatisfiedLinkError e) {
            LOGGER.mo11960e("Failed to find native irservice library", e);
        }
    }

    public static void setIRDisabled(boolean z) {
        if (sWorker != null) {
            sWorker.sendMessage(sWorker.obtainMessage(1, Boolean.valueOf(z)));
        }
    }

    public static void setIRTuning(Tuning tuning) {
        if (sWorker != null) {
            sWorker.sendMessage(sWorker.obtainMessage(2, tuning));
        }
    }

    public static int getStoredHubVersion() {
        return sVersion;
    }

    public static void getHubVersion(SensorHubVersionListener sensorHubVersionListener) {
        if (sWorker != null) {
            sWorker.sendMessage(sWorker.obtainMessage(4, sensorHubVersionListener));
        }
    }

    public static void setChopConfig(FlashOnChopInterfaceConfig flashOnChopInterfaceConfig, SensorHubResultListener sensorHubResultListener) {
        if (sWorker != null) {
            FlashOnChopWrapper flashOnChopWrapper = new FlashOnChopWrapper();
            flashOnChopWrapper.mConfig = flashOnChopInterfaceConfig;
            flashOnChopWrapper.mListener = sensorHubResultListener;
            sWorker.sendMessage(sWorker.obtainMessage(5, flashOnChopWrapper));
        }
    }

    public static void getMotionHistory(SensorHubMotionHistoryListener sensorHubMotionHistoryListener) {
        if (sWorker != null) {
            sWorker.sendMessage(sWorker.obtainMessage(6, sensorHubMotionHistoryListener));
        }
    }

    private static void setupWorker() {
        sHandlerThread = new HandlerThread("stm worker thread");
        sHandlerThread.start();
        sWorker = new Handler(sHandlerThread.getLooper()) {
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void handleMessage(android.os.Message r6) {
                /*
                    r5 = this;
                    int r0 = r6.what     // Catch:{ Throwable -> 0x0251 }
                    switch(r0) {
                        case 1: goto L_0x01e7;
                        case 2: goto L_0x0178;
                        case 3: goto L_0x0166;
                        case 4: goto L_0x0123;
                        case 5: goto L_0x0069;
                        case 6: goto L_0x000b;
                        default: goto L_0x0005;
                    }     // Catch:{ Throwable -> 0x0251 }
                L_0x0005:
                    com.motorola.actions.utils.MALogger r0 = com.motorola.actions.approach.p005ir.SensorHub.LOGGER     // Catch:{ Throwable -> 0x0251 }
                    goto L_0x023a
                L_0x000b:
                    java.lang.Object r0 = r6.obj     // Catch:{ Throwable -> 0x0251 }
                    boolean r0 = r0 instanceof com.motorola.actions.approach.p005ir.SensorHub.SensorHubMotionHistoryListener     // Catch:{ Throwable -> 0x0251 }
                    if (r0 != 0) goto L_0x0034
                    com.motorola.actions.utils.MALogger r0 = com.motorola.actions.approach.p005ir.SensorHub.LOGGER     // Catch:{ Throwable -> 0x0251 }
                    java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0251 }
                    r1.<init>()     // Catch:{ Throwable -> 0x0251 }
                    java.lang.String r2 = "Unexpected type sent for GET_MOTION_HISTORY: "
                    r1.append(r2)     // Catch:{ Throwable -> 0x0251 }
                    java.lang.Object r2 = r6.obj     // Catch:{ Throwable -> 0x0251 }
                    java.lang.Class r2 = r2.getClass()     // Catch:{ Throwable -> 0x0251 }
                    java.lang.String r2 = r2.getName()     // Catch:{ Throwable -> 0x0251 }
                    r1.append(r2)     // Catch:{ Throwable -> 0x0251 }
                    java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x0251 }
                    r0.mo11959e(r1)     // Catch:{ Throwable -> 0x0251 }
                    return
                L_0x0034:
                    com.motorola.actions.approach.ir.SensorHub$SensorHubType r0 = com.motorola.actions.approach.p005ir.SensorHub.getSensorHubType()     // Catch:{ Throwable -> 0x0251 }
                    com.motorola.actions.approach.ir.SensorHub$SensorHubType r1 = com.motorola.actions.approach.p005ir.SensorHub.SensorHubType.STML0xx     // Catch:{ Throwable -> 0x0251 }
                    if (r0 != r1) goto L_0x0044
                    int r0 = com.motorola.actions.approach.p005ir.SensorHub.nativeGetMotionHistory()     // Catch:{ Throwable -> 0x0251 }
                    com.motorola.actions.approach.p005ir.SensorHub.sMotionHistory = r0     // Catch:{ Throwable -> 0x0251 }
                    goto L_0x0053
                L_0x0044:
                    com.motorola.actions.approach.ir.SensorHub$SensorHubType r0 = com.motorola.actions.approach.p005ir.SensorHub.getSensorHubType()     // Catch:{ Throwable -> 0x0251 }
                    com.motorola.actions.approach.ir.SensorHub$SensorHubType r1 = com.motorola.actions.approach.p005ir.SensorHub.SensorHubType.STM401     // Catch:{ Throwable -> 0x0251 }
                    if (r0 != r1) goto L_0x005f
                    int r0 = com.motorola.actions.approach.p005ir.SensorHub.nativeGetMotionHistoryL4()     // Catch:{ Throwable -> 0x0251 }
                    com.motorola.actions.approach.p005ir.SensorHub.sMotionHistory = r0     // Catch:{ Throwable -> 0x0251 }
                L_0x0053:
                    java.lang.Object r0 = r6.obj     // Catch:{ Throwable -> 0x0251 }
                    com.motorola.actions.approach.ir.SensorHub$SensorHubMotionHistoryListener r0 = (com.motorola.actions.approach.p005ir.SensorHub.SensorHubMotionHistoryListener) r0     // Catch:{ Throwable -> 0x0251 }
                    int r1 = com.motorola.actions.approach.p005ir.SensorHub.sMotionHistory     // Catch:{ Throwable -> 0x0251 }
                    r0.onMotionHistory(r1)     // Catch:{ Throwable -> 0x0251 }
                    return
                L_0x005f:
                    com.motorola.actions.utils.MALogger r0 = com.motorola.actions.approach.p005ir.SensorHub.LOGGER     // Catch:{ Throwable -> 0x0251 }
                    java.lang.String r1 = "Device do not have sh w/ Motion History enabled: "
                    r0.mo11959e(r1)     // Catch:{ Throwable -> 0x0251 }
                    return
                L_0x0069:
                    java.lang.Object r0 = r6.obj     // Catch:{ Throwable -> 0x0251 }
                    boolean r0 = r0 instanceof com.motorola.actions.approach.p005ir.SensorHub.FlashOnChopWrapper     // Catch:{ Throwable -> 0x0251 }
                    if (r0 != 0) goto L_0x0092
                    com.motorola.actions.utils.MALogger r0 = com.motorola.actions.approach.p005ir.SensorHub.LOGGER     // Catch:{ Throwable -> 0x0251 }
                    java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0251 }
                    r1.<init>()     // Catch:{ Throwable -> 0x0251 }
                    java.lang.String r2 = "Unexpected type sent for SET_CHOP_CONFIG: "
                    r1.append(r2)     // Catch:{ Throwable -> 0x0251 }
                    java.lang.Object r2 = r6.obj     // Catch:{ Throwable -> 0x0251 }
                    java.lang.Class r2 = r2.getClass()     // Catch:{ Throwable -> 0x0251 }
                    java.lang.String r2 = r2.getName()     // Catch:{ Throwable -> 0x0251 }
                    r1.append(r2)     // Catch:{ Throwable -> 0x0251 }
                    java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x0251 }
                    r0.mo11959e(r1)     // Catch:{ Throwable -> 0x0251 }
                    return
                L_0x0092:
                    java.lang.Object r0 = r6.obj     // Catch:{ Throwable -> 0x0251 }
                    com.motorola.actions.approach.ir.SensorHub$FlashOnChopWrapper r0 = (com.motorola.actions.approach.p005ir.SensorHub.FlashOnChopWrapper) r0     // Catch:{ Throwable -> 0x0251 }
                    com.motorola.actions.foc.config.FlashOnChopInterfaceConfig r1 = r0.mConfig     // Catch:{ Throwable -> 0x0251 }
                    com.motorola.actions.foc.config.FlashOnChopInterfaceConfig$FlashOnChipsetType r1 = r1.getFlashOnChipsetType()     // Catch:{ Throwable -> 0x0251 }
                    com.motorola.actions.foc.config.FlashOnChopInterfaceConfig$FlashOnChipsetType r2 = com.motorola.actions.foc.config.FlashOnChopInterfaceConfig.FlashOnChipsetType.FlashOnChopL0     // Catch:{ Throwable -> 0x0251 }
                    r3 = 1
                    r4 = 0
                    if (r1 != r2) goto L_0x00cc
                    com.motorola.actions.foc.config.FlashOnChopInterfaceConfig r1 = r0.mConfig     // Catch:{ Throwable -> 0x0251 }
                    short[] r1 = r1.getConfigAsArray()     // Catch:{ Throwable -> 0x0251 }
                    boolean r1 = com.motorola.actions.approach.p005ir.SensorHub.nativeSetChopConfig(r1)     // Catch:{ Throwable -> 0x0251 }
                    if (r1 != 0) goto L_0x00bd
                    com.motorola.actions.utils.MALogger r1 = com.motorola.actions.approach.p005ir.SensorHub.LOGGER     // Catch:{ Throwable -> 0x0251 }
                    java.lang.String r2 = "Could not apply Chop L0 configuration"
                    r1.mo11959e(r2)     // Catch:{ Throwable -> 0x0251 }
                    com.motorola.actions.approach.ir.SensorHub$SensorHubResultListener r0 = r0.mListener     // Catch:{ Throwable -> 0x0251 }
                    r0.onCommandComplete(r4)     // Catch:{ Throwable -> 0x0251 }
                    goto L_0x00ff
                L_0x00bd:
                    com.motorola.actions.utils.MALogger r1 = com.motorola.actions.approach.p005ir.SensorHub.LOGGER     // Catch:{ Throwable -> 0x0251 }
                    java.lang.String r2 = "Applied chop L0 configuration successfully"
                    r1.mo11959e(r2)     // Catch:{ Throwable -> 0x0251 }
                    com.motorola.actions.approach.ir.SensorHub$SensorHubResultListener r0 = r0.mListener     // Catch:{ Throwable -> 0x0251 }
                    r0.onCommandComplete(r3)     // Catch:{ Throwable -> 0x0251 }
                    goto L_0x00ff
                L_0x00cc:
                    com.motorola.actions.foc.config.FlashOnChopInterfaceConfig r1 = r0.mConfig     // Catch:{ Throwable -> 0x0251 }
                    com.motorola.actions.foc.config.FlashOnChopInterfaceConfig$FlashOnChipsetType r1 = r1.getFlashOnChipsetType()     // Catch:{ Throwable -> 0x0251 }
                    com.motorola.actions.foc.config.FlashOnChopInterfaceConfig$FlashOnChipsetType r2 = com.motorola.actions.foc.config.FlashOnChopInterfaceConfig.FlashOnChipsetType.FlashOnChopL4     // Catch:{ Throwable -> 0x0251 }
                    if (r1 != r2) goto L_0x0100
                    com.motorola.actions.foc.config.FlashOnChopInterfaceConfig r1 = r0.mConfig     // Catch:{ Throwable -> 0x0251 }
                    short[] r1 = r1.getConfigAsArray()     // Catch:{ Throwable -> 0x0251 }
                    boolean r1 = com.motorola.actions.approach.p005ir.SensorHub.nativeSetChopL4Config(r1)     // Catch:{ Throwable -> 0x0251 }
                    if (r1 != 0) goto L_0x00f1
                    com.motorola.actions.utils.MALogger r1 = com.motorola.actions.approach.p005ir.SensorHub.LOGGER     // Catch:{ Throwable -> 0x0251 }
                    java.lang.String r2 = "Could not apply Chop L4 configuration"
                    r1.mo11959e(r2)     // Catch:{ Throwable -> 0x0251 }
                    com.motorola.actions.approach.ir.SensorHub$SensorHubResultListener r0 = r0.mListener     // Catch:{ Throwable -> 0x0251 }
                    r0.onCommandComplete(r4)     // Catch:{ Throwable -> 0x0251 }
                    goto L_0x00ff
                L_0x00f1:
                    com.motorola.actions.utils.MALogger r1 = com.motorola.actions.approach.p005ir.SensorHub.LOGGER     // Catch:{ Throwable -> 0x0251 }
                    java.lang.String r2 = "Applied chop L4 configuration successfully"
                    r1.mo11959e(r2)     // Catch:{ Throwable -> 0x0251 }
                    com.motorola.actions.approach.ir.SensorHub$SensorHubResultListener r0 = r0.mListener     // Catch:{ Throwable -> 0x0251 }
                    r0.onCommandComplete(r3)     // Catch:{ Throwable -> 0x0251 }
                L_0x00ff:
                    return
                L_0x0100:
                    com.motorola.actions.utils.MALogger r0 = com.motorola.actions.approach.p005ir.SensorHub.LOGGER     // Catch:{ Throwable -> 0x0251 }
                    java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0251 }
                    r1.<init>()     // Catch:{ Throwable -> 0x0251 }
                    java.lang.String r2 = "Unexpected type of FlashOnChipsetType "
                    r1.append(r2)     // Catch:{ Throwable -> 0x0251 }
                    java.lang.Object r2 = r6.obj     // Catch:{ Throwable -> 0x0251 }
                    java.lang.Class r2 = r2.getClass()     // Catch:{ Throwable -> 0x0251 }
                    java.lang.String r2 = r2.getName()     // Catch:{ Throwable -> 0x0251 }
                    r1.append(r2)     // Catch:{ Throwable -> 0x0251 }
                    java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x0251 }
                    r0.mo11959e(r1)     // Catch:{ Throwable -> 0x0251 }
                    return
                L_0x0123:
                    java.lang.Object r0 = r6.obj     // Catch:{ Throwable -> 0x0251 }
                    boolean r0 = r0 instanceof com.motorola.actions.approach.p005ir.SensorHub.SensorHubVersionListener     // Catch:{ Throwable -> 0x0251 }
                    if (r0 != 0) goto L_0x014c
                    com.motorola.actions.utils.MALogger r0 = com.motorola.actions.approach.p005ir.SensorHub.LOGGER     // Catch:{ Throwable -> 0x0251 }
                    java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0251 }
                    r1.<init>()     // Catch:{ Throwable -> 0x0251 }
                    java.lang.String r2 = "Unexpected type sent for GET_HUB_VERSION: "
                    r1.append(r2)     // Catch:{ Throwable -> 0x0251 }
                    java.lang.Object r2 = r6.obj     // Catch:{ Throwable -> 0x0251 }
                    java.lang.Class r2 = r2.getClass()     // Catch:{ Throwable -> 0x0251 }
                    java.lang.String r2 = r2.getName()     // Catch:{ Throwable -> 0x0251 }
                    r1.append(r2)     // Catch:{ Throwable -> 0x0251 }
                    java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x0251 }
                    r0.mo11959e(r1)     // Catch:{ Throwable -> 0x0251 }
                    return
                L_0x014c:
                    int r0 = com.motorola.actions.approach.p005ir.SensorHub.sVersion     // Catch:{ Throwable -> 0x0251 }
                    r1 = -1
                    if (r0 != r1) goto L_0x015a
                    int r0 = com.motorola.actions.approach.p005ir.SensorHub.nativeGetVersion()     // Catch:{ Throwable -> 0x0251 }
                    com.motorola.actions.approach.p005ir.SensorHub.sVersion = r0     // Catch:{ Throwable -> 0x0251 }
                L_0x015a:
                    java.lang.Object r0 = r6.obj     // Catch:{ Throwable -> 0x0251 }
                    com.motorola.actions.approach.ir.SensorHub$SensorHubVersionListener r0 = (com.motorola.actions.approach.p005ir.SensorHub.SensorHubVersionListener) r0     // Catch:{ Throwable -> 0x0251 }
                    int r1 = com.motorola.actions.approach.p005ir.SensorHub.sVersion     // Catch:{ Throwable -> 0x0251 }
                    r0.onGetVersion(r1)     // Catch:{ Throwable -> 0x0251 }
                    return
                L_0x0166:
                    boolean r0 = com.motorola.actions.approach.p005ir.SensorHub.nativeSetAlternateConfig()     // Catch:{ Throwable -> 0x0251 }
                    if (r0 == 0) goto L_0x016d
                    return
                L_0x016d:
                    com.motorola.actions.utils.MALogger r0 = com.motorola.actions.approach.p005ir.SensorHub.LOGGER     // Catch:{ Throwable -> 0x0251 }
                    java.lang.String r1 = "Unknown error setting IR alternate configuration."
                    r0.mo11959e(r1)     // Catch:{ Throwable -> 0x0251 }
                    goto L_0x026c
                L_0x0178:
                    java.lang.Object r0 = r6.obj     // Catch:{ Throwable -> 0x0251 }
                    boolean r0 = r0 instanceof com.motorola.actions.approach.p005ir.tuning.Tuning     // Catch:{ Throwable -> 0x0251 }
                    if (r0 != 0) goto L_0x01a1
                    com.motorola.actions.utils.MALogger r0 = com.motorola.actions.approach.p005ir.SensorHub.LOGGER     // Catch:{ Throwable -> 0x0251 }
                    java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0251 }
                    r1.<init>()     // Catch:{ Throwable -> 0x0251 }
                    java.lang.String r2 = "Unexpected type sent for SET_IR_TUNING: "
                    r1.append(r2)     // Catch:{ Throwable -> 0x0251 }
                    java.lang.Object r2 = r6.obj     // Catch:{ Throwable -> 0x0251 }
                    java.lang.Class r2 = r2.getClass()     // Catch:{ Throwable -> 0x0251 }
                    java.lang.String r2 = r2.getName()     // Catch:{ Throwable -> 0x0251 }
                    r1.append(r2)     // Catch:{ Throwable -> 0x0251 }
                    java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x0251 }
                    r0.mo11959e(r1)     // Catch:{ Throwable -> 0x0251 }
                    return
                L_0x01a1:
                    java.lang.Object r0 = r6.obj     // Catch:{ VersionNotFoundException -> 0x01bd, Throwable -> 0x01bb }
                    com.motorola.actions.approach.ir.tuning.Tuning r0 = (com.motorola.actions.approach.p005ir.tuning.Tuning) r0     // Catch:{ VersionNotFoundException -> 0x01bd, Throwable -> 0x01bb }
                    int r1 = com.motorola.actions.approach.p005ir.SensorHub.nativeGetIRConfigVersion()     // Catch:{ VersionNotFoundException -> 0x01bd, Throwable -> 0x01bb }
                    boolean r0 = r0.applyConfig(r1)     // Catch:{ VersionNotFoundException -> 0x01bd, Throwable -> 0x01bb }
                    if (r0 == 0) goto L_0x01b0
                    return
                L_0x01b0:
                    com.motorola.actions.utils.MALogger r0 = com.motorola.actions.approach.p005ir.SensorHub.LOGGER     // Catch:{ Throwable -> 0x0251 }
                    java.lang.String r1 = "Unknown error setting IR Tuning parameters"
                    r0.mo11959e(r1)     // Catch:{ Throwable -> 0x0251 }
                    goto L_0x026c
                L_0x01bb:
                    r0 = move-exception
                    throw r0     // Catch:{ Throwable -> 0x0251 }
                L_0x01bd:
                    r0 = move-exception
                    com.motorola.actions.utils.MALogger r1 = com.motorola.actions.approach.p005ir.SensorHub.LOGGER     // Catch:{ Throwable -> 0x0251 }
                    java.lang.String r2 = "Could not apply configuration"
                    r1.mo11960e(r2, r0)     // Catch:{ Throwable -> 0x0251 }
                    boolean r0 = com.motorola.actions.utils.Device.isWhiteDevice()     // Catch:{ Throwable -> 0x0251 }
                    if (r0 == 0) goto L_0x01e6
                    com.motorola.actions.utils.MALogger r0 = com.motorola.actions.approach.p005ir.SensorHub.LOGGER     // Catch:{ Throwable -> 0x0251 }
                    java.lang.String r1 = "Setting alternate configuration on SH"
                    r0.mo11957d(r1)     // Catch:{ Throwable -> 0x0251 }
                    android.os.Handler r0 = com.motorola.actions.approach.p005ir.SensorHub.sWorker     // Catch:{ Throwable -> 0x0251 }
                    android.os.Handler r1 = com.motorola.actions.approach.p005ir.SensorHub.sWorker     // Catch:{ Throwable -> 0x0251 }
                    r2 = 3
                    android.os.Message r1 = r1.obtainMessage(r2)     // Catch:{ Throwable -> 0x0251 }
                    r0.sendMessage(r1)     // Catch:{ Throwable -> 0x0251 }
                L_0x01e6:
                    return
                L_0x01e7:
                    java.lang.Object r0 = r6.obj     // Catch:{ Throwable -> 0x0251 }
                    boolean r0 = r0 instanceof java.lang.Boolean     // Catch:{ Throwable -> 0x0251 }
                    if (r0 != 0) goto L_0x0210
                    com.motorola.actions.utils.MALogger r0 = com.motorola.actions.approach.p005ir.SensorHub.LOGGER     // Catch:{ Throwable -> 0x0251 }
                    java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0251 }
                    r1.<init>()     // Catch:{ Throwable -> 0x0251 }
                    java.lang.String r2 = "Unexpected type sent for SET_IR_DISABLED: "
                    r1.append(r2)     // Catch:{ Throwable -> 0x0251 }
                    java.lang.Object r2 = r6.obj     // Catch:{ Throwable -> 0x0251 }
                    java.lang.Class r2 = r2.getClass()     // Catch:{ Throwable -> 0x0251 }
                    java.lang.String r2 = r2.getName()     // Catch:{ Throwable -> 0x0251 }
                    r1.append(r2)     // Catch:{ Throwable -> 0x0251 }
                    java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x0251 }
                    r0.mo11959e(r1)     // Catch:{ Throwable -> 0x0251 }
                    return
                L_0x0210:
                    java.lang.Object r0 = r6.obj     // Catch:{ Throwable -> 0x0251 }
                    java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ Throwable -> 0x0251 }
                    boolean r0 = r0.booleanValue()     // Catch:{ Throwable -> 0x0251 }
                    boolean r0 = com.motorola.actions.approach.p005ir.SensorHub.nativeSetIRDisabled(r0)     // Catch:{ Throwable -> 0x0251 }
                    if (r0 == 0) goto L_0x021f
                    return
                L_0x021f:
                    com.motorola.actions.utils.MALogger r0 = com.motorola.actions.approach.p005ir.SensorHub.LOGGER     // Catch:{ Throwable -> 0x0251 }
                    java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0251 }
                    r1.<init>()     // Catch:{ Throwable -> 0x0251 }
                    java.lang.String r2 = "Error setting IR disabled state to "
                    r1.append(r2)     // Catch:{ Throwable -> 0x0251 }
                    java.lang.Object r2 = r6.obj     // Catch:{ Throwable -> 0x0251 }
                    r1.append(r2)     // Catch:{ Throwable -> 0x0251 }
                    java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x0251 }
                    r0.mo11959e(r1)     // Catch:{ Throwable -> 0x0251 }
                    goto L_0x026c
                L_0x023a:
                    java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0251 }
                    r1.<init>()     // Catch:{ Throwable -> 0x0251 }
                    java.lang.String r2 = "Unexpected request: "
                    r1.append(r2)     // Catch:{ Throwable -> 0x0251 }
                    int r2 = r6.what     // Catch:{ Throwable -> 0x0251 }
                    r1.append(r2)     // Catch:{ Throwable -> 0x0251 }
                    java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x0251 }
                    r0.mo11959e(r1)     // Catch:{ Throwable -> 0x0251 }
                    return
                L_0x0251:
                    r0 = move-exception
                    com.motorola.actions.utils.MALogger r1 = com.motorola.actions.approach.p005ir.SensorHub.LOGGER
                    java.lang.StringBuilder r2 = new java.lang.StringBuilder
                    r2.<init>()
                    java.lang.String r3 = "Exception talking to sensorhub, request: "
                    r2.append(r3)
                    int r3 = r6.what
                    r2.append(r3)
                    java.lang.String r2 = r2.toString()
                    r1.mo11960e(r2, r0)
                L_0x026c:
                    int r0 = r6.what
                    java.lang.Object r6 = r6.obj
                    android.os.Message r6 = r5.obtainMessage(r0, r6)
                    r5.sendMessageAtFrontOfQueue(r6)
                    r5 = 5000(0x1388, double:2.4703E-320)
                    android.os.SystemClock.sleep(r5)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.motorola.actions.approach.p005ir.SensorHub.C05191.handleMessage(android.os.Message):void");
            }
        };
    }

    public static SensorHubType getSensorHubType() {
        if (Device.isAffinityDevice() || Device.isDanteDevice() || Device.isCopperfieldDevice()) {
            return SensorHubType.STML0xx;
        }
        if (Device.isVertexDevice() || Device.isVectorDevice()) {
            return SensorHubType.STM401;
        }
        return SensorHubType.UNKNOWN;
    }
}
