package p016io.reactivex.observers;

import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.observers.DisposableSingleObserver */
public abstract class DisposableSingleObserver<T> implements SingleObserver<T>, Disposable {

    /* renamed from: s */
    final AtomicReference<Disposable> f558s = new AtomicReference<>();

    /* access modifiers changed from: protected */
    public void onStart() {
    }

    public final void onSubscribe(Disposable disposable) {
        if (DisposableHelper.setOnce(this.f558s, disposable)) {
            onStart();
        }
    }

    public final boolean isDisposed() {
        return this.f558s.get() == DisposableHelper.DISPOSED;
    }

    public final void dispose() {
        DisposableHelper.dispose(this.f558s);
    }
}
