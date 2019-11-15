package p016io.reactivex.internal.disposables;

import java.util.concurrent.atomic.AtomicInteger;

/* renamed from: io.reactivex.internal.disposables.FullArbiterWip */
/* compiled from: ObserverFullArbiter */
class FullArbiterWip extends FullArbiterPad0 {
    final AtomicInteger wip = new AtomicInteger();

    FullArbiterWip() {
    }
}
