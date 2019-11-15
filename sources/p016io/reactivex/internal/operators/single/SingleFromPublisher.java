package p016io.reactivex.internal.operators.single;

import java.util.NoSuchElementException;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Single;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.single.SingleFromPublisher */
public final class SingleFromPublisher<T> extends Single<T> {
    final Publisher<? extends T> publisher;

    /* renamed from: io.reactivex.internal.operators.single.SingleFromPublisher$ToSingleObserver */
    static final class ToSingleObserver<T> implements Subscriber<T>, Disposable {
        final SingleObserver<? super T> actual;
        volatile boolean disposed;
        boolean done;

        /* renamed from: s */
        Subscription f516s;
        T value;

        ToSingleObserver(SingleObserver<? super T> singleObserver) {
            this.actual = singleObserver;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f516s, subscription)) {
                this.f516s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(LongCompanionObject.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                if (this.value != null) {
                    this.f516s.cancel();
                    this.done = true;
                    this.value = null;
                    this.actual.onError(new IndexOutOfBoundsException("Too many elements in the Publisher"));
                } else {
                    this.value = t;
                }
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.value = null;
            this.actual.onError(th);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                T t = this.value;
                this.value = null;
                if (t == null) {
                    this.actual.onError(new NoSuchElementException("The source Publisher is empty"));
                } else {
                    this.actual.onSuccess(t);
                }
            }
        }

        public boolean isDisposed() {
            return this.disposed;
        }

        public void dispose() {
            this.disposed = true;
            this.f516s.cancel();
        }
    }

    public SingleFromPublisher(Publisher<? extends T> publisher2) {
        this.publisher = publisher2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> singleObserver) {
        this.publisher.subscribe(new ToSingleObserver(singleObserver));
    }
}
