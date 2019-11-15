package com.google.android.gms.common.api.internal;

import android.support.annotation.BinderThread;
import com.google.android.gms.signin.internal.BaseSignInCallbacks;
import com.google.android.gms.signin.internal.SignInResponse;
import java.lang.ref.WeakReference;

final class zzaq extends BaseSignInCallbacks {
    private final WeakReference<zzaj> zzhw;

    zzaq(zzaj zzaj) {
        this.zzhw = new WeakReference<>(zzaj);
    }

    @BinderThread
    public final void onSignInComplete(SignInResponse signInResponse) {
        zzaj zzaj = (zzaj) this.zzhw.get();
        if (zzaj != null) {
            zzaj.zzhf.zza((zzbe) new zzar(this, zzaj, zzaj, signInResponse));
        }
    }
}
