package p016io.reactivex.internal.operators.observable;

import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.util.EmptyComponent;

/* renamed from: io.reactivex.internal.operators.observable.ObservableDetach */
public final class ObservableDetach<T> extends AbstractObservableWithUpstream<T, T> {

    /* renamed from: io.reactivex.internal.operators.observable.ObservableDetach$DetachObserver */
    static final class DetachObserver<T> implements Observer<T>, Disposable {
        Observer<? super T> actual;

        /* renamed from: s */
        Disposable f423s;

        DetachObserver(Observer<? super T> observer) {
            this.actual = observer;
        }

        public void dispose() {
            Disposable disposable = this.f423s;
            this.f423s = EmptyComponent.INSTANCE;
            this.actual = EmptyComponent.asObserver();
            disposable.dispose();
        }

        public boolean isDisposed() {
            return this.f423s.isDisposed();
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f423s, disposable)) {
                this.f423s = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable th) {
            Observer<? super T> observer = this.actual;
            this.f423s = EmptyComponent.INSTANCE;
            this.actual = EmptyComponent.asObserver();
            observer.onError(th);
        }

        public void onComplete() {
            Observer<? super T> observer = this.actual;
            this.f423s = EmptyComponent.INSTANCE;
            this.actual = EmptyComponent.asObserver();
            observer.onComplete();
        }
    }

    public ObservableDetach(ObservableSource<T> observableSource) {
        super(observableSource);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        this.source.subscribe(new DetachObserver(observer));
    }
}
