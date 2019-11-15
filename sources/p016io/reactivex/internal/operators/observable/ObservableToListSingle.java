package p016io.reactivex.internal.operators.observable;

import java.util.Collection;
import java.util.concurrent.Callable;
import p016io.reactivex.Observable;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.Single;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.disposables.EmptyDisposable;
import p016io.reactivex.internal.functions.Functions;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.fuseable.FuseToObservable;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableToListSingle */
public final class ObservableToListSingle<T, U extends Collection<? super T>> extends Single<U> implements FuseToObservable<U> {
    final Callable<U> collectionSupplier;
    final ObservableSource<T> source;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableToListSingle$ToListObserver */
    static final class ToListObserver<T, U extends Collection<? super T>> implements Observer<T>, Disposable {
        final SingleObserver<? super U> actual;
        U collection;

        /* renamed from: s */
        Disposable f488s;

        ToListObserver(SingleObserver<? super U> singleObserver, U u) {
            this.actual = singleObserver;
            this.collection = u;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f488s, disposable)) {
                this.f488s = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void dispose() {
            this.f488s.dispose();
        }

        public boolean isDisposed() {
            return this.f488s.isDisposed();
        }

        public void onNext(T t) {
            this.collection.add(t);
        }

        public void onError(Throwable th) {
            this.collection = null;
            this.actual.onError(th);
        }

        public void onComplete() {
            U u = this.collection;
            this.collection = null;
            this.actual.onSuccess(u);
        }
    }

    public ObservableToListSingle(ObservableSource<T> observableSource, int i) {
        this.source = observableSource;
        this.collectionSupplier = Functions.createArrayList(i);
    }

    public ObservableToListSingle(ObservableSource<T> observableSource, Callable<U> callable) {
        this.source = observableSource;
        this.collectionSupplier = callable;
    }

    public void subscribeActual(SingleObserver<? super U> singleObserver) {
        try {
            this.source.subscribe(new ToListObserver(singleObserver, (Collection) ObjectHelper.requireNonNull(this.collectionSupplier.call(), "The collectionSupplier returned a null collection. Null values are generally not allowed in 2.x operators and sources.")));
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptyDisposable.error(th, singleObserver);
        }
    }

    public Observable<U> fuseToObservable() {
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableToList<T>(this.source, this.collectionSupplier));
    }
}
