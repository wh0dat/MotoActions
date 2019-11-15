package p016io.reactivex.internal.operators.flowable;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.MissingBackpressureException;
import p016io.reactivex.internal.queue.SpscArrayQueue;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.ExceptionHelper;

/* renamed from: io.reactivex.internal.operators.flowable.BlockingFlowableIterable */
public final class BlockingFlowableIterable<T> implements Iterable<T> {
    final int bufferSize;
    final Publisher<? extends T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.BlockingFlowableIterable$BlockingFlowableIterator */
    static final class BlockingFlowableIterator<T> extends AtomicReference<Subscription> implements Subscriber<T>, Iterator<T>, Runnable, Disposable {
        private static final long serialVersionUID = 6695226475494099826L;
        final long batchSize;
        final Condition condition = this.lock.newCondition();
        volatile boolean done;
        Throwable error;
        final long limit;
        final Lock lock = new ReentrantLock();
        long produced;
        final SpscArrayQueue<T> queue;

        BlockingFlowableIterator(int i) {
            this.queue = new SpscArrayQueue<>(i);
            this.batchSize = (long) i;
            this.limit = (long) (i - (i >> 2));
        }

        public boolean hasNext() {
            while (true) {
                boolean z = this.done;
                boolean isEmpty = this.queue.isEmpty();
                if (z) {
                    Throwable th = this.error;
                    if (th != null) {
                        throw ExceptionHelper.wrapOrThrow(th);
                    } else if (isEmpty) {
                        return false;
                    }
                }
                if (!isEmpty) {
                    return true;
                }
                this.lock.lock();
                while (!this.done && this.queue.isEmpty()) {
                    try {
                        this.condition.await();
                    } catch (InterruptedException e) {
                        run();
                        throw ExceptionHelper.wrapOrThrow(e);
                    } catch (Throwable th2) {
                        this.lock.unlock();
                        throw th2;
                    }
                }
                this.lock.unlock();
            }
        }

        public T next() {
            if (hasNext()) {
                T poll = this.queue.poll();
                long j = this.produced + 1;
                if (j == this.limit) {
                    this.produced = 0;
                    ((Subscription) get()).request(j);
                } else {
                    this.produced = j;
                }
                return poll;
            }
            throw new NoSuchElementException();
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.setOnce(this, subscription)) {
                subscription.request(this.batchSize);
            }
        }

        public void onNext(T t) {
            if (!this.queue.offer(t)) {
                SubscriptionHelper.cancel(this);
                onError(new MissingBackpressureException("Queue full?!"));
                return;
            }
            signalConsumer();
        }

        public void onError(Throwable th) {
            this.error = th;
            this.done = true;
            signalConsumer();
        }

        public void onComplete() {
            this.done = true;
            signalConsumer();
        }

        /* access modifiers changed from: 0000 */
        public void signalConsumer() {
            this.lock.lock();
            try {
                this.condition.signalAll();
            } finally {
                this.lock.unlock();
            }
        }

        public void run() {
            SubscriptionHelper.cancel(this);
            signalConsumer();
        }

        public void remove() {
            throw new UnsupportedOperationException("remove");
        }

        public void dispose() {
            SubscriptionHelper.cancel(this);
        }

        public boolean isDisposed() {
            return SubscriptionHelper.isCancelled((Subscription) get());
        }
    }

    public BlockingFlowableIterable(Publisher<? extends T> publisher, int i) {
        this.source = publisher;
        this.bufferSize = i;
    }

    public Iterator<T> iterator() {
        BlockingFlowableIterator blockingFlowableIterator = new BlockingFlowableIterator(this.bufferSize);
        this.source.subscribe(blockingFlowableIterator);
        return blockingFlowableIterator;
    }
}
