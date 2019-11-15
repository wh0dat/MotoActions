package p016io.reactivex.internal.operators.observable;

import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableElementAt */
public final class ObservableElementAt<T> extends AbstractObservableWithUpstream<T, T> {
    final T defaultValue;
    final long index;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableElementAt$ElementAtObserver */
    static final class ElementAtObserver<T> implements Observer<T>, Disposable {
        final Observer<? super T> actual;
        long count;
        final T defaultValue;
        boolean done;
        final long index;

        /* renamed from: s */
        Disposable f427s;

        ElementAtObserver(Observer<? super T> observer, long j, T t) {
            this.actual = observer;
            this.index = j;
            this.defaultValue = t;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f427s, disposable)) {
                this.f427s = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void dispose() {
            this.f427s.dispose();
        }

        public boolean isDisposed() {
            return this.f427s.isDisposed();
        }

        public void onNext(T t) {
            if (!this.done) {
                long j = this.count;
                if (j == this.index) {
                    this.done = true;
                    this.f427s.dispose();
                    this.actual.onNext(t);
                    this.actual.onComplete();
                    return;
                }
                this.count = j + 1;
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.actual.onError(th);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                T t = this.defaultValue;
                if (t != null) {
                    this.actual.onNext(t);
                }
                this.actual.onComplete();
            }
        }
    }

    public ObservableElementAt(ObservableSource<T> observableSource, long j, T t) {
        super(observableSource);
        this.index = j;
        this.defaultValue = t;
    }

    public void subscribeActual(Observer<? super T> observer) {
        this.source.subscribe(new ElementAtObserver(observer, this.index, this.defaultValue));
    }
}
