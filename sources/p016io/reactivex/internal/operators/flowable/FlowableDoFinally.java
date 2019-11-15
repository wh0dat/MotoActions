package p016io.reactivex.internal.operators.flowable;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.annotations.Experimental;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Action;
import p016io.reactivex.internal.fuseable.ConditionalSubscriber;
import p016io.reactivex.internal.fuseable.QueueSubscription;
import p016io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

@Experimental
/* renamed from: io.reactivex.internal.operators.flowable.FlowableDoFinally */
public final class FlowableDoFinally<T> extends AbstractFlowableWithUpstream<T, T> {
    final Action onFinally;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableDoFinally$DoFinallyConditionalSubscriber */
    static final class DoFinallyConditionalSubscriber<T> extends BasicIntQueueSubscription<T> implements ConditionalSubscriber<T> {
        private static final long serialVersionUID = 4109457741734051389L;
        final ConditionalSubscriber<? super T> actual;
        final Action onFinally;

        /* renamed from: qs */
        QueueSubscription<T> f270qs;

        /* renamed from: s */
        Subscription f271s;
        boolean syncFused;

        DoFinallyConditionalSubscriber(ConditionalSubscriber<? super T> conditionalSubscriber, Action action) {
            this.actual = conditionalSubscriber;
            this.onFinally = action;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f271s, subscription)) {
                this.f271s = subscription;
                if (subscription instanceof QueueSubscription) {
                    this.f270qs = (QueueSubscription) subscription;
                }
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public boolean tryOnNext(T t) {
            return this.actual.tryOnNext(t);
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
            runFinally();
        }

        public void onComplete() {
            this.actual.onComplete();
            runFinally();
        }

        public void cancel() {
            this.f271s.cancel();
            runFinally();
        }

        public void request(long j) {
            this.f271s.request(j);
        }

        public int requestFusion(int i) {
            QueueSubscription<T> queueSubscription = this.f270qs;
            if (queueSubscription == null || (i & 4) != 0) {
                return 0;
            }
            int requestFusion = queueSubscription.requestFusion(i);
            if (requestFusion != 0) {
                boolean z = true;
                if (requestFusion != 1) {
                    z = false;
                }
                this.syncFused = z;
            }
            return requestFusion;
        }

        public void clear() {
            this.f270qs.clear();
        }

        public boolean isEmpty() {
            return this.f270qs.isEmpty();
        }

        public T poll() throws Exception {
            T poll = this.f270qs.poll();
            if (poll == null && this.syncFused) {
                runFinally();
            }
            return poll;
        }

        /* access modifiers changed from: 0000 */
        public void runFinally() {
            if (compareAndSet(0, 1)) {
                try {
                    this.onFinally.run();
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    RxJavaPlugins.onError(th);
                }
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableDoFinally$DoFinallySubscriber */
    static final class DoFinallySubscriber<T> extends BasicIntQueueSubscription<T> implements Subscriber<T> {
        private static final long serialVersionUID = 4109457741734051389L;
        final Subscriber<? super T> actual;
        final Action onFinally;

        /* renamed from: qs */
        QueueSubscription<T> f272qs;

        /* renamed from: s */
        Subscription f273s;
        boolean syncFused;

        DoFinallySubscriber(Subscriber<? super T> subscriber, Action action) {
            this.actual = subscriber;
            this.onFinally = action;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f273s, subscription)) {
                this.f273s = subscription;
                if (subscription instanceof QueueSubscription) {
                    this.f272qs = (QueueSubscription) subscription;
                }
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
            runFinally();
        }

        public void onComplete() {
            this.actual.onComplete();
            runFinally();
        }

        public void cancel() {
            this.f273s.cancel();
            runFinally();
        }

        public void request(long j) {
            this.f273s.request(j);
        }

        public int requestFusion(int i) {
            QueueSubscription<T> queueSubscription = this.f272qs;
            if (queueSubscription == null || (i & 4) != 0) {
                return 0;
            }
            int requestFusion = queueSubscription.requestFusion(i);
            if (requestFusion != 0) {
                boolean z = true;
                if (requestFusion != 1) {
                    z = false;
                }
                this.syncFused = z;
            }
            return requestFusion;
        }

        public void clear() {
            this.f272qs.clear();
        }

        public boolean isEmpty() {
            return this.f272qs.isEmpty();
        }

        public T poll() throws Exception {
            T poll = this.f272qs.poll();
            if (poll == null && this.syncFused) {
                runFinally();
            }
            return poll;
        }

        /* access modifiers changed from: 0000 */
        public void runFinally() {
            if (compareAndSet(0, 1)) {
                try {
                    this.onFinally.run();
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    RxJavaPlugins.onError(th);
                }
            }
        }
    }

    public FlowableDoFinally(Publisher<T> publisher, Action action) {
        super(publisher);
        this.onFinally = action;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        if (subscriber instanceof ConditionalSubscriber) {
            this.source.subscribe(new DoFinallyConditionalSubscriber((ConditionalSubscriber) subscriber, this.onFinally));
        } else {
            this.source.subscribe(new DoFinallySubscriber(subscriber, this.onFinally));
        }
    }
}
