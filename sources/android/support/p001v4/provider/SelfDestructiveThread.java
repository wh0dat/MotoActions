package android.support.p001v4.provider;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.GuardedBy;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.annotation.VisibleForTesting;
import java.util.concurrent.Callable;

@RestrictTo({Scope.LIBRARY_GROUP})
/* renamed from: android.support.v4.provider.SelfDestructiveThread */
public class SelfDestructiveThread {
    private static final int MSG_DESTRUCTION = 0;
    private static final int MSG_INVOKE_RUNNABLE = 1;
    private Callback mCallback = new Callback() {
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    SelfDestructiveThread.this.onDestruction();
                    return true;
                case 1:
                    SelfDestructiveThread.this.onInvokeRunnable((Runnable) message.obj);
                    return true;
                default:
                    return true;
            }
        }
    };
    private final int mDestructAfterMillisec;
    @GuardedBy("mLock")
    private int mGeneration;
    @GuardedBy("mLock")
    private Handler mHandler;
    private final Object mLock = new Object();
    private final int mPriority;
    @GuardedBy("mLock")
    private HandlerThread mThread;
    private final String mThreadName;

    /* renamed from: android.support.v4.provider.SelfDestructiveThread$ReplyCallback */
    public interface ReplyCallback<T> {
        void onReply(T t);
    }

    public SelfDestructiveThread(String str, int i, int i2) {
        this.mThreadName = str;
        this.mPriority = i;
        this.mDestructAfterMillisec = i2;
        this.mGeneration = 0;
    }

    @VisibleForTesting
    public boolean isRunning() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mThread != null;
        }
        return z;
    }

    @VisibleForTesting
    public int getGeneration() {
        int i;
        synchronized (this.mLock) {
            i = this.mGeneration;
        }
        return i;
    }

    private void post(Runnable runnable) {
        synchronized (this.mLock) {
            if (this.mThread == null) {
                this.mThread = new HandlerThread(this.mThreadName, this.mPriority);
                this.mThread.start();
                this.mHandler = new Handler(this.mThread.getLooper(), this.mCallback);
                this.mGeneration++;
            }
            this.mHandler.removeMessages(0);
            this.mHandler.sendMessage(this.mHandler.obtainMessage(1, runnable));
        }
    }

    public <T> void postAndReply(final Callable<T> callable, final ReplyCallback<T> replyCallback) {
        final Handler handler = new Handler();
        post(new Runnable() {
            public void run() {
                final Object obj;
                try {
                    obj = callable.call();
                } catch (Exception unused) {
                    obj = null;
                }
                handler.post(new Runnable() {
                    public void run() {
                        replyCallback.onReply(obj);
                    }
                });
            }
        });
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:9|10|11|12|13|(4:26|15|16|17)(1:18)) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x0040 */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x004e  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0046 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T> T postAndWait(java.util.concurrent.Callable<T> r13, int r14) throws java.lang.InterruptedException {
        /*
            r12 = this;
            java.util.concurrent.locks.ReentrantLock r7 = new java.util.concurrent.locks.ReentrantLock
            r7.<init>()
            java.util.concurrent.locks.Condition r8 = r7.newCondition()
            java.util.concurrent.atomic.AtomicReference r9 = new java.util.concurrent.atomic.AtomicReference
            r9.<init>()
            java.util.concurrent.atomic.AtomicBoolean r10 = new java.util.concurrent.atomic.AtomicBoolean
            r0 = 1
            r10.<init>(r0)
            android.support.v4.provider.SelfDestructiveThread$3 r11 = new android.support.v4.provider.SelfDestructiveThread$3
            r0 = r11
            r1 = r12
            r2 = r9
            r3 = r13
            r4 = r7
            r5 = r10
            r6 = r8
            r0.<init>(r2, r3, r4, r5, r6)
            r12.post(r11)
            r7.lock()
            boolean r12 = r10.get()     // Catch:{ all -> 0x005c }
            if (r12 != 0) goto L_0x0034
            java.lang.Object r12 = r9.get()     // Catch:{ all -> 0x005c }
            r7.unlock()
            return r12
        L_0x0034:
            java.util.concurrent.TimeUnit r12 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch:{ all -> 0x005c }
            long r13 = (long) r14     // Catch:{ all -> 0x005c }
            long r12 = r12.toNanos(r13)     // Catch:{ all -> 0x005c }
        L_0x003b:
            long r0 = r8.awaitNanos(r12)     // Catch:{ InterruptedException -> 0x0040 }
            r12 = r0
        L_0x0040:
            boolean r14 = r10.get()     // Catch:{ all -> 0x005c }
            if (r14 != 0) goto L_0x004e
            java.lang.Object r12 = r9.get()     // Catch:{ all -> 0x005c }
            r7.unlock()
            return r12
        L_0x004e:
            r0 = 0
            int r14 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1))
            if (r14 > 0) goto L_0x003b
            java.lang.InterruptedException r12 = new java.lang.InterruptedException     // Catch:{ all -> 0x005c }
            java.lang.String r13 = "timeout"
            r12.<init>(r13)     // Catch:{ all -> 0x005c }
            throw r12     // Catch:{ all -> 0x005c }
        L_0x005c:
            r12 = move-exception
            r7.unlock()
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p001v4.provider.SelfDestructiveThread.postAndWait(java.util.concurrent.Callable, int):java.lang.Object");
    }

    /* access modifiers changed from: private */
    public void onInvokeRunnable(Runnable runnable) {
        runnable.run();
        synchronized (this.mLock) {
            this.mHandler.removeMessages(0);
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(0), (long) this.mDestructAfterMillisec);
        }
    }

    /* access modifiers changed from: private */
    public void onDestruction() {
        synchronized (this.mLock) {
            if (!this.mHandler.hasMessages(1)) {
                this.mThread.quit();
                this.mThread = null;
                this.mHandler = null;
            }
        }
    }
}
