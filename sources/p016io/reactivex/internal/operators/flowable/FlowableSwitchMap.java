package p016io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.exceptions.MissingBackpressureException;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.fuseable.QueueSubscription;
import p016io.reactivex.internal.fuseable.SimpleQueue;
import p016io.reactivex.internal.queue.SpscArrayQueue;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.AtomicThrowable;
import p016io.reactivex.internal.util.BackpressureHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableSwitchMap */
public final class FlowableSwitchMap<T, R> extends AbstractFlowableWithUpstream<T, R> {
    final int bufferSize;
    final boolean delayErrors;
    final Function<? super T, ? extends Publisher<? extends R>> mapper;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableSwitchMap$SwitchMapInnerSubscriber */
    static final class SwitchMapInnerSubscriber<T, R> extends AtomicReference<Subscription> implements Subscriber<R> {
        private static final long serialVersionUID = 3837284832786408377L;
        final int bufferSize;
        volatile boolean done;
        int fusionMode;
        final long index;
        final SwitchMapSubscriber<T, R> parent;
        volatile SimpleQueue<R> queue;

        SwitchMapInnerSubscriber(SwitchMapSubscriber<T, R> switchMapSubscriber, long j, int i) {
            this.parent = switchMapSubscriber;
            this.index = j;
            this.bufferSize = i;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.setOnce(this, subscription)) {
                if (subscription instanceof QueueSubscription) {
                    QueueSubscription queueSubscription = (QueueSubscription) subscription;
                    int requestFusion = queueSubscription.requestFusion(3);
                    if (requestFusion == 1) {
                        this.fusionMode = requestFusion;
                        this.queue = queueSubscription;
                        this.done = true;
                        this.parent.drain();
                        return;
                    } else if (requestFusion == 2) {
                        this.fusionMode = requestFusion;
                        this.queue = queueSubscription;
                        subscription.request((long) this.bufferSize);
                        return;
                    }
                }
                this.queue = new SpscArrayQueue(this.bufferSize);
                subscription.request((long) this.bufferSize);
            }
        }

        public void onNext(R r) {
            SwitchMapSubscriber<T, R> switchMapSubscriber = this.parent;
            if (this.index == switchMapSubscriber.unique) {
                if (this.fusionMode != 0 || this.queue.offer(r)) {
                    switchMapSubscriber.drain();
                } else {
                    onError(new MissingBackpressureException("Queue full?!"));
                }
            }
        }

        public void onError(Throwable th) {
            SwitchMapSubscriber<T, R> switchMapSubscriber = this.parent;
            if (this.index != switchMapSubscriber.unique || !switchMapSubscriber.error.addThrowable(th)) {
                RxJavaPlugins.onError(th);
                return;
            }
            if (!switchMapSubscriber.delayErrors) {
                switchMapSubscriber.f327s.cancel();
            }
            this.done = true;
            switchMapSubscriber.drain();
        }

        public void onComplete() {
            SwitchMapSubscriber<T, R> switchMapSubscriber = this.parent;
            if (this.index == switchMapSubscriber.unique) {
                this.done = true;
                switchMapSubscriber.drain();
            }
        }

        public void cancel() {
            SubscriptionHelper.cancel(this);
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableSwitchMap$SwitchMapSubscriber */
    static final class SwitchMapSubscriber<T, R> extends AtomicInteger implements Subscriber<T>, Subscription {
        static final SwitchMapInnerSubscriber<Object, Object> CANCELLED = new SwitchMapInnerSubscriber<>(null, -1, 1);
        private static final long serialVersionUID = -3491074160481096299L;
        final AtomicReference<SwitchMapInnerSubscriber<T, R>> active = new AtomicReference<>();
        final Subscriber<? super R> actual;
        final int bufferSize;
        volatile boolean cancelled;
        final boolean delayErrors;
        volatile boolean done;
        final AtomicThrowable error;
        final Function<? super T, ? extends Publisher<? extends R>> mapper;
        final AtomicLong requested = new AtomicLong();

        /* renamed from: s */
        Subscription f327s;
        volatile long unique;

        static {
            CANCELLED.cancel();
        }

        SwitchMapSubscriber(Subscriber<? super R> subscriber, Function<? super T, ? extends Publisher<? extends R>> function, int i, boolean z) {
            this.actual = subscriber;
            this.mapper = function;
            this.bufferSize = i;
            this.delayErrors = z;
            this.error = new AtomicThrowable();
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f327s, subscription)) {
                this.f327s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                long j = this.unique + 1;
                this.unique = j;
                SwitchMapInnerSubscriber switchMapInnerSubscriber = (SwitchMapInnerSubscriber) this.active.get();
                if (switchMapInnerSubscriber != null) {
                    switchMapInnerSubscriber.cancel();
                }
                try {
                    Publisher publisher = (Publisher) ObjectHelper.requireNonNull(this.mapper.apply(t), "The publisher returned is null");
                    SwitchMapInnerSubscriber switchMapInnerSubscriber2 = new SwitchMapInnerSubscriber(this, j, this.bufferSize);
                    while (true) {
                        SwitchMapInnerSubscriber<Object, Object> switchMapInnerSubscriber3 = (SwitchMapInnerSubscriber) this.active.get();
                        if (switchMapInnerSubscriber3 != CANCELLED) {
                            if (this.active.compareAndSet(switchMapInnerSubscriber3, switchMapInnerSubscriber2)) {
                                publisher.subscribe(switchMapInnerSubscriber2);
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.f327s.cancel();
                    onError(th);
                }
            }
        }

        public void onError(Throwable th) {
            if (this.done || !this.error.addThrowable(th)) {
                RxJavaPlugins.onError(th);
                return;
            }
            if (!this.delayErrors) {
                disposeInner();
            }
            this.done = true;
            drain();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                drain();
            }
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this.requested, j);
                if (this.unique == 0) {
                    this.f327s.request(LongCompanionObject.MAX_VALUE);
                } else {
                    drain();
                }
            }
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.f327s.cancel();
                disposeInner();
            }
        }

        /* access modifiers changed from: 0000 */
        public void disposeInner() {
            if (((SwitchMapInnerSubscriber) this.active.get()) != CANCELLED) {
                SwitchMapInnerSubscriber<Object, Object> switchMapInnerSubscriber = (SwitchMapInnerSubscriber) this.active.getAndSet(CANCELLED);
                if (switchMapInnerSubscriber != CANCELLED && switchMapInnerSubscriber != null) {
                    switchMapInnerSubscriber.cancel();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:63:0x00e9, code lost:
            r15 = true;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drain() {
            /*
                r19 = this;
                r1 = r19
                int r2 = r19.getAndIncrement()
                if (r2 == 0) goto L_0x0009
                return
            L_0x0009:
                org.reactivestreams.Subscriber<? super R> r2 = r1.actual
                r4 = 1
            L_0x000c:
                boolean r5 = r1.cancelled
                r6 = 0
                if (r5 == 0) goto L_0x0017
                java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.flowable.FlowableSwitchMap$SwitchMapInnerSubscriber<T, R>> r1 = r1.active
                r1.lazySet(r6)
                return
            L_0x0017:
                boolean r5 = r1.done
                if (r5 == 0) goto L_0x0062
                boolean r5 = r1.delayErrors
                if (r5 == 0) goto L_0x003f
                java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.flowable.FlowableSwitchMap$SwitchMapInnerSubscriber<T, R>> r5 = r1.active
                java.lang.Object r5 = r5.get()
                if (r5 != 0) goto L_0x0062
                io.reactivex.internal.util.AtomicThrowable r3 = r1.error
                java.lang.Object r3 = r3.get()
                java.lang.Throwable r3 = (java.lang.Throwable) r3
                if (r3 == 0) goto L_0x003b
                io.reactivex.internal.util.AtomicThrowable r1 = r1.error
                java.lang.Throwable r1 = r1.terminate()
                r2.onError(r1)
                goto L_0x003e
            L_0x003b:
                r2.onComplete()
            L_0x003e:
                return
            L_0x003f:
                io.reactivex.internal.util.AtomicThrowable r5 = r1.error
                java.lang.Object r5 = r5.get()
                java.lang.Throwable r5 = (java.lang.Throwable) r5
                if (r5 == 0) goto L_0x0056
                r19.disposeInner()
                io.reactivex.internal.util.AtomicThrowable r1 = r1.error
                java.lang.Throwable r1 = r1.terminate()
                r2.onError(r1)
                return
            L_0x0056:
                java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.flowable.FlowableSwitchMap$SwitchMapInnerSubscriber<T, R>> r5 = r1.active
                java.lang.Object r5 = r5.get()
                if (r5 != 0) goto L_0x0062
                r2.onComplete()
                return
            L_0x0062:
                java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.flowable.FlowableSwitchMap$SwitchMapInnerSubscriber<T, R>> r5 = r1.active
                java.lang.Object r5 = r5.get()
                io.reactivex.internal.operators.flowable.FlowableSwitchMap$SwitchMapInnerSubscriber r5 = (p016io.reactivex.internal.operators.flowable.FlowableSwitchMap.SwitchMapInnerSubscriber) r5
                if (r5 == 0) goto L_0x006f
                io.reactivex.internal.fuseable.SimpleQueue<R> r7 = r5.queue
                goto L_0x0070
            L_0x006f:
                r7 = r6
            L_0x0070:
                if (r7 == 0) goto L_0x0143
                boolean r8 = r5.done
                if (r8 == 0) goto L_0x00ab
                boolean r8 = r1.delayErrors
                if (r8 != 0) goto L_0x009e
                io.reactivex.internal.util.AtomicThrowable r8 = r1.error
                java.lang.Object r8 = r8.get()
                java.lang.Throwable r8 = (java.lang.Throwable) r8
                if (r8 == 0) goto L_0x0091
                r19.disposeInner()
                io.reactivex.internal.util.AtomicThrowable r1 = r1.error
                java.lang.Throwable r1 = r1.terminate()
                r2.onError(r1)
                return
            L_0x0091:
                boolean r8 = r7.isEmpty()
                if (r8 == 0) goto L_0x00ab
                java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.flowable.FlowableSwitchMap$SwitchMapInnerSubscriber<T, R>> r7 = r1.active
                r7.compareAndSet(r5, r6)
                goto L_0x000c
            L_0x009e:
                boolean r8 = r7.isEmpty()
                if (r8 == 0) goto L_0x00ab
                java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.flowable.FlowableSwitchMap$SwitchMapInnerSubscriber<T, R>> r7 = r1.active
                r7.compareAndSet(r5, r6)
                goto L_0x000c
            L_0x00ab:
                java.util.concurrent.atomic.AtomicLong r8 = r1.requested
                long r8 = r8.get()
                r10 = 0
                r12 = r10
            L_0x00b4:
                int r14 = (r12 > r8 ? 1 : (r12 == r8 ? 0 : -1))
                r15 = 0
                if (r14 == 0) goto L_0x011f
                boolean r14 = r1.cancelled
                if (r14 == 0) goto L_0x00be
                return
            L_0x00be:
                boolean r14 = r5.done
                java.lang.Object r16 = r7.poll()     // Catch:{ Throwable -> 0x00cb }
                r18 = r16
                r16 = r14
                r14 = r18
                goto L_0x00da
            L_0x00cb:
                r0 = move-exception
                p016io.reactivex.exceptions.Exceptions.throwIfFatal(r0)
                r5.cancel()
                io.reactivex.internal.util.AtomicThrowable r14 = r1.error
                r14.addThrowable(r0)
                r14 = r6
                r16 = 1
            L_0x00da:
                if (r14 != 0) goto L_0x00df
                r17 = 1
                goto L_0x00e1
            L_0x00df:
                r17 = r15
            L_0x00e1:
                java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.flowable.FlowableSwitchMap$SwitchMapInnerSubscriber<T, R>> r3 = r1.active
                java.lang.Object r3 = r3.get()
                if (r5 == r3) goto L_0x00eb
            L_0x00e9:
                r15 = 1
                goto L_0x011f
            L_0x00eb:
                if (r16 == 0) goto L_0x0115
                boolean r3 = r1.delayErrors
                if (r3 != 0) goto L_0x010d
                io.reactivex.internal.util.AtomicThrowable r3 = r1.error
                java.lang.Object r3 = r3.get()
                java.lang.Throwable r3 = (java.lang.Throwable) r3
                if (r3 == 0) goto L_0x0105
                io.reactivex.internal.util.AtomicThrowable r1 = r1.error
                java.lang.Throwable r1 = r1.terminate()
                r2.onError(r1)
                return
            L_0x0105:
                if (r17 == 0) goto L_0x0115
                java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.flowable.FlowableSwitchMap$SwitchMapInnerSubscriber<T, R>> r3 = r1.active
                r3.compareAndSet(r5, r6)
                goto L_0x00e9
            L_0x010d:
                if (r17 == 0) goto L_0x0115
                java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.flowable.FlowableSwitchMap$SwitchMapInnerSubscriber<T, R>> r3 = r1.active
                r3.compareAndSet(r5, r6)
                goto L_0x00e9
            L_0x0115:
                if (r17 == 0) goto L_0x0118
                goto L_0x011f
            L_0x0118:
                r2.onNext(r14)
                r14 = 1
                long r12 = r12 + r14
                goto L_0x00b4
            L_0x011f:
                int r3 = (r12 > r10 ? 1 : (r12 == r10 ? 0 : -1))
                if (r3 == 0) goto L_0x013f
                boolean r3 = r1.cancelled
                if (r3 != 0) goto L_0x013f
                r6 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r3 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1))
                if (r3 == 0) goto L_0x0136
                java.util.concurrent.atomic.AtomicLong r3 = r1.requested
                long r6 = -r12
                r3.addAndGet(r6)
            L_0x0136:
                java.lang.Object r3 = r5.get()
                org.reactivestreams.Subscription r3 = (org.reactivestreams.Subscription) r3
                r3.request(r12)
            L_0x013f:
                if (r15 == 0) goto L_0x0143
                goto L_0x000c
            L_0x0143:
                int r3 = -r4
                int r4 = r1.addAndGet(r3)
                if (r4 != 0) goto L_0x000c
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: p016io.reactivex.internal.operators.flowable.FlowableSwitchMap.SwitchMapSubscriber.drain():void");
        }
    }

    public FlowableSwitchMap(Publisher<T> publisher, Function<? super T, ? extends Publisher<? extends R>> function, int i, boolean z) {
        super(publisher);
        this.mapper = function;
        this.bufferSize = i;
        this.delayErrors = z;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super R> subscriber) {
        if (!FlowableScalarXMap.tryScalarXMapSubscribe(this.source, subscriber, this.mapper)) {
            this.source.subscribe(new SwitchMapSubscriber(subscriber, this.mapper, this.bufferSize, this.delayErrors));
        }
    }
}
