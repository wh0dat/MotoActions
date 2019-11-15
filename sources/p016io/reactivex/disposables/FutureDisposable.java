package p016io.reactivex.disposables;

import java.util.concurrent.Future;

/* renamed from: io.reactivex.disposables.FutureDisposable */
final class FutureDisposable extends ReferenceDisposable<Future<?>> {
    private static final long serialVersionUID = 6545242830671168775L;
    private final boolean allowInterrupt;

    FutureDisposable(Future<?> future, boolean z) {
        super(future);
        this.allowInterrupt = z;
    }

    /* access modifiers changed from: protected */
    public void onDisposed(Future<?> future) {
        future.cancel(this.allowInterrupt);
    }
}
