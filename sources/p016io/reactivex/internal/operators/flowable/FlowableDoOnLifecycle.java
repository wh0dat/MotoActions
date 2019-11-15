package p016io.reactivex.internal.operators.flowable;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Flowable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Action;
import p016io.reactivex.functions.Consumer;
import p016io.reactivex.functions.LongConsumer;
import p016io.reactivex.internal.subscriptions.EmptySubscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableDoOnLifecycle */
public final class FlowableDoOnLifecycle<T> extends AbstractFlowableWithUpstream<T, T> {
    private final Action onCancel;
    private final LongConsumer onRequest;
    private final Consumer<? super Subscription> onSubscribe;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableDoOnLifecycle$SubscriptionLambdaSubscriber */
    static final class SubscriptionLambdaSubscriber<T> implements Subscriber<T>, Subscription {
        final Subscriber<? super T> actual;
        final Action onCancel;
        final LongConsumer onRequest;
        final Consumer<? super Subscription> onSubscribe;

        /* renamed from: s */
        Subscription f274s;

        SubscriptionLambdaSubscriber(Subscriber<? super T> subscriber, Consumer<? super Subscription> consumer, LongConsumer longConsumer, Action action) {
            this.actual = subscriber;
            this.onSubscribe = consumer;
            this.onCancel = action;
            this.onRequest = longConsumer;
        }

        public void onSubscribe(Subscription subscription) {
            try {
                this.onSubscribe.accept(subscription);
                if (SubscriptionHelper.validate(this.f274s, subscription)) {
                    this.f274s = subscription;
                    this.actual.onSubscribe(this);
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                subscription.cancel();
                RxJavaPlugins.onError(th);
                EmptySubscription.error(th, this.actual);
            }
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

        public void request(long j) {
            try {
                this.onRequest.accept(j);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                RxJavaPlugins.onError(th);
            }
            this.f274s.request(j);
        }

        public void cancel() {
            try {
                this.onCancel.run();
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                RxJavaPlugins.onError(th);
            }
            this.f274s.cancel();
        }
    }

    public FlowableDoOnLifecycle(Flowable<T> flowable, Consumer<? super Subscription> consumer, LongConsumer longConsumer, Action action) {
        super(flowable);
        this.onSubscribe = consumer;
        this.onRequest = longConsumer;
        this.onCancel = action;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        this.source.subscribe(new SubscriptionLambdaSubscriber(subscriber, this.onSubscribe, this.onRequest, this.onCancel));
    }
}
