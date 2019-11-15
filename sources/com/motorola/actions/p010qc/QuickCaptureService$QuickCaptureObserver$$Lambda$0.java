package com.motorola.actions.p010qc;

import android.content.Context;
import android.net.Uri;
import java.util.function.Consumer;

/* renamed from: com.motorola.actions.qc.QuickCaptureService$QuickCaptureObserver$$Lambda$0 */
final /* synthetic */ class QuickCaptureService$QuickCaptureObserver$$Lambda$0 implements Consumer {
    private final QuickCaptureObserver arg$1;
    private final Context arg$2;

    QuickCaptureService$QuickCaptureObserver$$Lambda$0(QuickCaptureObserver quickCaptureObserver, Context context) {
        this.arg$1 = quickCaptureObserver;
        this.arg$2 = context;
    }

    public void accept(Object obj) {
        this.arg$1.lambda$register$0$QuickCaptureService$QuickCaptureObserver(this.arg$2, (Uri) obj);
    }
}
