package p016io.reactivex.internal.operators.flowable;

import java.util.Collection;
import java.util.concurrent.Callable;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import p016io.reactivex.internal.subscriptions.EmptySubscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableToList */
public final class FlowableToList<T, U extends Collection<? super T>> extends AbstractFlowableWithUpstream<T, U> {
    final Callable<U> collectionSupplier;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableToList$ToListSubscriber */
    static final class ToListSubscriber<T, U extends Collection<? super T>> extends DeferredScalarSubscription<U> implements Subscriber<T>, Subscription {
        private static final long serialVersionUID = -8134157938864266736L;

        /* renamed from: s */
        Subscription f340s;

        ToListSubscriber(Subscriber<? super U> subscriber, U u) {
            super(subscriber);
            this.value = u;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f340s, subscription)) {
                this.f340s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(LongCompanionObject.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            ((Collection) this.value).add(t);
        }

        public void onError(Throwable th) {
            this.value = null;
            this.actual.onError(th);
        }

        public void onComplete() {
            complete(this.value);
        }

        public void cancel() {
            super.cancel();
            this.f340s.cancel();
        }
    }

    public FlowableToList(Publisher<T> publisher, Callable<U> callable) {
        super(publisher);
        this.collectionSupplier = callable;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super U> subscriber) {
        try {
            this.source.subscribe(new ToListSubscriber(subscriber, (Collection) ObjectHelper.requireNonNull(this.collectionSupplier.call(), "The collectionSupplier returned a null collection. Null values are generally not allowed in 2.x operators and sources.")));
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptySubscription.error(th, subscriber);
        }
    }
}
