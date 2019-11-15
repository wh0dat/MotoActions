package p016io.reactivex.observers;

import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.MaybeObserver;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.observers.DisposableMaybeObserver */
public abstract class DisposableMaybeObserver<T> implements MaybeObserver<T>, Disposable {

    /* renamed from: s */
    final AtomicReference<Disposable> f556s = new AtomicReference<>();

    /* access modifiers changed from: protected */
    public void onStart() {
    }

    public final void onSubscribe(Disposable disposable) {
        if (DisposableHelper.setOnce(this.f556s, disposable)) {
            onStart();
        }
    }

    public final boolean isDisposed() {
        return this.f556s.get() == DisposableHelper.DISPOSED;
    }

    public final void dispose() {
        DisposableHelper.dispose(this.f556s);
    }
}
