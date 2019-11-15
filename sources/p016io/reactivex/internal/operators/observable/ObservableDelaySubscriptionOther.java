package p016io.reactivex.internal.operators.observable;

import p016io.reactivex.Observable;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.SequentialDisposable;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableDelaySubscriptionOther */
public final class ObservableDelaySubscriptionOther<T, U> extends Observable<T> {
    final ObservableSource<? extends T> main;
    final ObservableSource<U> other;

    public ObservableDelaySubscriptionOther(ObservableSource<? extends T> observableSource, ObservableSource<U> observableSource2) {
        this.main = observableSource;
        this.other = observableSource2;
    }

    public void subscribeActual(final Observer<? super T> observer) {
        final SequentialDisposable sequentialDisposable = new SequentialDisposable();
        observer.onSubscribe(sequentialDisposable);
        this.other.subscribe(new Observer<U>() {
            boolean done;

            public void onSubscribe(Disposable disposable) {
                sequentialDisposable.update(disposable);
            }

            public void onNext(U u) {
                onComplete();
            }

            public void onError(Throwable th) {
                if (this.done) {
                    RxJavaPlugins.onError(th);
                    return;
                }
                this.done = true;
                observer.onError(th);
            }

            public void onComplete() {
                if (!this.done) {
                    this.done = true;
                    ObservableDelaySubscriptionOther.this.main.subscribe(new Observer<T>() {
                        public void onSubscribe(Disposable disposable) {
                            sequentialDisposable.update(disposable);
                        }

                        public void onNext(T t) {
                            observer.onNext(t);
                        }

                        public void onError(Throwable th) {
                            observer.onError(th);
                        }

                        public void onComplete() {
                            observer.onComplete();
                        }
                    });
                }
            }
        });
    }
}
