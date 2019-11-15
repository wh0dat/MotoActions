package p016io.reactivex.internal.observers;

import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.fuseable.SimpleQueue;
import p016io.reactivex.internal.util.ObservableQueueDrain;
import p016io.reactivex.internal.util.QueueDrainHelper;

/* renamed from: io.reactivex.internal.observers.QueueDrainObserver */
public abstract class QueueDrainObserver<T, U, V> extends QueueDrainSubscriberPad2 implements Observer<T>, ObservableQueueDrain<U, V> {
    protected final Observer<? super V> actual;
    /* access modifiers changed from: protected */
    public volatile boolean cancelled;
    protected volatile boolean done;
    protected Throwable error;
    /* access modifiers changed from: protected */
    public final SimpleQueue<U> queue;

    public void accept(Observer<? super V> observer, U u) {
    }

    public QueueDrainObserver(Observer<? super V> observer, SimpleQueue<U> simpleQueue) {
        this.actual = observer;
        this.queue = simpleQueue;
    }

    public final boolean cancelled() {
        return this.cancelled;
    }

    public final boolean done() {
        return this.done;
    }

    public final boolean enter() {
        return this.wip.getAndIncrement() == 0;
    }

    public final boolean fastEnter() {
        return this.wip.get() == 0 && this.wip.compareAndSet(0, 1);
    }

    /* access modifiers changed from: protected */
    public final void fastPathEmit(U u, boolean z, Disposable disposable) {
        Observer<? super V> observer = this.actual;
        SimpleQueue<U> simpleQueue = this.queue;
        if (this.wip.get() != 0 || !this.wip.compareAndSet(0, 1)) {
            simpleQueue.offer(u);
            if (!enter()) {
                return;
            }
        } else {
            accept(observer, u);
            if (leave(-1) == 0) {
                return;
            }
        }
        QueueDrainHelper.drainLoop(simpleQueue, observer, z, disposable, this);
    }

    /* access modifiers changed from: protected */
    public final void fastPathOrderedEmit(U u, boolean z, Disposable disposable) {
        Observer<? super V> observer = this.actual;
        SimpleQueue<U> simpleQueue = this.queue;
        if (this.wip.get() != 0 || !this.wip.compareAndSet(0, 1)) {
            simpleQueue.offer(u);
            if (!enter()) {
                return;
            }
        } else if (simpleQueue.isEmpty()) {
            accept(observer, u);
            if (leave(-1) == 0) {
                return;
            }
        } else {
            simpleQueue.offer(u);
        }
        QueueDrainHelper.drainLoop(simpleQueue, observer, z, disposable, this);
    }

    public final Throwable error() {
        return this.error;
    }

    public final int leave(int i) {
        return this.wip.addAndGet(i);
    }

    public void drain(boolean z, Disposable disposable) {
        if (enter()) {
            QueueDrainHelper.drainLoop(this.queue, this.actual, z, disposable, this);
        }
    }
}
