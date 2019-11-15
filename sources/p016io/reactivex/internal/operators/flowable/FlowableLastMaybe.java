package p016io.reactivex.internal.operators.flowable;

import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Maybe;
import p016io.reactivex.MaybeObserver;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableLastMaybe */
public final class FlowableLastMaybe<T> extends Maybe<T> {
    final Publisher<T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableLastMaybe$LastSubscriber */
    static final class LastSubscriber<T> implements Subscriber<T>, Disposable {
        final MaybeObserver<? super T> actual;
        T item;

        /* renamed from: s */
        Subscription f293s;

        LastSubscriber(MaybeObserver<? super T> maybeObserver) {
            this.actual = maybeObserver;
        }

        public void dispose() {
            this.f293s.cancel();
            this.f293s = SubscriptionHelper.CANCELLED;
        }

        public boolean isDisposed() {
            return this.f293s == SubscriptionHelper.CANCELLED;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f293s, subscription)) {
                this.f293s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(LongCompanionObject.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            this.item = t;
        }

        public void onError(Throwable th) {
            this.f293s = SubscriptionHelper.CANCELLED;
            this.item = null;
            this.actual.onError(th);
        }

        public void onComplete() {
            this.f293s = SubscriptionHelper.CANCELLED;
            T t = this.item;
            if (t != null) {
                this.item = null;
                this.actual.onSuccess(t);
                return;
            }
            this.actual.onComplete();
        }
    }

    public FlowableLastMaybe(Publisher<T> publisher) {
        this.source = publisher;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> maybeObserver) {
        this.source.subscribe(new LastSubscriber(maybeObserver));
    }
}
