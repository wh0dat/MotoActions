package p016io.reactivex.internal.operators.flowable;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Processor;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Flowable;
import p016io.reactivex.internal.queue.SpscLinkedArrayQueue;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.BackpressureHelper;
import p016io.reactivex.plugins.RxJavaPlugins;
import p016io.reactivex.processors.UnicastProcessor;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableWindow */
public final class FlowableWindow<T> extends AbstractFlowableWithUpstream<T, Flowable<T>> {
    final int bufferSize;
    final long size;
    final long skip;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableWindow$WindowExactSubscriber */
    static final class WindowExactSubscriber<T> extends AtomicInteger implements Subscriber<T>, Subscription, Runnable {
        private static final long serialVersionUID = -2365647875069161133L;
        final Subscriber<? super Flowable<T>> actual;
        final int bufferSize;
        boolean done;
        long index;
        final AtomicBoolean once = new AtomicBoolean();

        /* renamed from: s */
        Subscription f344s;
        final long size;
        UnicastProcessor<T> window;

        WindowExactSubscriber(Subscriber<? super Flowable<T>> subscriber, long j, int i) {
            super(1);
            this.actual = subscriber;
            this.size = j;
            this.bufferSize = i;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f344s, subscription)) {
                this.f344s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                long j = this.index;
                UnicastProcessor<T> unicastProcessor = this.window;
                if (j == 0) {
                    getAndIncrement();
                    unicastProcessor = UnicastProcessor.create(this.bufferSize, this);
                    this.window = unicastProcessor;
                    this.actual.onNext(unicastProcessor);
                }
                long j2 = j + 1;
                unicastProcessor.onNext(t);
                if (j2 == this.size) {
                    this.index = 0;
                    this.window = null;
                    unicastProcessor.onComplete();
                } else {
                    this.index = j2;
                }
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            UnicastProcessor<T> unicastProcessor = this.window;
            if (unicastProcessor != null) {
                this.window = null;
                unicastProcessor.onError(th);
            }
            this.actual.onError(th);
        }

        public void onComplete() {
            if (!this.done) {
                UnicastProcessor<T> unicastProcessor = this.window;
                if (unicastProcessor != null) {
                    this.window = null;
                    unicastProcessor.onComplete();
                }
                this.actual.onComplete();
            }
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                this.f344s.request(BackpressureHelper.multiplyCap(this.size, j));
            }
        }

        public void cancel() {
            if (this.once.compareAndSet(false, true)) {
                run();
            }
        }

        public void run() {
            if (decrementAndGet() == 0) {
                this.f344s.cancel();
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableWindow$WindowOverlapSubscriber */
    static final class WindowOverlapSubscriber<T> extends AtomicInteger implements Subscriber<T>, Subscription, Runnable {
        private static final long serialVersionUID = 2428527070996323976L;
        final Subscriber<? super Flowable<T>> actual;
        final int bufferSize;
        volatile boolean cancelled;
        volatile boolean done;
        Throwable error;
        final AtomicBoolean firstRequest = new AtomicBoolean();
        long index;
        final AtomicBoolean once = new AtomicBoolean();
        long produced;
        final SpscLinkedArrayQueue<UnicastProcessor<T>> queue;
        final AtomicLong requested = new AtomicLong();

        /* renamed from: s */
        Subscription f345s;
        final long size;
        final long skip;
        final ArrayDeque<UnicastProcessor<T>> windows = new ArrayDeque<>();
        final AtomicInteger wip = new AtomicInteger();

        WindowOverlapSubscriber(Subscriber<? super Flowable<T>> subscriber, long j, long j2, int i) {
            super(1);
            this.actual = subscriber;
            this.size = j;
            this.skip = j2;
            this.queue = new SpscLinkedArrayQueue<>(i);
            this.bufferSize = i;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f345s, subscription)) {
                this.f345s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                long j = this.index;
                if (j == 0 && !this.cancelled) {
                    getAndIncrement();
                    UnicastProcessor create = UnicastProcessor.create(this.bufferSize, this);
                    this.windows.offer(create);
                    this.queue.offer(create);
                    drain();
                }
                long j2 = j + 1;
                Iterator it = this.windows.iterator();
                while (it.hasNext()) {
                    ((Processor) it.next()).onNext(t);
                }
                long j3 = this.produced + 1;
                if (j3 == this.size) {
                    this.produced = j3 - this.skip;
                    Processor processor = (Processor) this.windows.poll();
                    if (processor != null) {
                        processor.onComplete();
                    }
                } else {
                    this.produced = j3;
                }
                if (j2 == this.skip) {
                    this.index = 0;
                } else {
                    this.index = j2;
                }
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            Iterator it = this.windows.iterator();
            while (it.hasNext()) {
                ((Processor) it.next()).onError(th);
            }
            this.windows.clear();
            this.error = th;
            this.done = true;
            drain();
        }

        public void onComplete() {
            if (!this.done) {
                Iterator it = this.windows.iterator();
                while (it.hasNext()) {
                    ((Processor) it.next()).onComplete();
                }
                this.windows.clear();
                this.done = true;
                drain();
            }
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            int i;
            if (this.wip.getAndIncrement() == 0) {
                Subscriber<? super Flowable<T>> subscriber = this.actual;
                SpscLinkedArrayQueue<UnicastProcessor<T>> spscLinkedArrayQueue = this.queue;
                int i2 = 1;
                do {
                    long j = this.requested.get();
                    long j2 = 0;
                    while (true) {
                        i = (j2 > j ? 1 : (j2 == j ? 0 : -1));
                        if (i == 0) {
                            break;
                        }
                        boolean z = this.done;
                        UnicastProcessor unicastProcessor = (UnicastProcessor) spscLinkedArrayQueue.poll();
                        boolean z2 = unicastProcessor == null;
                        if (!checkTerminated(z, z2, subscriber, spscLinkedArrayQueue)) {
                            if (z2) {
                                break;
                            }
                            subscriber.onNext(unicastProcessor);
                            j2++;
                        } else {
                            return;
                        }
                    }
                    if (i != 0 || !checkTerminated(this.done, spscLinkedArrayQueue.isEmpty(), subscriber, spscLinkedArrayQueue)) {
                        if (!(j2 == 0 || j == LongCompanionObject.MAX_VALUE)) {
                            this.requested.addAndGet(-j2);
                        }
                        i2 = this.wip.addAndGet(-i2);
                    } else {
                        return;
                    }
                } while (i2 != 0);
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminated(boolean z, boolean z2, Subscriber<?> subscriber, SpscLinkedArrayQueue<?> spscLinkedArrayQueue) {
            if (this.cancelled) {
                spscLinkedArrayQueue.clear();
                return true;
            }
            if (z) {
                Throwable th = this.error;
                if (th != null) {
                    spscLinkedArrayQueue.clear();
                    subscriber.onError(th);
                    return true;
                } else if (z2) {
                    subscriber.onComplete();
                    return true;
                }
            }
            return false;
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this.requested, j);
                if (this.firstRequest.get() || !this.firstRequest.compareAndSet(false, true)) {
                    this.f345s.request(BackpressureHelper.multiplyCap(this.skip, j));
                } else {
                    this.f345s.request(BackpressureHelper.addCap(this.size, BackpressureHelper.multiplyCap(this.skip, j - 1)));
                }
                drain();
            }
        }

        public void cancel() {
            this.cancelled = true;
            if (this.once.compareAndSet(false, true)) {
                run();
            }
        }

        public void run() {
            if (decrementAndGet() == 0) {
                this.f345s.cancel();
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableWindow$WindowSkipSubscriber */
    static final class WindowSkipSubscriber<T> extends AtomicInteger implements Subscriber<T>, Subscription, Runnable {
        private static final long serialVersionUID = -8792836352386833856L;
        final Subscriber<? super Flowable<T>> actual;
        final int bufferSize;
        boolean done;
        final AtomicBoolean firstRequest = new AtomicBoolean();
        long index;
        final AtomicBoolean once = new AtomicBoolean();

        /* renamed from: s */
        Subscription f346s;
        final long size;
        final long skip;
        UnicastProcessor<T> window;

        WindowSkipSubscriber(Subscriber<? super Flowable<T>> subscriber, long j, long j2, int i) {
            super(1);
            this.actual = subscriber;
            this.size = j;
            this.skip = j2;
            this.bufferSize = i;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f346s, subscription)) {
                this.f346s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                long j = this.index;
                UnicastProcessor<T> unicastProcessor = this.window;
                if (j == 0) {
                    getAndIncrement();
                    unicastProcessor = UnicastProcessor.create(this.bufferSize, this);
                    this.window = unicastProcessor;
                    this.actual.onNext(unicastProcessor);
                }
                long j2 = j + 1;
                if (unicastProcessor != null) {
                    unicastProcessor.onNext(t);
                }
                if (j2 == this.size) {
                    this.window = null;
                    unicastProcessor.onComplete();
                }
                if (j2 == this.skip) {
                    this.index = 0;
                } else {
                    this.index = j2;
                }
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            UnicastProcessor<T> unicastProcessor = this.window;
            if (unicastProcessor != null) {
                this.window = null;
                unicastProcessor.onError(th);
            }
            this.actual.onError(th);
        }

        public void onComplete() {
            if (!this.done) {
                UnicastProcessor<T> unicastProcessor = this.window;
                if (unicastProcessor != null) {
                    this.window = null;
                    unicastProcessor.onComplete();
                }
                this.actual.onComplete();
            }
        }

        public void request(long j) {
            if (!SubscriptionHelper.validate(j)) {
                return;
            }
            if (this.firstRequest.get() || !this.firstRequest.compareAndSet(false, true)) {
                this.f346s.request(BackpressureHelper.multiplyCap(this.skip, j));
                return;
            }
            this.f346s.request(BackpressureHelper.addCap(BackpressureHelper.multiplyCap(this.size, j), BackpressureHelper.multiplyCap(this.skip - this.size, j - 1)));
        }

        public void cancel() {
            if (this.once.compareAndSet(false, true)) {
                run();
            }
        }

        public void run() {
            if (decrementAndGet() == 0) {
                this.f346s.cancel();
            }
        }
    }

    public FlowableWindow(Publisher<T> publisher, long j, long j2, int i) {
        super(publisher);
        this.size = j;
        this.skip = j2;
        this.bufferSize = i;
    }

    public void subscribeActual(Subscriber<? super Flowable<T>> subscriber) {
        if (this.skip == this.size) {
            this.source.subscribe(new WindowExactSubscriber(subscriber, this.size, this.bufferSize));
        } else if (this.skip > this.size) {
            Publisher publisher = this.source;
            WindowSkipSubscriber windowSkipSubscriber = new WindowSkipSubscriber(subscriber, this.size, this.skip, this.bufferSize);
            publisher.subscribe(windowSkipSubscriber);
        } else {
            Publisher publisher2 = this.source;
            WindowOverlapSubscriber windowOverlapSubscriber = new WindowOverlapSubscriber(subscriber, this.size, this.skip, this.bufferSize);
            publisher2.subscribe(windowOverlapSubscriber);
        }
    }
}
