package com.google.android.gms.common.internal;

import android.support.annotation.NonNull;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.BaseGmsClient.BaseOnConnectionFailedListener;

final class zzg implements BaseOnConnectionFailedListener {
    private final /* synthetic */ OnConnectionFailedListener zzte;

    zzg(OnConnectionFailedListener onConnectionFailedListener) {
        this.zzte = onConnectionFailedListener;
    }

    public final void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        this.zzte.onConnectionFailed(connectionResult);
    }
}
