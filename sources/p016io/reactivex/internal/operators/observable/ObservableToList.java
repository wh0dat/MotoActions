package p016io.reactivex.internal.operators.observable;

import java.util.Collection;
import java.util.concurrent.Callable;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.disposables.EmptyDisposable;
import p016io.reactivex.internal.functions.Functions;
import p016io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.internal.operators.observable.ObservableToList */
public final class ObservableToList<T, U extends Collection<? super T>> extends AbstractObservableWithUpstream<T, U> {
    final Callable<U> collectionSupplier;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableToList$ToListObserver */
    static final class ToListObserver<T, U extends Collection<? super T>> implements Observer<T>, Disposable {
        final Observer<? super U> actual;
        U collection;

        /* renamed from: s */
        Disposable f487s;

        ToListObserver(Observer<? super U> observer, U u) {
            this.actual = observer;
            this.collection = u;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f487s, disposable)) {
                this.f487s = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void dispose() {
            this.f487s.dispose();
        }

        public boolean isDisposed() {
            return this.f487s.isDisposed();
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
            this.actual.onNext(u);
            this.actual.onComplete();
        }
    }

    public ObservableToList(ObservableSource<T> observableSource, int i) {
        super(observableSource);
        this.collectionSupplier = Functions.createArrayList(i);
    }

    public ObservableToList(ObservableSource<T> observableSource, Callable<U> callable) {
        super(observableSource);
        this.collectionSupplier = callable;
    }

    public void subscribeActual(Observer<? super U> observer) {
        try {
            this.source.subscribe(new ToListObserver(observer, (Collection) ObjectHelper.requireNonNull(this.collectionSupplier.call(), "The collectionSupplier returned a null collection. Null values are generally not allowed in 2.x operators and sources.")));
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptyDisposable.error(th, observer);
        }
    }
}
