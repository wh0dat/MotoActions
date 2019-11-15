package p016io.reactivex.internal.observers;

import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.observers.DeferredScalarObserver */
public abstract class DeferredScalarObserver<T, R> extends DeferredScalarDisposable<R> implements Observer<T> {
    private static final long serialVersionUID = -266195175408988651L;

    /* renamed from: s */
    protected Disposable f214s;

    public DeferredScalarObserver(Observer<? super R> observer) {
        super(observer);
    }

    public void onSubscribe(Disposable disposable) {
        if (DisposableHelper.validate(this.f214s, disposable)) {
            this.f214s = disposable;
            this.actual.onSubscribe(this);
        }
    }

    public void onError(Throwable th) {
        this.value = null;
        error(th);
    }

    public void onComplete() {
        Object obj = this.value;
        if (obj != null) {
            this.value = null;
            complete(obj);
            return;
        }
        complete();
    }

    public void dispose() {
        super.dispose();
        this.f214s.dispose();
    }
}
