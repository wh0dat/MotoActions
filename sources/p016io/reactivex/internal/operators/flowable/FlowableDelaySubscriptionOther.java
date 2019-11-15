package p016io.reactivex.internal.operators.flowable;

import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Flowable;
import p016io.reactivex.internal.subscriptions.SubscriptionArbiter;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableDelaySubscriptionOther */
public final class FlowableDelaySubscriptionOther<T, U> extends Flowable<T> {
    final Publisher<? extends T> main;
    final Publisher<U> other;

    public FlowableDelaySubscriptionOther(Publisher<? extends T> publisher, Publisher<U> publisher2) {
        this.main = publisher;
        this.other = publisher2;
    }

    public void subscribeActual(final Subscriber<? super T> subscriber) {
        final SubscriptionArbiter subscriptionArbiter = new SubscriptionArbiter();
        subscriber.onSubscribe(subscriptionArbiter);
        this.other.subscribe(new Subscriber<U>() {
            boolean done;

            public void onSubscribe(final Subscription subscription) {
                subscriptionArbiter.setSubscription(new Subscription() {
                    public void request(long j) {
                    }

                    public void cancel() {
                        subscription.cancel();
                    }
                });
                subscription.request(LongCompanionObject.MAX_VALUE);
            }

            public void onNext(U u) {
                onComplete();
            }

            public void onError(Throwable th) {
                if (this.done) {
                    RxJavaPlugins.onError(th);
                    return;
                }
                this.done = true;
                subscriber.onError(th);
            }

            public void onComplete() {
                if (!this.done) {
                    this.done = true;
                    FlowableDelaySubscriptionOther.this.main.subscribe(new Subscriber<T>() {
                        public void onSubscribe(Subscription subscription) {
                            subscriptionArbiter.setSubscription(subscription);
                        }

                        public void onNext(T t) {
                            subscriber.onNext(t);
                        }

                        public void onError(Throwable th) {
                            subscriber.onError(th);
                        }

                        public void onComplete() {
                            subscriber.onComplete();
                        }
                    });
                }
            }
        });
    }
}
