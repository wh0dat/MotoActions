package p016io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicBoolean;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Scheduler;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableUnsubscribeOn */
public final class FlowableUnsubscribeOn<T> extends AbstractFlowableWithUpstream<T, T> {
    final Scheduler scheduler;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableUnsubscribeOn$UnsubscribeSubscriber */
    static final class UnsubscribeSubscriber<T> extends AtomicBoolean implements Subscriber<T>, Subscription {
        private static final long serialVersionUID = 1015244841293359600L;
        final Subscriber<? super T> actual;

        /* renamed from: s */
        Subscription f342s;
        final Scheduler scheduler;

        UnsubscribeSubscriber(Subscriber<? super T> subscriber, Scheduler scheduler2) {
            this.actual = subscriber;
            this.scheduler = scheduler2;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f342s, subscription)) {
                this.f342s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!get()) {
                this.actual.onNext(t);
            }
        }

        public void onError(Throwable th) {
            if (get()) {
                RxJavaPlugins.onError(th);
            } else {
                this.actual.onError(th);
            }
        }

        public void onComplete() {
            if (!get()) {
                this.actual.onComplete();
            }
        }

        public void request(long j) {
            this.f342s.request(j);
        }

        public void cancel() {
            if (compareAndSet(false, true)) {
                this.scheduler.scheduleDirect(new Runnable() {
                    public void run() {
                        UnsubscribeSubscriber.this.f342s.cancel();
                    }
                });
            }
        }
    }

    public FlowableUnsubscribeOn(Publisher<T> publisher, Scheduler scheduler2) {
        super(publisher);
        this.scheduler = scheduler2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        this.source.subscribe(new UnsubscribeSubscriber(subscriber, this.scheduler));
    }
}
