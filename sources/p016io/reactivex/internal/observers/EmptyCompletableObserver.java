package p016io.reactivex.internal.observers;

import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.observers.EmptyCompletableObserver */
public final class EmptyCompletableObserver extends AtomicReference<Disposable> implements CompletableObserver, Disposable {
    private static final long serialVersionUID = -7545121636549663526L;

    public void dispose() {
        DisposableHelper.dispose(this);
    }

    public boolean isDisposed() {
        return get() == DisposableHelper.DISPOSED;
    }

    public void onComplete() {
        lazySet(DisposableHelper.DISPOSED);
    }

    public void onError(Throwable th) {
        lazySet(DisposableHelper.DISPOSED);
        RxJavaPlugins.onError(th);
    }

    public void onSubscribe(Disposable disposable) {
        DisposableHelper.setOnce(this, disposable);
    }
}
