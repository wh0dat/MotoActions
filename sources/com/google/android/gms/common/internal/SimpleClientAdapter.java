package com.google.android.gms.common.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import com.google.android.gms.common.api.Api.SimpleClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

public class SimpleClientAdapter<T extends IInterface> extends GmsClient<T> {
    private final SimpleClient<T> zzva;

    public SimpleClientAdapter(Context context, Looper looper, int i, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, ClientSettings clientSettings, SimpleClient<T> simpleClient) {
        super(context, looper, i, clientSettings, connectionCallbacks, onConnectionFailedListener);
        this.zzva = simpleClient;
    }

    /* access modifiers changed from: protected */
    public T createServiceInterface(IBinder iBinder) {
        return this.zzva.createServiceInterface(iBinder);
    }

    public SimpleClient<T> getClient() {
        return this.zzva;
    }

    public int getMinApkVersion() {
        return super.getMinApkVersion();
    }

    /* access modifiers changed from: protected */
    public String getServiceDescriptor() {
        return this.zzva.getServiceDescriptor();
    }

    /* access modifiers changed from: protected */
    public String getStartServiceAction() {
        return this.zzva.getStartServiceAction();
    }

    /* access modifiers changed from: protected */
    public void onSetConnectState(int i, T t) {
        this.zzva.setState(i, t);
    }
}
