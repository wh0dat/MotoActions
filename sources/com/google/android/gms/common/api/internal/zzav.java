package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.p001v4.app.FragmentActivity;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.AbstractClientBuilder;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.Api.AnyClientKey;
import com.google.android.gms.common.api.Api.Client;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.GmsClientEventManager;
import com.google.android.gms.common.internal.GmsClientEventManager.GmsClientEventState;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.signin.SignInClient;
import com.google.android.gms.signin.SignInOptions;
import com.motorola.actions.foc.gesture.util.FlashOnChopAlgorithmUtil;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import javax.annotation.concurrent.GuardedBy;

public final class zzav extends GoogleApiClient implements zzbq {
    /* access modifiers changed from: private */
    public final Context mContext;
    private final Looper zzcn;
    private final int zzde;
    private final GoogleApiAvailability zzdg;
    private final AbstractClientBuilder<? extends SignInClient, SignInOptions> zzdh;
    private boolean zzdk;
    private final Lock zzga;
    private final ClientSettings zzgf;
    private final Map<Api<?>, Boolean> zzgi;
    @VisibleForTesting
    final Queue<ApiMethodImpl<?, ?>> zzgo = new LinkedList();
    private final GmsClientEventManager zzie;
    private zzbp zzif = null;
    private volatile boolean zzig;
    private long zzih = 120000;
    private long zzii = 5000;
    private final zzba zzij;
    @VisibleForTesting
    private GooglePlayServicesUpdatedReceiver zzik;
    final Map<AnyClientKey<?>, Client> zzil;
    Set<Scope> zzim = new HashSet();
    private final ListenerHolders zzin = new ListenerHolders();
    private final ArrayList<zzp> zzio;
    private Integer zzip = null;
    Set<zzch> zziq = null;
    final zzck zzir;
    private final GmsClientEventState zzis = new zzaw(this);

    public zzav(Context context, Lock lock, Looper looper, ClientSettings clientSettings, GoogleApiAvailability googleApiAvailability, AbstractClientBuilder<? extends SignInClient, SignInOptions> abstractClientBuilder, Map<Api<?>, Boolean> map, List<ConnectionCallbacks> list, List<OnConnectionFailedListener> list2, Map<AnyClientKey<?>, Client> map2, int i, int i2, ArrayList<zzp> arrayList, boolean z) {
        Looper looper2 = looper;
        this.mContext = context;
        this.zzga = lock;
        this.zzdk = false;
        this.zzie = new GmsClientEventManager(looper2, this.zzis);
        this.zzcn = looper2;
        this.zzij = new zzba(this, looper2);
        this.zzdg = googleApiAvailability;
        this.zzde = i;
        if (this.zzde >= 0) {
            this.zzip = Integer.valueOf(i2);
        }
        this.zzgi = map;
        this.zzil = map2;
        this.zzio = arrayList;
        this.zzir = new zzck(this.zzil);
        for (ConnectionCallbacks registerConnectionCallbacks : list) {
            this.zzie.registerConnectionCallbacks(registerConnectionCallbacks);
        }
        for (OnConnectionFailedListener registerConnectionFailedListener : list2) {
            this.zzie.registerConnectionFailedListener(registerConnectionFailedListener);
        }
        this.zzgf = clientSettings;
        this.zzdh = abstractClientBuilder;
    }

    /* access modifiers changed from: private */
    public final void resume() {
        this.zzga.lock();
        try {
            if (this.zzig) {
                zzax();
            }
        } finally {
            this.zzga.unlock();
        }
    }

    public static int zza(Iterable<Client> iterable, boolean z) {
        boolean z2 = false;
        boolean z3 = false;
        for (Client client : iterable) {
            if (client.requiresSignIn()) {
                z2 = true;
            }
            if (client.providesSignIn()) {
                z3 = true;
            }
        }
        if (z2) {
            return (!z3 || !z) ? 1 : 2;
        }
        return 3;
    }

    /* access modifiers changed from: private */
    public final void zza(GoogleApiClient googleApiClient, StatusPendingResult statusPendingResult, boolean z) {
        Common.CommonApi.clearDefaultAccount(googleApiClient).setResultCallback(new zzaz(this, statusPendingResult, z, googleApiClient));
    }

    @GuardedBy("mLock")
    private final void zzax() {
        this.zzie.enableCallbacks();
        this.zzif.connect();
    }

    /* access modifiers changed from: private */
    public final void zzay() {
        this.zzga.lock();
        try {
            if (zzaz()) {
                zzax();
            }
        } finally {
            this.zzga.unlock();
        }
    }

    private final void zzg(int i) {
        if (this.zzip == null) {
            this.zzip = Integer.valueOf(i);
        } else if (this.zzip.intValue() != i) {
            String zzh = zzh(i);
            String zzh2 = zzh(this.zzip.intValue());
            StringBuilder sb = new StringBuilder(String.valueOf(zzh).length() + 51 + String.valueOf(zzh2).length());
            sb.append("Cannot use sign-in mode: ");
            sb.append(zzh);
            sb.append(". Mode was already set to ");
            sb.append(zzh2);
            throw new IllegalStateException(sb.toString());
        }
        if (this.zzif == null) {
            boolean z = false;
            boolean z2 = false;
            for (Client client : this.zzil.values()) {
                if (client.requiresSignIn()) {
                    z = true;
                }
                if (client.providesSignIn()) {
                    z2 = true;
                }
            }
            switch (this.zzip.intValue()) {
                case 1:
                    if (!z) {
                        throw new IllegalStateException("SIGN_IN_MODE_REQUIRED cannot be used on a GoogleApiClient that does not contain any authenticated APIs. Use connect() instead.");
                    } else if (z2) {
                        throw new IllegalStateException("Cannot use SIGN_IN_MODE_REQUIRED with GOOGLE_SIGN_IN_API. Use connect(SIGN_IN_MODE_OPTIONAL) instead.");
                    }
                    break;
                case 2:
                    if (z) {
                        if (this.zzdk) {
                            zzw zzw = new zzw(this.mContext, this.zzga, this.zzcn, this.zzdg, this.zzil, this.zzgf, this.zzgi, this.zzdh, this.zzio, this, true);
                            this.zzif = zzw;
                            return;
                        }
                        this.zzif = zzr.zza(this.mContext, this, this.zzga, this.zzcn, this.zzdg, this.zzil, this.zzgf, this.zzgi, this.zzdh, this.zzio);
                        return;
                    }
                    break;
            }
            if (!this.zzdk || z2) {
                zzbd zzbd = new zzbd(this.mContext, this, this.zzga, this.zzcn, this.zzdg, this.zzil, this.zzgf, this.zzgi, this.zzdh, this.zzio, this);
                this.zzif = zzbd;
                return;
            }
            zzw zzw2 = new zzw(this.mContext, this.zzga, this.zzcn, this.zzdg, this.zzil, this.zzgf, this.zzgi, this.zzdh, this.zzio, this, false);
            this.zzif = zzw2;
        }
    }

    private static String zzh(int i) {
        switch (i) {
            case 1:
                return "SIGN_IN_MODE_REQUIRED";
            case 2:
                return "SIGN_IN_MODE_OPTIONAL";
            case 3:
                return "SIGN_IN_MODE_NONE";
            default:
                return FlashOnChopAlgorithmUtil.KEY_TEST_USE_ONLY_FOC_ALGO_UNKNOWN;
        }
    }

    public final ConnectionResult blockingConnect() {
        boolean z = true;
        Preconditions.checkState(Looper.myLooper() != Looper.getMainLooper(), "blockingConnect must not be called on the UI thread");
        this.zzga.lock();
        try {
            if (this.zzde >= 0) {
                if (this.zzip == null) {
                    z = false;
                }
                Preconditions.checkState(z, "Sign-in mode should have been set explicitly by auto-manage.");
            } else if (this.zzip == null) {
                this.zzip = Integer.valueOf(zza(this.zzil.values(), false));
            } else if (this.zzip.intValue() == 2) {
                throw new IllegalStateException("Cannot call blockingConnect() when sign-in mode is set to SIGN_IN_MODE_OPTIONAL. Call connect(SIGN_IN_MODE_OPTIONAL) instead.");
            }
            zzg(this.zzip.intValue());
            this.zzie.enableCallbacks();
            ConnectionResult blockingConnect = this.zzif.blockingConnect();
            return blockingConnect;
        } finally {
            this.zzga.unlock();
        }
    }

    public final ConnectionResult blockingConnect(long j, @NonNull TimeUnit timeUnit) {
        Preconditions.checkState(Looper.myLooper() != Looper.getMainLooper(), "blockingConnect must not be called on the UI thread");
        Preconditions.checkNotNull(timeUnit, "TimeUnit must not be null");
        this.zzga.lock();
        try {
            if (this.zzip == null) {
                this.zzip = Integer.valueOf(zza(this.zzil.values(), false));
            } else if (this.zzip.intValue() == 2) {
                throw new IllegalStateException("Cannot call blockingConnect() when sign-in mode is set to SIGN_IN_MODE_OPTIONAL. Call connect(SIGN_IN_MODE_OPTIONAL) instead.");
            }
            zzg(this.zzip.intValue());
            this.zzie.enableCallbacks();
            ConnectionResult blockingConnect = this.zzif.blockingConnect(j, timeUnit);
            return blockingConnect;
        } finally {
            this.zzga.unlock();
        }
    }

    public final PendingResult<Status> clearDefaultAccountAndReconnect() {
        Preconditions.checkState(isConnected(), "GoogleApiClient is not connected yet.");
        Preconditions.checkState(this.zzip.intValue() != 2, "Cannot use clearDefaultAccountAndReconnect with GOOGLE_SIGN_IN_API");
        StatusPendingResult statusPendingResult = new StatusPendingResult((GoogleApiClient) this);
        if (this.zzil.containsKey(Common.CLIENT_KEY)) {
            zza(this, statusPendingResult, false);
            return statusPendingResult;
        }
        AtomicReference atomicReference = new AtomicReference();
        GoogleApiClient build = new Builder(this.mContext).addApi(Common.API).addConnectionCallbacks(new zzax(this, atomicReference, statusPendingResult)).addOnConnectionFailedListener(new zzay(this, statusPendingResult)).setHandler(this.zzij).build();
        atomicReference.set(build);
        build.connect();
        return statusPendingResult;
    }

    public final void connect() {
        this.zzga.lock();
        try {
            boolean z = false;
            if (this.zzde >= 0) {
                if (this.zzip != null) {
                    z = true;
                }
                Preconditions.checkState(z, "Sign-in mode should have been set explicitly by auto-manage.");
            } else if (this.zzip == null) {
                this.zzip = Integer.valueOf(zza(this.zzil.values(), false));
            } else if (this.zzip.intValue() == 2) {
                throw new IllegalStateException("Cannot call connect() when SignInMode is set to SIGN_IN_MODE_OPTIONAL. Call connect(SIGN_IN_MODE_OPTIONAL) instead.");
            }
            connect(this.zzip.intValue());
        } finally {
            this.zzga.unlock();
        }
    }

    public final void connect(int i) {
        this.zzga.lock();
        boolean z = true;
        if (!(i == 3 || i == 1 || i == 2)) {
            z = false;
        }
        try {
            StringBuilder sb = new StringBuilder(33);
            sb.append("Illegal sign-in mode: ");
            sb.append(i);
            Preconditions.checkArgument(z, sb.toString());
            zzg(i);
            zzax();
        } finally {
            this.zzga.unlock();
        }
    }

    public final void disconnect() {
        this.zzga.lock();
        try {
            this.zzir.release();
            if (this.zzif != null) {
                this.zzif.disconnect();
            }
            this.zzin.release();
            for (ApiMethodImpl apiMethodImpl : this.zzgo) {
                apiMethodImpl.zza((zzcn) null);
                apiMethodImpl.cancel();
            }
            this.zzgo.clear();
            if (this.zzif != null) {
                zzaz();
                this.zzie.disableCallbacks();
            }
        } finally {
            this.zzga.unlock();
        }
    }

    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.append(str).append("mContext=").println(this.mContext);
        printWriter.append(str).append("mResuming=").print(this.zzig);
        printWriter.append(" mWorkQueue.size()=").print(this.zzgo.size());
        printWriter.append(" mUnconsumedApiCalls.size()=").println(this.zzir.zzmo.size());
        if (this.zzif != null) {
            this.zzif.dump(str, fileDescriptor, printWriter, strArr);
        }
    }

    public final <A extends AnyClient, R extends Result, T extends ApiMethodImpl<R, A>> T enqueue(@NonNull T t) {
        Preconditions.checkArgument(t.getClientKey() != null, "This task can not be enqueued (it's probably a Batch or malformed)");
        boolean containsKey = this.zzil.containsKey(t.getClientKey());
        String name = t.getApi() != null ? t.getApi().getName() : "the API";
        StringBuilder sb = new StringBuilder(String.valueOf(name).length() + 65);
        sb.append("GoogleApiClient is not configured to use ");
        sb.append(name);
        sb.append(" required for this call.");
        Preconditions.checkArgument(containsKey, sb.toString());
        this.zzga.lock();
        try {
            if (this.zzif == null) {
                this.zzgo.add(t);
            } else {
                t = this.zzif.enqueue(t);
            }
            return t;
        } finally {
            this.zzga.unlock();
        }
    }

    public final <A extends AnyClient, T extends ApiMethodImpl<? extends Result, A>> T execute(@NonNull T t) {
        Preconditions.checkArgument(t.getClientKey() != null, "This task can not be executed (it's probably a Batch or malformed)");
        boolean containsKey = this.zzil.containsKey(t.getClientKey());
        String name = t.getApi() != null ? t.getApi().getName() : "the API";
        StringBuilder sb = new StringBuilder(String.valueOf(name).length() + 65);
        sb.append("GoogleApiClient is not configured to use ");
        sb.append(name);
        sb.append(" required for this call.");
        Preconditions.checkArgument(containsKey, sb.toString());
        this.zzga.lock();
        try {
            if (this.zzif == null) {
                throw new IllegalStateException("GoogleApiClient is not connected yet.");
            }
            if (this.zzig) {
                this.zzgo.add(t);
                while (!this.zzgo.isEmpty()) {
                    ApiMethodImpl apiMethodImpl = (ApiMethodImpl) this.zzgo.remove();
                    this.zzir.zzb(apiMethodImpl);
                    apiMethodImpl.setFailedResult(Status.RESULT_INTERNAL_ERROR);
                }
            } else {
                t = this.zzif.execute(t);
            }
            return t;
        } finally {
            this.zzga.unlock();
        }
    }

    @NonNull
    public final <C extends Client> C getClient(@NonNull AnyClientKey<C> anyClientKey) {
        C c = (Client) this.zzil.get(anyClientKey);
        Preconditions.checkNotNull(c, "Appropriate Api was not requested.");
        return c;
    }

    @NonNull
    public final ConnectionResult getConnectionResult(@NonNull Api<?> api) {
        ConnectionResult connectionResult;
        this.zzga.lock();
        try {
            if (!isConnected() && !this.zzig) {
                throw new IllegalStateException("Cannot invoke getConnectionResult unless GoogleApiClient is connected");
            } else if (this.zzil.containsKey(api.getClientKey())) {
                ConnectionResult connectionResult2 = this.zzif.getConnectionResult(api);
                if (connectionResult2 == null) {
                    if (this.zzig) {
                        connectionResult = ConnectionResult.RESULT_SUCCESS;
                    } else {
                        Log.w("GoogleApiClientImpl", zzbb());
                        Log.wtf("GoogleApiClientImpl", String.valueOf(api.getName()).concat(" requested in getConnectionResult is not connected but is not present in the failed  connections map"), new Exception());
                        connectionResult = new ConnectionResult(8, null);
                    }
                    return connectionResult;
                }
                this.zzga.unlock();
                return connectionResult2;
            } else {
                throw new IllegalArgumentException(String.valueOf(api.getName()).concat(" was never registered with GoogleApiClient"));
            }
        } finally {
            this.zzga.unlock();
        }
    }

    public final Context getContext() {
        return this.mContext;
    }

    public final Looper getLooper() {
        return this.zzcn;
    }

    public final boolean hasApi(@NonNull Api<?> api) {
        return this.zzil.containsKey(api.getClientKey());
    }

    public final boolean hasConnectedApi(@NonNull Api<?> api) {
        if (!isConnected()) {
            return false;
        }
        Client client = (Client) this.zzil.get(api.getClientKey());
        return client != null && client.isConnected();
    }

    public final boolean isConnected() {
        return this.zzif != null && this.zzif.isConnected();
    }

    public final boolean isConnecting() {
        return this.zzif != null && this.zzif.isConnecting();
    }

    public final boolean isConnectionCallbacksRegistered(@NonNull ConnectionCallbacks connectionCallbacks) {
        return this.zzie.isConnectionCallbacksRegistered(connectionCallbacks);
    }

    public final boolean isConnectionFailedListenerRegistered(@NonNull OnConnectionFailedListener onConnectionFailedListener) {
        return this.zzie.isConnectionFailedListenerRegistered(onConnectionFailedListener);
    }

    public final boolean maybeSignIn(SignInConnectionListener signInConnectionListener) {
        return this.zzif != null && this.zzif.maybeSignIn(signInConnectionListener);
    }

    public final void maybeSignOut() {
        if (this.zzif != null) {
            this.zzif.maybeSignOut();
        }
    }

    public final void reconnect() {
        disconnect();
        connect();
    }

    public final void registerConnectionCallbacks(@NonNull ConnectionCallbacks connectionCallbacks) {
        this.zzie.registerConnectionCallbacks(connectionCallbacks);
    }

    public final void registerConnectionFailedListener(@NonNull OnConnectionFailedListener onConnectionFailedListener) {
        this.zzie.registerConnectionFailedListener(onConnectionFailedListener);
    }

    public final <L> ListenerHolder<L> registerListener(@NonNull L l) {
        this.zzga.lock();
        try {
            ListenerHolder<L> zza = this.zzin.zza(l, this.zzcn, "NO_TYPE");
            return zza;
        } finally {
            this.zzga.unlock();
        }
    }

    public final void stopAutoManage(@NonNull FragmentActivity fragmentActivity) {
        LifecycleActivity lifecycleActivity = new LifecycleActivity(fragmentActivity);
        if (this.zzde >= 0) {
            zzi.zza(lifecycleActivity).zzc(this.zzde);
            return;
        }
        throw new IllegalStateException("Called stopAutoManage but automatic lifecycle management is not enabled.");
    }

    public final void unregisterConnectionCallbacks(@NonNull ConnectionCallbacks connectionCallbacks) {
        this.zzie.unregisterConnectionCallbacks(connectionCallbacks);
    }

    public final void unregisterConnectionFailedListener(@NonNull OnConnectionFailedListener onConnectionFailedListener) {
        this.zzie.unregisterConnectionFailedListener(onConnectionFailedListener);
    }

    public final void zza(zzch zzch) {
        this.zzga.lock();
        try {
            if (this.zziq == null) {
                this.zziq = new HashSet();
            }
            this.zziq.add(zzch);
        } finally {
            this.zzga.unlock();
        }
    }

    /* access modifiers changed from: 0000 */
    @GuardedBy("mLock")
    public final boolean zzaz() {
        if (!this.zzig) {
            return false;
        }
        this.zzig = false;
        this.zzij.removeMessages(2);
        this.zzij.removeMessages(1);
        if (this.zzik != null) {
            this.zzik.unregister();
            this.zzik = null;
        }
        return true;
    }

    @GuardedBy("mLock")
    public final void zzb(int i, boolean z) {
        if (i == 1 && !z && !this.zzig) {
            this.zzig = true;
            if (this.zzik == null) {
                this.zzik = this.zzdg.registerCallbackOnUpdate(this.mContext.getApplicationContext(), new zzbb(this));
            }
            this.zzij.sendMessageDelayed(this.zzij.obtainMessage(1), this.zzih);
            this.zzij.sendMessageDelayed(this.zzij.obtainMessage(2), this.zzii);
        }
        this.zzir.zzce();
        this.zzie.onUnintentionalDisconnection(i);
        this.zzie.disableCallbacks();
        if (i == 2) {
            zzax();
        }
    }

    @GuardedBy("mLock")
    public final void zzb(Bundle bundle) {
        while (!this.zzgo.isEmpty()) {
            execute((ApiMethodImpl) this.zzgo.remove());
        }
        this.zzie.onConnectionSuccess(bundle);
    }

    public final void zzb(zzch zzch) {
        String str;
        String str2;
        Exception exc;
        this.zzga.lock();
        try {
            if (this.zziq == null) {
                str = "GoogleApiClientImpl";
                str2 = "Attempted to remove pending transform when no transforms are registered.";
                exc = new Exception();
            } else if (!this.zziq.remove(zzch)) {
                str = "GoogleApiClientImpl";
                str2 = "Failed to remove pending transform - this may lead to memory leaks!";
                exc = new Exception();
            } else {
                if (!zzba()) {
                    this.zzif.zzz();
                }
            }
            Log.wtf(str, str2, exc);
        } finally {
            this.zzga.unlock();
        }
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: 0000 */
    public final boolean zzba() {
        this.zzga.lock();
        try {
            if (this.zziq == null) {
                this.zzga.unlock();
                return false;
            }
            boolean z = !this.zziq.isEmpty();
            this.zzga.unlock();
            return z;
        } catch (Throwable th) {
            this.zzga.unlock();
            throw th;
        }
    }

    /* access modifiers changed from: 0000 */
    public final String zzbb() {
        StringWriter stringWriter = new StringWriter();
        dump("", null, new PrintWriter(stringWriter), null);
        return stringWriter.toString();
    }

    @GuardedBy("mLock")
    public final void zzc(ConnectionResult connectionResult) {
        if (!this.zzdg.isPlayServicesPossiblyUpdating(this.mContext, connectionResult.getErrorCode())) {
            zzaz();
        }
        if (!this.zzig) {
            this.zzie.onConnectionFailure(connectionResult);
            this.zzie.disableCallbacks();
        }
    }
}
