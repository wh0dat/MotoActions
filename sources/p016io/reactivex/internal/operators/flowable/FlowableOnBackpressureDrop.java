package p016io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicLong;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Consumer;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.BackpressureHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableOnBackpressureDrop */
public final class FlowableOnBackpressureDrop<T> extends AbstractFlowableWithUpstream<T, T> implements Consumer<T> {
    final Consumer<? super T> onDrop;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableOnBackpressureDrop$BackpressureDropSubscriber */
    static final class BackpressureDropSubscriber<T> extends AtomicLong implements Subscriber<T>, Subscription {
        private static final long serialVersionUID = -6246093802440953054L;
        final Subscriber<? super T> actual;
        boolean done;
        final Consumer<? super T> onDrop;

        /* renamed from: s */
        Subscription f298s;

        BackpressureDropSubscriber(Subscriber<? super T> subscriber, Consumer<? super T> consumer) {
            this.actual = subscriber;
            this.onDrop = consumer;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f298s, subscription)) {
                this.f298s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(LongCompanionObject.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                if (get() != 0) {
                    this.actual.onNext(t);
                    BackpressureHelper.produced(this, 1);
                } else {
                    try {
                        this.onDrop.accept(t);
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        cancel();
                        onError(th);
                    }
                }
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

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this, j);
            }
        }

        public void cancel() {
            this.f298s.cancel();
        }
    }

    public void accept(T t) {
    }

    public FlowableOnBackpressureDrop(Publisher<T> publisher) {
        super(publisher);
        this.onDrop = this;
    }

    public FlowableOnBackpressureDrop(Publisher<T> publisher, Consumer<? super T> consumer) {
        super(publisher);
        this.onDrop = consumer;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        this.source.subscribe(new BackpressureDropSubscriber(subscriber, this.onDrop));
    }
}
