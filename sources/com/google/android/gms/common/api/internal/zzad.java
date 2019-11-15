package com.google.android.gms.common.api.internal;

import android.app.Activity;
import android.support.p001v4.util.ArraySet;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.Preconditions;

public class zzad extends zzk {
    private GoogleApiManager zzcq;
    private final ArraySet<zzh<?>> zzhb = new ArraySet<>();

    private zzad(LifecycleFragment lifecycleFragment) {
        super(lifecycleFragment);
        this.mLifecycleFragment.addCallback("ConnectionlessLifecycleHelper", this);
    }

    public static void zza(Activity activity, GoogleApiManager googleApiManager, zzh<?> zzh) {
        LifecycleFragment fragment = getFragment(activity);
        zzad zzad = (zzad) fragment.getCallbackOrNull("ConnectionlessLifecycleHelper", zzad.class);
        if (zzad == null) {
            zzad = new zzad(fragment);
        }
        zzad.zzcq = googleApiManager;
        Preconditions.checkNotNull(zzh, "ApiKey cannot be null");
        zzad.zzhb.add(zzh);
        googleApiManager.zza(zzad);
    }

    private final void zzan() {
        if (!this.zzhb.isEmpty()) {
            this.zzcq.zza(this);
        }
    }

    public final void onResume() {
        super.onResume();
        zzan();
    }

    public final void onStart() {
        super.onStart();
        zzan();
    }

    public void onStop() {
        super.onStop();
        this.zzcq.zzb(this);
    }

    /* access modifiers changed from: protected */
    public final void zza(ConnectionResult connectionResult, int i) {
        this.zzcq.zza(connectionResult, i);
    }

    /* access modifiers changed from: 0000 */
    public final ArraySet<zzh<?>> zzam() {
        return this.zzhb;
    }

    /* access modifiers changed from: protected */
    public final void zzr() {
        this.zzcq.zzr();
    }
}
