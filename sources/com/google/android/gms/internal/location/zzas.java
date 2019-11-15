package com.google.android.gms.internal.location;

import android.app.PendingIntent;
import android.content.ContentProviderClient;
import android.content.Context;
import android.location.Location;
import android.os.RemoteException;
import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.common.api.internal.ListenerHolder.ListenerKey;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.zzu;
import com.google.android.gms.location.zzx;
import java.util.HashMap;
import java.util.Map;

public final class zzas {
    private final zzbj<zzao> zzcb;
    private final Context zzcu;
    private ContentProviderClient zzcv = null;
    private boolean zzcw = false;
    private final Map<ListenerKey<LocationListener>, zzax> zzcx = new HashMap();
    private final Map<ListenerKey<Object>, zzaw> zzcy = new HashMap();
    private final Map<ListenerKey<LocationCallback>, zzat> zzcz = new HashMap();

    public zzas(Context context, zzbj<zzao> zzbj) {
        this.zzcu = context;
        this.zzcb = zzbj;
    }

    private final zzax zza(ListenerHolder<LocationListener> listenerHolder) {
        zzax zzax;
        synchronized (this.zzcx) {
            zzax = (zzax) this.zzcx.get(listenerHolder.getListenerKey());
            if (zzax == null) {
                zzax = new zzax(listenerHolder);
            }
            this.zzcx.put(listenerHolder.getListenerKey(), zzax);
        }
        return zzax;
    }

    private final zzat zzb(ListenerHolder<LocationCallback> listenerHolder) {
        zzat zzat;
        synchronized (this.zzcz) {
            zzat = (zzat) this.zzcz.get(listenerHolder.getListenerKey());
            if (zzat == null) {
                zzat = new zzat(listenerHolder);
            }
            this.zzcz.put(listenerHolder.getListenerKey(), zzat);
        }
        return zzat;
    }

    public final Location getLastLocation() throws RemoteException {
        this.zzcb.checkConnected();
        return ((zzao) this.zzcb.getService()).zza(this.zzcu.getPackageName());
    }

    public final void removeAllListeners() throws RemoteException {
        synchronized (this.zzcx) {
            for (zzax zzax : this.zzcx.values()) {
                if (zzax != null) {
                    ((zzao) this.zzcb.getService()).zza(zzbf.zza((zzx) zzax, (zzaj) null));
                }
            }
            this.zzcx.clear();
        }
        synchronized (this.zzcz) {
            for (zzat zzat : this.zzcz.values()) {
                if (zzat != null) {
                    ((zzao) this.zzcb.getService()).zza(zzbf.zza((zzu) zzat, (zzaj) null));
                }
            }
            this.zzcz.clear();
        }
        synchronized (this.zzcy) {
            for (zzaw zzaw : this.zzcy.values()) {
                if (zzaw != null) {
                    ((zzao) this.zzcb.getService()).zza(new zzo(2, null, zzaw.asBinder(), null));
                }
            }
            this.zzcy.clear();
        }
    }

    public final LocationAvailability zza() throws RemoteException {
        this.zzcb.checkConnected();
        return ((zzao) this.zzcb.getService()).zzb(this.zzcu.getPackageName());
    }

    public final void zza(PendingIntent pendingIntent, zzaj zzaj) throws RemoteException {
        this.zzcb.checkConnected();
        zzao zzao = (zzao) this.zzcb.getService();
        zzbf zzbf = new zzbf(2, null, null, pendingIntent, null, zzaj != null ? zzaj.asBinder() : null);
        zzao.zza(zzbf);
    }

    public final void zza(Location location) throws RemoteException {
        this.zzcb.checkConnected();
        ((zzao) this.zzcb.getService()).zza(location);
    }

    public final void zza(ListenerKey<LocationListener> listenerKey, zzaj zzaj) throws RemoteException {
        this.zzcb.checkConnected();
        Preconditions.checkNotNull(listenerKey, "Invalid null listener key");
        synchronized (this.zzcx) {
            zzax zzax = (zzax) this.zzcx.remove(listenerKey);
            if (zzax != null) {
                zzax.release();
                ((zzao) this.zzcb.getService()).zza(zzbf.zza((zzx) zzax, zzaj));
            }
        }
    }

    public final void zza(zzaj zzaj) throws RemoteException {
        this.zzcb.checkConnected();
        ((zzao) this.zzcb.getService()).zza(zzaj);
    }

    public final void zza(zzbd zzbd, ListenerHolder<LocationCallback> listenerHolder, zzaj zzaj) throws RemoteException {
        this.zzcb.checkConnected();
        zzat zzb = zzb(listenerHolder);
        zzao zzao = (zzao) this.zzcb.getService();
        zzbf zzbf = new zzbf(1, zzbd, null, null, zzb.asBinder(), zzaj != null ? zzaj.asBinder() : null);
        zzao.zza(zzbf);
    }

    public final void zza(LocationRequest locationRequest, PendingIntent pendingIntent, zzaj zzaj) throws RemoteException {
        this.zzcb.checkConnected();
        zzao zzao = (zzao) this.zzcb.getService();
        zzbf zzbf = new zzbf(1, zzbd.zza(locationRequest), null, pendingIntent, null, zzaj != null ? zzaj.asBinder() : null);
        zzao.zza(zzbf);
    }

    public final void zza(LocationRequest locationRequest, ListenerHolder<LocationListener> listenerHolder, zzaj zzaj) throws RemoteException {
        this.zzcb.checkConnected();
        zzax zza = zza(listenerHolder);
        zzao zzao = (zzao) this.zzcb.getService();
        zzbf zzbf = new zzbf(1, zzbd.zza(locationRequest), zza.asBinder(), null, null, zzaj != null ? zzaj.asBinder() : null);
        zzao.zza(zzbf);
    }

    public final void zza(boolean z) throws RemoteException {
        this.zzcb.checkConnected();
        ((zzao) this.zzcb.getService()).zza(z);
        this.zzcw = z;
    }

    public final void zzb() throws RemoteException {
        if (this.zzcw) {
            zza(false);
        }
    }

    public final void zzb(ListenerKey<LocationCallback> listenerKey, zzaj zzaj) throws RemoteException {
        this.zzcb.checkConnected();
        Preconditions.checkNotNull(listenerKey, "Invalid null listener key");
        synchronized (this.zzcz) {
            zzat zzat = (zzat) this.zzcz.remove(listenerKey);
            if (zzat != null) {
                zzat.release();
                ((zzao) this.zzcb.getService()).zza(zzbf.zza((zzu) zzat, zzaj));
            }
        }
    }
}
