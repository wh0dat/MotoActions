package p016io.reactivex.internal.operators.flowable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.disposables.CompositeDisposable;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.fuseable.SimplePlainQueue;
import p016io.reactivex.internal.queue.MpscLinkedQueue;
import p016io.reactivex.internal.subscribers.QueueDrainSubscriber;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.QueueDrainHelper;
import p016io.reactivex.plugins.RxJavaPlugins;
import p016io.reactivex.subscribers.DisposableSubscriber;
import p016io.reactivex.subscribers.SerializedSubscriber;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableBufferBoundary */
public final class FlowableBufferBoundary<T, U extends Collection<? super T>, Open, Close> extends AbstractFlowableWithUpstream<T, U> {
    final Function<? super Open, ? extends Publisher<? extends Close>> bufferClose;
    final Publisher<? extends Open> bufferOpen;
    final Callable<U> bufferSupplier;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableBufferBoundary$BufferBoundarySubscriber */
    static final class BufferBoundarySubscriber<T, U extends Collection<? super T>, Open, Close> extends QueueDrainSubscriber<T, U, U> implements Subscription, Disposable {
        final Function<? super Open, ? extends Publisher<? extends Close>> bufferClose;
        final Publisher<? extends Open> bufferOpen;
        final Callable<U> bufferSupplier;
        final List<U> buffers;
        final CompositeDisposable resources;

        /* renamed from: s */
        Subscription f248s;
        final AtomicInteger windows = new AtomicInteger();

        BufferBoundarySubscriber(Subscriber<? super U> subscriber, Publisher<? extends Open> publisher, Function<? super Open, ? extends Publisher<? extends Close>> function, Callable<U> callable) {
            super(subscriber, new MpscLinkedQueue());
            this.bufferOpen = publisher;
            this.bufferClose = function;
            this.bufferSupplier = callable;
            this.buffers = new LinkedList();
            this.resources = new CompositeDisposable();
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f248s, subscription)) {
                this.f248s = subscription;
                BufferOpenSubscriber bufferOpenSubscriber = new BufferOpenSubscriber(this);
                this.resources.add(bufferOpenSubscriber);
                this.actual.onSubscribe(this);
                this.windows.lazySet(1);
                this.bufferOpen.subscribe(bufferOpenSubscriber);
                subscription.request(LongCompanionObject.MAX_VALUE);
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
            cancel();
            this.cancelled = true;
            synchronized (this) {
                this.buffers.clear();
            }
            this.actual.onError(th);
        }

        public void onComplete() {
            if (this.windows.decrementAndGet() == 0) {
                complete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void complete() {
            ArrayList<Collection> arrayList;
            synchronized (this) {
                arrayList = new ArrayList<>(this.buffers);
                this.buffers.clear();
            }
            SimplePlainQueue simplePlainQueue = this.queue;
            for (Collection offer : arrayList) {
                simplePlainQueue.offer(offer);
            }
            this.done = true;
            if (enter()) {
                QueueDrainHelper.drainMaxLoop(simplePlainQueue, this.actual, false, this, this);
            }
        }

        public void request(long j) {
            requested(j);
        }

        public void dispose() {
            this.resources.dispose();
        }

        public boolean isDisposed() {
            return this.resources.isDisposed();
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                dispose();
            }
        }

        public boolean accept(Subscriber<? super U> subscriber, U u) {
            subscriber.onNext(u);
            return true;
        }

        /* access modifiers changed from: 0000 */
        public void open(Open open) {
            if (!this.cancelled) {
                try {
                    Collection collection = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
                    try {
                        Publisher publisher = (Publisher) ObjectHelper.requireNonNull(this.bufferClose.apply(open), "The buffer closing publisher is null");
                        if (!this.cancelled) {
                            synchronized (this) {
                                if (!this.cancelled) {
                                    this.buffers.add(collection);
                                    BufferCloseSubscriber bufferCloseSubscriber = new BufferCloseSubscriber(collection, this);
                                    this.resources.add(bufferCloseSubscriber);
                                    this.windows.getAndIncrement();
                                    publisher.subscribe(bufferCloseSubscriber);
                                }
                            }
                        }
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        onError(th);
                    }
                } catch (Throwable th2) {
                    Exceptions.throwIfFatal(th2);
                    onError(th2);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void openFinished(Disposable disposable) {
            if (this.resources.remove(disposable) && this.windows.decrementAndGet() == 0) {
                complete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void close(U u, Disposable disposable) {
            boolean remove;
            synchronized (this) {
                remove = this.buffers.remove(u);
            }
            if (remove) {
                fastPathOrderedEmitMax(u, false, this);
            }
            if (this.resources.remove(disposable) && this.windows.decrementAndGet() == 0) {
                complete();
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableBufferBoundary$BufferCloseSubscriber */
    static final class BufferCloseSubscriber<T, U extends Collection<? super T>, Open, Close> extends DisposableSubscriber<Close> {
        boolean done;
        final BufferBoundarySubscriber<T, U, Open, Close> parent;
        final U value;

        BufferCloseSubscriber(U u, BufferBoundarySubscriber<T, U, Open, Close> bufferBoundarySubscriber) {
            this.parent = bufferBoundarySubscriber;
            this.value = u;
        }

        public void onNext(Close close) {
            onComplete();
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
            } else {
                this.parent.onError(th);
            }
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.parent.close(this.value, this);
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableBufferBoundary$BufferOpenSubscriber */
    static final class BufferOpenSubscriber<T, U extends Collection<? super T>, Open, Close> extends DisposableSubscriber<Open> {
        boolean done;
        final BufferBoundarySubscriber<T, U, Open, Close> parent;

        BufferOpenSubscriber(BufferBoundarySubscriber<T, U, Open, Close> bufferBoundarySubscriber) {
            this.parent = bufferBoundarySubscriber;
        }

        public void onNext(Open open) {
            if (!this.done) {
                this.parent.open(open);
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.parent.onError(th);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.parent.openFinished(this);
            }
        }
    }

    public FlowableBufferBoundary(Publisher<T> publisher, Publisher<? extends Open> publisher2, Function<? super Open, ? extends Publisher<? extends Close>> function, Callable<U> callable) {
        super(publisher);
        this.bufferOpen = publisher2;
        this.bufferClose = function;
        this.bufferSupplier = callable;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super U> subscriber) {
        this.source.subscribe(new BufferBoundarySubscriber(new SerializedSubscriber(subscriber), this.bufferOpen, this.bufferClose, this.bufferSupplier));
    }
}
