package p016io.reactivex.internal.operators.flowable;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.BooleanSupplier;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.BackpressureHelper;
import p016io.reactivex.internal.util.QueueDrainHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableBuffer */
public final class FlowableBuffer<T, C extends Collection<? super T>> extends AbstractFlowableWithUpstream<T, C> {
    final Callable<C> bufferSupplier;
    final int size;
    final int skip;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableBuffer$PublisherBufferExactSubscriber */
    static final class PublisherBufferExactSubscriber<T, C extends Collection<? super T>> implements Subscriber<T>, Subscription {
        final Subscriber<? super C> actual;
        C buffer;
        final Callable<C> bufferSupplier;
        boolean done;
        int index;

        /* renamed from: s */
        Subscription f245s;
        final int size;

        PublisherBufferExactSubscriber(Subscriber<? super C> subscriber, int i, Callable<C> callable) {
            this.actual = subscriber;
            this.size = i;
            this.bufferSupplier = callable;
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                this.f245s.request(BackpressureHelper.multiplyCap(j, (long) this.size));
            }
        }

        public void cancel() {
            this.f245s.cancel();
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f245s, subscription)) {
                this.f245s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                C c = this.buffer;
                if (c == null) {
                    try {
                        c = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The bufferSupplier returned a null buffer");
                        this.buffer = c;
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        cancel();
                        onError(th);
                        return;
                    }
                }
                c.add(t);
                int i = this.index + 1;
                if (i == this.size) {
                    this.index = 0;
                    this.buffer = null;
                    this.actual.onNext(c);
                } else {
                    this.index = i;
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
                C c = this.buffer;
                if (c != null && !c.isEmpty()) {
                    this.actual.onNext(c);
                }
                this.actual.onComplete();
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableBuffer$PublisherBufferOverlappingSubscriber */
    static final class PublisherBufferOverlappingSubscriber<T, C extends Collection<? super T>> extends AtomicLong implements Subscriber<T>, Subscription, BooleanSupplier {
        private static final long serialVersionUID = -7370244972039324525L;
        final Subscriber<? super C> actual;
        final Callable<C> bufferSupplier;
        final ArrayDeque<C> buffers = new ArrayDeque<>();
        volatile boolean cancelled;
        boolean done;
        int index;
        final AtomicBoolean once = new AtomicBoolean();
        long produced;

        /* renamed from: s */
        Subscription f246s;
        final int size;
        final int skip;

        PublisherBufferOverlappingSubscriber(Subscriber<? super C> subscriber, int i, int i2, Callable<C> callable) {
            this.actual = subscriber;
            this.size = i;
            this.skip = i2;
            this.bufferSupplier = callable;
        }

        public boolean getAsBoolean() {
            return this.cancelled;
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                if (!QueueDrainHelper.postCompleteRequest(j, this.actual, this.buffers, this, this)) {
                    if (this.once.get() || !this.once.compareAndSet(false, true)) {
                        this.f246s.request(BackpressureHelper.multiplyCap((long) this.skip, j));
                    } else {
                        this.f246s.request(BackpressureHelper.addCap((long) this.size, BackpressureHelper.multiplyCap((long) this.skip, j - 1)));
                    }
                }
            }
        }

        public void cancel() {
            this.cancelled = true;
            this.f246s.cancel();
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f246s, subscription)) {
                this.f246s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                ArrayDeque<C> arrayDeque = this.buffers;
                int i = this.index;
                int i2 = i + 1;
                if (i == 0) {
                    try {
                        arrayDeque.offer((Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The bufferSupplier returned a null buffer"));
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        cancel();
                        onError(th);
                        return;
                    }
                }
                Collection collection = (Collection) arrayDeque.peek();
                if (collection != null && collection.size() + 1 == this.size) {
                    arrayDeque.poll();
                    collection.add(t);
                    this.produced++;
                    this.actual.onNext(collection);
                }
                Iterator it = arrayDeque.iterator();
                while (it.hasNext()) {
                    ((Collection) it.next()).add(t);
                }
                if (i2 == this.skip) {
                    i2 = 0;
                }
                this.index = i2;
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.buffers.clear();
            this.actual.onError(th);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                long j = this.produced;
                if (j != 0) {
                    BackpressureHelper.produced(this, j);
                }
                QueueDrainHelper.postComplete(this.actual, this.buffers, this, this);
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableBuffer$PublisherBufferSkipSubscriber */
    static final class PublisherBufferSkipSubscriber<T, C extends Collection<? super T>> extends AtomicInteger implements Subscriber<T>, Subscription {
        private static final long serialVersionUID = -5616169793639412593L;
        final Subscriber<? super C> actual;
        C buffer;
        final Callable<C> bufferSupplier;
        boolean done;
        int index;

        /* renamed from: s */
        Subscription f247s;
        final int size;
        final int skip;

        PublisherBufferSkipSubscriber(Subscriber<? super C> subscriber, int i, int i2, Callable<C> callable) {
            this.actual = subscriber;
            this.size = i;
            this.skip = i2;
            this.bufferSupplier = callable;
        }

        public void request(long j) {
            if (!SubscriptionHelper.validate(j)) {
                return;
            }
            if (get() != 0 || !compareAndSet(0, 1)) {
                this.f247s.request(BackpressureHelper.multiplyCap((long) this.skip, j));
                return;
            }
            this.f247s.request(BackpressureHelper.addCap(BackpressureHelper.multiplyCap(j, (long) this.size), BackpressureHelper.multiplyCap((long) (this.skip - this.size), j - 1)));
        }

        public void cancel() {
            this.f247s.cancel();
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f247s, subscription)) {
                this.f247s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                C c = this.buffer;
                int i = this.index;
                int i2 = i + 1;
                if (i == 0) {
                    try {
                        c = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The bufferSupplier returned a null buffer");
                        this.buffer = c;
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        cancel();
                        onError(th);
                        return;
                    }
                }
                if (c != null) {
                    c.add(t);
                    if (c.size() == this.size) {
                        this.buffer = null;
                        this.actual.onNext(c);
                    }
                }
                if (i2 == this.skip) {
                    i2 = 0;
                }
                this.index = i2;
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.buffer = null;
            this.actual.onError(th);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                C c = this.buffer;
                this.buffer = null;
                if (c != null) {
                    this.actual.onNext(c);
                }
                this.actual.onComplete();
            }
        }
    }

    public FlowableBuffer(Publisher<T> publisher, int i, int i2, Callable<C> callable) {
        super(publisher);
        this.size = i;
        this.skip = i2;
        this.bufferSupplier = callable;
    }

    public void subscribeActual(Subscriber<? super C> subscriber) {
        if (this.size == this.skip) {
            this.source.subscribe(new PublisherBufferExactSubscriber(subscriber, this.size, this.bufferSupplier));
        } else if (this.skip > this.size) {
            this.source.subscribe(new PublisherBufferSkipSubscriber(subscriber, this.size, this.skip, this.bufferSupplier));
        } else {
            this.source.subscribe(new PublisherBufferOverlappingSubscriber(subscriber, this.size, this.skip, this.bufferSupplier));
        }
    }
}
