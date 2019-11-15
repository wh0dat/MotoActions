package com.google.android.gms.common;

import android.support.annotation.NonNull;
import android.util.Log;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

@CheckReturnValue
class zzg {
    private static final zzg zzbk = new zzg(true, null, null);
    private final Throwable cause;
    final boolean zzbl;
    private final String zzbm;

    zzg(boolean z, @Nullable String str, @Nullable Throwable th) {
        this.zzbl = z;
        this.zzbm = str;
        this.cause = th;
    }

    static zzg zza(String str, CertData certData, boolean z, boolean z2) {
        zzi zzi = new zzi(str, certData, z, z2);
        return zzi;
    }

    static zzg zza(@NonNull String str, @NonNull Throwable th) {
        return new zzg(false, str, th);
    }

    static zzg zze(@NonNull String str) {
        return new zzg(false, str, null);
    }

    static zzg zzg() {
        return zzbk;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public String getErrorMessage() {
        return this.zzbm;
    }

    /* access modifiers changed from: 0000 */
    public final void zzh() throws SecurityException {
        if (!this.zzbl) {
            String valueOf = String.valueOf("GoogleCertificatesRslt: ");
            String valueOf2 = String.valueOf(getErrorMessage());
            String concat = valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf);
            if (this.cause != null) {
                throw new SecurityException(concat, this.cause);
            }
            throw new SecurityException(concat);
        }
    }

    /* access modifiers changed from: 0000 */
    public final void zzi() {
        if (!this.zzbl) {
            if (this.cause != null) {
                Log.d("GoogleCertificatesRslt", getErrorMessage(), this.cause);
                return;
            }
            Log.d("GoogleCertificatesRslt", getErrorMessage());
        }
    }
}
