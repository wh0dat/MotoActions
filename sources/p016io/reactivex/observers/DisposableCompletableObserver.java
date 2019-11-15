package p016io.reactivex.observers;

import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.observers.DisposableCompletableObserver */
public abstract class DisposableCompletableObserver implements CompletableObserver, Disposable {

    /* renamed from: s */
    final AtomicReference<Disposable> f555s = new AtomicReference<>();

    /* access modifiers changed from: protected */
    public void onStart() {
    }

    public final void onSubscribe(Disposable disposable) {
        if (DisposableHelper.setOnce(this.f555s, disposable)) {
            onStart();
        }
    }

    public final boolean isDisposed() {
        return this.f555s.get() == DisposableHelper.DISPOSED;
    }

    public final void dispose() {
        DisposableHelper.dispose(this.f555s);
    }
}
