package p016io.reactivex.internal.operators.observable;

import java.util.NoSuchElementException;
import p016io.reactivex.Observable;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.Single;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.fuseable.FuseToObservable;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableElementAtSingle */
public final class ObservableElementAtSingle<T> extends Single<T> implements FuseToObservable<T> {
    final T defaultValue;
    final long index;
    final ObservableSource<T> source;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableElementAtSingle$ElementAtObserver */
    static final class ElementAtObserver<T> implements Observer<T>, Disposable {
        final SingleObserver<? super T> actual;
        long count;
        final T defaultValue;
        boolean done;
        final long index;

        /* renamed from: s */
        Disposable f429s;

        ElementAtObserver(SingleObserver<? super T> singleObserver, long j, T t) {
            this.actual = singleObserver;
            this.index = j;
            this.defaultValue = t;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f429s, disposable)) {
                this.f429s = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void dispose() {
            this.f429s.dispose();
        }

        public boolean isDisposed() {
            return this.f429s.isDisposed();
        }

        public void onNext(T t) {
            if (!this.done) {
                long j = this.count;
                if (j == this.index) {
                    this.done = true;
                    this.f429s.dispose();
                    this.actual.onSuccess(t);
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
                    this.actual.onSuccess(t);
                } else {
                    this.actual.onError(new NoSuchElementException());
                }
            }
        }
    }

    public ObservableElementAtSingle(ObservableSource<T> observableSource, long j, T t) {
        this.source = observableSource;
        this.index = j;
        this.defaultValue = t;
    }

    public void subscribeActual(SingleObserver<? super T> singleObserver) {
        this.source.subscribe(new ElementAtObserver(singleObserver, this.index, this.defaultValue));
    }

    public Observable<T> fuseToObservable() {
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableElementAt<T>(this.source, this.index, this.defaultValue));
    }
}
