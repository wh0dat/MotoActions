package p016io.reactivex.observers;

import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.observers.DisposableObserver */
public abstract class DisposableObserver<T> implements Observer<T>, Disposable {

    /* renamed from: s */
    final AtomicReference<Disposable> f557s = new AtomicReference<>();

    /* access modifiers changed from: protected */
    public void onStart() {
    }

    public final void onSubscribe(Disposable disposable) {
        if (DisposableHelper.setOnce(this.f557s, disposable)) {
            onStart();
        }
    }

    public final boolean isDisposed() {
        return this.f557s.get() == DisposableHelper.DISPOSED;
    }

    public final void dispose() {
        DisposableHelper.dispose(this.f557s);
    }
}
