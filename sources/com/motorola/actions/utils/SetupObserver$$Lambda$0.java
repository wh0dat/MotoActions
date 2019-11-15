package com.motorola.actions.utils;

import android.net.Uri;
import java.util.function.Consumer;

final /* synthetic */ class SetupObserver$$Lambda$0 implements Consumer {
    private final SetupObserver arg$1;

    SetupObserver$$Lambda$0(SetupObserver setupObserver) {
        this.arg$1 = setupObserver;
    }

    public void accept(Object obj) {
        this.arg$1.lambda$observe$0$SetupObserver((Uri) obj);
    }
}
