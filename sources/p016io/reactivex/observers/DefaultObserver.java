package p016io.reactivex.observers;

import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.observers.DefaultObserver */
public abstract class DefaultObserver<T> implements Observer<T> {

    /* renamed from: s */
    private Disposable f554s;

    /* access modifiers changed from: protected */
    public void onStart() {
    }

    public final void onSubscribe(Disposable disposable) {
        if (DisposableHelper.validate(this.f554s, disposable)) {
            this.f554s = disposable;
            onStart();
        }
    }

    /* access modifiers changed from: protected */
    public final void cancel() {
        Disposable disposable = this.f554s;
        this.f554s = DisposableHelper.DISPOSED;
        disposable.dispose();
    }
}
