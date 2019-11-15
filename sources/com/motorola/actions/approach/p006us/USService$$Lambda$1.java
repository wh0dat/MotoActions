package com.motorola.actions.approach.p006us;

import p016io.reactivex.functions.Consumer;

/* renamed from: com.motorola.actions.approach.us.USService$$Lambda$1 */
final /* synthetic */ class USService$$Lambda$1 implements Consumer {
    private final USService arg$1;

    USService$$Lambda$1(USService uSService) {
        this.arg$1 = uSService;
    }

    public void accept(Object obj) {
        this.arg$1.bridge$lambda$1$USService((Float) obj);
    }
}
