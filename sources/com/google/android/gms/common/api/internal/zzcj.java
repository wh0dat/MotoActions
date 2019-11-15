package com.google.android.gms.common.api.internal;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;

final class zzcj extends Handler {
    private final /* synthetic */ zzch zzml;

    public zzcj(zzch zzch, Looper looper) {
        this.zzml = zzch;
        super(looper);
    }

    public final void handleMessage(Message message) {
        zzch zzg;
        Status status;
        switch (message.what) {
            case 0:
                PendingResult pendingResult = (PendingResult) message.obj;
                synchronized (this.zzml.zzfa) {
                    if (pendingResult == null) {
                        try {
                            zzg = this.zzml.zzme;
                            status = new Status(13, "Transform returned null");
                        } catch (Throwable th) {
                            throw th;
                        }
                    } else if (pendingResult instanceof zzbx) {
                        zzg = this.zzml.zzme;
                        status = ((zzbx) pendingResult).getStatus();
                    } else {
                        this.zzml.zzme.zza(pendingResult);
                    }
                    zzg.zzd(status);
                }
                return;
            case 1:
                RuntimeException runtimeException = (RuntimeException) message.obj;
                String str = "TransformedResultImpl";
                String str2 = "Runtime exception on the transformation worker thread: ";
                String valueOf = String.valueOf(runtimeException.getMessage());
                Log.e(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
                throw runtimeException;
            default:
                int i = message.what;
                StringBuilder sb = new StringBuilder(70);
                sb.append("TransformationResultHandler received unknown message type: ");
                sb.append(i);
                Log.e("TransformedResultImpl", sb.toString());
                return;
        }
    }
}
