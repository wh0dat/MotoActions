package p016io.reactivex.internal.disposables;

import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Cancellable;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.disposables.CancellableDisposable */
public final class CancellableDisposable extends AtomicReference<Cancellable> implements Disposable {
    private static final long serialVersionUID = 5718521705281392066L;

    public CancellableDisposable(Cancellable cancellable) {
        super(cancellable);
    }

    public boolean isDisposed() {
        return get() == null;
    }

    public void dispose() {
        if (get() != null) {
            Cancellable cancellable = (Cancellable) getAndSet(null);
            if (cancellable != null) {
                try {
                    cancellable.cancel();
                } catch (Exception e) {
                    Exceptions.throwIfFatal(e);
                    RxJavaPlugins.onError(e);
                }
            }
        }
    }
}
