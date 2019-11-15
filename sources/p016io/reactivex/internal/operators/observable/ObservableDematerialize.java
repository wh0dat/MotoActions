package p016io.reactivex.internal.operators.observable;

import p016io.reactivex.Notification;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableDematerialize */
public final class ObservableDematerialize<T> extends AbstractObservableWithUpstream<Notification<T>, T> {

    /* renamed from: io.reactivex.internal.operators.observable.ObservableDematerialize$DematerializeObserver */
    static final class DematerializeObserver<T> implements Observer<Notification<T>>, Disposable {
        final Observer<? super T> actual;
        boolean done;

        /* renamed from: s */
        Disposable f422s;

        DematerializeObserver(Observer<? super T> observer) {
            this.actual = observer;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f422s, disposable)) {
                this.f422s = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void dispose() {
            this.f422s.dispose();
        }

        public boolean isDisposed() {
            return this.f422s.isDisposed();
        }

        public void onNext(Notification<T> notification) {
            if (this.done) {
                if (notification.isOnError()) {
                    RxJavaPlugins.onError(notification.getError());
                }
                return;
            }
            if (notification.isOnError()) {
                this.f422s.dispose();
                onError(notification.getError());
            } else if (notification.isOnComplete()) {
                this.f422s.dispose();
                onComplete();
            } else {
                this.actual.onNext(notification.getValue());
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
                this.actual.onComplete();
            }
        }
    }

    public ObservableDematerialize(ObservableSource<Notification<T>> observableSource) {
        super(observableSource);
    }

    public void subscribeActual(Observer<? super T> observer) {
        this.source.subscribe(new DematerializeObserver(observer));
    }
}
