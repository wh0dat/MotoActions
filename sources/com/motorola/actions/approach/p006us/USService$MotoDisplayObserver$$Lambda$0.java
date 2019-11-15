package com.motorola.actions.approach.p006us;

import android.net.Uri;
import java.util.function.Consumer;

/* renamed from: com.motorola.actions.approach.us.USService$MotoDisplayObserver$$Lambda$0 */
final /* synthetic */ class USService$MotoDisplayObserver$$Lambda$0 implements Consumer {
    private final MotoDisplayObserver arg$1;

    USService$MotoDisplayObserver$$Lambda$0(MotoDisplayObserver motoDisplayObserver) {
        this.arg$1 = motoDisplayObserver;
    }

    public void accept(Object obj) {
        this.arg$1.lambda$observe$0$USService$MotoDisplayObserver((Uri) obj);
    }
}
