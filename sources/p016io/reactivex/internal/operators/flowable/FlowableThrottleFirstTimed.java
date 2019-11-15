package p016io.reactivex.internal.operators.flowable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Scheduler;
import p016io.reactivex.Scheduler.Worker;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.MissingBackpressureException;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.disposables.SequentialDisposable;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.BackpressureHelper;
import p016io.reactivex.plugins.RxJavaPlugins;
import p016io.reactivex.subscribers.SerializedSubscriber;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableThrottleFirstTimed */
public final class FlowableThrottleFirstTimed<T> extends AbstractFlowableWithUpstream<T, T> {
    final Scheduler scheduler;
    final long timeout;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableThrottleFirstTimed$DebounceTimedSubscriber */
    static final class DebounceTimedSubscriber<T> extends AtomicLong implements Subscriber<T>, Subscription, Runnable {
        private static final long serialVersionUID = -9102637559663639004L;
        final Subscriber<? super T> actual;
        boolean done;
        volatile boolean gate;

        /* renamed from: s */
        Subscription f334s;
        final long timeout;
        final SequentialDisposable timer = new SequentialDisposable();
        final TimeUnit unit;
        final Worker worker;

        DebounceTimedSubscriber(Subscriber<? super T> subscriber, long j, TimeUnit timeUnit, Worker worker2) {
            this.actual = subscriber;
            this.timeout = j;
            this.unit = timeUnit;
            this.worker = worker2;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f334s, subscription)) {
                this.f334s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(LongCompanionObject.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (!this.done && !this.gate) {
                this.gate = true;
                if (get() != 0) {
                    this.actual.onNext(t);
                    BackpressureHelper.produced(this, 1);
                    Disposable disposable = (Disposable) this.timer.get();
                    if (disposable != null) {
                        disposable.dispose();
                    }
                    this.timer.replace(this.worker.schedule(this, this.timeout, this.unit));
                } else {
                    this.done = true;
                    cancel();
                    this.actual.onError(new MissingBackpressureException("Could not deliver value due to lack of requests"));
                }
            }
        }

        public void run() {
            this.gate = false;
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            DisposableHelper.dispose(this.timer);
            this.actual.onError(th);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                DisposableHelper.dispose(this.timer);
                this.worker.dispose();
                this.actual.onComplete();
            }
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this, j);
            }
        }

        public void cancel() {
            DisposableHelper.dispose(this.timer);
            this.worker.dispose();
            this.f334s.cancel();
        }
    }

    public FlowableThrottleFirstTimed(Publisher<T> publisher, long j, TimeUnit timeUnit, Scheduler scheduler2) {
        super(publisher);
        this.timeout = j;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        Publisher publisher = this.source;
        DebounceTimedSubscriber debounceTimedSubscriber = new DebounceTimedSubscriber(new SerializedSubscriber(subscriber), this.timeout, this.unit, this.scheduler.createWorker());
        publisher.subscribe(debounceTimedSubscriber);
    }
}
