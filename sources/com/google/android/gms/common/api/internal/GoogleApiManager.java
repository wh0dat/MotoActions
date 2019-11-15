package com.google.android.gms.common.api.internal;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.p001v4.util.ArrayMap;
import android.support.p001v4.util.ArraySet;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.Api.Client;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.UnsupportedApiCallException;
import com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl;
import com.google.android.gms.common.api.internal.ListenerHolder.ListenerKey;
import com.google.android.gms.common.internal.BaseGmsClient.ConnectionProgressReportCallbacks;
import com.google.android.gms.common.internal.GoogleApiAvailabilityCache;
import com.google.android.gms.common.internal.IAccountAccessor;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.SimpleClientAdapter;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.common.util.PlatformVersion;
import com.google.android.gms.signin.SignInClient;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.concurrent.GuardedBy;

@KeepForSdk
public class GoogleApiManager implements Callback {
    /* access modifiers changed from: private */
    public static final Object lock = new Object();
    public static final Status zzjj = new Status(4, "Sign-out occurred while this API call was in progress.");
    /* access modifiers changed from: private */
    public static final Status zzjk = new Status(4, "The user must be signed in to make this API call.");
    @GuardedBy("lock")
    private static GoogleApiManager zzjo;
    /* access modifiers changed from: private */
    public final Handler handler;
    /* access modifiers changed from: private */
    public long zzjl = 5000;
    /* access modifiers changed from: private */
    public long zzjm = 120000;
    /* access modifiers changed from: private */
    public long zzjn = 10000;
    /* access modifiers changed from: private */
    public final Context zzjp;
    /* access modifiers changed from: private */
    public final GoogleApiAvailability zzjq;
    /* access modifiers changed from: private */
    public final GoogleApiAvailabilityCache zzjr;
    private final AtomicInteger zzjs = new AtomicInteger(1);
    private final AtomicInteger zzjt = new AtomicInteger(0);
    /* access modifiers changed from: private */
    public final Map<zzh<?>, zza<?>> zzju = new ConcurrentHashMap(5, 0.75f, 1);
    /* access modifiers changed from: private */
    @GuardedBy("lock")
    public zzad zzjv = null;
    /* access modifiers changed from: private */
    @GuardedBy("lock")
    public final Set<zzh<?>> zzjw = new ArraySet();
    private final Set<zzh<?>> zzjx = new ArraySet();

    public class zza<O extends ApiOptions> implements ConnectionCallbacks, OnConnectionFailedListener, zzq {
        private final zzh<O> zzhc;
        private final Queue<zzb> zzjz = new LinkedList();
        /* access modifiers changed from: private */
        public final Client zzka;
        private final AnyClient zzkb;
        private final zzaa zzkc;
        private final Set<zzj> zzkd = new HashSet();
        private final Map<ListenerKey<?>, zzbv> zzke = new HashMap();
        private final int zzkf;
        private final zzby zzkg;
        private boolean zzkh;
        private final List<zzb> zzki = new ArrayList();
        private ConnectionResult zzkj = null;

        @WorkerThread
        public zza(GoogleApi<O> googleApi) {
            this.zzka = googleApi.zza(GoogleApiManager.this.handler.getLooper(), this);
            this.zzkb = this.zzka instanceof SimpleClientAdapter ? ((SimpleClientAdapter) this.zzka).getClient() : this.zzka;
            this.zzhc = googleApi.zzm();
            this.zzkc = new zzaa();
            this.zzkf = googleApi.getInstanceId();
            if (this.zzka.requiresSignIn()) {
                this.zzkg = googleApi.zza(GoogleApiManager.this.zzjp, GoogleApiManager.this.handler);
            } else {
                this.zzkg = null;
            }
        }

        /* access modifiers changed from: private */
        @WorkerThread
        public final void zza(zzb zzb) {
            if (this.zzki.contains(zzb) && !this.zzkh) {
                if (!this.zzka.isConnected()) {
                    connect();
                    return;
                }
                zzbl();
            }
        }

        /* access modifiers changed from: private */
        @WorkerThread
        public final void zzb(zzb zzb) {
            if (this.zzki.remove(zzb)) {
                GoogleApiManager.this.handler.removeMessages(15, zzb);
                GoogleApiManager.this.handler.removeMessages(16, zzb);
                Feature zzd = zzb.zzdr;
                ArrayList arrayList = new ArrayList(this.zzjz.size());
                for (zzb zzb2 : this.zzjz) {
                    if (zzb2 instanceof zzf) {
                        Feature[] requiredFeatures = ((zzf) zzb2).getRequiredFeatures();
                        if (requiredFeatures != null && ArrayUtils.contains((T[]) requiredFeatures, zzd)) {
                            arrayList.add(zzb2);
                        }
                    }
                }
                ArrayList arrayList2 = arrayList;
                int size = arrayList2.size();
                int i = 0;
                while (i < size) {
                    Object obj = arrayList2.get(i);
                    i++;
                    zzb zzb3 = (zzb) obj;
                    this.zzjz.remove(zzb3);
                    zzb3.zza((RuntimeException) new UnsupportedApiCallException(zzd));
                }
            }
        }

        @WorkerThread
        private final boolean zzb(zzb zzb) {
            if (!(zzb instanceof zzf)) {
                zzc(zzb);
                return true;
            }
            zzf zzf = (zzf) zzb;
            Feature[] requiredFeatures = zzf.getRequiredFeatures();
            if (requiredFeatures == null || requiredFeatures.length == 0) {
                zzc(zzb);
                return true;
            }
            Feature[] availableFeatures = this.zzka.getAvailableFeatures();
            if (availableFeatures == null) {
                availableFeatures = new Feature[0];
            }
            ArrayMap arrayMap = new ArrayMap(availableFeatures.length);
            for (Feature feature : availableFeatures) {
                arrayMap.put(feature.getName(), Long.valueOf(feature.getVersion()));
            }
            for (Feature feature2 : requiredFeatures) {
                if (!arrayMap.containsKey(feature2.getName()) || ((Long) arrayMap.get(feature2.getName())).longValue() < feature2.getVersion()) {
                    if (zzf.shouldAutoResolveMissingFeatures()) {
                        zzb zzb2 = new zzb(this.zzhc, feature2, null);
                        int indexOf = this.zzki.indexOf(zzb2);
                        if (indexOf >= 0) {
                            zzb zzb3 = (zzb) this.zzki.get(indexOf);
                            GoogleApiManager.this.handler.removeMessages(15, zzb3);
                            GoogleApiManager.this.handler.sendMessageDelayed(Message.obtain(GoogleApiManager.this.handler, 15, zzb3), GoogleApiManager.this.zzjl);
                            return false;
                        }
                        this.zzki.add(zzb2);
                        GoogleApiManager.this.handler.sendMessageDelayed(Message.obtain(GoogleApiManager.this.handler, 15, zzb2), GoogleApiManager.this.zzjl);
                        GoogleApiManager.this.handler.sendMessageDelayed(Message.obtain(GoogleApiManager.this.handler, 16, zzb2), GoogleApiManager.this.zzjm);
                        ConnectionResult connectionResult = new ConnectionResult(2, null);
                        if (!zzh(connectionResult)) {
                            GoogleApiManager.this.zzc(connectionResult, this.zzkf);
                            return false;
                        }
                    } else {
                        zzf.zza((RuntimeException) new UnsupportedApiCallException(feature2));
                    }
                    return false;
                }
                this.zzki.remove(new zzb(this.zzhc, feature2, null));
            }
            zzc(zzb);
            return true;
        }

        /* access modifiers changed from: private */
        @WorkerThread
        public final boolean zzb(boolean z) {
            Preconditions.checkHandlerThread(GoogleApiManager.this.handler);
            if (!this.zzka.isConnected() || this.zzke.size() != 0) {
                return false;
            }
            if (this.zzkc.zzaj()) {
                if (z) {
                    zzbr();
                }
                return false;
            }
            this.zzka.disconnect();
            return true;
        }

        /* access modifiers changed from: private */
        @WorkerThread
        public final void zzbj() {
            zzbo();
            zzi(ConnectionResult.RESULT_SUCCESS);
            zzbq();
            for (zzbv zzbv : this.zzke.values()) {
                try {
                    zzbv.zzlt.registerListener(this.zzkb, new TaskCompletionSource());
                } catch (DeadObjectException unused) {
                    onConnectionSuspended(1);
                    this.zzka.disconnect();
                } catch (RemoteException unused2) {
                }
            }
            zzbl();
            zzbr();
        }

        /* access modifiers changed from: private */
        @WorkerThread
        public final void zzbk() {
            zzbo();
            this.zzkh = true;
            this.zzkc.zzal();
            GoogleApiManager.this.handler.sendMessageDelayed(Message.obtain(GoogleApiManager.this.handler, 9, this.zzhc), GoogleApiManager.this.zzjl);
            GoogleApiManager.this.handler.sendMessageDelayed(Message.obtain(GoogleApiManager.this.handler, 11, this.zzhc), GoogleApiManager.this.zzjm);
            GoogleApiManager.this.zzjr.flush();
        }

        @WorkerThread
        private final void zzbl() {
            ArrayList arrayList = new ArrayList(this.zzjz);
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                zzb zzb = (zzb) obj;
                if (!this.zzka.isConnected()) {
                    return;
                }
                if (zzb(zzb)) {
                    this.zzjz.remove(zzb);
                }
            }
        }

        @WorkerThread
        private final void zzbq() {
            if (this.zzkh) {
                GoogleApiManager.this.handler.removeMessages(11, this.zzhc);
                GoogleApiManager.this.handler.removeMessages(9, this.zzhc);
                this.zzkh = false;
            }
        }

        private final void zzbr() {
            GoogleApiManager.this.handler.removeMessages(12, this.zzhc);
            GoogleApiManager.this.handler.sendMessageDelayed(GoogleApiManager.this.handler.obtainMessage(12, this.zzhc), GoogleApiManager.this.zzjn);
        }

        @WorkerThread
        private final void zzc(zzb zzb) {
            zzb.zza(this.zzkc, requiresSignIn());
            try {
                zzb.zza(this);
            } catch (DeadObjectException unused) {
                onConnectionSuspended(1);
                this.zzka.disconnect();
            }
        }

        @WorkerThread
        private final boolean zzh(@NonNull ConnectionResult connectionResult) {
            synchronized (GoogleApiManager.lock) {
                if (GoogleApiManager.this.zzjv == null || !GoogleApiManager.this.zzjw.contains(this.zzhc)) {
                    return false;
                }
                GoogleApiManager.this.zzjv.zzb(connectionResult, this.zzkf);
                return true;
            }
        }

        @WorkerThread
        private final void zzi(ConnectionResult connectionResult) {
            for (zzj zzj : this.zzkd) {
                String str = null;
                if (Objects.equal(connectionResult, ConnectionResult.RESULT_SUCCESS)) {
                    str = this.zzka.getEndpointPackageName();
                }
                zzj.zza(this.zzhc, connectionResult, str);
            }
            this.zzkd.clear();
        }

        @WorkerThread
        public final void connect() {
            Preconditions.checkHandlerThread(GoogleApiManager.this.handler);
            if (!this.zzka.isConnected() && !this.zzka.isConnecting()) {
                int clientAvailability = GoogleApiManager.this.zzjr.getClientAvailability(GoogleApiManager.this.zzjp, this.zzka);
                if (clientAvailability != 0) {
                    onConnectionFailed(new ConnectionResult(clientAvailability, null));
                    return;
                }
                zzc zzc = new zzc(this.zzka, this.zzhc);
                if (this.zzka.requiresSignIn()) {
                    this.zzkg.zza((zzcb) zzc);
                }
                this.zzka.connect(zzc);
            }
        }

        public final int getInstanceId() {
            return this.zzkf;
        }

        /* access modifiers changed from: 0000 */
        public final boolean isConnected() {
            return this.zzka.isConnected();
        }

        public final void onConnected(@Nullable Bundle bundle) {
            if (Looper.myLooper() == GoogleApiManager.this.handler.getLooper()) {
                zzbj();
            } else {
                GoogleApiManager.this.handler.post(new zzbi(this));
            }
        }

        @WorkerThread
        public final void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            Preconditions.checkHandlerThread(GoogleApiManager.this.handler);
            if (this.zzkg != null) {
                this.zzkg.zzbz();
            }
            zzbo();
            GoogleApiManager.this.zzjr.flush();
            zzi(connectionResult);
            if (connectionResult.getErrorCode() == 4) {
                zzc(GoogleApiManager.zzjk);
            } else if (this.zzjz.isEmpty()) {
                this.zzkj = connectionResult;
            } else {
                if (!zzh(connectionResult) && !GoogleApiManager.this.zzc(connectionResult, this.zzkf)) {
                    if (connectionResult.getErrorCode() == 18) {
                        this.zzkh = true;
                    }
                    if (this.zzkh) {
                        GoogleApiManager.this.handler.sendMessageDelayed(Message.obtain(GoogleApiManager.this.handler, 9, this.zzhc), GoogleApiManager.this.zzjl);
                        return;
                    }
                    String zzq = this.zzhc.zzq();
                    StringBuilder sb = new StringBuilder(String.valueOf(zzq).length() + 38);
                    sb.append("API: ");
                    sb.append(zzq);
                    sb.append(" is not available on this device.");
                    zzc(new Status(17, sb.toString()));
                }
            }
        }

        public final void onConnectionSuspended(int i) {
            if (Looper.myLooper() == GoogleApiManager.this.handler.getLooper()) {
                zzbk();
            } else {
                GoogleApiManager.this.handler.post(new zzbj(this));
            }
        }

        public final boolean requiresSignIn() {
            return this.zzka.requiresSignIn();
        }

        @WorkerThread
        public final void resume() {
            Preconditions.checkHandlerThread(GoogleApiManager.this.handler);
            if (this.zzkh) {
                connect();
            }
        }

        public final void zza(ConnectionResult connectionResult, Api<?> api, boolean z) {
            if (Looper.myLooper() == GoogleApiManager.this.handler.getLooper()) {
                onConnectionFailed(connectionResult);
            } else {
                GoogleApiManager.this.handler.post(new zzbk(this, connectionResult));
            }
        }

        @WorkerThread
        public final void zza(zzb zzb) {
            Preconditions.checkHandlerThread(GoogleApiManager.this.handler);
            if (!this.zzka.isConnected()) {
                this.zzjz.add(zzb);
                if (this.zzkj == null || !this.zzkj.hasResolution()) {
                    connect();
                } else {
                    onConnectionFailed(this.zzkj);
                }
            } else if (zzb(zzb)) {
                zzbr();
            } else {
                this.zzjz.add(zzb);
            }
        }

        @WorkerThread
        public final void zza(zzj zzj) {
            Preconditions.checkHandlerThread(GoogleApiManager.this.handler);
            this.zzkd.add(zzj);
        }

        public final Client zzae() {
            return this.zzka;
        }

        @WorkerThread
        public final void zzay() {
            Preconditions.checkHandlerThread(GoogleApiManager.this.handler);
            if (this.zzkh) {
                zzbq();
                zzc(GoogleApiManager.this.zzjq.isGooglePlayServicesAvailable(GoogleApiManager.this.zzjp) == 18 ? new Status(8, "Connection timed out while waiting for Google Play services update to complete.") : new Status(8, "API failed to connect while resuming due to an unknown error."));
                this.zzka.disconnect();
            }
        }

        @WorkerThread
        public final void zzbm() {
            Preconditions.checkHandlerThread(GoogleApiManager.this.handler);
            zzc(GoogleApiManager.zzjj);
            this.zzkc.zzak();
            for (ListenerKey zzg : (ListenerKey[]) this.zzke.keySet().toArray(new ListenerKey[this.zzke.size()])) {
                zza((zzb) new zzg(zzg, new TaskCompletionSource()));
            }
            zzi(new ConnectionResult(4));
            if (this.zzka.isConnected()) {
                this.zzka.onUserSignOut(new zzbl(this));
            }
        }

        public final Map<ListenerKey<?>, zzbv> zzbn() {
            return this.zzke;
        }

        @WorkerThread
        public final void zzbo() {
            Preconditions.checkHandlerThread(GoogleApiManager.this.handler);
            this.zzkj = null;
        }

        @WorkerThread
        public final ConnectionResult zzbp() {
            Preconditions.checkHandlerThread(GoogleApiManager.this.handler);
            return this.zzkj;
        }

        @WorkerThread
        public final boolean zzbs() {
            return zzb(true);
        }

        /* access modifiers changed from: 0000 */
        public final SignInClient zzbt() {
            if (this.zzkg == null) {
                return null;
            }
            return this.zzkg.zzbt();
        }

        @WorkerThread
        public final void zzc(Status status) {
            Preconditions.checkHandlerThread(GoogleApiManager.this.handler);
            for (zzb zza : this.zzjz) {
                zza.zza(status);
            }
            this.zzjz.clear();
        }

        @WorkerThread
        public final void zzg(@NonNull ConnectionResult connectionResult) {
            Preconditions.checkHandlerThread(GoogleApiManager.this.handler);
            this.zzka.disconnect();
            onConnectionFailed(connectionResult);
        }
    }

    private static class zzb {
        /* access modifiers changed from: private */
        public final Feature zzdr;
        /* access modifiers changed from: private */
        public final zzh<?> zzkn;

        private zzb(zzh<?> zzh, Feature feature) {
            this.zzkn = zzh;
            this.zzdr = feature;
        }

        /* synthetic */ zzb(zzh zzh, Feature feature, zzbh zzbh) {
            this(zzh, feature);
        }

        public final boolean equals(Object obj) {
            if (obj != null && (obj instanceof zzb)) {
                zzb zzb = (zzb) obj;
                if (Objects.equal(this.zzkn, zzb.zzkn) && Objects.equal(this.zzdr, zzb.zzdr)) {
                    return true;
                }
            }
            return false;
        }

        public final int hashCode() {
            return Objects.hashCode(this.zzkn, this.zzdr);
        }

        public final String toString() {
            return Objects.toStringHelper(this).add("key", this.zzkn).add("feature", this.zzdr).toString();
        }
    }

    private class zzc implements zzcb, ConnectionProgressReportCallbacks {
        /* access modifiers changed from: private */
        public final zzh<?> zzhc;
        /* access modifiers changed from: private */
        public final Client zzka;
        private IAccountAccessor zzko = null;
        private Set<Scope> zzkp = null;
        /* access modifiers changed from: private */
        public boolean zzkq = false;

        public zzc(Client client, zzh<?> zzh) {
            this.zzka = client;
            this.zzhc = zzh;
        }

        /* access modifiers changed from: private */
        @WorkerThread
        public final void zzbu() {
            if (this.zzkq && this.zzko != null) {
                this.zzka.getRemoteService(this.zzko, this.zzkp);
            }
        }

        public final void onReportServiceBinding(@NonNull ConnectionResult connectionResult) {
            GoogleApiManager.this.handler.post(new zzbn(this, connectionResult));
        }

        @WorkerThread
        public final void zza(IAccountAccessor iAccountAccessor, Set<Scope> set) {
            if (iAccountAccessor == null || set == null) {
                Log.wtf("GoogleApiManager", "Received null response from onSignInSuccess", new Exception());
                zzg(new ConnectionResult(4));
                return;
            }
            this.zzko = iAccountAccessor;
            this.zzkp = set;
            zzbu();
        }

        @WorkerThread
        public final void zzg(ConnectionResult connectionResult) {
            ((zza) GoogleApiManager.this.zzju.get(this.zzhc)).zzg(connectionResult);
        }
    }

    @KeepForSdk
    private GoogleApiManager(Context context, Looper looper, GoogleApiAvailability googleApiAvailability) {
        this.zzjp = context;
        this.handler = new Handler(looper, this);
        this.zzjq = googleApiAvailability;
        this.zzjr = new GoogleApiAvailabilityCache(googleApiAvailability);
        this.handler.sendMessage(this.handler.obtainMessage(6));
    }

    @KeepForSdk
    public static void reportSignOut() {
        synchronized (lock) {
            if (zzjo != null) {
                GoogleApiManager googleApiManager = zzjo;
                googleApiManager.zzjt.incrementAndGet();
                googleApiManager.handler.sendMessageAtFrontOfQueue(googleApiManager.handler.obtainMessage(10));
            }
        }
    }

    public static GoogleApiManager zzb(Context context) {
        GoogleApiManager googleApiManager;
        synchronized (lock) {
            if (zzjo == null) {
                HandlerThread handlerThread = new HandlerThread("GoogleApiHandler", 9);
                handlerThread.start();
                zzjo = new GoogleApiManager(context.getApplicationContext(), handlerThread.getLooper(), GoogleApiAvailability.getInstance());
            }
            googleApiManager = zzjo;
        }
        return googleApiManager;
    }

    @WorkerThread
    private final void zzb(GoogleApi<?> googleApi) {
        zzh zzm = googleApi.zzm();
        zza zza2 = (zza) this.zzju.get(zzm);
        if (zza2 == null) {
            zza2 = new zza(googleApi);
            this.zzju.put(zzm, zza2);
        }
        if (zza2.requiresSignIn()) {
            this.zzjx.add(zzm);
        }
        zza2.connect();
    }

    public static GoogleApiManager zzbf() {
        GoogleApiManager googleApiManager;
        synchronized (lock) {
            Preconditions.checkNotNull(zzjo, "Must guarantee manager is non-null before using getInstance");
            googleApiManager = zzjo;
        }
        return googleApiManager;
    }

    @WorkerThread
    public boolean handleMessage(Message message) {
        zza zza2;
        long j = 300000;
        switch (message.what) {
            case 1:
                if (((Boolean) message.obj).booleanValue()) {
                    j = 10000;
                }
                this.zzjn = j;
                this.handler.removeMessages(12);
                for (zzh obtainMessage : this.zzju.keySet()) {
                    this.handler.sendMessageDelayed(this.handler.obtainMessage(12, obtainMessage), this.zzjn);
                }
                break;
            case 2:
                zzj zzj = (zzj) message.obj;
                for (zzh zzh : zzj.zzs()) {
                    zza zza3 = (zza) this.zzju.get(zzh);
                    if (zza3 == null) {
                        zzj.zza(zzh, new ConnectionResult(13), null);
                        return true;
                    } else if (zza3.isConnected()) {
                        zzj.zza(zzh, ConnectionResult.RESULT_SUCCESS, zza3.zzae().getEndpointPackageName());
                    } else if (zza3.zzbp() != null) {
                        zzj.zza(zzh, zza3.zzbp(), null);
                    } else {
                        zza3.zza(zzj);
                    }
                }
                break;
            case 3:
                for (zza zza4 : this.zzju.values()) {
                    zza4.zzbo();
                    zza4.connect();
                }
                break;
            case 4:
            case 8:
            case 13:
                zzbu zzbu = (zzbu) message.obj;
                zza zza5 = (zza) this.zzju.get(zzbu.zzlr.zzm());
                if (zza5 == null) {
                    zzb(zzbu.zzlr);
                    zza5 = (zza) this.zzju.get(zzbu.zzlr.zzm());
                }
                if (!zza5.requiresSignIn() || this.zzjt.get() == zzbu.zzlq) {
                    zza5.zza(zzbu.zzlp);
                    return true;
                }
                zzbu.zzlp.zza(zzjj);
                zza5.zzbm();
                return true;
            case 5:
                int i = message.arg1;
                ConnectionResult connectionResult = (ConnectionResult) message.obj;
                Iterator it = this.zzju.values().iterator();
                while (true) {
                    if (it.hasNext()) {
                        zza2 = (zza) it.next();
                        if (zza2.getInstanceId() == i) {
                        }
                    } else {
                        zza2 = null;
                    }
                }
                if (zza2 != null) {
                    String errorString = this.zzjq.getErrorString(connectionResult.getErrorCode());
                    String errorMessage = connectionResult.getErrorMessage();
                    StringBuilder sb = new StringBuilder(String.valueOf(errorString).length() + 69 + String.valueOf(errorMessage).length());
                    sb.append("Error resolution was canceled by the user, original error message: ");
                    sb.append(errorString);
                    sb.append(": ");
                    sb.append(errorMessage);
                    zza2.zzc(new Status(17, sb.toString()));
                    return true;
                }
                StringBuilder sb2 = new StringBuilder(76);
                sb2.append("Could not find API instance ");
                sb2.append(i);
                sb2.append(" while trying to fail enqueued calls.");
                Log.wtf("GoogleApiManager", sb2.toString(), new Exception());
                return true;
            case 6:
                if (PlatformVersion.isAtLeastIceCreamSandwich() && (this.zzjp.getApplicationContext() instanceof Application)) {
                    BackgroundDetector.initialize((Application) this.zzjp.getApplicationContext());
                    BackgroundDetector.getInstance().addListener(new zzbh(this));
                    if (!BackgroundDetector.getInstance().readCurrentStateIfPossible(true)) {
                        this.zzjn = 300000;
                        return true;
                    }
                }
                break;
            case 7:
                zzb((GoogleApi) message.obj);
                return true;
            case 9:
                if (this.zzju.containsKey(message.obj)) {
                    ((zza) this.zzju.get(message.obj)).resume();
                    return true;
                }
                break;
            case 10:
                for (zzh remove : this.zzjx) {
                    ((zza) this.zzju.remove(remove)).zzbm();
                }
                this.zzjx.clear();
                return true;
            case 11:
                if (this.zzju.containsKey(message.obj)) {
                    ((zza) this.zzju.get(message.obj)).zzay();
                    return true;
                }
                break;
            case 12:
                if (this.zzju.containsKey(message.obj)) {
                    ((zza) this.zzju.get(message.obj)).zzbs();
                    return true;
                }
                break;
            case 14:
                zzae zzae = (zzae) message.obj;
                zzh zzm = zzae.zzm();
                if (!this.zzju.containsKey(zzm)) {
                    zzae.zzao().setResult(Boolean.valueOf(false));
                    return true;
                }
                zzae.zzao().setResult(Boolean.valueOf(((zza) this.zzju.get(zzm)).zzb(false)));
                return true;
            case 15:
                zzb zzb2 = (zzb) message.obj;
                if (this.zzju.containsKey(zzb2.zzkn)) {
                    ((zza) this.zzju.get(zzb2.zzkn)).zza(zzb2);
                    return true;
                }
                break;
            case 16:
                zzb zzb3 = (zzb) message.obj;
                if (this.zzju.containsKey(zzb3.zzkn)) {
                    ((zza) this.zzju.get(zzb3.zzkn)).zzb(zzb3);
                    return true;
                }
                break;
            default:
                int i2 = message.what;
                StringBuilder sb3 = new StringBuilder(31);
                sb3.append("Unknown message id: ");
                sb3.append(i2);
                Log.w("GoogleApiManager", sb3.toString());
                return false;
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    public final void maybeSignOut() {
        this.zzjt.incrementAndGet();
        this.handler.sendMessage(this.handler.obtainMessage(10));
    }

    /* access modifiers changed from: 0000 */
    public final PendingIntent zza(zzh<?> zzh, int i) {
        zza zza2 = (zza) this.zzju.get(zzh);
        if (zza2 == null) {
            return null;
        }
        SignInClient zzbt = zza2.zzbt();
        if (zzbt == null) {
            return null;
        }
        return PendingIntent.getActivity(this.zzjp, i, zzbt.getSignInIntent(), 134217728);
    }

    public final <O extends ApiOptions> Task<Boolean> zza(@NonNull GoogleApi<O> googleApi, @NonNull ListenerKey<?> listenerKey) {
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        this.handler.sendMessage(this.handler.obtainMessage(13, new zzbu(new zzg(listenerKey, taskCompletionSource), this.zzjt.get(), googleApi)));
        return taskCompletionSource.getTask();
    }

    public final <O extends ApiOptions> Task<Void> zza(@NonNull GoogleApi<O> googleApi, @NonNull RegisterListenerMethod<AnyClient, ?> registerListenerMethod, @NonNull UnregisterListenerMethod<AnyClient, ?> unregisterListenerMethod) {
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        this.handler.sendMessage(this.handler.obtainMessage(8, new zzbu(new zze(new zzbv(registerListenerMethod, unregisterListenerMethod), taskCompletionSource), this.zzjt.get(), googleApi)));
        return taskCompletionSource.getTask();
    }

    public final Task<Map<zzh<?>, String>> zza(Iterable<? extends GoogleApi<?>> iterable) {
        zzj zzj = new zzj(iterable);
        this.handler.sendMessage(this.handler.obtainMessage(2, zzj));
        return zzj.getTask();
    }

    public final void zza(ConnectionResult connectionResult, int i) {
        if (!zzc(connectionResult, i)) {
            this.handler.sendMessage(this.handler.obtainMessage(5, i, 0, connectionResult));
        }
    }

    public final void zza(GoogleApi<?> googleApi) {
        this.handler.sendMessage(this.handler.obtainMessage(7, googleApi));
    }

    public final <O extends ApiOptions> void zza(GoogleApi<O> googleApi, int i, ApiMethodImpl<? extends Result, AnyClient> apiMethodImpl) {
        this.handler.sendMessage(this.handler.obtainMessage(4, new zzbu(new zzd(i, apiMethodImpl), this.zzjt.get(), googleApi)));
    }

    public final <O extends ApiOptions, ResultT> void zza(GoogleApi<O> googleApi, int i, TaskApiCall<AnyClient, ResultT> taskApiCall, TaskCompletionSource<ResultT> taskCompletionSource, StatusExceptionMapper statusExceptionMapper) {
        this.handler.sendMessage(this.handler.obtainMessage(4, new zzbu(new zzf(i, taskApiCall, taskCompletionSource, statusExceptionMapper), this.zzjt.get(), googleApi)));
    }

    public final void zza(@NonNull zzad zzad) {
        synchronized (lock) {
            if (this.zzjv != zzad) {
                this.zzjv = zzad;
                this.zzjw.clear();
            }
            this.zzjw.addAll(zzad.zzam());
        }
    }

    /* access modifiers changed from: 0000 */
    public final void zzb(@NonNull zzad zzad) {
        synchronized (lock) {
            if (this.zzjv == zzad) {
                this.zzjv = null;
                this.zzjw.clear();
            }
        }
    }

    public final int zzbg() {
        return this.zzjs.getAndIncrement();
    }

    public final Task<Boolean> zzc(GoogleApi<?> googleApi) {
        zzae zzae = new zzae(googleApi.zzm());
        this.handler.sendMessage(this.handler.obtainMessage(14, zzae));
        return zzae.zzao().getTask();
    }

    /* access modifiers changed from: 0000 */
    public final boolean zzc(ConnectionResult connectionResult, int i) {
        return this.zzjq.showWrappedErrorNotification(this.zzjp, connectionResult, i);
    }

    public final void zzr() {
        this.handler.sendMessage(this.handler.obtainMessage(3));
    }
}
