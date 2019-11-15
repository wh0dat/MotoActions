package p016io.reactivex.internal.operators.flowable;

import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.exceptions.MissingBackpressureException;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.fuseable.QueueSubscription;
import p016io.reactivex.internal.fuseable.SimpleQueue;
import p016io.reactivex.internal.queue.SpscArrayQueue;
import p016io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
import p016io.reactivex.internal.subscriptions.EmptySubscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.BackpressureHelper;
import p016io.reactivex.internal.util.ExceptionHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableFlattenIterable */
public final class FlowableFlattenIterable<T, R> extends AbstractFlowableWithUpstream<T, R> {
    final Function<? super T, ? extends Iterable<? extends R>> mapper;
    final int prefetch;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableFlattenIterable$FlattenIterableSubscriber */
    static final class FlattenIterableSubscriber<T, R> extends BasicIntQueueSubscription<R> implements Subscriber<T> {
        private static final long serialVersionUID = -3096000382929934955L;
        final Subscriber<? super R> actual;
        volatile boolean cancelled;
        int consumed;
        Iterator<? extends R> current;
        volatile boolean done;
        final AtomicReference<Throwable> error = new AtomicReference<>();
        int fusionMode;
        final int limit;
        final Function<? super T, ? extends Iterable<? extends R>> mapper;
        final int prefetch;
        SimpleQueue<T> queue;
        final AtomicLong requested = new AtomicLong();

        /* renamed from: s */
        Subscription f284s;

        FlattenIterableSubscriber(Subscriber<? super R> subscriber, Function<? super T, ? extends Iterable<? extends R>> function, int i) {
            this.actual = subscriber;
            this.mapper = function;
            this.prefetch = i;
            this.limit = i - (i >> 2);
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f284s, subscription)) {
                this.f284s = subscription;
                if (subscription instanceof QueueSubscription) {
                    QueueSubscription queueSubscription = (QueueSubscription) subscription;
                    int requestFusion = queueSubscription.requestFusion(3);
                    if (requestFusion == 1) {
                        this.fusionMode = requestFusion;
                        this.queue = queueSubscription;
                        this.done = true;
                        this.actual.onSubscribe(this);
                        return;
                    } else if (requestFusion == 2) {
                        this.fusionMode = requestFusion;
                        this.queue = queueSubscription;
                        this.actual.onSubscribe(this);
                        subscription.request((long) this.prefetch);
                        return;
                    }
                }
                this.queue = new SpscArrayQueue(this.prefetch);
                this.actual.onSubscribe(this);
                subscription.request((long) this.prefetch);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                if (this.fusionMode != 0 || this.queue.offer(t)) {
                    drain();
                } else {
                    onError(new MissingBackpressureException("Queue is full?!"));
                }
            }
        }

        public void onError(Throwable th) {
            if (this.done || !ExceptionHelper.addThrowable(this.error, th)) {
                RxJavaPlugins.onError(th);
                return;
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
                drain();
            }
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.f284s.cancel();
                if (getAndIncrement() == 0) {
                    this.queue.clear();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:68:0x0118, code lost:
            if (r7 == null) goto L_0x0123;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drain() {
            /*
                r20 = this;
                r1 = r20
                int r2 = r20.getAndIncrement()
                if (r2 == 0) goto L_0x0009
                return
            L_0x0009:
                org.reactivestreams.Subscriber<? super R> r2 = r1.actual
                io.reactivex.internal.fuseable.SimpleQueue<T> r3 = r1.queue
                int r4 = r1.fusionMode
                r5 = 0
                r6 = 1
                if (r4 == r6) goto L_0x0015
                r4 = r6
                goto L_0x0016
            L_0x0015:
                r4 = r5
            L_0x0016:
                java.util.Iterator<? extends R> r7 = r1.current
                r8 = 0
                r9 = r6
            L_0x001a:
                if (r7 != 0) goto L_0x0080
                boolean r10 = r1.done
                java.lang.Object r11 = r3.poll()     // Catch:{ Throwable -> 0x0063 }
                if (r11 != 0) goto L_0x0026
                r12 = r6
                goto L_0x0027
            L_0x0026:
                r12 = r5
            L_0x0027:
                boolean r10 = r1.checkTerminated(r10, r12, r2, r3)
                if (r10 == 0) goto L_0x002e
                return
            L_0x002e:
                if (r11 == 0) goto L_0x0080
                io.reactivex.functions.Function<? super T, ? extends java.lang.Iterable<? extends R>> r7 = r1.mapper     // Catch:{ Throwable -> 0x004a }
                java.lang.Object r7 = r7.apply(r11)     // Catch:{ Throwable -> 0x004a }
                java.lang.Iterable r7 = (java.lang.Iterable) r7     // Catch:{ Throwable -> 0x004a }
                java.util.Iterator r7 = r7.iterator()     // Catch:{ Throwable -> 0x004a }
                boolean r10 = r7.hasNext()     // Catch:{ Throwable -> 0x004a }
                if (r10 != 0) goto L_0x0047
                r1.consumedOne(r4)
                r7 = r8
                goto L_0x001a
            L_0x0047:
                r1.current = r7
                goto L_0x0080
            L_0x004a:
                r0 = move-exception
                r3 = r0
                p016io.reactivex.exceptions.Exceptions.throwIfFatal(r3)
                org.reactivestreams.Subscription r4 = r1.f284s
                r4.cancel()
                java.util.concurrent.atomic.AtomicReference<java.lang.Throwable> r4 = r1.error
                p016io.reactivex.internal.util.ExceptionHelper.addThrowable(r4, r3)
                java.util.concurrent.atomic.AtomicReference<java.lang.Throwable> r1 = r1.error
                java.lang.Throwable r1 = p016io.reactivex.internal.util.ExceptionHelper.terminate(r1)
                r2.onError(r1)
                return
            L_0x0063:
                r0 = move-exception
                p016io.reactivex.exceptions.Exceptions.throwIfFatal(r0)
                org.reactivestreams.Subscription r4 = r1.f284s
                r4.cancel()
                java.util.concurrent.atomic.AtomicReference<java.lang.Throwable> r4 = r1.error
                p016io.reactivex.internal.util.ExceptionHelper.addThrowable(r4, r0)
                java.util.concurrent.atomic.AtomicReference<java.lang.Throwable> r4 = r1.error
                java.lang.Throwable r4 = p016io.reactivex.internal.util.ExceptionHelper.terminate(r4)
                r1.current = r8
                r3.clear()
                r2.onError(r4)
                return
            L_0x0080:
                if (r7 == 0) goto L_0x011b
                java.util.concurrent.atomic.AtomicLong r10 = r1.requested
                long r10 = r10.get()
                r12 = 0
                r14 = r12
            L_0x008b:
                int r16 = (r14 > r10 ? 1 : (r14 == r10 ? 0 : -1))
                if (r16 == 0) goto L_0x00ef
                boolean r6 = r1.done
                boolean r6 = r1.checkTerminated(r6, r5, r2, r3)
                if (r6 == 0) goto L_0x0098
                return
            L_0x0098:
                java.lang.Object r6 = r7.next()     // Catch:{ Throwable -> 0x00d5 }
                r2.onNext(r6)
                boolean r6 = r1.done
                boolean r6 = r1.checkTerminated(r6, r5, r2, r3)
                if (r6 == 0) goto L_0x00a8
                return
            L_0x00a8:
                r17 = 1
                long r14 = r14 + r17
                boolean r6 = r7.hasNext()     // Catch:{ Throwable -> 0x00bb }
                if (r6 != 0) goto L_0x00b9
                r1.consumedOne(r4)
                r1.current = r8
                r7 = r8
                goto L_0x00ef
            L_0x00b9:
                r6 = 1
                goto L_0x008b
            L_0x00bb:
                r0 = move-exception
                p016io.reactivex.exceptions.Exceptions.throwIfFatal(r0)
                r1.current = r8
                org.reactivestreams.Subscription r3 = r1.f284s
                r3.cancel()
                java.util.concurrent.atomic.AtomicReference<java.lang.Throwable> r3 = r1.error
                p016io.reactivex.internal.util.ExceptionHelper.addThrowable(r3, r0)
                java.util.concurrent.atomic.AtomicReference<java.lang.Throwable> r1 = r1.error
                java.lang.Throwable r1 = p016io.reactivex.internal.util.ExceptionHelper.terminate(r1)
                r2.onError(r1)
                return
            L_0x00d5:
                r0 = move-exception
                p016io.reactivex.exceptions.Exceptions.throwIfFatal(r0)
                r1.current = r8
                org.reactivestreams.Subscription r3 = r1.f284s
                r3.cancel()
                java.util.concurrent.atomic.AtomicReference<java.lang.Throwable> r3 = r1.error
                p016io.reactivex.internal.util.ExceptionHelper.addThrowable(r3, r0)
                java.util.concurrent.atomic.AtomicReference<java.lang.Throwable> r1 = r1.error
                java.lang.Throwable r1 = p016io.reactivex.internal.util.ExceptionHelper.terminate(r1)
                r2.onError(r1)
                return
            L_0x00ef:
                int r6 = (r14 > r10 ? 1 : (r14 == r10 ? 0 : -1))
                if (r6 != 0) goto L_0x0105
                boolean r6 = r1.done
                boolean r16 = r3.isEmpty()
                if (r16 == 0) goto L_0x00fe
                if (r7 != 0) goto L_0x00fe
                r5 = 1
            L_0x00fe:
                boolean r5 = r1.checkTerminated(r6, r5, r2, r3)
                if (r5 == 0) goto L_0x0105
                return
            L_0x0105:
                int r5 = (r14 > r12 ? 1 : (r14 == r12 ? 0 : -1))
                if (r5 == 0) goto L_0x0118
                r5 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r5 = (r10 > r5 ? 1 : (r10 == r5 ? 0 : -1))
                if (r5 == 0) goto L_0x0118
                java.util.concurrent.atomic.AtomicLong r5 = r1.requested
                long r10 = -r14
                r5.addAndGet(r10)
            L_0x0118:
                if (r7 != 0) goto L_0x011b
                goto L_0x0123
            L_0x011b:
                int r5 = -r9
                int r9 = r1.addAndGet(r5)
                if (r9 != 0) goto L_0x0123
                return
            L_0x0123:
                r5 = 0
                r6 = 1
                goto L_0x001a
            */
            throw new UnsupportedOperationException("Method not decompiled: p016io.reactivex.internal.operators.flowable.FlowableFlattenIterable.FlattenIterableSubscriber.drain():void");
        }

        /* access modifiers changed from: 0000 */
        public void consumedOne(boolean z) {
            if (z) {
                int i = this.consumed + 1;
                if (i == this.limit) {
                    this.consumed = 0;
                    this.f284s.request((long) i);
                    return;
                }
                this.consumed = i;
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminated(boolean z, boolean z2, Subscriber<?> subscriber, SimpleQueue<?> simpleQueue) {
            if (this.cancelled) {
                this.current = null;
                simpleQueue.clear();
                return true;
            }
            if (z) {
                if (((Throwable) this.error.get()) != null) {
                    Throwable terminate = ExceptionHelper.terminate(this.error);
                    this.current = null;
                    simpleQueue.clear();
                    subscriber.onError(terminate);
                    return true;
                } else if (z2) {
                    subscriber.onComplete();
                    return true;
                }
            }
            return false;
        }

        public void clear() {
            this.current = null;
            this.queue.clear();
        }

        public boolean isEmpty() {
            Iterator<? extends R> it = this.current;
            return (it != null && !it.hasNext()) || this.queue.isEmpty();
        }

        public R poll() throws Exception {
            Iterator<? extends R> it = this.current;
            while (true) {
                if (it != null) {
                    break;
                }
                Object poll = this.queue.poll();
                if (poll != null) {
                    it = ((Iterable) this.mapper.apply(poll)).iterator();
                    if (it.hasNext()) {
                        this.current = it;
                        break;
                    }
                    it = null;
                } else {
                    return null;
                }
            }
            R next = it.next();
            if (!it.hasNext()) {
                this.current = null;
            }
            return next;
        }

        public int requestFusion(int i) {
            return ((i & 1) == 0 || this.fusionMode != 1) ? 0 : 1;
        }
    }

    public FlowableFlattenIterable(Publisher<T> publisher, Function<? super T, ? extends Iterable<? extends R>> function, int i) {
        super(publisher);
        this.mapper = function;
        this.prefetch = i;
    }

    public void subscribeActual(Subscriber<? super R> subscriber) {
        if (this.source instanceof Callable) {
            try {
                Object call = ((Callable) this.source).call();
                if (call == null) {
                    EmptySubscription.complete(subscriber);
                    return;
                }
                try {
                    FlowableFromIterable.subscribe(subscriber, ((Iterable) this.mapper.apply(call)).iterator());
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    EmptySubscription.error(th, subscriber);
                }
            } catch (Throwable th2) {
                Exceptions.throwIfFatal(th2);
                EmptySubscription.error(th2, subscriber);
            }
        } else {
            this.source.subscribe(new FlattenIterableSubscriber(subscriber, this.mapper, this.prefetch));
        }
    }
}
