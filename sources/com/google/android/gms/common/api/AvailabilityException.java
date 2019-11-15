package com.google.android.gms.common.api;

import android.support.p001v4.util.ArrayMap;
import android.text.TextUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.internal.zzh;
import com.google.android.gms.common.internal.Preconditions;
import java.util.ArrayList;

public class AvailabilityException extends Exception {
    private final ArrayMap<zzh<?>, ConnectionResult> zzcc;

    public AvailabilityException(ArrayMap<zzh<?>, ConnectionResult> arrayMap) {
        this.zzcc = arrayMap;
    }

    public ConnectionResult getConnectionResult(GoogleApi<? extends ApiOptions> googleApi) {
        zzh zzm = googleApi.zzm();
        Preconditions.checkArgument(this.zzcc.get(zzm) != null, "The given API was not part of the availability request.");
        return (ConnectionResult) this.zzcc.get(zzm);
    }

    public String getMessage() {
        ArrayList arrayList = new ArrayList();
        boolean z = true;
        for (zzh zzh : this.zzcc.keySet()) {
            ConnectionResult connectionResult = (ConnectionResult) this.zzcc.get(zzh);
            if (connectionResult.isSuccess()) {
                z = false;
            }
            String zzq = zzh.zzq();
            String valueOf = String.valueOf(connectionResult);
            StringBuilder sb = new StringBuilder(String.valueOf(zzq).length() + 2 + String.valueOf(valueOf).length());
            sb.append(zzq);
            sb.append(": ");
            sb.append(valueOf);
            arrayList.add(sb.toString());
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(z ? "None of the queried APIs are available. " : "Some of the queried APIs are unavailable. ");
        sb2.append(TextUtils.join("; ", arrayList));
        return sb2.toString();
    }

    public final ArrayMap<zzh<?>, ConnectionResult> zzl() {
        return this.zzcc;
    }
}
