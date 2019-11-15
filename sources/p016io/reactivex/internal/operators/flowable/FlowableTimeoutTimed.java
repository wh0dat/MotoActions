package p016io.reactivex.internal.operators.flowable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Scheduler;
import p016io.reactivex.Scheduler.Worker;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.subscribers.FullArbiterSubscriber;
import p016io.reactivex.internal.subscriptions.FullArbiter;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.plugins.RxJavaPlugins;
import p016io.reactivex.subscribers.SerializedSubscriber;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableTimeoutTimed */
public final class FlowableTimeoutTimed<T> extends AbstractFlowableWithUpstream<T, T> {
    static final Disposable NEW_TIMER = new Disposable() {
        public void dispose() {
        }

        public boolean isDisposed() {
            return true;
        }
    };
    final Publisher<? extends T> other;
    final Scheduler scheduler;
    final long timeout;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableTimeoutTimed$TimeoutTimedOtherSubscriber */
    static final class TimeoutTimedOtherSubscriber<T> implements Subscriber<T>, Disposable {
        final Subscriber<? super T> actual;
        final FullArbiter<T> arbiter;
        volatile boolean done;
        volatile long index;
        final Publisher<? extends T> other;

        /* renamed from: s */
        Subscription f338s;
        final long timeout;
        final AtomicReference<Disposable> timer = new AtomicReference<>();
        final TimeUnit unit;
        final Worker worker;

        TimeoutTimedOtherSubscriber(Subscriber<? super T> subscriber, long j, TimeUnit timeUnit, Worker worker2, Publisher<? extends T> publisher) {
            this.actual = subscriber;
            this.timeout = j;
            this.unit = timeUnit;
            this.worker = worker2;
            this.other = publisher;
            this.arbiter = new FullArbiter<>(subscriber, this, 8);
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f338s, subscription)) {
                this.f338s = subscription;
                if (this.arbiter.setSubscription(subscription)) {
                    this.actual.onSubscribe(this.arbiter);
                    scheduleTimeout(0);
                }
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                long j = this.index + 1;
                this.index = j;
                if (this.arbiter.onNext(t, this.f338s)) {
                    scheduleTimeout(j);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void scheduleTimeout(final long j) {
            Disposable disposable = (Disposable) this.timer.get();
            if (disposable != null) {
                disposable.dispose();
            }
            if (this.timer.compareAndSet(disposable, FlowableTimeoutTimed.NEW_TIMER)) {
                DisposableHelper.replace(this.timer, this.worker.schedule(new Runnable() {
                    public void run() {
                        if (j == TimeoutTimedOtherSubscriber.this.index) {
                            TimeoutTimedOtherSubscriber.this.done = true;
                            TimeoutTimedOtherSubscriber.this.f338s.cancel();
                            DisposableHelper.dispose(TimeoutTimedOtherSubscriber.this.timer);
                            TimeoutTimedOtherSubscriber.this.subscribeNext();
                            TimeoutTimedOtherSubscriber.this.worker.dispose();
                        }
                    }
                }, this.timeout, this.unit));
            }
        }

        /* access modifiers changed from: 0000 */
        public void subscribeNext() {
            this.other.subscribe(new FullArbiterSubscriber(this.arbiter));
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.worker.dispose();
            DisposableHelper.dispose(this.timer);
            this.arbiter.onError(th, this.f338s);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.worker.dispose();
                DisposableHelper.dispose(this.timer);
                this.arbiter.onComplete(this.f338s);
            }
        }

        public void dispose() {
            this.worker.dispose();
            DisposableHelper.dispose(this.timer);
        }

        public boolean isDisposed() {
            return this.worker.isDisposed();
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableTimeoutTimed$TimeoutTimedSubscriber */
    static final class TimeoutTimedSubscriber<T> implements Subscriber<T>, Disposable, Subscription {
        final Subscriber<? super T> actual;
        volatile boolean done;
        volatile long index;

        /* renamed from: s */
        Subscription f339s;
        final long timeout;
        final AtomicReference<Disposable> timer = new AtomicReference<>();
        final TimeUnit unit;
        final Worker worker;

        TimeoutTimedSubscriber(Subscriber<? super T> subscriber, long j, TimeUnit timeUnit, Worker worker2) {
            this.actual = subscriber;
            this.timeout = j;
            this.unit = timeUnit;
            this.worker = worker2;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f339s, subscription)) {
                this.f339s = subscription;
                this.actual.onSubscribe(this);
                scheduleTimeout(0);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                long j = this.index + 1;
                this.index = j;
                this.actual.onNext(t);
                scheduleTimeout(j);
            }
        }

        /* access modifiers changed from: 0000 */
        public void scheduleTimeout(final long j) {
            Disposable disposable = (Disposable) this.timer.get();
            if (disposable != null) {
                disposable.dispose();
            }
            if (this.timer.compareAndSet(disposable, FlowableTimeoutTimed.NEW_TIMER)) {
                DisposableHelper.replace(this.timer, this.worker.schedule(new Runnable() {
                    public void run() {
                        if (j == TimeoutTimedSubscriber.this.index) {
                            TimeoutTimedSubscriber.this.done = true;
                            TimeoutTimedSubscriber.this.dispose();
                            TimeoutTimedSubscriber.this.actual.onError(new TimeoutException());
                        }
                    }
                }, this.timeout, this.unit));
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            dispose();
            this.actual.onError(th);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                dispose();
                this.actual.onComplete();
            }
        }

        public void dispose() {
            this.worker.dispose();
            DisposableHelper.dispose(this.timer);
            this.f339s.cancel();
        }

        public boolean isDisposed() {
            return this.worker.isDisposed();
        }

        public void request(long j) {
            this.f339s.request(j);
        }

        public void cancel() {
            dispose();
        }
    }

    public FlowableTimeoutTimed(Publisher<T> publisher, long j, TimeUnit timeUnit, Scheduler scheduler2, Publisher<? extends T> publisher2) {
        super(publisher);
        this.timeout = j;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
        this.other = publisher2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        if (this.other == null) {
            Publisher publisher = this.source;
            TimeoutTimedSubscriber timeoutTimedSubscriber = new TimeoutTimedSubscriber(new SerializedSubscriber(subscriber), this.timeout, this.unit, this.scheduler.createWorker());
            publisher.subscribe(timeoutTimedSubscriber);
            return;
        }
        Publisher publisher2 = this.source;
        TimeoutTimedOtherSubscriber timeoutTimedOtherSubscriber = new TimeoutTimedOtherSubscriber(subscriber, this.timeout, this.unit, this.scheduler.createWorker(), this.other);
        publisher2.subscribe(timeoutTimedOtherSubscriber);
    }
}
