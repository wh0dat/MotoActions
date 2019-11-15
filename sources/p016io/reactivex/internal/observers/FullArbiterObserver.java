package p016io.reactivex.internal.observers;

import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.disposables.ObserverFullArbiter;

/* renamed from: io.reactivex.internal.observers.FullArbiterObserver */
public final class FullArbiterObserver<T> implements Observer<T> {
    final ObserverFullArbiter<T> arbiter;

    /* renamed from: s */
    Disposable f216s;

    public FullArbiterObserver(ObserverFullArbiter<T> observerFullArbiter) {
        this.arbiter = observerFullArbiter;
    }

    public void onSubscribe(Disposable disposable) {
        if (DisposableHelper.validate(this.f216s, disposable)) {
            this.f216s = disposable;
            this.arbiter.setDisposable(disposable);
        }
    }

    public void onNext(T t) {
        this.arbiter.onNext(t, this.f216s);
    }

    public void onError(Throwable th) {
        this.arbiter.onError(th, this.f216s);
    }

    public void onComplete() {
        this.arbiter.onComplete(this.f216s);
    }
}
