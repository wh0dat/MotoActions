package p016io.reactivex.internal.observers;

import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.observers.ResumeSingleObserver */
public final class ResumeSingleObserver<T> implements SingleObserver<T> {
    final SingleObserver<? super T> actual;
    final AtomicReference<Disposable> parent;

    public ResumeSingleObserver(AtomicReference<Disposable> atomicReference, SingleObserver<? super T> singleObserver) {
        this.parent = atomicReference;
        this.actual = singleObserver;
    }

    public void onSubscribe(Disposable disposable) {
        DisposableHelper.replace(this.parent, disposable);
    }

    public void onSuccess(T t) {
        this.actual.onSuccess(t);
    }

    public void onError(Throwable th) {
        this.actual.onError(th);
    }
}
