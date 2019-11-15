package com.motorola.actions.reflect;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.PowerManager;
import com.motorola.actions.utils.MALogger;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressLint({"PrivateApi"})
public class PowerManagerPrivateProxy {
    public static final String ACTION_SCREEN_BRIGHT;
    public static final String ACTION_SCREEN_DIM;
    public static final String ACTION_SCREEN_OFF_REASON;
    private static final String ACTION_SCREEN_PRE_DIM;
    private static final int DEFAULT_GO_TO_SLEEP_REASON_TIMEOUT = 2;
    public static final String EXTRA_SCREEN_OFF_REASON;
    public static final int GO_TO_SLEEP_REASON_TIMEOUT;
    private static final boolean HAS_HOOKS;
    private static final MALogger LOGGER = new MALogger(PowerManagerPrivateProxy.class);
    private static final Method SET_QUICK_DIM;
    private static final Method SET_QUICK_OFF;
    private PowerManager mPowerManager;

    /* JADX WARNING: type inference failed for: r8v0, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r7v0, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r6v0, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r5v1, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r3v1, types: [java.lang.reflect.Method] */
    /* JADX WARNING: type inference failed for: r8v1 */
    /* JADX WARNING: type inference failed for: r7v1 */
    /* JADX WARNING: type inference failed for: r6v1 */
    /* JADX WARNING: type inference failed for: r5v2 */
    /* JADX WARNING: type inference failed for: r3v2 */
    /* JADX WARNING: type inference failed for: r8v2 */
    /* JADX WARNING: type inference failed for: r7v2 */
    /* JADX WARNING: type inference failed for: r6v2 */
    /* JADX WARNING: type inference failed for: r5v3 */
    /* JADX WARNING: type inference failed for: r3v3 */
    /* JADX WARNING: type inference failed for: r7v3 */
    /* JADX WARNING: type inference failed for: r6v3 */
    /* JADX WARNING: type inference failed for: r5v4 */
    /* JADX WARNING: type inference failed for: r8v3 */
    /* JADX WARNING: type inference failed for: r6v4 */
    /* JADX WARNING: type inference failed for: r5v5 */
    /* JADX WARNING: type inference failed for: r7v4 */
    /* JADX WARNING: type inference failed for: r5v6 */
    /* JADX WARNING: type inference failed for: r6v5 */
    /* JADX WARNING: type inference failed for: r5v7 */
    /* JADX WARNING: type inference failed for: r5v9 */
    /* JADX WARNING: type inference failed for: r5v12, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r6v8 */
    /* JADX WARNING: type inference failed for: r6v11, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r7v7 */
    /* JADX WARNING: type inference failed for: r7v10, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r8v6 */
    /* JADX WARNING: type inference failed for: r8v9, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r3v4 */
    /* JADX WARNING: type inference failed for: r3v6, types: [java.lang.reflect.Method] */
    /* JADX WARNING: type inference failed for: r8v10 */
    /* JADX WARNING: type inference failed for: r7v11 */
    /* JADX WARNING: type inference failed for: r6v12 */
    /* JADX WARNING: type inference failed for: r5v13 */
    /* JADX WARNING: type inference failed for: r3v7 */
    /* JADX WARNING: type inference failed for: r8v11 */
    /* JADX WARNING: type inference failed for: r7v12 */
    /* JADX WARNING: type inference failed for: r6v13 */
    /* JADX WARNING: type inference failed for: r5v14 */
    /* JADX WARNING: type inference failed for: r7v13 */
    /* JADX WARNING: type inference failed for: r6v14 */
    /* JADX WARNING: type inference failed for: r5v15 */
    /* JADX WARNING: type inference failed for: r6v15 */
    /* JADX WARNING: type inference failed for: r5v16 */
    /* JADX WARNING: type inference failed for: r5v17 */
    /* JADX WARNING: type inference failed for: r5v18 */
    /* JADX WARNING: type inference failed for: r5v19 */
    /* JADX WARNING: type inference failed for: r5v20 */
    /* JADX WARNING: type inference failed for: r5v21 */
    /* JADX WARNING: type inference failed for: r5v22 */
    /* JADX WARNING: type inference failed for: r5v23 */
    /* JADX WARNING: type inference failed for: r6v16 */
    /* JADX WARNING: type inference failed for: r6v17 */
    /* JADX WARNING: type inference failed for: r6v18 */
    /* JADX WARNING: type inference failed for: r6v19 */
    /* JADX WARNING: type inference failed for: r6v20 */
    /* JADX WARNING: type inference failed for: r7v14 */
    /* JADX WARNING: type inference failed for: r7v15 */
    /* JADX WARNING: type inference failed for: r7v16 */
    /* JADX WARNING: type inference failed for: r7v17 */
    /* JADX WARNING: type inference failed for: r8v12 */
    /* JADX WARNING: type inference failed for: r8v13 */
    /* JADX WARNING: type inference failed for: r8v14 */
    /* JADX WARNING: type inference failed for: r3v8 */
    /* JADX WARNING: type inference failed for: r3v9 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r8v1
      assigns: []
      uses: []
      mth insns count: 122
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 29 */
    static {
        /*
            com.motorola.actions.utils.MALogger r0 = new com.motorola.actions.utils.MALogger
            java.lang.Class<com.motorola.actions.reflect.PowerManagerPrivateProxy> r1 = com.motorola.actions.reflect.PowerManagerPrivateProxy.class
            r0.<init>(r1)
            LOGGER = r0
            r0 = 1
            r1 = 0
            r2 = 0
            r3 = 2
            java.lang.Class<android.os.PowerManager> r4 = android.os.PowerManager.class
            java.lang.String r5 = "ACTION_SCREEN_OFF_REASON"
            java.lang.reflect.Field r4 = r4.getDeclaredField(r5)     // Catch:{ IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x0097 }
            java.lang.Object r4 = r4.get(r2)     // Catch:{ IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x0097 }
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x0097 }
            java.lang.Class<android.os.PowerManager> r5 = android.os.PowerManager.class
            java.lang.String r6 = "ACTION_SCREEN_PRE_DIM"
            java.lang.reflect.Field r5 = r5.getDeclaredField(r6)     // Catch:{ IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x0094 }
            java.lang.Object r5 = r5.get(r2)     // Catch:{ IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x0094 }
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x0094 }
            java.lang.Class<android.os.PowerManager> r6 = android.os.PowerManager.class
            java.lang.String r7 = "ACTION_SCREEN_DIM"
            java.lang.reflect.Field r6 = r6.getDeclaredField(r7)     // Catch:{ IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x0091 }
            java.lang.Object r6 = r6.get(r2)     // Catch:{ IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x0091 }
            java.lang.String r6 = (java.lang.String) r6     // Catch:{ IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x0091 }
            java.lang.Class<android.os.PowerManager> r7 = android.os.PowerManager.class
            java.lang.String r8 = "ACTION_SCREEN_BRIGHT"
            java.lang.reflect.Field r7 = r7.getDeclaredField(r8)     // Catch:{ IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x008e }
            java.lang.Object r7 = r7.get(r2)     // Catch:{ IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x008e }
            java.lang.String r7 = (java.lang.String) r7     // Catch:{ IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x008e }
            java.lang.Class<android.os.PowerManager> r8 = android.os.PowerManager.class
            java.lang.String r9 = "EXTRA_SCREEN_OFF_REASON"
            java.lang.reflect.Field r8 = r8.getDeclaredField(r9)     // Catch:{ IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x008b }
            java.lang.Object r8 = r8.get(r2)     // Catch:{ IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x008b }
            java.lang.String r8 = (java.lang.String) r8     // Catch:{ IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x008b }
            java.lang.Class<android.os.PowerManager> r9 = android.os.PowerManager.class
            java.lang.String r10 = "GO_TO_SLEEP_REASON_TIMEOUT"
            java.lang.reflect.Field r9 = r9.getDeclaredField(r10)     // Catch:{ IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x0087 }
            java.lang.Object r9 = r9.get(r2)     // Catch:{ IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x0087 }
            java.lang.Integer r9 = (java.lang.Integer) r9     // Catch:{ IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x0087 }
            int r9 = r9.intValue()     // Catch:{ IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x0087 }
            java.lang.Class<android.os.PowerManager> r3 = android.os.PowerManager.class
            java.lang.String r10 = "setQuickDim"
            java.lang.Class[] r11 = new java.lang.Class[r0]     // Catch:{ IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x0085 }
            java.lang.Class r12 = java.lang.Integer.TYPE     // Catch:{ IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x0085 }
            r11[r1] = r12     // Catch:{ IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x0085 }
            java.lang.reflect.Method r3 = r3.getDeclaredMethod(r10, r11)     // Catch:{ IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x0085 }
            java.lang.Class<android.os.PowerManager> r10 = android.os.PowerManager.class
            java.lang.String r11 = "setQuickOff"
            java.lang.Class[] r12 = new java.lang.Class[r0]     // Catch:{ IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x0083 }
            java.lang.Class r13 = java.lang.Float.TYPE     // Catch:{ IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x0083 }
            r12[r1] = r13     // Catch:{ IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x0083 }
            java.lang.reflect.Method r10 = r10.getDeclaredMethod(r11, r12)     // Catch:{ IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x0083 }
            r2 = r10
            goto L_0x00a7
        L_0x0083:
            r0 = move-exception
            goto L_0x009f
        L_0x0085:
            r0 = move-exception
            goto L_0x0089
        L_0x0087:
            r0 = move-exception
            r9 = r3
        L_0x0089:
            r3 = r2
            goto L_0x009f
        L_0x008b:
            r0 = move-exception
            r8 = r2
            goto L_0x009d
        L_0x008e:
            r0 = move-exception
            r7 = r2
            goto L_0x009c
        L_0x0091:
            r0 = move-exception
            r6 = r2
            goto L_0x009b
        L_0x0094:
            r0 = move-exception
            r5 = r2
            goto L_0x009a
        L_0x0097:
            r0 = move-exception
            r4 = r2
            r5 = r4
        L_0x009a:
            r6 = r5
        L_0x009b:
            r7 = r6
        L_0x009c:
            r8 = r7
        L_0x009d:
            r9 = r3
            r3 = r8
        L_0x009f:
            com.motorola.actions.utils.MALogger r10 = LOGGER
            java.lang.String r11 = "Reflection error"
            r10.mo11964w(r11, r0)
            r0 = r1
        L_0x00a7:
            ACTION_SCREEN_OFF_REASON = r4
            ACTION_SCREEN_PRE_DIM = r5
            ACTION_SCREEN_DIM = r6
            ACTION_SCREEN_BRIGHT = r7
            EXTRA_SCREEN_OFF_REASON = r8
            GO_TO_SLEEP_REASON_TIMEOUT = r9
            SET_QUICK_DIM = r3
            SET_QUICK_OFF = r2
            HAS_HOOKS = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.motorola.actions.reflect.PowerManagerPrivateProxy.<clinit>():void");
    }

    public PowerManagerPrivateProxy(Context context) {
        this.mPowerManager = (PowerManager) context.getSystemService("power");
    }

    public static boolean hasHooks() {
        return HAS_HOOKS;
    }

    public void setQuickDim(int i) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Setting quick dim: ");
        sb.append(i);
        mALogger.mo11957d(sb.toString());
        try {
            SET_QUICK_DIM.invoke(this.mPowerManager, new Object[]{Integer.valueOf(i)});
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.mo11965w(e);
        }
    }

    public void setQuickOff(float f) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Setting quick off %: ");
        sb.append(f);
        mALogger.mo11957d(sb.toString());
        try {
            SET_QUICK_OFF.invoke(this.mPowerManager, new Object[]{Float.valueOf(f)});
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.mo11965w(e);
        }
    }
}
