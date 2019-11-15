package p016io.reactivex.internal.operators.single;

import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Single;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.SingleSource;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.single.SingleTakeUntil */
public final class SingleTakeUntil<T, U> extends Single<T> {
    final Publisher<U> other;
    final SingleSource<T> source;

    /* renamed from: io.reactivex.internal.operators.single.SingleTakeUntil$TakeUntilMainObserver */
    static final class TakeUntilMainObserver<T> extends AtomicReference<Disposable> implements SingleObserver<T>, Disposable {
        private static final long serialVersionUID = -622603812305745221L;
        final SingleObserver<? super T> actual;
        final TakeUntilOtherSubscriber other = new TakeUntilOtherSubscriber(this);

        TakeUntilMainObserver(SingleObserver<? super T> singleObserver) {
            this.actual = singleObserver;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.setOnce(this, disposable);
        }

        public void onSuccess(T t) {
            this.other.dispose();
            if (((Disposable) get()) != DisposableHelper.DISPOSED && ((Disposable) getAndSet(DisposableHelper.DISPOSED)) != DisposableHelper.DISPOSED) {
                this.actual.onSuccess(t);
            }
        }

        public void onError(Throwable th) {
            this.other.dispose();
            if (((Disposable) get()) == DisposableHelper.DISPOSED || ((Disposable) getAndSet(DisposableHelper.DISPOSED)) == DisposableHelper.DISPOSED) {
                RxJavaPlugins.onError(th);
            } else {
                this.actual.onError(th);
            }
        }

        /* access modifiers changed from: 0000 */
        public void otherError(Throwable th) {
            if (((Disposable) get()) != DisposableHelper.DISPOSED) {
                Disposable disposable = (Disposable) getAndSet(DisposableHelper.DISPOSED);
                if (disposable != DisposableHelper.DISPOSED) {
                    if (disposable != null) {
                        disposable.dispose();
                    }
                    this.actual.onError(th);
                    return;
                }
            }
            RxJavaPlugins.onError(th);
        }
    }

    /* renamed from: io.reactivex.internal.operators.single.SingleTakeUntil$TakeUntilOtherSubscriber */
    static final class TakeUntilOtherSubscriber extends AtomicReference<Subscription> implements Subscriber<Object> {
        private static final long serialVersionUID = 5170026210238877381L;
        final TakeUntilMainObserver<?> parent;

        TakeUntilOtherSubscriber(TakeUntilMainObserver<?> takeUntilMainObserver) {
            this.parent = takeUntilMainObserver;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.setOnce(this, subscription)) {
                subscription.request(LongCompanionObject.MAX_VALUE);
            }
        }

        public void onNext(Object obj) {
            if (SubscriptionHelper.cancel(this)) {
                onComplete();
            }
        }

        public void onError(Throwable th) {
            this.parent.otherError(th);
        }

        public void onComplete() {
            this.parent.otherError(new CancellationException());
        }

        public void dispose() {
            SubscriptionHelper.cancel(this);
        }
    }

    public SingleTakeUntil(SingleSource<T> singleSource, Publisher<U> publisher) {
        this.source = singleSource;
        this.other = publisher;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> singleObserver) {
        TakeUntilMainObserver takeUntilMainObserver = new TakeUntilMainObserver(singleObserver);
        singleObserver.onSubscribe(takeUntilMainObserver);
        this.other.subscribe(takeUntilMainObserver.other);
        this.source.subscribe(takeUntilMainObserver);
    }
}
