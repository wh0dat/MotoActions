package com.google.android.gms.common.api.internal;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.annotation.KeepName;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.PendingResult.StatusListener;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultTransform;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.TransformedResult;
import com.google.android.gms.common.internal.ICancelToken;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.VisibleForTesting;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@KeepName
@KeepForSdk
public abstract class BasePendingResult<R extends Result> extends PendingResult<R> {
    static final ThreadLocal<Boolean> zzez = new zzo();
    @KeepName
    private zza mResultGuardian;
    private Status mStatus;
    /* access modifiers changed from: private */
    public R zzdm;
    private final Object zzfa;
    private final CallbackHandler<R> zzfb;
    private final WeakReference<GoogleApiClient> zzfc;
    private final CountDownLatch zzfd;
    private final ArrayList<StatusListener> zzfe;
    private ResultCallback<? super R> zzff;
    private final AtomicReference<zzcn> zzfg;
    private volatile boolean zzfh;
    private boolean zzfi;
    private boolean zzfj;
    private ICancelToken zzfk;
    private volatile zzch<R> zzfl;
    private boolean zzfm;

    @VisibleForTesting
    public static class CallbackHandler<R extends Result> extends Handler {
        public CallbackHandler() {
            this(Looper.getMainLooper());
        }

        public CallbackHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    Pair pair = (Pair) message.obj;
                    ResultCallback resultCallback = (ResultCallback) pair.first;
                    Result result = (Result) pair.second;
                    try {
                        resultCallback.onResult(result);
                        return;
                    } catch (RuntimeException e) {
                        BasePendingResult.zzb(result);
                        throw e;
                    }
                case 2:
                    ((BasePendingResult) message.obj).zzb(Status.RESULT_TIMEOUT);
                    return;
                default:
                    int i = message.what;
                    StringBuilder sb = new StringBuilder(45);
                    sb.append("Don't know how to handle message: ");
                    sb.append(i);
                    Log.wtf("BasePendingResult", sb.toString(), new Exception());
                    return;
            }
        }

        public final void zza(ResultCallback<? super R> resultCallback, R r) {
            sendMessage(obtainMessage(1, new Pair(resultCallback, r)));
        }
    }

    private final class zza {
        private zza() {
        }

        /* synthetic */ zza(BasePendingResult basePendingResult, zzo zzo) {
            this();
        }

        /* access modifiers changed from: protected */
        public final void finalize() throws Throwable {
            BasePendingResult.zzb(BasePendingResult.this.zzdm);
            super.finalize();
        }
    }

    @Deprecated
    BasePendingResult() {
        this.zzfa = new Object();
        this.zzfd = new CountDownLatch(1);
        this.zzfe = new ArrayList<>();
        this.zzfg = new AtomicReference<>();
        this.zzfm = false;
        this.zzfb = new CallbackHandler<>(Looper.getMainLooper());
        this.zzfc = new WeakReference<>(null);
    }

    @KeepForSdk
    @Deprecated
    protected BasePendingResult(Looper looper) {
        this.zzfa = new Object();
        this.zzfd = new CountDownLatch(1);
        this.zzfe = new ArrayList<>();
        this.zzfg = new AtomicReference<>();
        this.zzfm = false;
        this.zzfb = new CallbackHandler<>(looper);
        this.zzfc = new WeakReference<>(null);
    }

    @KeepForSdk
    protected BasePendingResult(GoogleApiClient googleApiClient) {
        this.zzfa = new Object();
        this.zzfd = new CountDownLatch(1);
        this.zzfe = new ArrayList<>();
        this.zzfg = new AtomicReference<>();
        this.zzfm = false;
        this.zzfb = new CallbackHandler<>(googleApiClient != null ? googleApiClient.getLooper() : Looper.getMainLooper());
        this.zzfc = new WeakReference<>(googleApiClient);
    }

    @KeepForSdk
    @VisibleForTesting
    protected BasePendingResult(@NonNull CallbackHandler<R> callbackHandler) {
        this.zzfa = new Object();
        this.zzfd = new CountDownLatch(1);
        this.zzfe = new ArrayList<>();
        this.zzfg = new AtomicReference<>();
        this.zzfm = false;
        this.zzfb = (CallbackHandler) Preconditions.checkNotNull(callbackHandler, "CallbackHandler must not be null");
        this.zzfc = new WeakReference<>(null);
    }

    private final R get() {
        R r;
        synchronized (this.zzfa) {
            Preconditions.checkState(!this.zzfh, "Result has already been consumed.");
            Preconditions.checkState(isReady(), "Result is not ready.");
            r = this.zzdm;
            this.zzdm = null;
            this.zzff = null;
            this.zzfh = true;
        }
        zzcn zzcn = (zzcn) this.zzfg.getAndSet(null);
        if (zzcn != null) {
            zzcn.zzc(this);
        }
        return r;
    }

    private final void zza(R r) {
        this.zzdm = r;
        this.zzfk = null;
        this.zzfd.countDown();
        this.mStatus = this.zzdm.getStatus();
        if (this.zzfi) {
            this.zzff = null;
        } else if (this.zzff != null) {
            this.zzfb.removeMessages(2);
            this.zzfb.zza(this.zzff, get());
        } else if (this.zzdm instanceof Releasable) {
            this.mResultGuardian = new zza(this, null);
        }
        ArrayList arrayList = this.zzfe;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            ((StatusListener) obj).onComplete(this.mStatus);
        }
        this.zzfe.clear();
    }

    public static void zzb(Result result) {
        if (result instanceof Releasable) {
            try {
                ((Releasable) result).release();
            } catch (RuntimeException e) {
                String valueOf = String.valueOf(result);
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 18);
                sb.append("Unable to release ");
                sb.append(valueOf);
                Log.w("BasePendingResult", sb.toString(), e);
            }
        }
    }

    public final void addStatusListener(StatusListener statusListener) {
        Preconditions.checkArgument(statusListener != null, "Callback cannot be null.");
        synchronized (this.zzfa) {
            if (isReady()) {
                statusListener.onComplete(this.mStatus);
            } else {
                this.zzfe.add(statusListener);
            }
        }
    }

    public final R await() {
        Preconditions.checkNotMainThread("await must not be called on the UI thread");
        boolean z = true;
        Preconditions.checkState(!this.zzfh, "Result has already been consumed");
        if (this.zzfl != null) {
            z = false;
        }
        Preconditions.checkState(z, "Cannot await if then() has been called.");
        try {
            this.zzfd.await();
        } catch (InterruptedException unused) {
            zzb(Status.RESULT_INTERRUPTED);
        }
        Preconditions.checkState(isReady(), "Result is not ready.");
        return get();
    }

    public final R await(long j, TimeUnit timeUnit) {
        if (j > 0) {
            Preconditions.checkNotMainThread("await must not be called on the UI thread when time is greater than zero.");
        }
        boolean z = true;
        Preconditions.checkState(!this.zzfh, "Result has already been consumed.");
        if (this.zzfl != null) {
            z = false;
        }
        Preconditions.checkState(z, "Cannot await if then() has been called.");
        try {
            if (!this.zzfd.await(j, timeUnit)) {
                zzb(Status.RESULT_TIMEOUT);
            }
        } catch (InterruptedException unused) {
            zzb(Status.RESULT_INTERRUPTED);
        }
        Preconditions.checkState(isReady(), "Result is not ready.");
        return get();
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:8|(2:10|11)|12|13|14|15) */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0029, code lost:
        return;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x0015 */
    @com.google.android.gms.common.annotation.KeepForSdk
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void cancel() {
        /*
            r2 = this;
            java.lang.Object r0 = r2.zzfa
            monitor-enter(r0)
            boolean r1 = r2.zzfi     // Catch:{ all -> 0x002a }
            if (r1 != 0) goto L_0x0028
            boolean r1 = r2.zzfh     // Catch:{ all -> 0x002a }
            if (r1 == 0) goto L_0x000c
            goto L_0x0028
        L_0x000c:
            com.google.android.gms.common.internal.ICancelToken r1 = r2.zzfk     // Catch:{ all -> 0x002a }
            if (r1 == 0) goto L_0x0015
            com.google.android.gms.common.internal.ICancelToken r1 = r2.zzfk     // Catch:{ RemoteException -> 0x0015 }
            r1.cancel()     // Catch:{ RemoteException -> 0x0015 }
        L_0x0015:
            R r1 = r2.zzdm     // Catch:{ all -> 0x002a }
            zzb(r1)     // Catch:{ all -> 0x002a }
            r1 = 1
            r2.zzfi = r1     // Catch:{ all -> 0x002a }
            com.google.android.gms.common.api.Status r1 = com.google.android.gms.common.api.Status.RESULT_CANCELED     // Catch:{ all -> 0x002a }
            com.google.android.gms.common.api.Result r1 = r2.createFailedResult(r1)     // Catch:{ all -> 0x002a }
            r2.zza((R) r1)     // Catch:{ all -> 0x002a }
            monitor-exit(r0)     // Catch:{ all -> 0x002a }
            return
        L_0x0028:
            monitor-exit(r0)     // Catch:{ all -> 0x002a }
            return
        L_0x002a:
            r2 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x002a }
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.BasePendingResult.cancel():void");
    }

    /* access modifiers changed from: protected */
    @KeepForSdk
    @NonNull
    public abstract R createFailedResult(Status status);

    public boolean isCanceled() {
        boolean z;
        synchronized (this.zzfa) {
            z = this.zzfi;
        }
        return z;
    }

    @KeepForSdk
    public final boolean isReady() {
        return this.zzfd.getCount() == 0;
    }

    /* access modifiers changed from: protected */
    @KeepForSdk
    public final void setCancelToken(ICancelToken iCancelToken) {
        synchronized (this.zzfa) {
            this.zzfk = iCancelToken;
        }
    }

    @KeepForSdk
    public final void setResult(R r) {
        synchronized (this.zzfa) {
            if (this.zzfj || this.zzfi) {
                zzb((Result) r);
                return;
            }
            isReady();
            Preconditions.checkState(!isReady(), "Results have already been set");
            Preconditions.checkState(!this.zzfh, "Result has already been consumed");
            zza(r);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x003b, code lost:
        return;
     */
    @com.google.android.gms.common.annotation.KeepForSdk
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void setResultCallback(com.google.android.gms.common.api.ResultCallback<? super R> r5) {
        /*
            r4 = this;
            java.lang.Object r0 = r4.zzfa
            monitor-enter(r0)
            if (r5 != 0) goto L_0x000c
            r5 = 0
            r4.zzff = r5     // Catch:{ all -> 0x000a }
            monitor-exit(r0)     // Catch:{ all -> 0x000a }
            return
        L_0x000a:
            r4 = move-exception
            goto L_0x003c
        L_0x000c:
            boolean r1 = r4.zzfh     // Catch:{ all -> 0x000a }
            r2 = 1
            r1 = r1 ^ r2
            java.lang.String r3 = "Result has already been consumed."
            com.google.android.gms.common.internal.Preconditions.checkState(r1, r3)     // Catch:{ all -> 0x000a }
            com.google.android.gms.common.api.internal.zzch<R> r1 = r4.zzfl     // Catch:{ all -> 0x000a }
            if (r1 != 0) goto L_0x001a
            goto L_0x001b
        L_0x001a:
            r2 = 0
        L_0x001b:
            java.lang.String r1 = "Cannot set callbacks if then() has been called."
            com.google.android.gms.common.internal.Preconditions.checkState(r2, r1)     // Catch:{ all -> 0x000a }
            boolean r1 = r4.isCanceled()     // Catch:{ all -> 0x000a }
            if (r1 == 0) goto L_0x0028
            monitor-exit(r0)     // Catch:{ all -> 0x000a }
            return
        L_0x0028:
            boolean r1 = r4.isReady()     // Catch:{ all -> 0x000a }
            if (r1 == 0) goto L_0x0038
            com.google.android.gms.common.api.internal.BasePendingResult$CallbackHandler<R> r1 = r4.zzfb     // Catch:{ all -> 0x000a }
            com.google.android.gms.common.api.Result r4 = r4.get()     // Catch:{ all -> 0x000a }
            r1.zza(r5, r4)     // Catch:{ all -> 0x000a }
            goto L_0x003a
        L_0x0038:
            r4.zzff = r5     // Catch:{ all -> 0x000a }
        L_0x003a:
            monitor-exit(r0)     // Catch:{ all -> 0x000a }
            return
        L_0x003c:
            monitor-exit(r0)     // Catch:{ all -> 0x000a }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.BasePendingResult.setResultCallback(com.google.android.gms.common.api.ResultCallback):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0049, code lost:
        return;
     */
    @com.google.android.gms.common.annotation.KeepForSdk
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void setResultCallback(com.google.android.gms.common.api.ResultCallback<? super R> r5, long r6, java.util.concurrent.TimeUnit r8) {
        /*
            r4 = this;
            java.lang.Object r0 = r4.zzfa
            monitor-enter(r0)
            if (r5 != 0) goto L_0x000c
            r5 = 0
            r4.zzff = r5     // Catch:{ all -> 0x000a }
            monitor-exit(r0)     // Catch:{ all -> 0x000a }
            return
        L_0x000a:
            r4 = move-exception
            goto L_0x004a
        L_0x000c:
            boolean r1 = r4.zzfh     // Catch:{ all -> 0x000a }
            r2 = 1
            r1 = r1 ^ r2
            java.lang.String r3 = "Result has already been consumed."
            com.google.android.gms.common.internal.Preconditions.checkState(r1, r3)     // Catch:{ all -> 0x000a }
            com.google.android.gms.common.api.internal.zzch<R> r1 = r4.zzfl     // Catch:{ all -> 0x000a }
            if (r1 != 0) goto L_0x001a
            goto L_0x001b
        L_0x001a:
            r2 = 0
        L_0x001b:
            java.lang.String r1 = "Cannot set callbacks if then() has been called."
            com.google.android.gms.common.internal.Preconditions.checkState(r2, r1)     // Catch:{ all -> 0x000a }
            boolean r1 = r4.isCanceled()     // Catch:{ all -> 0x000a }
            if (r1 == 0) goto L_0x0028
            monitor-exit(r0)     // Catch:{ all -> 0x000a }
            return
        L_0x0028:
            boolean r1 = r4.isReady()     // Catch:{ all -> 0x000a }
            if (r1 == 0) goto L_0x0038
            com.google.android.gms.common.api.internal.BasePendingResult$CallbackHandler<R> r6 = r4.zzfb     // Catch:{ all -> 0x000a }
            com.google.android.gms.common.api.Result r4 = r4.get()     // Catch:{ all -> 0x000a }
            r6.zza(r5, r4)     // Catch:{ all -> 0x000a }
            goto L_0x0048
        L_0x0038:
            r4.zzff = r5     // Catch:{ all -> 0x000a }
            com.google.android.gms.common.api.internal.BasePendingResult$CallbackHandler<R> r5 = r4.zzfb     // Catch:{ all -> 0x000a }
            long r6 = r8.toMillis(r6)     // Catch:{ all -> 0x000a }
            r8 = 2
            android.os.Message r4 = r5.obtainMessage(r8, r4)     // Catch:{ all -> 0x000a }
            r5.sendMessageDelayed(r4, r6)     // Catch:{ all -> 0x000a }
        L_0x0048:
            monitor-exit(r0)     // Catch:{ all -> 0x000a }
            return
        L_0x004a:
            monitor-exit(r0)     // Catch:{ all -> 0x000a }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.BasePendingResult.setResultCallback(com.google.android.gms.common.api.ResultCallback, long, java.util.concurrent.TimeUnit):void");
    }

    public <S extends Result> TransformedResult<S> then(ResultTransform<? super R, ? extends S> resultTransform) {
        TransformedResult<S> then;
        Preconditions.checkState(!this.zzfh, "Result has already been consumed.");
        synchronized (this.zzfa) {
            boolean z = false;
            Preconditions.checkState(this.zzfl == null, "Cannot call then() twice.");
            if (this.zzff == null) {
                z = true;
            }
            Preconditions.checkState(z, "Cannot call then() if callbacks are set.");
            Preconditions.checkState(!this.zzfi, "Cannot call then() if result was canceled.");
            this.zzfm = true;
            this.zzfl = new zzch<>(this.zzfc);
            then = this.zzfl.then(resultTransform);
            if (isReady()) {
                this.zzfb.zza(this.zzfl, get());
            } else {
                this.zzff = this.zzfl;
            }
        }
        return then;
    }

    public final void zza(zzcn zzcn) {
        this.zzfg.set(zzcn);
    }

    public final void zzb(Status status) {
        synchronized (this.zzfa) {
            if (!isReady()) {
                setResult(createFailedResult(status));
                this.zzfj = true;
            }
        }
    }

    public final Integer zzo() {
        return null;
    }

    public final boolean zzw() {
        boolean isCanceled;
        synchronized (this.zzfa) {
            if (((GoogleApiClient) this.zzfc.get()) == null || !this.zzfm) {
                cancel();
            }
            isCanceled = isCanceled();
        }
        return isCanceled;
    }

    public final void zzx() {
        this.zzfm = this.zzfm || ((Boolean) zzez.get()).booleanValue();
    }
}
