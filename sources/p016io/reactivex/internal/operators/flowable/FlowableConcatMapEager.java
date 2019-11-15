package p016io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.exceptions.MissingBackpressureException;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.queue.SpscLinkedArrayQueue;
import p016io.reactivex.internal.subscribers.InnerQueuedSubscriber;
import p016io.reactivex.internal.subscribers.InnerQueuedSubscriberSupport;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.AtomicThrowable;
import p016io.reactivex.internal.util.BackpressureHelper;
import p016io.reactivex.internal.util.ErrorMode;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableConcatMapEager */
public final class FlowableConcatMapEager<T, R> extends AbstractFlowableWithUpstream<T, R> {
    final ErrorMode errorMode;
    final Function<? super T, ? extends Publisher<? extends R>> mapper;
    final int maxConcurrency;
    final int prefetch;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableConcatMapEager$ConcatMapEagerDelayErrorSubscriber */
    static final class ConcatMapEagerDelayErrorSubscriber<T, R> extends AtomicInteger implements Subscriber<T>, Subscription, InnerQueuedSubscriberSupport<R> {
        private static final long serialVersionUID = -4255299542215038287L;
        final Subscriber<? super R> actual;
        volatile boolean cancelled;
        volatile InnerQueuedSubscriber<R> current;
        volatile boolean done;
        final ErrorMode errorMode;
        final AtomicThrowable errors = new AtomicThrowable();
        final Function<? super T, ? extends Publisher<? extends R>> mapper;
        final int maxConcurrency;
        final int prefetch;
        final AtomicLong requested = new AtomicLong();

        /* renamed from: s */
        Subscription f261s;
        final SpscLinkedArrayQueue<InnerQueuedSubscriber<R>> subscribers;

        ConcatMapEagerDelayErrorSubscriber(Subscriber<? super R> subscriber, Function<? super T, ? extends Publisher<? extends R>> function, int i, int i2, ErrorMode errorMode2) {
            this.actual = subscriber;
            this.mapper = function;
            this.maxConcurrency = i;
            this.prefetch = i2;
            this.errorMode = errorMode2;
            this.subscribers = new SpscLinkedArrayQueue<>(Math.min(i2, i));
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f261s, subscription)) {
                this.f261s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(this.maxConcurrency == Integer.MAX_VALUE ? LongCompanionObject.MAX_VALUE : (long) this.maxConcurrency);
            }
        }

        public void onNext(T t) {
            try {
                Publisher publisher = (Publisher) ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null Publisher");
                InnerQueuedSubscriber innerQueuedSubscriber = new InnerQueuedSubscriber(this, this.prefetch);
                if (!this.cancelled) {
                    this.subscribers.offer(innerQueuedSubscriber);
                    if (!this.cancelled) {
                        publisher.subscribe(innerQueuedSubscriber);
                        if (this.cancelled) {
                            innerQueuedSubscriber.cancel();
                            drainAndCancel();
                        }
                    }
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.f261s.cancel();
                onError(th);
            }
        }

        public void onError(Throwable th) {
            if (this.errors.addThrowable(th)) {
                this.done = true;
                drain();
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void onComplete() {
            this.done = true;
            drain();
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.f261s.cancel();
                drainAndCancel();
            }
        }

        /* access modifiers changed from: 0000 */
        public void drainAndCancel() {
            if (getAndIncrement() == 0) {
                do {
                    cancelAll();
                } while (decrementAndGet() != 0);
            }
        }

        /* access modifiers changed from: 0000 */
        public void cancelAll() {
            while (true) {
                InnerQueuedSubscriber innerQueuedSubscriber = (InnerQueuedSubscriber) this.subscribers.poll();
                if (innerQueuedSubscriber != null) {
                    innerQueuedSubscriber.cancel();
                } else {
                    return;
                }
            }
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this.requested, j);
                drain();
            }
        }

        public void innerNext(InnerQueuedSubscriber<R> innerQueuedSubscriber, R r) {
            if (innerQueuedSubscriber.queue().offer(r)) {
                drain();
                return;
            }
            innerQueuedSubscriber.cancel();
            innerError(innerQueuedSubscriber, new MissingBackpressureException());
        }

        public void innerError(InnerQueuedSubscriber<R> innerQueuedSubscriber, Throwable th) {
            if (this.errors.addThrowable(th)) {
                innerQueuedSubscriber.setDone();
                if (this.errorMode != ErrorMode.END) {
                    this.f261s.cancel();
                }
                drain();
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void innerComplete(InnerQueuedSubscriber<R> innerQueuedSubscriber) {
            innerQueuedSubscriber.setDone();
            drain();
        }

        /* JADX WARNING: Removed duplicated region for block: B:78:0x012f  */
        /* JADX WARNING: Removed duplicated region for block: B:79:0x0133  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drain() {
            /*
                r20 = this;
                r1 = r20
                int r2 = r20.getAndIncrement()
                if (r2 == 0) goto L_0x0009
                return
            L_0x0009:
                io.reactivex.internal.subscribers.InnerQueuedSubscriber<R> r2 = r1.current
                org.reactivestreams.Subscriber<? super R> r3 = r1.actual
                io.reactivex.internal.util.ErrorMode r4 = r1.errorMode
                r6 = 1
            L_0x0010:
                java.util.concurrent.atomic.AtomicLong r7 = r1.requested
                long r7 = r7.get()
                if (r2 != 0) goto L_0x0056
                io.reactivex.internal.util.ErrorMode r2 = p016io.reactivex.internal.util.ErrorMode.END
                if (r4 == r2) goto L_0x0033
                io.reactivex.internal.util.AtomicThrowable r2 = r1.errors
                java.lang.Object r2 = r2.get()
                java.lang.Throwable r2 = (java.lang.Throwable) r2
                if (r2 == 0) goto L_0x0033
                r20.cancelAll()
                io.reactivex.internal.util.AtomicThrowable r1 = r1.errors
                java.lang.Throwable r1 = r1.terminate()
                r3.onError(r1)
                return
            L_0x0033:
                boolean r2 = r1.done
                io.reactivex.internal.queue.SpscLinkedArrayQueue<io.reactivex.internal.subscribers.InnerQueuedSubscriber<R>> r9 = r1.subscribers
                java.lang.Object r9 = r9.poll()
                io.reactivex.internal.subscribers.InnerQueuedSubscriber r9 = (p016io.reactivex.internal.subscribers.InnerQueuedSubscriber) r9
                if (r2 == 0) goto L_0x0051
                if (r9 != 0) goto L_0x0051
                io.reactivex.internal.util.AtomicThrowable r1 = r1.errors
                java.lang.Throwable r1 = r1.terminate()
                if (r1 == 0) goto L_0x004d
                r3.onError(r1)
                goto L_0x0050
            L_0x004d:
                r3.onComplete()
            L_0x0050:
                return
            L_0x0051:
                if (r9 == 0) goto L_0x0055
                r1.current = r9
            L_0x0055:
                r2 = r9
            L_0x0056:
                r12 = 0
                if (r2 == 0) goto L_0x0112
                io.reactivex.internal.fuseable.SimpleQueue r13 = r2.queue()
                if (r13 == 0) goto L_0x0112
                r14 = 0
            L_0x0061:
                int r16 = (r14 > r7 ? 1 : (r14 == r7 ? 0 : -1))
                r17 = r6
                r5 = 1
                if (r16 == 0) goto L_0x00ca
                boolean r9 = r1.cancelled
                if (r9 == 0) goto L_0x0071
                r20.cancelAll()
                return
            L_0x0071:
                io.reactivex.internal.util.ErrorMode r9 = p016io.reactivex.internal.util.ErrorMode.IMMEDIATE
                if (r4 != r9) goto L_0x0091
                io.reactivex.internal.util.AtomicThrowable r9 = r1.errors
                java.lang.Object r9 = r9.get()
                java.lang.Throwable r9 = (java.lang.Throwable) r9
                if (r9 == 0) goto L_0x0091
                r1.current = r12
                r2.cancel()
                r20.cancelAll()
                io.reactivex.internal.util.AtomicThrowable r1 = r1.errors
                java.lang.Throwable r1 = r1.terminate()
                r3.onError(r1)
                return
            L_0x0091:
                boolean r9 = r2.isDone()
                java.lang.Object r10 = r13.poll()     // Catch:{ Throwable -> 0x00ba }
                if (r10 != 0) goto L_0x009d
                r11 = 1
                goto L_0x009e
            L_0x009d:
                r11 = 0
            L_0x009e:
                if (r9 == 0) goto L_0x00ad
                if (r11 == 0) goto L_0x00ad
                r1.current = r12
                org.reactivestreams.Subscription r2 = r1.f261s
                r2.request(r5)
                r2 = r12
                r18 = 1
                goto L_0x00cc
            L_0x00ad:
                if (r11 == 0) goto L_0x00b0
                goto L_0x00ca
            L_0x00b0:
                r3.onNext(r10)
                long r14 = r14 + r5
                r2.requestOne()
                r6 = r17
                goto L_0x0061
            L_0x00ba:
                r0 = move-exception
                p016io.reactivex.exceptions.Exceptions.throwIfFatal(r0)
                r1.current = r12
                r2.cancel()
                r20.cancelAll()
                r3.onError(r0)
                return
            L_0x00ca:
                r18 = 0
            L_0x00cc:
                if (r16 != 0) goto L_0x010f
                boolean r9 = r1.cancelled
                if (r9 == 0) goto L_0x00d6
                r20.cancelAll()
                return
            L_0x00d6:
                io.reactivex.internal.util.ErrorMode r9 = p016io.reactivex.internal.util.ErrorMode.IMMEDIATE
                if (r4 != r9) goto L_0x00f6
                io.reactivex.internal.util.AtomicThrowable r9 = r1.errors
                java.lang.Object r9 = r9.get()
                java.lang.Throwable r9 = (java.lang.Throwable) r9
                if (r9 == 0) goto L_0x00f6
                r1.current = r12
                r2.cancel()
                r20.cancelAll()
                io.reactivex.internal.util.AtomicThrowable r1 = r1.errors
                java.lang.Throwable r1 = r1.terminate()
                r3.onError(r1)
                return
            L_0x00f6:
                boolean r9 = r2.isDone()
                boolean r10 = r13.isEmpty()
                if (r9 == 0) goto L_0x010f
                if (r10 == 0) goto L_0x010f
                r1.current = r12
                org.reactivestreams.Subscription r2 = r1.f261s
                r2.request(r5)
                r2 = r12
                r5 = 0
                r18 = 1
                goto L_0x011a
            L_0x010f:
                r5 = 0
                goto L_0x011a
            L_0x0112:
                r17 = r6
                r5 = 0
                r14 = 0
                r18 = 0
            L_0x011a:
                int r5 = (r14 > r5 ? 1 : (r14 == r5 ? 0 : -1))
                if (r5 == 0) goto L_0x012d
                r5 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r5 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
                if (r5 == 0) goto L_0x012d
                java.util.concurrent.atomic.AtomicLong r5 = r1.requested
                long r6 = -r14
                r5.addAndGet(r6)
            L_0x012d:
                if (r18 == 0) goto L_0x0133
                r6 = r17
                goto L_0x0010
            L_0x0133:
                r5 = r17
                int r5 = -r5
                int r6 = r1.addAndGet(r5)
                if (r6 != 0) goto L_0x0010
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: p016io.reactivex.internal.operators.flowable.FlowableConcatMapEager.ConcatMapEagerDelayErrorSubscriber.drain():void");
        }
    }

    public FlowableConcatMapEager(Publisher<T> publisher, Function<? super T, ? extends Publisher<? extends R>> function, int i, int i2, ErrorMode errorMode2) {
        super(publisher);
        this.mapper = function;
        this.maxConcurrency = i;
        this.prefetch = i2;
        this.errorMode = errorMode2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super R> subscriber) {
        Publisher publisher = this.source;
        ConcatMapEagerDelayErrorSubscriber concatMapEagerDelayErrorSubscriber = new ConcatMapEagerDelayErrorSubscriber(subscriber, this.mapper, this.maxConcurrency, this.prefetch, this.errorMode);
        publisher.subscribe(concatMapEagerDelayErrorSubscriber);
    }
}
