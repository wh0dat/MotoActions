package p016io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.disposables.CompositeDisposable;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.disposables.Disposables;
import p016io.reactivex.flowables.ConnectableFlowable;
import p016io.reactivex.functions.Consumer;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableRefCount */
public final class FlowableRefCount<T> extends AbstractFlowableWithUpstream<T, T> {
    volatile CompositeDisposable baseDisposable = new CompositeDisposable();
    final ReentrantLock lock = new ReentrantLock();
    final ConnectableFlowable<? extends T> source;
    final AtomicInteger subscriptionCount = new AtomicInteger();

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableRefCount$ConnectionSubscriber */
    final class ConnectionSubscriber extends AtomicReference<Subscription> implements Subscriber<T>, Subscription {
        private static final long serialVersionUID = 152064694420235350L;
        final CompositeDisposable currentBase;
        final AtomicLong requested = new AtomicLong();
        final Disposable resource;
        final Subscriber<? super T> subscriber;

        ConnectionSubscriber(Subscriber<? super T> subscriber2, CompositeDisposable compositeDisposable, Disposable disposable) {
            this.subscriber = subscriber2;
            this.currentBase = compositeDisposable;
            this.resource = disposable;
        }

        public void onSubscribe(Subscription subscription) {
            SubscriptionHelper.deferredSetOnce(this, this.requested, subscription);
        }

        public void onError(Throwable th) {
            cleanup();
            this.subscriber.onError(th);
        }

        public void onNext(T t) {
            this.subscriber.onNext(t);
        }

        public void onComplete() {
            cleanup();
            this.subscriber.onComplete();
        }

        public void request(long j) {
            SubscriptionHelper.deferredRequest(this, this.requested, j);
        }

        public void cancel() {
            SubscriptionHelper.cancel(this);
            this.resource.dispose();
        }

        /* access modifiers changed from: 0000 */
        public void cleanup() {
            FlowableRefCount.this.lock.lock();
            try {
                if (FlowableRefCount.this.baseDisposable == this.currentBase) {
                    FlowableRefCount.this.baseDisposable.dispose();
                    FlowableRefCount.this.baseDisposable = new CompositeDisposable();
                    FlowableRefCount.this.subscriptionCount.set(0);
                }
            } finally {
                FlowableRefCount.this.lock.unlock();
            }
        }
    }

    public FlowableRefCount(ConnectableFlowable<T> connectableFlowable) {
        super(connectableFlowable);
        this.source = connectableFlowable;
    }

    public void subscribeActual(Subscriber<? super T> subscriber) {
        this.lock.lock();
        if (this.subscriptionCount.incrementAndGet() == 1) {
            AtomicBoolean atomicBoolean = new AtomicBoolean(true);
            try {
                this.source.connect(onSubscribe(subscriber, atomicBoolean));
            } finally {
                if (atomicBoolean.get()) {
                    this.lock.unlock();
                }
            }
        } else {
            try {
                doSubscribe(subscriber, this.baseDisposable);
            } finally {
                this.lock.unlock();
            }
        }
    }

    private Consumer<Disposable> onSubscribe(final Subscriber<? super T> subscriber, final AtomicBoolean atomicBoolean) {
        return new Consumer<Disposable>() {
            public void accept(Disposable disposable) {
                try {
                    FlowableRefCount.this.baseDisposable.add(disposable);
                    FlowableRefCount.this.doSubscribe(subscriber, FlowableRefCount.this.baseDisposable);
                } finally {
                    FlowableRefCount.this.lock.unlock();
                    atomicBoolean.set(false);
                }
            }
        };
    }

    /* access modifiers changed from: 0000 */
    public void doSubscribe(Subscriber<? super T> subscriber, CompositeDisposable compositeDisposable) {
        ConnectionSubscriber connectionSubscriber = new ConnectionSubscriber(subscriber, compositeDisposable, disconnect(compositeDisposable));
        subscriber.onSubscribe(connectionSubscriber);
        this.source.subscribe((Subscriber<? super T>) connectionSubscriber);
    }

    private Disposable disconnect(final CompositeDisposable compositeDisposable) {
        return Disposables.fromRunnable(new Runnable() {
            public void run() {
                FlowableRefCount.this.lock.lock();
                try {
                    if (FlowableRefCount.this.baseDisposable == compositeDisposable && FlowableRefCount.this.subscriptionCount.decrementAndGet() == 0) {
                        FlowableRefCount.this.baseDisposable.dispose();
                        FlowableRefCount.this.baseDisposable = new CompositeDisposable();
                    }
                } finally {
                    FlowableRefCount.this.lock.unlock();
                }
            }
        });
    }
}
