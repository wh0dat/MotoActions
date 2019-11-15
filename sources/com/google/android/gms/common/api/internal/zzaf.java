package com.google.android.gms.common.api.internal;

import android.support.annotation.NonNull;
import android.support.p001v4.app.FragmentActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

public class zzaf extends GoogleApiClient {
    private final String zzhe;

    public zzaf(String str) {
        this.zzhe = str;
    }

    public ConnectionResult blockingConnect() {
        throw new UnsupportedOperationException(this.zzhe);
    }

    public ConnectionResult blockingConnect(long j, @NonNull TimeUnit timeUnit) {
        throw new UnsupportedOperationException(this.zzhe);
    }

    public PendingResult<Status> clearDefaultAccountAndReconnect() {
        throw new UnsupportedOperationException(this.zzhe);
    }

    public void connect() {
        throw new UnsupportedOperationException(this.zzhe);
    }

    public void disconnect() {
        throw new UnsupportedOperationException(this.zzhe);
    }

    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        throw new UnsupportedOperationException(this.zzhe);
    }

    @NonNull
    public ConnectionResult getConnectionResult(@NonNull Api<?> api) {
        throw new UnsupportedOperationException(this.zzhe);
    }

    public boolean hasConnectedApi(@NonNull Api<?> api) {
        throw new UnsupportedOperationException(this.zzhe);
    }

    public boolean isConnected() {
        throw new UnsupportedOperationException(this.zzhe);
    }

    public boolean isConnecting() {
        throw new UnsupportedOperationException(this.zzhe);
    }

    public boolean isConnectionCallbacksRegistered(@NonNull ConnectionCallbacks connectionCallbacks) {
        throw new UnsupportedOperationException(this.zzhe);
    }

    public boolean isConnectionFailedListenerRegistered(@NonNull OnConnectionFailedListener onConnectionFailedListener) {
        throw new UnsupportedOperationException(this.zzhe);
    }

    public void reconnect() {
        throw new UnsupportedOperationException(this.zzhe);
    }

    public void registerConnectionCallbacks(@NonNull ConnectionCallbacks connectionCallbacks) {
        throw new UnsupportedOperationException(this.zzhe);
    }

    public void registerConnectionFailedListener(@NonNull OnConnectionFailedListener onConnectionFailedListener) {
        throw new UnsupportedOperationException(this.zzhe);
    }

    public void stopAutoManage(@NonNull FragmentActivity fragmentActivity) {
        throw new UnsupportedOperationException(this.zzhe);
    }

    public void unregisterConnectionCallbacks(@NonNull ConnectionCallbacks connectionCallbacks) {
        throw new UnsupportedOperationException(this.zzhe);
    }

    public void unregisterConnectionFailedListener(@NonNull OnConnectionFailedListener onConnectionFailedListener) {
        throw new UnsupportedOperationException(this.zzhe);
    }
}
