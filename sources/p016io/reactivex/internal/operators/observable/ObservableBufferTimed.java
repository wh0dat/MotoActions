package p016io.reactivex.internal.operators.observable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.Scheduler;
import p016io.reactivex.Scheduler.Worker;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.disposables.EmptyDisposable;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.observers.QueueDrainObserver;
import p016io.reactivex.internal.queue.MpscLinkedQueue;
import p016io.reactivex.internal.util.QueueDrainHelper;
import p016io.reactivex.observers.SerializedObserver;

/* renamed from: io.reactivex.internal.operators.observable.ObservableBufferTimed */
public final class ObservableBufferTimed<T, U extends Collection<? super T>> extends AbstractObservableWithUpstream<T, U> {
    final Callable<U> bufferSupplier;
    final int maxSize;
    final boolean restartTimerOnMaxSize;
    final Scheduler scheduler;
    final long timeskip;
    final long timespan;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableBufferTimed$BufferExactBoundedObserver */
    static final class BufferExactBoundedObserver<T, U extends Collection<? super T>> extends QueueDrainObserver<T, U, U> implements Runnable, Disposable {
        U buffer;
        final Callable<U> bufferSupplier;
        long consumerIndex;
        final int maxSize;
        long producerIndex;
        final boolean restartTimerOnMaxSize;

        /* renamed from: s */
        Disposable f402s;
        Disposable timer;
        final long timespan;
        final TimeUnit unit;

        /* renamed from: w */
        final Worker f403w;

        BufferExactBoundedObserver(Observer<? super U> observer, Callable<U> callable, long j, TimeUnit timeUnit, int i, boolean z, Worker worker) {
            super(observer, new MpscLinkedQueue());
            this.bufferSupplier = callable;
            this.timespan = j;
            this.unit = timeUnit;
            this.maxSize = i;
            this.restartTimerOnMaxSize = z;
            this.f403w = worker;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f402s, disposable)) {
                this.f402s = disposable;
                try {
                    this.buffer = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
                    this.actual.onSubscribe(this);
                    this.timer = this.f403w.schedulePeriodically(this, this.timespan, this.timespan, this.unit);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.f403w.dispose();
                    disposable.dispose();
                    EmptyDisposable.error(th, this.actual);
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
            fastPathOrderedEmit(r0, false, r12);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
            r13 = (java.util.Collection) p016io.reactivex.internal.functions.ObjectHelper.requireNonNull(r12.bufferSupplier.call(), "The buffer supplied is null");
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
            r12.timer = r12.f403w.schedulePeriodically(r12, r12.timespan, r12.timespan, r12.unit);
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
            dispose();
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
                r12.fastPathOrderedEmit(r0, r13, r12)
                java.util.concurrent.Callable<U> r13 = r12.bufferSupplier     // Catch:{ Throwable -> 0x0062 }
                java.lang.Object r13 = r13.call()     // Catch:{ Throwable -> 0x0062 }
                java.lang.String r0 = "The buffer supplied is null"
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
                io.reactivex.Scheduler$Worker r5 = r12.f403w
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
                r12.dispose()
                io.reactivex.Observer r12 = r12.actual
                r12.onError(r13)
                return
            L_0x006f:
                r13 = move-exception
                monitor-exit(r12)     // Catch:{ all -> 0x006f }
                throw r13
            */
            throw new UnsupportedOperationException("Method not decompiled: p016io.reactivex.internal.operators.observable.ObservableBufferTimed.BufferExactBoundedObserver.onNext(java.lang.Object):void");
        }

        public void onError(Throwable th) {
            this.f403w.dispose();
            synchronized (this) {
                this.buffer = null;
            }
            this.actual.onError(th);
        }

        public void onComplete() {
            U u;
            this.f403w.dispose();
            synchronized (this) {
                u = this.buffer;
                this.buffer = null;
            }
            this.queue.offer(u);
            this.done = true;
            if (enter()) {
                QueueDrainHelper.drainLoop(this.queue, this.actual, false, this, this);
            }
        }

        public void accept(Observer<? super U> observer, U u) {
            observer.onNext(u);
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.f403w.dispose();
                synchronized (this) {
                    this.buffer = null;
                }
                this.f402s.dispose();
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        public void run() {
            try {
                U u = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The bufferSupplier returned a null buffer");
                synchronized (this) {
                    U u2 = this.buffer;
                    if (u2 != null) {
                        if (this.producerIndex == this.consumerIndex) {
                            this.buffer = u;
                            fastPathOrderedEmit(u2, false, this);
                        }
                    }
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                dispose();
                this.actual.onError(th);
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableBufferTimed$BufferExactUnboundedObserver */
    static final class BufferExactUnboundedObserver<T, U extends Collection<? super T>> extends QueueDrainObserver<T, U, U> implements Runnable, Disposable {
        U buffer;
        final Callable<U> bufferSupplier;

        /* renamed from: s */
        Disposable f404s;
        final Scheduler scheduler;
        final AtomicReference<Disposable> timer = new AtomicReference<>();
        final long timespan;
        final TimeUnit unit;

        BufferExactUnboundedObserver(Observer<? super U> observer, Callable<U> callable, long j, TimeUnit timeUnit, Scheduler scheduler2) {
            super(observer, new MpscLinkedQueue());
            this.bufferSupplier = callable;
            this.timespan = j;
            this.unit = timeUnit;
            this.scheduler = scheduler2;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f404s, disposable)) {
                this.f404s = disposable;
                try {
                    this.buffer = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
                    this.actual.onSubscribe(this);
                    if (!this.cancelled) {
                        Disposable schedulePeriodicallyDirect = this.scheduler.schedulePeriodicallyDirect(this, this.timespan, this.timespan, this.unit);
                        if (!this.timer.compareAndSet(null, schedulePeriodicallyDirect)) {
                            schedulePeriodicallyDirect.dispose();
                        }
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    dispose();
                    EmptyDisposable.error(th, this.actual);
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

        public void onComplete() {
            U u;
            DisposableHelper.dispose(this.timer);
            synchronized (this) {
                u = this.buffer;
                this.buffer = null;
            }
            if (u != null) {
                this.queue.offer(u);
                this.done = true;
                if (enter()) {
                    QueueDrainHelper.drainLoop(this.queue, this.actual, false, this, this);
                }
            }
        }

        public void dispose() {
            DisposableHelper.dispose(this.timer);
            this.f404s.dispose();
        }

        public boolean isDisposed() {
            return this.timer.get() == DisposableHelper.DISPOSED;
        }

        public void run() {
            U u;
            try {
                U u2 = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The bufferSupplier returned a null buffer");
                synchronized (this) {
                    u = this.buffer;
                    if (u != null) {
                        this.buffer = u2;
                    }
                }
                if (u == null) {
                    DisposableHelper.dispose(this.timer);
                } else {
                    fastPathEmit(u, false, this);
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                dispose();
                this.actual.onError(th);
            }
        }

        public void accept(Observer<? super U> observer, U u) {
            this.actual.onNext(u);
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableBufferTimed$BufferSkipBoundedObserver */
    static final class BufferSkipBoundedObserver<T, U extends Collection<? super T>> extends QueueDrainObserver<T, U, U> implements Runnable, Disposable {
        final Callable<U> bufferSupplier;
        final List<U> buffers = new LinkedList();

        /* renamed from: s */
        Disposable f405s;
        final long timeskip;
        final long timespan;
        final TimeUnit unit;

        /* renamed from: w */
        final Worker f406w;

        BufferSkipBoundedObserver(Observer<? super U> observer, Callable<U> callable, long j, long j2, TimeUnit timeUnit, Worker worker) {
            super(observer, new MpscLinkedQueue());
            this.bufferSupplier = callable;
            this.timespan = j;
            this.timeskip = j2;
            this.unit = timeUnit;
            this.f406w = worker;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f405s, disposable)) {
                this.f405s = disposable;
                try {
                    final Collection collection = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
                    this.buffers.add(collection);
                    this.actual.onSubscribe(this);
                    this.f406w.schedulePeriodically(this, this.timeskip, this.timeskip, this.unit);
                    this.f406w.schedule(new Runnable() {
                        public void run() {
                            synchronized (BufferSkipBoundedObserver.this) {
                                BufferSkipBoundedObserver.this.buffers.remove(collection);
                            }
                            BufferSkipBoundedObserver.this.fastPathOrderedEmit(collection, false, BufferSkipBoundedObserver.this.f406w);
                        }
                    }, this.timespan, this.unit);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.f406w.dispose();
                    disposable.dispose();
                    EmptyDisposable.error(th, this.actual);
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
            this.f406w.dispose();
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
                QueueDrainHelper.drainLoop(this.queue, this.actual, false, this.f406w, this);
            }
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.f406w.dispose();
                clear();
                this.f405s.dispose();
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
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
                    final Collection collection = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The bufferSupplier returned a null buffer");
                    synchronized (this) {
                        if (!this.cancelled) {
                            this.buffers.add(collection);
                            this.f406w.schedule(new Runnable() {
                                public void run() {
                                    synchronized (BufferSkipBoundedObserver.this) {
                                        BufferSkipBoundedObserver.this.buffers.remove(collection);
                                    }
                                    BufferSkipBoundedObserver.this.fastPathOrderedEmit(collection, false, BufferSkipBoundedObserver.this.f406w);
                                }
                            }, this.timespan, this.unit);
                        }
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    dispose();
                    this.actual.onError(th);
                }
            }
        }

        public void accept(Observer<? super U> observer, U u) {
            observer.onNext(u);
        }
    }

    public ObservableBufferTimed(ObservableSource<T> observableSource, long j, long j2, TimeUnit timeUnit, Scheduler scheduler2, Callable<U> callable, int i, boolean z) {
        super(observableSource);
        this.timespan = j;
        this.timeskip = j2;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
        this.bufferSupplier = callable;
        this.maxSize = i;
        this.restartTimerOnMaxSize = z;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super U> observer) {
        if (this.timespan == this.timeskip && this.maxSize == Integer.MAX_VALUE) {
            ObservableSource observableSource = this.source;
            BufferExactUnboundedObserver bufferExactUnboundedObserver = new BufferExactUnboundedObserver(new SerializedObserver(observer), this.bufferSupplier, this.timespan, this.unit, this.scheduler);
            observableSource.subscribe(bufferExactUnboundedObserver);
            return;
        }
        Worker createWorker = this.scheduler.createWorker();
        if (this.timespan == this.timeskip) {
            ObservableSource observableSource2 = this.source;
            BufferExactBoundedObserver bufferExactBoundedObserver = new BufferExactBoundedObserver(new SerializedObserver(observer), this.bufferSupplier, this.timespan, this.unit, this.maxSize, this.restartTimerOnMaxSize, createWorker);
            observableSource2.subscribe(bufferExactBoundedObserver);
            return;
        }
        ObservableSource observableSource3 = this.source;
        BufferSkipBoundedObserver bufferSkipBoundedObserver = new BufferSkipBoundedObserver(new SerializedObserver(observer), this.bufferSupplier, this.timespan, this.timeskip, this.unit, createWorker);
        observableSource3.subscribe(bufferSkipBoundedObserver);
    }
}
