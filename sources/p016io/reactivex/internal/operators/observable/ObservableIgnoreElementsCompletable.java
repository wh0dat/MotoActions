package p016io.reactivex.internal.operators.observable;

import p016io.reactivex.Completable;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.Observable;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.fuseable.FuseToObservable;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableIgnoreElementsCompletable */
public final class ObservableIgnoreElementsCompletable<T> extends Completable implements FuseToObservable<T> {
    final ObservableSource<T> source;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableIgnoreElementsCompletable$IgnoreObservable */
    static final class IgnoreObservable<T> implements Observer<T>, Disposable {
        final CompletableObserver actual;

        /* renamed from: d */
        Disposable f442d;

        public void onNext(T t) {
        }

        IgnoreObservable(CompletableObserver completableObserver) {
            this.actual = completableObserver;
        }

        public void onSubscribe(Disposable disposable) {
            this.f442d = disposable;
            this.actual.onSubscribe(this);
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void dispose() {
            this.f442d.dispose();
        }

        public boolean isDisposed() {
            return this.f442d.isDisposed();
        }
    }

    public ObservableIgnoreElementsCompletable(ObservableSource<T> observableSource) {
        this.source = observableSource;
    }

    public void subscribeActual(CompletableObserver completableObserver) {
        this.source.subscribe(new IgnoreObservable(completableObserver));
    }

    public Observable<T> fuseToObservable() {
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableIgnoreElements<T>(this.source));
    }
}
