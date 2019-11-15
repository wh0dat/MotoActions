package p016io.reactivex.internal.operators.flowable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Scheduler;
import p016io.reactivex.Scheduler.Worker;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.queue.MpscLinkedQueue;
import p016io.reactivex.internal.subscribers.QueueDrainSubscriber;
import p016io.reactivex.internal.subscriptions.EmptySubscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.QueueDrainHelper;
import p016io.reactivex.subscribers.SerializedSubscriber;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableBufferTimed */
public final class FlowableBufferTimed<T, U extends Collection<? super T>> extends AbstractFlowableWithUpstream<T, U> {
    final Callable<U> bufferSupplier;
    final int maxSize;
    final boolean restartTimerOnMaxSize;
    final Scheduler scheduler;
    final long timeskip;
    final long timespan;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableBufferTimed$BufferExactBoundedSubscriber */
    static final class BufferExactBoundedSubscriber<T, U extends Collection<? super T>> extends QueueDrainSubscriber<T, U, U> implements Subscription, Runnable, Disposable {
        U buffer;
        final Callable<U> bufferSupplier;
        long consumerIndex;
        final int maxSize;
        long producerIndex;
        final boolean restartTimerOnMaxSize;

        /* renamed from: s */
        Subscription f251s;
        Disposable timer;
        final long timespan;
        final TimeUnit unit;

        /* renamed from: w */
        final Worker f252w;

        BufferExactBoundedSubscriber(Subscriber<? super U> subscriber, Callable<U> callable, long j, TimeUnit timeUnit, int i, boolean z, Worker worker) {
            super(subscriber, new MpscLinkedQueue());
            this.bufferSupplier = callable;
            this.timespan = j;
            this.unit = timeUnit;
            this.maxSize = i;
            this.restartTimerOnMaxSize = z;
            this.f252w = worker;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f251s, subscription)) {
                this.f251s = subscription;
                try {
                    this.buffer = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The supplied buffer is null");
                    this.actual.onSubscribe(this);
                    this.timer = this.f252w.schedulePeriodically(this, this.timespan, this.timespan, this.unit);
                    subscription.request(LongCompanionObject.MAX_VALUE);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.f252w.dispose();
                    subscription.cancel();
                    EmptySubscription.error(th, this.actual);
                }
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0019, code lost:
            if (r12.restartTimerOnMaxSize == false) goto L_0x0028;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x001b, code lost:
            r12.buffer = null;
            r12.producerIndex++;
            r12.timer.dispose();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0028, code lost:
            fastPathOrderedEmitMax(r0, false, r12);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
            r13 = (java.util.Collection) p016io.reactivex.internal.functions.ObjectHelper.requireNonNull(r12.bufferSupplier.call(), "The supplied buffer is null");
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x003c, code lost:
            if (r12.restartTimerOnMaxSize == false) goto L_0x005a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x003e, code lost:
            monitor-enter(r12);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
            r12.buffer = r13;
            r12.consumerIndex++;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0046, code lost:
            monitor-exit(r12);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0047, code lost:
            r12.timer = r12.f252w.schedulePeriodically(r12, r12.timespan, r12.timespan, r12.unit);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x005a, code lost:
            monitor-enter(r12);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:?, code lost:
            r12.buffer = r13;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x005d, code lost:
            monitor-exit(r12);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x005e, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x0062, code lost:
            r13 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x0063, code lost:
            p016io.reactivex.exceptions.Exceptions.throwIfFatal(r13);
            cancel();
            r12.actual.onError(r13);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x006e, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onNext(T r13) {
            /*
                r12 = this;
                monitor-enter(r12)
                U r0 = r12.buffer     // Catch:{ all -> 0x006f }
                if (r0 != 0) goto L_0x0007
                monitor-exit(r12)     // Catch:{ all -> 0x006f }
                return
            L_0x0007:
                r0.add(r13)     // Catch:{ all -> 0x006f }
                int r13 = r0.size()     // Catch:{ all -> 0x006f }
                int r1 = r12.maxSize     // Catch:{ all -> 0x006f }
                if (r13 >= r1) goto L_0x0014
                monitor-exit(r12)     // Catch:{ all -> 0x006f }
                return
            L_0x0014:
                monitor-exit(r12)     // Catch:{ all -> 0x006f }
                boolean r13 = r12.restartTimerOnMaxSize
                r1 = 1
                if (r13 == 0) goto L_0x0028
                r13 = 0
                r12.buffer = r13
                long r3 = r12.producerIndex
                long r3 = r3 + r1
                r12.producerIndex = r3
                io.reactivex.disposables.Disposable r13 = r12.timer
                r13.dispose()
            L_0x0028:
                r13 = 0
                r12.fastPathOrderedEmitMax(r0, r13, r12)
                java.util.concurrent.Callable<U> r13 = r12.bufferSupplier     // Catch:{ Throwable -> 0x0062 }
                java.lang.Object r13 = r13.call()     // Catch:{ Throwable -> 0x0062 }
                java.lang.String r0 = "The supplied buffer is null"
                java.lang.Object r13 = p016io.reactivex.internal.functions.ObjectHelper.requireNonNull(r13, r0)     // Catch:{ Throwable -> 0x0062 }
                java.util.Collection r13 = (java.util.Collection) r13     // Catch:{ Throwable -> 0x0062 }
                boolean r0 = r12.restartTimerOnMaxSize
                if (r0 == 0) goto L_0x005a
                monitor-enter(r12)
                r12.buffer = r13     // Catch:{ all -> 0x0057 }
                long r3 = r12.consumerIndex     // Catch:{ all -> 0x0057 }
                long r3 = r3 + r1
                r12.consumerIndex = r3     // Catch:{ all -> 0x0057 }
                monitor-exit(r12)     // Catch:{ all -> 0x0057 }
                io.reactivex.Scheduler$Worker r5 = r12.f252w
                long r7 = r12.timespan
                long r9 = r12.timespan
                java.util.concurrent.TimeUnit r11 = r12.unit
                r6 = r12
                io.reactivex.disposables.Disposable r13 = r5.schedulePeriodically(r6, r7, r9, r11)
                r12.timer = r13
                goto L_0x005e
            L_0x0057:
                r13 = move-exception
                monitor-exit(r12)     // Catch:{ all -> 0x0057 }
                throw r13
            L_0x005a:
                monitor-enter(r12)
                r12.buffer = r13     // Catch:{ all -> 0x005f }
                monitor-exit(r12)     // Catch:{ all -> 0x005f }
            L_0x005e:
                return
            L_0x005f:
                r13 = move-exception
                monitor-exit(r12)     // Catch:{ all -> 0x005f }
                throw r13
            L_0x0062:
                r13 = move-exception
                p016io.reactivex.exceptions.Exceptions.throwIfFatal(r13)
                r12.cancel()
                org.reactivestreams.Subscriber r12 = r12.actual
                r12.onError(r13)
                return
            L_0x006f:
                r13 = move-exception
                monitor-exit(r12)     // Catch:{ all -> 0x006f }
                throw r13
            */
            throw new UnsupportedOperationException("Method not decompiled: p016io.reactivex.internal.operators.flowable.FlowableBufferTimed.BufferExactBoundedSubscriber.onNext(java.lang.Object):void");
        }

        public void onError(Throwable th) {
            this.f252w.dispose();
            synchronized (this) {
                this.buffer = null;
            }
            this.actual.onError(th);
        }

        public void onComplete() {
            U u;
            this.f252w.dispose();
            synchronized (this) {
                u = this.buffer;
                this.buffer = null;
            }
            this.queue.offer(u);
            this.done = true;
            if (enter()) {
                QueueDrainHelper.drainMaxLoop(this.queue, this.actual, false, this, this);
            }
        }

        public boolean accept(Subscriber<? super U> subscriber, U u) {
            subscriber.onNext(u);
            return true;
        }

        public void request(long j) {
            requested(j);
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                dispose();
            }
        }

        public void dispose() {
            this.f252w.dispose();
            synchronized (this) {
                this.buffer = null;
            }
            this.f251s.cancel();
        }

        public boolean isDisposed() {
            return this.f252w.isDisposed();
        }

        public void run() {
            try {
                U u = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The supplied buffer is null");
                synchronized (this) {
                    U u2 = this.buffer;
                    if (u2 != null) {
                        if (this.producerIndex == this.consumerIndex) {
                            this.buffer = u;
                            fastPathOrderedEmitMax(u2, false, this);
                        }
                    }
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                cancel();
                this.actual.onError(th);
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableBufferTimed$BufferExactUnboundedSubscriber */
    static final class BufferExactUnboundedSubscriber<T, U extends Collection<? super T>> extends QueueDrainSubscriber<T, U, U> implements Subscription, Runnable, Disposable {
        U buffer;
        final Callable<U> bufferSupplier;

        /* renamed from: s */
        Subscription f253s;
        final Scheduler scheduler;
        final AtomicReference<Disposable> timer = new AtomicReference<>();
        final long timespan;
        final TimeUnit unit;

        BufferExactUnboundedSubscriber(Subscriber<? super U> subscriber, Callable<U> callable, long j, TimeUnit timeUnit, Scheduler scheduler2) {
            super(subscriber, new MpscLinkedQueue());
            this.bufferSupplier = callable;
            this.timespan = j;
            this.unit = timeUnit;
            this.scheduler = scheduler2;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f253s, subscription)) {
                this.f253s = subscription;
                try {
                    this.buffer = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The supplied buffer is null");
                    this.actual.onSubscribe(this);
                    if (!this.cancelled) {
                        subscription.request(LongCompanionObject.MAX_VALUE);
                        Disposable schedulePeriodicallyDirect = this.scheduler.schedulePeriodicallyDirect(this, this.timespan, this.timespan, this.unit);
                        if (!this.timer.compareAndSet(null, schedulePeriodicallyDirect)) {
                            schedulePeriodicallyDirect.dispose();
                        }
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    cancel();
                    EmptySubscription.error(th, this.actual);
                }
            }
        }

        public void onNext(T t) {
            synchronized (this) {
                U u = this.buffer;
                if (u != null) {
                    u.add(t);
                }
            }
        }

        public void onError(Throwable th) {
            DisposableHelper.dispose(this.timer);
            synchronized (this) {
                this.buffer = null;
            }
            this.actual.onError(th);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x001c, code lost:
            if (enter() == false) goto L_0x0026;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x001e, code lost:
            p016io.reactivex.internal.util.QueueDrainHelper.drainMaxLoop(r3.queue, r3.actual, false, r3, r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0026, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x0010, code lost:
            r3.queue.offer(r0);
            r3.done = true;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onComplete() {
            /*
                r3 = this;
                java.util.concurrent.atomic.AtomicReference<io.reactivex.disposables.Disposable> r0 = r3.timer
                p016io.reactivex.internal.disposables.DisposableHelper.dispose(r0)
                monitor-enter(r3)
                U r0 = r3.buffer     // Catch:{ all -> 0x0027 }
                if (r0 != 0) goto L_0x000c
                monitor-exit(r3)     // Catch:{ all -> 0x0027 }
                return
            L_0x000c:
                r1 = 0
                r3.buffer = r1     // Catch:{ all -> 0x0027 }
                monitor-exit(r3)     // Catch:{ all -> 0x0027 }
                io.reactivex.internal.fuseable.SimplePlainQueue r1 = r3.queue
                r1.offer(r0)
                r0 = 1
                r3.done = r0
                boolean r0 = r3.enter()
                if (r0 == 0) goto L_0x0026
                io.reactivex.internal.fuseable.SimplePlainQueue r0 = r3.queue
                org.reactivestreams.Subscriber r1 = r3.actual
                r2 = 0
                p016io.reactivex.internal.util.QueueDrainHelper.drainMaxLoop(r0, r1, r2, r3, r3)
            L_0x0026:
                return
            L_0x0027:
                r0 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x0027 }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: p016io.reactivex.internal.operators.flowable.FlowableBufferTimed.BufferExactUnboundedSubscriber.onComplete():void");
        }

        public void request(long j) {
            requested(j);
        }

        public void cancel() {
            DisposableHelper.dispose(this.timer);
            this.f253s.cancel();
        }

        public void run() {
            U u;
            try {
                U u2 = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The supplied buffer is null");
                synchronized (this) {
                    u = this.buffer;
                    if (u != null) {
                        this.buffer = u2;
                    }
                }
                if (u == null) {
                    DisposableHelper.dispose(this.timer);
                } else {
                    fastPathEmitMax(u, false, this);
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                cancel();
                this.actual.onError(th);
            }
        }

        public boolean accept(Subscriber<? super U> subscriber, U u) {
            this.actual.onNext(u);
            return true;
        }

        public void dispose() {
            cancel();
        }

        public boolean isDisposed() {
            return this.timer.get() == DisposableHelper.DISPOSED;
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableBufferTimed$BufferSkipBoundedSubscriber */
    static final class BufferSkipBoundedSubscriber<T, U extends Collection<? super T>> extends QueueDrainSubscriber<T, U, U> implements Subscription, Runnable {
        final Callable<U> bufferSupplier;
        final List<U> buffers = new LinkedList();

        /* renamed from: s */
        Subscription f254s;
        final long timeskip;
        final long timespan;
        final TimeUnit unit;

        /* renamed from: w */
        final Worker f255w;

        BufferSkipBoundedSubscriber(Subscriber<? super U> subscriber, Callable<U> callable, long j, long j2, TimeUnit timeUnit, Worker worker) {
            super(subscriber, new MpscLinkedQueue());
            this.bufferSupplier = callable;
            this.timespan = j;
            this.timeskip = j2;
            this.unit = timeUnit;
            this.f255w = worker;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f254s, subscription)) {
                this.f254s = subscription;
                try {
                    final Collection collection = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The supplied buffer is null");
                    this.buffers.add(collection);
                    this.actual.onSubscribe(this);
                    subscription.request(LongCompanionObject.MAX_VALUE);
                    this.f255w.schedulePeriodically(this, this.timeskip, this.timeskip, this.unit);
                    this.f255w.schedule(new Runnable() {
                        public void run() {
                            synchronized (BufferSkipBoundedSubscriber.this) {
                                BufferSkipBoundedSubscriber.this.buffers.remove(collection);
                            }
                            BufferSkipBoundedSubscriber.this.fastPathOrderedEmitMax(collection, false, BufferSkipBoundedSubscriber.this.f255w);
                        }
                    }, this.timespan, this.unit);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.f255w.dispose();
                    subscription.cancel();
                    EmptySubscription.error(th, this.actual);
                }
            }
        }

        public void onNext(T t) {
            synchronized (this) {
                for (U add : this.buffers) {
                    add.add(t);
                }
            }
        }

        public void onError(Throwable th) {
            this.done = true;
            this.f255w.dispose();
            clear();
            this.actual.onError(th);
        }

        public void onComplete() {
            ArrayList<Collection> arrayList;
            synchronized (this) {
                arrayList = new ArrayList<>(this.buffers);
                this.buffers.clear();
            }
            for (Collection offer : arrayList) {
                this.queue.offer(offer);
            }
            this.done = true;
            if (enter()) {
                QueueDrainHelper.drainMaxLoop(this.queue, this.actual, false, this.f255w, this);
            }
        }

        public void request(long j) {
            requested(j);
        }

        public void cancel() {
            this.f255w.dispose();
            clear();
            this.f254s.cancel();
        }

        /* access modifiers changed from: 0000 */
        public void clear() {
            synchronized (this) {
                this.buffers.clear();
            }
        }

        public void run() {
            if (!this.cancelled) {
                try {
                    final Collection collection = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The supplied buffer is null");
                    synchronized (this) {
                        if (!this.cancelled) {
                            this.buffers.add(collection);
                            this.f255w.schedule(new Runnable() {
                                public void run() {
                                    synchronized (BufferSkipBoundedSubscriber.this) {
                                        BufferSkipBoundedSubscriber.this.buffers.remove(collection);
                                    }
                                    BufferSkipBoundedSubscriber.this.fastPathOrderedEmitMax(collection, false, BufferSkipBoundedSubscriber.this.f255w);
                                }
                            }, this.timespan, this.unit);
                        }
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    cancel();
                    this.actual.onError(th);
                }
            }
        }

        public boolean accept(Subscriber<? super U> subscriber, U u) {
            subscriber.onNext(u);
            return true;
        }
    }

    public FlowableBufferTimed(Publisher<T> publisher, long j, long j2, TimeUnit timeUnit, Scheduler scheduler2, Callable<U> callable, int i, boolean z) {
        super(publisher);
        this.timespan = j;
        this.timeskip = j2;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
        this.bufferSupplier = callable;
        this.maxSize = i;
        this.restartTimerOnMaxSize = z;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super U> subscriber) {
        if (this.timespan == this.timeskip && this.maxSize == Integer.MAX_VALUE) {
            Publisher publisher = this.source;
            BufferExactUnboundedSubscriber bufferExactUnboundedSubscriber = new BufferExactUnboundedSubscriber(new SerializedSubscriber(subscriber), this.bufferSupplier, this.timespan, this.unit, this.scheduler);
            publisher.subscribe(bufferExactUnboundedSubscriber);
            return;
        }
        Worker createWorker = this.scheduler.createWorker();
        if (this.timespan == this.timeskip) {
            Publisher publisher2 = this.source;
            BufferExactBoundedSubscriber bufferExactBoundedSubscriber = new BufferExactBoundedSubscriber(new SerializedSubscriber(subscriber), this.bufferSupplier, this.timespan, this.unit, this.maxSize, this.restartTimerOnMaxSize, createWorker);
            publisher2.subscribe(bufferExactBoundedSubscriber);
            return;
        }
        Publisher publisher3 = this.source;
        BufferSkipBoundedSubscriber bufferSkipBoundedSubscriber = new BufferSkipBoundedSubscriber(new SerializedSubscriber(subscriber), this.bufferSupplier, this.timespan, this.timeskip, this.unit, createWorker);
        publisher3.subscribe(bufferSkipBoundedSubscriber);
    }
}
