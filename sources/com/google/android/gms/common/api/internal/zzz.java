package com.google.android.gms.common.api.internal;

import android.support.annotation.NonNull;
import android.support.p001v4.util.ArrayMap;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.AvailabilityException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.util.Collections;
import java.util.Map;

final class zzz implements OnCompleteListener<Map<zzh<?>, String>> {
    private final /* synthetic */ zzw zzgu;
    private SignInConnectionListener zzgv;

    zzz(zzw zzw, SignInConnectionListener signInConnectionListener) {
        this.zzgu = zzw;
        this.zzgv = signInConnectionListener;
    }

    /* access modifiers changed from: 0000 */
    public final void cancel() {
        this.zzgv.onComplete();
    }

    public final void onComplete(@NonNull Task<Map<zzh<?>, String>> task) {
        SignInConnectionListener signInConnectionListener;
        Map zzg;
        this.zzgu.zzga.lock();
        try {
            if (!this.zzgu.zzgp) {
                signInConnectionListener = this.zzgv;
            } else {
                if (task.isSuccessful()) {
                    this.zzgu.zzgr = new ArrayMap(this.zzgu.zzgh.size());
                    for (zzv zzm : this.zzgu.zzgh.values()) {
                        this.zzgu.zzgr.put(zzm.zzm(), ConnectionResult.RESULT_SUCCESS);
                    }
                } else if (task.getException() instanceof AvailabilityException) {
                    AvailabilityException availabilityException = (AvailabilityException) task.getException();
                    if (this.zzgu.zzgn) {
                        this.zzgu.zzgr = new ArrayMap(this.zzgu.zzgh.size());
                        for (zzv zzv : this.zzgu.zzgh.values()) {
                            zzh zzm2 = zzv.zzm();
                            ConnectionResult connectionResult = availabilityException.getConnectionResult(zzv);
                            if (this.zzgu.zza(zzv, connectionResult)) {
                                zzg = this.zzgu.zzgr;
                                connectionResult = new ConnectionResult(16);
                            } else {
                                zzg = this.zzgu.zzgr;
                            }
                            zzg.put(zzm2, connectionResult);
                        }
                    } else {
                        this.zzgu.zzgr = availabilityException.zzl();
                    }
                } else {
                    Log.e("ConnectionlessGAC", "Unexpected availability exception", task.getException());
                    this.zzgu.zzgr = Collections.emptyMap();
                }
                if (this.zzgu.isConnected()) {
                    this.zzgu.zzgq.putAll(this.zzgu.zzgr);
                    if (this.zzgu.zzai() == null) {
                        this.zzgu.zzag();
                        this.zzgu.zzah();
                        this.zzgu.zzgl.signalAll();
                    }
                }
                signInConnectionListener = this.zzgv;
            }
            signInConnectionListener.onComplete();
        } finally {
            this.zzgu.zzga.unlock();
        }
    }
}
