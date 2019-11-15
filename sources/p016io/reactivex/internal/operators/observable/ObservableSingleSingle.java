package p016io.reactivex.internal.operators.observable;

import java.util.NoSuchElementException;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.Single;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableSingleSingle */
public final class ObservableSingleSingle<T> extends Single<T> {
    final T defaultValue;
    final ObservableSource<? extends T> source;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSingleSingle$SingleElementObserver */
    static final class SingleElementObserver<T> implements Observer<T>, Disposable {
        final SingleObserver<? super T> actual;
        final T defaultValue;
        boolean done;

        /* renamed from: s */
        Disposable f465s;
        T value;

        SingleElementObserver(SingleObserver<? super T> singleObserver, T t) {
            this.actual = singleObserver;
            this.defaultValue = t;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f465s, disposable)) {
                this.f465s = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void dispose() {
            this.f465s.dispose();
        }

        public boolean isDisposed() {
            return this.f465s.isDisposed();
        }

        public void onNext(T t) {
            if (!this.done) {
                if (this.value != null) {
                    this.done = true;
                    this.f465s.dispose();
                    this.actual.onError(new IllegalArgumentException("Sequence contains more than one element!"));
                    return;
                }
                this.value = t;
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
                T t = this.value;
                this.value = null;
                if (t == null) {
                    t = this.defaultValue;
                }
                if (t != null) {
                    this.actual.onSuccess(t);
                } else {
                    this.actual.onError(new NoSuchElementException());
                }
            }
        }
    }

    public ObservableSingleSingle(ObservableSource<? extends T> observableSource, T t) {
        this.source = observableSource;
        this.defaultValue = t;
    }

    public void subscribeActual(SingleObserver<? super T> singleObserver) {
        this.source.subscribe(new SingleElementObserver(singleObserver, this.defaultValue));
    }
}
