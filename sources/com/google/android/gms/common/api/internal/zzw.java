package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.p001v4.util.ArrayMap;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.AbstractClientBuilder;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.Api.AnyClientKey;
import com.google.android.gms.common.api.Api.Client;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.ClientSettings.OptionalApiSettings;
import com.google.android.gms.common.util.concurrent.HandlerExecutor;
import com.google.android.gms.signin.SignInClient;
import com.google.android.gms.signin.SignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import javax.annotation.concurrent.GuardedBy;

public final class zzw implements zzbp {
    private final Looper zzcn;
    private final GoogleApiManager zzcq;
    /* access modifiers changed from: private */
    public final Lock zzga;
    private final ClientSettings zzgf;
    /* access modifiers changed from: private */
    public final Map<AnyClientKey<?>, zzv<?>> zzgg = new HashMap();
    /* access modifiers changed from: private */
    public final Map<AnyClientKey<?>, zzv<?>> zzgh = new HashMap();
    private final Map<Api<?>, Boolean> zzgi;
    /* access modifiers changed from: private */
    public final zzav zzgj;
    private final GoogleApiAvailabilityLight zzgk;
    /* access modifiers changed from: private */
    public final Condition zzgl;
    private final boolean zzgm;
    /* access modifiers changed from: private */
    public final boolean zzgn;
    private final Queue<ApiMethodImpl<?, ?>> zzgo = new LinkedList();
    /* access modifiers changed from: private */
    @GuardedBy("mLock")
    public boolean zzgp;
    /* access modifiers changed from: private */
    @GuardedBy("mLock")
    public Map<zzh<?>, ConnectionResult> zzgq;
    /* access modifiers changed from: private */
    @GuardedBy("mLock")
    public Map<zzh<?>, ConnectionResult> zzgr;
    @GuardedBy("mLock")
    private zzz zzgs;
    /* access modifiers changed from: private */
    @GuardedBy("mLock")
    public ConnectionResult zzgt;

    public zzw(Context context, Lock lock, Looper looper, GoogleApiAvailabilityLight googleApiAvailabilityLight, Map<AnyClientKey<?>, Client> map, ClientSettings clientSettings, Map<Api<?>, Boolean> map2, AbstractClientBuilder<? extends SignInClient, SignInOptions> abstractClientBuilder, ArrayList<zzp> arrayList, zzav zzav, boolean z) {
        boolean z2;
        boolean z3;
        boolean z4;
        this.zzga = lock;
        Looper looper2 = looper;
        this.zzcn = looper2;
        this.zzgl = lock.newCondition();
        this.zzgk = googleApiAvailabilityLight;
        this.zzgj = zzav;
        this.zzgi = map2;
        ClientSettings clientSettings2 = clientSettings;
        this.zzgf = clientSettings2;
        this.zzgm = z;
        HashMap hashMap = new HashMap();
        for (Api api : map2.keySet()) {
            hashMap.put(api.getClientKey(), api);
        }
        HashMap hashMap2 = new HashMap();
        ArrayList arrayList2 = arrayList;
        int size = arrayList2.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList2.get(i);
            i++;
            zzp zzp = (zzp) obj;
            hashMap2.put(zzp.mApi, zzp);
        }
        boolean z5 = false;
        boolean z6 = true;
        boolean z7 = false;
        for (Entry entry : map.entrySet()) {
            Api api2 = (Api) hashMap.get(entry.getKey());
            Client client = (Client) entry.getValue();
            if (client.requiresGooglePlayServices()) {
                if (!((Boolean) this.zzgi.get(api2)).booleanValue()) {
                    z3 = z6;
                    z4 = true;
                } else {
                    z3 = z6;
                    z4 = z7;
                }
                z2 = true;
            } else {
                z2 = z5;
                z4 = z7;
                z3 = false;
            }
            zzv zzv = r1;
            Client client2 = client;
            Entry entry2 = entry;
            zzv zzv2 = new zzv(context, api2, looper2, client, (zzp) hashMap2.get(api2), clientSettings2, abstractClientBuilder);
            this.zzgg.put((AnyClientKey) entry2.getKey(), zzv);
            if (client2.requiresSignIn()) {
                this.zzgh.put((AnyClientKey) entry2.getKey(), zzv);
            }
            z7 = z4;
            z6 = z3;
            z5 = z2;
            looper2 = looper;
        }
        this.zzgn = z5 && !z6 && !z7;
        this.zzcq = GoogleApiManager.zzbf();
    }

    @Nullable
    private final ConnectionResult zza(@NonNull AnyClientKey<?> anyClientKey) {
        this.zzga.lock();
        try {
            zzv zzv = (zzv) this.zzgg.get(anyClientKey);
            if (this.zzgq == null || zzv == null) {
                this.zzga.unlock();
                return null;
            }
            ConnectionResult connectionResult = (ConnectionResult) this.zzgq.get(zzv.zzm());
            return connectionResult;
        } finally {
            this.zzga.unlock();
        }
    }

    /* access modifiers changed from: private */
    public final boolean zza(zzv<?> zzv, ConnectionResult connectionResult) {
        return !connectionResult.isSuccess() && !connectionResult.hasResolution() && ((Boolean) this.zzgi.get(zzv.getApi())).booleanValue() && zzv.zzae().requiresGooglePlayServices() && this.zzgk.isUserResolvableError(connectionResult.getErrorCode());
    }

    private final boolean zzaf() {
        this.zzga.lock();
        try {
            if (this.zzgp) {
                if (this.zzgm) {
                    for (AnyClientKey zza : this.zzgh.keySet()) {
                        ConnectionResult zza2 = zza(zza);
                        if (zza2 != null) {
                            if (!zza2.isSuccess()) {
                            }
                        }
                    }
                    this.zzga.unlock();
                    return true;
                }
            }
            return false;
        } finally {
            this.zzga.unlock();
        }
    }

    /* access modifiers changed from: private */
    @GuardedBy("mLock")
    public final void zzag() {
        Set<Scope> hashSet;
        zzav zzav;
        if (this.zzgf == null) {
            zzav = this.zzgj;
            hashSet = Collections.emptySet();
        } else {
            hashSet = new HashSet<>(this.zzgf.getRequiredScopes());
            Map optionalApiSettings = this.zzgf.getOptionalApiSettings();
            for (Api api : optionalApiSettings.keySet()) {
                ConnectionResult connectionResult = getConnectionResult(api);
                if (connectionResult != null && connectionResult.isSuccess()) {
                    hashSet.addAll(((OptionalApiSettings) optionalApiSettings.get(api)).mScopes);
                }
            }
            zzav = this.zzgj;
        }
        zzav.zzim = hashSet;
    }

    /* access modifiers changed from: private */
    @GuardedBy("mLock")
    public final void zzah() {
        while (!this.zzgo.isEmpty()) {
            execute((ApiMethodImpl) this.zzgo.remove());
        }
        this.zzgj.zzb((Bundle) null);
    }

    /* access modifiers changed from: private */
    @Nullable
    @GuardedBy("mLock")
    public final ConnectionResult zzai() {
        ConnectionResult connectionResult = null;
        int i = 0;
        int i2 = 0;
        ConnectionResult connectionResult2 = null;
        for (zzv zzv : this.zzgg.values()) {
            Api api = zzv.getApi();
            ConnectionResult connectionResult3 = (ConnectionResult) this.zzgq.get(zzv.zzm());
            if (!connectionResult3.isSuccess() && (!((Boolean) this.zzgi.get(api)).booleanValue() || connectionResult3.hasResolution() || this.zzgk.isUserResolvableError(connectionResult3.getErrorCode()))) {
                if (connectionResult3.getErrorCode() != 4 || !this.zzgm) {
                    int priority = api.zzj().getPriority();
                    if (connectionResult == null || i > priority) {
                        connectionResult = connectionResult3;
                        i = priority;
                    }
                } else {
                    int priority2 = api.zzj().getPriority();
                    if (connectionResult2 == null || i2 > priority2) {
                        connectionResult2 = connectionResult3;
                        i2 = priority2;
                    }
                }
            }
        }
        return (connectionResult == null || connectionResult2 == null || i <= i2) ? connectionResult : connectionResult2;
    }

    private final <T extends ApiMethodImpl<? extends Result, ? extends AnyClient>> boolean zzb(@NonNull T t) {
        AnyClientKey clientKey = t.getClientKey();
        ConnectionResult zza = zza(clientKey);
        if (zza == null || zza.getErrorCode() != 4) {
            return false;
        }
        t.setFailedResult(new Status(4, null, this.zzcq.zza(((zzv) this.zzgg.get(clientKey)).zzm(), System.identityHashCode(this.zzgj))));
        return true;
    }

    @GuardedBy("mLock")
    public final ConnectionResult blockingConnect() {
        connect();
        while (isConnecting()) {
            try {
                this.zzgl.await();
            } catch (InterruptedException unused) {
                Thread.currentThread().interrupt();
                return new ConnectionResult(15, null);
            }
        }
        return isConnected() ? ConnectionResult.RESULT_SUCCESS : this.zzgt != null ? this.zzgt : new ConnectionResult(13, null);
    }

    @GuardedBy("mLock")
    public final ConnectionResult blockingConnect(long j, TimeUnit timeUnit) {
        connect();
        long nanos = timeUnit.toNanos(j);
        while (isConnecting()) {
            if (nanos <= 0) {
                try {
                    disconnect();
                    return new ConnectionResult(14, null);
                } catch (InterruptedException unused) {
                    Thread.currentThread().interrupt();
                    return new ConnectionResult(15, null);
                }
            } else {
                nanos = this.zzgl.awaitNanos(nanos);
            }
        }
        return isConnected() ? ConnectionResult.RESULT_SUCCESS : this.zzgt != null ? this.zzgt : new ConnectionResult(13, null);
    }

    public final void connect() {
        this.zzga.lock();
        try {
            if (!this.zzgp) {
                this.zzgp = true;
                this.zzgq = null;
                this.zzgr = null;
                this.zzgs = null;
                this.zzgt = null;
                this.zzcq.zzr();
                this.zzcq.zza((Iterable<? extends GoogleApi<?>>) this.zzgg.values()).addOnCompleteListener((Executor) new HandlerExecutor(this.zzcn), (OnCompleteListener<TResult>) new zzy<TResult>(this));
            }
        } finally {
            this.zzga.unlock();
        }
    }

    public final void disconnect() {
        this.zzga.lock();
        try {
            this.zzgp = false;
            this.zzgq = null;
            this.zzgr = null;
            if (this.zzgs != null) {
                this.zzgs.cancel();
                this.zzgs = null;
            }
            this.zzgt = null;
            while (!this.zzgo.isEmpty()) {
                ApiMethodImpl apiMethodImpl = (ApiMethodImpl) this.zzgo.remove();
                apiMethodImpl.zza((zzcn) null);
                apiMethodImpl.cancel();
            }
            this.zzgl.signalAll();
        } finally {
            this.zzga.unlock();
        }
    }

    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    public final <A extends AnyClient, R extends Result, T extends ApiMethodImpl<R, A>> T enqueue(@NonNull T t) {
        if (this.zzgm && zzb(t)) {
            return t;
        }
        if (!isConnected()) {
            this.zzgo.add(t);
            return t;
        }
        this.zzgj.zzir.zzb(t);
        return ((zzv) this.zzgg.get(t.getClientKey())).doRead(t);
    }

    public final <A extends AnyClient, T extends ApiMethodImpl<? extends Result, A>> T execute(@NonNull T t) {
        AnyClientKey clientKey = t.getClientKey();
        if (this.zzgm && zzb(t)) {
            return t;
        }
        this.zzgj.zzir.zzb(t);
        return ((zzv) this.zzgg.get(clientKey)).doWrite(t);
    }

    @Nullable
    public final ConnectionResult getConnectionResult(@NonNull Api<?> api) {
        return zza(api.getClientKey());
    }

    public final boolean isConnected() {
        this.zzga.lock();
        try {
            boolean z = this.zzgq != null && this.zzgt == null;
            return z;
        } finally {
            this.zzga.unlock();
        }
    }

    public final boolean isConnecting() {
        this.zzga.lock();
        try {
            boolean z = this.zzgq == null && this.zzgp;
            return z;
        } finally {
            this.zzga.unlock();
        }
    }

    /* JADX INFO: finally extract failed */
    public final boolean maybeSignIn(SignInConnectionListener signInConnectionListener) {
        this.zzga.lock();
        try {
            if (!this.zzgp || zzaf()) {
                this.zzga.unlock();
                return false;
            }
            this.zzcq.zzr();
            this.zzgs = new zzz(this, signInConnectionListener);
            this.zzcq.zza((Iterable<? extends GoogleApi<?>>) this.zzgh.values()).addOnCompleteListener((Executor) new HandlerExecutor(this.zzcn), (OnCompleteListener<TResult>) this.zzgs);
            this.zzga.unlock();
            return true;
        } catch (Throwable th) {
            this.zzga.unlock();
            throw th;
        }
    }

    public final void maybeSignOut() {
        this.zzga.lock();
        try {
            this.zzcq.maybeSignOut();
            if (this.zzgs != null) {
                this.zzgs.cancel();
                this.zzgs = null;
            }
            if (this.zzgr == null) {
                this.zzgr = new ArrayMap(this.zzgh.size());
            }
            ConnectionResult connectionResult = new ConnectionResult(4);
            for (zzv zzm : this.zzgh.values()) {
                this.zzgr.put(zzm.zzm(), connectionResult);
            }
            if (this.zzgq != null) {
                this.zzgq.putAll(this.zzgr);
            }
        } finally {
            this.zzga.unlock();
        }
    }

    public final void zzz() {
    }
}
