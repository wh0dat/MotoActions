package p016io.reactivex.internal.operators.completable;

import p016io.reactivex.CompletableObserver;
import p016io.reactivex.CompletableSource;
import p016io.reactivex.Observable;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;

/* renamed from: io.reactivex.internal.operators.completable.CompletableToObservable */
public final class CompletableToObservable<T> extends Observable<T> {
    final CompletableSource source;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableToObservable$ObserverCompletableObserver */
    static final class ObserverCompletableObserver implements CompletableObserver {
        private final Observer<?> observer;

        ObserverCompletableObserver(Observer<?> observer2) {
            this.observer = observer2;
        }

        public void onComplete() {
            this.observer.onComplete();
        }

        public void onError(Throwable th) {
            this.observer.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            this.observer.onSubscribe(disposable);
        }
    }

    public CompletableToObservable(CompletableSource completableSource) {
        this.source = completableSource;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        this.source.subscribe(new ObserverCompletableObserver(observer));
    }
}
