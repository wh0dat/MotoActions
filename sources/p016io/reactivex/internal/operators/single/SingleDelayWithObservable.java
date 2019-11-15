package p016io.reactivex.internal.operators.single;

import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.Single;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.SingleSource;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.observers.ResumeSingleObserver;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.single.SingleDelayWithObservable */
public final class SingleDelayWithObservable<T, U> extends Single<T> {
    final ObservableSource<U> other;
    final SingleSource<T> source;

    /* renamed from: io.reactivex.internal.operators.single.SingleDelayWithObservable$OtherSubscriber */
    static final class OtherSubscriber<T, U> extends AtomicReference<Disposable> implements Observer<U>, Disposable {
        private static final long serialVersionUID = -8565274649390031272L;
        final SingleObserver<? super T> actual;
        boolean done;
        final SingleSource<T> source;

        OtherSubscriber(SingleObserver<? super T> singleObserver, SingleSource<T> singleSource) {
            this.actual = singleObserver;
            this.source = singleSource;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.set(this, disposable)) {
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(U u) {
            ((Disposable) get()).dispose();
            onComplete();
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
                this.source.subscribe(new ResumeSingleObserver(this, this.actual));
            }
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }
    }

    public SingleDelayWithObservable(SingleSource<T> singleSource, ObservableSource<U> observableSource) {
        this.source = singleSource;
        this.other = observableSource;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> singleObserver) {
        this.other.subscribe(new OtherSubscriber(singleObserver, this.source));
    }
}
