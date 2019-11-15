package p016io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.Scheduler;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.observable.ObservableSubscribeOn */
public final class ObservableSubscribeOn<T> extends AbstractObservableWithUpstream<T, T> {
    final Scheduler scheduler;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSubscribeOn$SubscribeOnObserver */
    static final class SubscribeOnObserver<T> extends AtomicReference<Disposable> implements Observer<T>, Disposable {
        private static final long serialVersionUID = 8094547886072529208L;
        final Observer<? super T> actual;

        /* renamed from: s */
        final AtomicReference<Disposable> f473s = new AtomicReference<>();

        SubscribeOnObserver(Observer<? super T> observer) {
            this.actual = observer;
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.setOnce(this.f473s, disposable);
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void dispose() {
            DisposableHelper.dispose(this.f473s);
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        /* access modifiers changed from: 0000 */
        public void setDisposable(Disposable disposable) {
            DisposableHelper.setOnce(this, disposable);
        }
    }

    public ObservableSubscribeOn(ObservableSource<T> observableSource, Scheduler scheduler2) {
        super(observableSource);
        this.scheduler = scheduler2;
    }

    public void subscribeActual(Observer<? super T> observer) {
        final SubscribeOnObserver subscribeOnObserver = new SubscribeOnObserver(observer);
        observer.onSubscribe(subscribeOnObserver);
        subscribeOnObserver.setDisposable(this.scheduler.scheduleDirect(new Runnable() {
            public void run() {
                ObservableSubscribeOn.this.source.subscribe(subscribeOnObserver);
            }
        }));
    }
}
