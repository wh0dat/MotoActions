package p016io.reactivex.internal.subscriptions;

import java.util.concurrent.atomic.AtomicLong;

/* renamed from: io.reactivex.internal.subscriptions.FullArbiterMissed */
/* compiled from: FullArbiter */
class FullArbiterMissed extends FullArbiterPad1 {
    final AtomicLong missedRequested = new AtomicLong();

    FullArbiterMissed() {
    }
}
