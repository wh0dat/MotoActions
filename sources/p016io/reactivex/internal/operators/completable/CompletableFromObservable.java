package p016io.reactivex.internal.operators.completable;

import p016io.reactivex.Completable;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;

/* renamed from: io.reactivex.internal.operators.completable.CompletableFromObservable */
public final class CompletableFromObservable<T> extends Completable {
    final ObservableSource<T> observable;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableFromObservable$CompletableFromObservableObserver */
    static final class CompletableFromObservableObserver<T> implements Observer<T> {

        /* renamed from: co */
        final CompletableObserver f235co;

        public void onNext(T t) {
        }

        CompletableFromObservableObserver(CompletableObserver completableObserver) {
            this.f235co = completableObserver;
        }

        public void onSubscribe(Disposable disposable) {
            this.f235co.onSubscribe(disposable);
        }

        public void onError(Throwable th) {
            this.f235co.onError(th);
        }

        public void onComplete() {
            this.f235co.onComplete();
        }
    }

    public CompletableFromObservable(ObservableSource<T> observableSource) {
        this.observable = observableSource;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver completableObserver) {
        this.observable.subscribe(new CompletableFromObservableObserver(completableObserver));
    }
}
