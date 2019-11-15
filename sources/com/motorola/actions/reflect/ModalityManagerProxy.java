package com.motorola.actions.reflect;

import android.annotation.SuppressLint;
import android.content.Context;
import com.motorola.actions.utils.MALogger;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ModalityManagerProxy {
    private static final Class<?> CLASS_MODALITY_LISTENER;
    private static final boolean IS_INITIALIZED;
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(ModalityManagerProxy.class);
    private static final Method METHOD_ADD_MODALITY_LISTENER;
    /* access modifiers changed from: private */
    public static final Method METHOD_EQUALS;
    private static final Method METHOD_GET_STOWED;
    /* access modifiers changed from: private */
    public static final Method METHOD_ON_MODALITY_CHANGE;
    private static final Method METHOD_REMOVE_MODALITY_LISTENER;
    public static final int MODALITY_MOTIONLESS = 2;
    public static final int STOWED_TRUE;
    /* access modifiers changed from: private */
    public Object mModalityListener;
    /* access modifiers changed from: private */
    public ModalityListenerProxy mModalityListenerProxy;
    private Object mModalityManager;

    public interface ModalityListenerProxy {
        void onModalityChange(TransitionProxy transitionProxy);
    }

    /* JADX WARNING: type inference failed for: r9v0, types: [java.lang.reflect.Method] */
    /* JADX WARNING: type inference failed for: r8v0, types: [java.lang.reflect.Method] */
    /* JADX WARNING: type inference failed for: r7v0, types: [java.lang.reflect.Method] */
    /* JADX WARNING: type inference failed for: r6v0, types: [java.lang.Class<?>] */
    /* JADX WARNING: type inference failed for: r9v1 */
    /* JADX WARNING: type inference failed for: r8v1 */
    /* JADX WARNING: type inference failed for: r7v1 */
    /* JADX WARNING: type inference failed for: r6v1 */
    /* JADX WARNING: type inference failed for: r8v2 */
    /* JADX WARNING: type inference failed for: r7v2 */
    /* JADX WARNING: type inference failed for: r6v2 */
    /* JADX WARNING: type inference failed for: r9v2 */
    /* JADX WARNING: type inference failed for: r7v3 */
    /* JADX WARNING: type inference failed for: r6v3 */
    /* JADX WARNING: type inference failed for: r8v3 */
    /* JADX WARNING: type inference failed for: r6v4 */
    /* JADX WARNING: type inference failed for: r7v4 */
    /* JADX WARNING: type inference failed for: r6v5 */
    /* JADX WARNING: type inference failed for: r6v8 */
    /* JADX WARNING: type inference failed for: r6v9, types: [java.lang.Class] */
    /* JADX WARNING: type inference failed for: r7v6 */
    /* JADX WARNING: type inference failed for: r8v5, types: [java.lang.Class[]] */
    /* JADX WARNING: type inference failed for: r7v7, types: [java.lang.reflect.Method] */
    /* JADX WARNING: type inference failed for: r8v7 */
    /* JADX WARNING: type inference failed for: r9v6, types: [java.lang.Class[]] */
    /* JADX WARNING: type inference failed for: r8v8, types: [java.lang.reflect.Method] */
    /* JADX WARNING: type inference failed for: r9v8 */
    /* JADX WARNING: type inference failed for: r9v10 */
    /* JADX WARNING: type inference failed for: r9v11, types: [java.lang.reflect.Method] */
    /* JADX WARNING: type inference failed for: r9v12 */
    /* JADX WARNING: type inference failed for: r8v9 */
    /* JADX WARNING: type inference failed for: r7v8 */
    /* JADX WARNING: type inference failed for: r6v10 */
    /* JADX WARNING: type inference failed for: r8v10 */
    /* JADX WARNING: type inference failed for: r7v9 */
    /* JADX WARNING: type inference failed for: r6v11 */
    /* JADX WARNING: type inference failed for: r7v10 */
    /* JADX WARNING: type inference failed for: r6v12 */
    /* JADX WARNING: type inference failed for: r6v13 */
    /* JADX WARNING: type inference failed for: r6v14 */
    /* JADX WARNING: type inference failed for: r6v15 */
    /* JADX WARNING: type inference failed for: r6v16 */
    /* JADX WARNING: type inference failed for: r6v17 */
    /* JADX WARNING: type inference failed for: r6v18 */
    /* JADX WARNING: type inference failed for: r6v19 */
    /* JADX WARNING: type inference failed for: r7v11 */
    /* JADX WARNING: type inference failed for: r7v12 */
    /* JADX WARNING: type inference failed for: r7v13 */
    /* JADX WARNING: type inference failed for: r7v14 */
    /* JADX WARNING: type inference failed for: r7v15 */
    /* JADX WARNING: type inference failed for: r8v11 */
    /* JADX WARNING: type inference failed for: r8v12 */
    /* JADX WARNING: type inference failed for: r8v13 */
    /* JADX WARNING: type inference failed for: r8v14 */
    /* JADX WARNING: type inference failed for: r9v13 */
    /* JADX WARNING: type inference failed for: r9v14 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r9v1
      assigns: []
      uses: []
      mth insns count: 111
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
    /* JADX WARNING: Unknown variable types count: 23 */
    static {
        /*
            com.motorola.actions.utils.MALogger r0 = new com.motorola.actions.utils.MALogger
            java.lang.Class<com.motorola.actions.reflect.ModalityManagerProxy> r1 = com.motorola.actions.reflect.ModalityManagerProxy.class
            r0.<init>(r1)
            LOGGER = r0
            r0 = 1
            r1 = 0
            r2 = 0
            java.lang.String r3 = "com.motorola.slpc.ModalityManager"
            java.lang.Class r3 = java.lang.Class.forName(r3)     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x0082 }
            java.lang.String r4 = "com.motorola.slpc.Transition"
            java.lang.Class r4 = java.lang.Class.forName(r4)     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x0082 }
            java.lang.String r5 = "getStowed"
            java.lang.Class[] r6 = new java.lang.Class[r1]     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x0082 }
            java.lang.reflect.Method r5 = r3.getDeclaredMethod(r5, r6)     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x0082 }
            java.lang.String r6 = "com.motorola.slpc.ModalityManager$ModalityListener"
            java.lang.Class r6 = java.lang.Class.forName(r6)     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x007e }
            java.lang.String r7 = "addModalityListener"
            r8 = 4
            java.lang.Class[] r8 = new java.lang.Class[r8]     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x007a }
            r8[r1] = r6     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x007a }
            java.lang.Class r9 = java.lang.Integer.TYPE     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x007a }
            r8[r0] = r9     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x007a }
            r9 = 2
            java.lang.Class r10 = java.lang.Long.TYPE     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x007a }
            r8[r9] = r10     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x007a }
            r9 = 3
            java.lang.Class r10 = java.lang.Long.TYPE     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x007a }
            r8[r9] = r10     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x007a }
            java.lang.reflect.Method r7 = r3.getDeclaredMethod(r7, r8)     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x007a }
            java.lang.String r8 = "removeModalityListener"
            java.lang.Class[] r9 = new java.lang.Class[r0]     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x0076 }
            r9[r1] = r6     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x0076 }
            java.lang.reflect.Method r8 = r3.getDeclaredMethod(r8, r9)     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x0076 }
            java.lang.String r9 = "onModalityChange"
            java.lang.Class[] r10 = new java.lang.Class[r0]     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x0072 }
            r10[r1] = r4     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x0072 }
            java.lang.reflect.Method r4 = r6.getDeclaredMethod(r9, r10)     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x0072 }
            java.lang.Class<java.lang.Object> r9 = java.lang.Object.class
            java.lang.String r10 = "equals"
            java.lang.Class[] r11 = new java.lang.Class[r0]     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x006f }
            java.lang.Class<java.lang.Object> r12 = java.lang.Object.class
            r11[r1] = r12     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x006f }
            java.lang.reflect.Method r9 = r9.getDeclaredMethod(r10, r11)     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x006f }
            java.lang.String r10 = "STOWED_TRUE"
            java.lang.reflect.Field r3 = r3.getDeclaredField(r10)     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x006d }
            int r2 = r3.getInt(r2)     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException -> 0x006d }
            r1 = r2
            goto L_0x0091
        L_0x006d:
            r0 = move-exception
            goto L_0x0089
        L_0x006f:
            r0 = move-exception
            r9 = r2
            goto L_0x0089
        L_0x0072:
            r0 = move-exception
            r4 = r2
            r9 = r4
            goto L_0x0089
        L_0x0076:
            r0 = move-exception
            r4 = r2
            r8 = r4
            goto L_0x0088
        L_0x007a:
            r0 = move-exception
            r4 = r2
            r7 = r4
            goto L_0x0087
        L_0x007e:
            r0 = move-exception
            r4 = r2
            r6 = r4
            goto L_0x0086
        L_0x0082:
            r0 = move-exception
            r4 = r2
            r5 = r4
            r6 = r5
        L_0x0086:
            r7 = r6
        L_0x0087:
            r8 = r7
        L_0x0088:
            r9 = r8
        L_0x0089:
            com.motorola.actions.utils.MALogger r2 = LOGGER
            java.lang.String r3 = "Reflection error"
            r2.mo11964w(r3, r0)
            r0 = r1
        L_0x0091:
            METHOD_GET_STOWED = r5
            METHOD_EQUALS = r9
            CLASS_MODALITY_LISTENER = r6
            METHOD_ADD_MODALITY_LISTENER = r7
            METHOD_REMOVE_MODALITY_LISTENER = r8
            METHOD_ON_MODALITY_CHANGE = r4
            STOWED_TRUE = r1
            IS_INITIALIZED = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.motorola.actions.reflect.ModalityManagerProxy.<clinit>():void");
    }

    public ModalityManagerProxy(Context context) {
        resetModalityManager(context);
    }

    @SuppressLint({"WrongConstant"})
    public void resetModalityManager(Context context) {
        this.mModalityManager = context.getSystemService(ContextPrivateProxy.MODALITY_SERVICE);
    }

    public TransitionProxy getStowed() {
        TransitionProxy transitionProxy;
        if (IS_INITIALIZED && this.mModalityManager != null) {
            try {
                Object invoke = METHOD_GET_STOWED.invoke(this.mModalityManager, new Object[0]);
                if (invoke == null) {
                    return null;
                }
                transitionProxy = new TransitionProxy(invoke);
                return transitionProxy;
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOGGER.mo11965w(e);
            }
        }
        transitionProxy = null;
        return transitionProxy;
    }

    public void addModalityListener(ModalityListenerProxy modalityListenerProxy, int i, long j, long j2) {
        if (IS_INITIALIZED && this.mModalityManager != null) {
            try {
                this.mModalityListenerProxy = modalityListenerProxy;
                removeModalityListener();
                this.mModalityListener = Proxy.newProxyInstance(CLASS_MODALITY_LISTENER.getClassLoader(), new Class[]{CLASS_MODALITY_LISTENER}, new InvocationHandler() {
                    public Object invoke(Object obj, Method method, Object[] objArr) {
                        try {
                            boolean z = false;
                            if (ModalityManagerProxy.METHOD_ON_MODALITY_CHANGE.equals(method)) {
                                if (obj == ModalityManagerProxy.this.mModalityListener) {
                                    ModalityManagerProxy.this.mModalityListenerProxy.onModalityChange(new TransitionProxy(objArr[0]));
                                }
                                return null;
                            } else if (!ModalityManagerProxy.METHOD_EQUALS.equals(method)) {
                                return method.invoke(ModalityManagerProxy.this.mModalityListenerProxy, objArr);
                            } else {
                                if (ModalityManagerProxy.this.mModalityListener == objArr[0]) {
                                    z = true;
                                }
                                return Boolean.valueOf(z);
                            }
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            ModalityManagerProxy.LOGGER.mo11965w(e);
                        }
                    }
                });
                METHOD_ADD_MODALITY_LISTENER.invoke(this.mModalityManager, new Object[]{this.mModalityListener, Integer.valueOf(i), Long.valueOf(j), Long.valueOf(j2)});
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOGGER.mo11965w(e);
            }
        }
    }

    public void removeModalityListener() {
        if (IS_INITIALIZED && this.mModalityManager != null) {
            try {
                METHOD_REMOVE_MODALITY_LISTENER.invoke(this.mModalityManager, new Object[]{this.mModalityListener});
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOGGER.mo11965w(e);
            }
        }
    }
}
