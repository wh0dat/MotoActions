package p016io.reactivex.internal.operators.observable;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.fuseable.QueueDisposable;
import p016io.reactivex.internal.fuseable.SimplePlainQueue;
import p016io.reactivex.internal.fuseable.SimpleQueue;
import p016io.reactivex.internal.queue.SpscArrayQueue;
import p016io.reactivex.internal.queue.SpscLinkedArrayQueue;
import p016io.reactivex.internal.util.AtomicThrowable;
import p016io.reactivex.internal.util.ExceptionHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableFlatMap */
public final class ObservableFlatMap<T, U> extends AbstractObservableWithUpstream<T, U> {
    final int bufferSize;
    final boolean delayErrors;
    final Function<? super T, ? extends ObservableSource<? extends U>> mapper;
    final int maxConcurrency;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableFlatMap$InnerObserver */
    static final class InnerObserver<T, U> extends AtomicReference<Disposable> implements Observer<U> {
        private static final long serialVersionUID = -4606175640614850599L;
        volatile boolean done;
        int fusionMode;

        /* renamed from: id */
        final long f430id;
        final MergeObserver<T, U> parent;
        volatile SimpleQueue<U> queue;

        InnerObserver(MergeObserver<T, U> mergeObserver, long j) {
            this.f430id = j;
            this.parent = mergeObserver;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.setOnce(this, disposable) && (disposable instanceof QueueDisposable)) {
                QueueDisposable queueDisposable = (QueueDisposable) disposable;
                int requestFusion = queueDisposable.requestFusion(3);
                if (requestFusion == 1) {
                    this.fusionMode = requestFusion;
                    this.queue = queueDisposable;
                    this.done = true;
                    this.parent.drain();
                } else if (requestFusion == 2) {
                    this.fusionMode = requestFusion;
                    this.queue = queueDisposable;
                }
            }
        }

        public void onNext(U u) {
            if (this.fusionMode == 0) {
                this.parent.tryEmit(u, this);
            } else {
                this.parent.drain();
            }
        }

        public void onError(Throwable th) {
            if (this.parent.errors.addThrowable(th)) {
                if (!this.parent.delayErrors) {
                    this.parent.disposeAll();
                }
                this.done = true;
                this.parent.drain();
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void onComplete() {
            this.done = true;
            this.parent.drain();
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableFlatMap$MergeObserver */
    static final class MergeObserver<T, U> extends AtomicInteger implements Disposable, Observer<T> {
        static final InnerObserver<?, ?>[] CANCELLED = new InnerObserver[0];
        static final InnerObserver<?, ?>[] EMPTY = new InnerObserver[0];
        private static final long serialVersionUID = -2117620485640801370L;
        final Observer<? super U> actual;
        final int bufferSize;
        volatile boolean cancelled;
        final boolean delayErrors;
        volatile boolean done;
        final AtomicThrowable errors = new AtomicThrowable();
        long lastId;
        int lastIndex;
        final Function<? super T, ? extends ObservableSource<? extends U>> mapper;
        final int maxConcurrency;
        final AtomicReference<InnerObserver<?, ?>[]> observers;
        volatile SimplePlainQueue<U> queue;

        /* renamed from: s */
        Disposable f431s;
        Queue<ObservableSource<? extends U>> sources;
        long uniqueId;
        int wip;

        MergeObserver(Observer<? super U> observer, Function<? super T, ? extends ObservableSource<? extends U>> function, boolean z, int i, int i2) {
            this.actual = observer;
            this.mapper = function;
            this.delayErrors = z;
            this.maxConcurrency = i;
            this.bufferSize = i2;
            if (i != Integer.MAX_VALUE) {
                this.sources = new ArrayDeque(i);
            }
            this.observers = new AtomicReference<>(EMPTY);
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f431s, disposable)) {
                this.f431s = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    ObservableSource observableSource = (ObservableSource) ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null ObservableSource");
                    if (this.maxConcurrency != Integer.MAX_VALUE) {
                        synchronized (this) {
                            if (this.wip == this.maxConcurrency) {
                                this.sources.offer(observableSource);
                                return;
                            }
                            this.wip++;
                        }
                    }
                    subscribeInner(observableSource);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.f431s.dispose();
                    onError(th);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void subscribeInner(ObservableSource<? extends U> observableSource) {
            while (observableSource instanceof Callable) {
                tryEmitScalar((Callable) observableSource);
                if (this.maxConcurrency != Integer.MAX_VALUE) {
                    synchronized (this) {
                        observableSource = (ObservableSource) this.sources.poll();
                        if (observableSource == null) {
                            this.wip--;
                            return;
                        }
                    }
                } else {
                    return;
                }
            }
            long j = this.uniqueId;
            this.uniqueId = 1 + j;
            InnerObserver innerObserver = new InnerObserver(this, j);
            addInner(innerObserver);
            observableSource.subscribe(innerObserver);
        }

        /* access modifiers changed from: 0000 */
        public void addInner(InnerObserver<T, U> innerObserver) {
            InnerObserver<?, ?>[] innerObserverArr;
            InnerObserver[] innerObserverArr2;
            do {
                innerObserverArr = (InnerObserver[]) this.observers.get();
                if (innerObserverArr == CANCELLED) {
                    innerObserver.dispose();
                    return;
                }
                int length = innerObserverArr.length;
                innerObserverArr2 = new InnerObserver[(length + 1)];
                System.arraycopy(innerObserverArr, 0, innerObserverArr2, 0, length);
                innerObserverArr2[length] = innerObserver;
            } while (!this.observers.compareAndSet(innerObserverArr, innerObserverArr2));
        }

        /* access modifiers changed from: 0000 */
        public void removeInner(InnerObserver<T, U> innerObserver) {
            InnerObserver<T, U>[] innerObserverArr;
            Object obj;
            do {
                innerObserverArr = (InnerObserver[]) this.observers.get();
                int length = innerObserverArr.length;
                if (length != 0) {
                    int i = -1;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= length) {
                            break;
                        } else if (innerObserverArr[i2] == innerObserver) {
                            i = i2;
                            break;
                        } else {
                            i2++;
                        }
                    }
                    if (i >= 0) {
                        if (length == 1) {
                            obj = EMPTY;
                        } else {
                            InnerObserver[] innerObserverArr2 = new InnerObserver[(length - 1)];
                            System.arraycopy(innerObserverArr, 0, innerObserverArr2, 0, i);
                            System.arraycopy(innerObserverArr, i + 1, innerObserverArr2, i, (length - i) - 1);
                            obj = innerObserverArr2;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } while (!this.observers.compareAndSet(innerObserverArr, obj));
        }

        /* access modifiers changed from: 0000 */
        public void tryEmitScalar(Callable<? extends U> callable) {
            try {
                Object call = callable.call();
                if (call != null) {
                    if (get() != 0 || !compareAndSet(0, 1)) {
                        SimplePlainQueue<U> simplePlainQueue = this.queue;
                        if (simplePlainQueue == null) {
                            if (this.maxConcurrency == Integer.MAX_VALUE) {
                                simplePlainQueue = new SpscLinkedArrayQueue<>(this.bufferSize);
                            } else {
                                simplePlainQueue = new SpscArrayQueue<>(this.maxConcurrency);
                            }
                            this.queue = simplePlainQueue;
                        }
                        if (!simplePlainQueue.offer(call)) {
                            onError(new IllegalStateException("Scalar queue full?!"));
                            return;
                        } else if (getAndIncrement() != 0) {
                            return;
                        }
                    } else {
                        this.actual.onNext(call);
                        if (decrementAndGet() == 0) {
                            return;
                        }
                    }
                    drainLoop();
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.errors.addThrowable(th);
                drain();
            }
        }

        /* access modifiers changed from: 0000 */
        public void tryEmit(U u, InnerObserver<T, U> innerObserver) {
            if (get() != 0 || !compareAndSet(0, 1)) {
                SimpleQueue simpleQueue = innerObserver.queue;
                if (simpleQueue == null) {
                    simpleQueue = new SpscLinkedArrayQueue(this.bufferSize);
                    innerObserver.queue = simpleQueue;
                }
                simpleQueue.offer(u);
                if (getAndIncrement() != 0) {
                    return;
                }
            } else {
                this.actual.onNext(u);
                if (decrementAndGet() == 0) {
                    return;
                }
            }
            drainLoop();
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            if (this.errors.addThrowable(th)) {
                this.done = true;
                drain();
            } else {
                RxJavaPlugins.onError(th);
            }
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                drain();
            }
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                if (disposeAll()) {
                    Throwable terminate = this.errors.terminate();
                    if (terminate != null && terminate != ExceptionHelper.TERMINATED) {
                        RxJavaPlugins.onError(terminate);
                    }
                }
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                drainLoop();
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:55:0x00a6, code lost:
            if (r11 != null) goto L_0x0094;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drainLoop() {
            /*
                r12 = this;
                io.reactivex.Observer<? super U> r0 = r12.actual
                r1 = 1
                r2 = r1
            L_0x0004:
                boolean r3 = r12.checkTerminate()
                if (r3 == 0) goto L_0x000b
                return
            L_0x000b:
                io.reactivex.internal.fuseable.SimplePlainQueue<U> r3 = r12.queue
                if (r3 == 0) goto L_0x0023
            L_0x000f:
                boolean r4 = r12.checkTerminate()
                if (r4 == 0) goto L_0x0016
                return
            L_0x0016:
                java.lang.Object r4 = r3.poll()
                if (r4 != 0) goto L_0x001f
                if (r4 != 0) goto L_0x000f
                goto L_0x0023
            L_0x001f:
                r0.onNext(r4)
                goto L_0x000f
            L_0x0023:
                boolean r3 = r12.done
                io.reactivex.internal.fuseable.SimplePlainQueue<U> r4 = r12.queue
                java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.observable.ObservableFlatMap$InnerObserver<?, ?>[]> r5 = r12.observers
                java.lang.Object r5 = r5.get()
                io.reactivex.internal.operators.observable.ObservableFlatMap$InnerObserver[] r5 = (p016io.reactivex.internal.operators.observable.ObservableFlatMap.InnerObserver[]) r5
                int r6 = r5.length
                if (r3 == 0) goto L_0x0054
                if (r4 == 0) goto L_0x003a
                boolean r3 = r4.isEmpty()
                if (r3 == 0) goto L_0x0054
            L_0x003a:
                if (r6 != 0) goto L_0x0054
                io.reactivex.internal.util.AtomicThrowable r1 = r12.errors
                java.lang.Object r1 = r1.get()
                java.lang.Throwable r1 = (java.lang.Throwable) r1
                if (r1 != 0) goto L_0x004a
                r0.onComplete()
                goto L_0x0053
            L_0x004a:
                io.reactivex.internal.util.AtomicThrowable r12 = r12.errors
                java.lang.Throwable r12 = r12.terminate()
                r0.onError(r12)
            L_0x0053:
                return
            L_0x0054:
                r3 = 0
                if (r6 == 0) goto L_0x00f5
                long r7 = r12.lastId
                int r4 = r12.lastIndex
                if (r6 <= r4) goto L_0x0065
                r9 = r5[r4]
                long r9 = r9.f430id
                int r9 = (r9 > r7 ? 1 : (r9 == r7 ? 0 : -1))
                if (r9 == 0) goto L_0x0086
            L_0x0065:
                if (r6 > r4) goto L_0x0068
                r4 = r3
            L_0x0068:
                r9 = r4
                r4 = r3
            L_0x006a:
                if (r4 >= r6) goto L_0x007d
                r10 = r5[r9]
                long r10 = r10.f430id
                int r10 = (r10 > r7 ? 1 : (r10 == r7 ? 0 : -1))
                if (r10 != 0) goto L_0x0075
                goto L_0x007d
            L_0x0075:
                int r9 = r9 + 1
                if (r9 != r6) goto L_0x007a
                r9 = r3
            L_0x007a:
                int r4 = r4 + 1
                goto L_0x006a
            L_0x007d:
                r12.lastIndex = r9
                r4 = r5[r9]
                long r7 = r4.f430id
                r12.lastId = r7
                r4 = r9
            L_0x0086:
                r8 = r3
                r7 = r4
                r4 = r8
            L_0x0089:
                if (r4 >= r6) goto L_0x00ec
                boolean r9 = r12.checkTerminate()
                if (r9 == 0) goto L_0x0092
                return
            L_0x0092:
                r9 = r5[r7]
            L_0x0094:
                boolean r10 = r12.checkTerminate()
                if (r10 == 0) goto L_0x009b
                return
            L_0x009b:
                io.reactivex.internal.fuseable.SimpleQueue<U> r10 = r9.queue
                if (r10 != 0) goto L_0x00a0
                goto L_0x00a8
            L_0x00a0:
                java.lang.Object r11 = r10.poll()     // Catch:{ Throwable -> 0x00d1 }
                if (r11 != 0) goto L_0x00c7
                if (r11 != 0) goto L_0x0094
            L_0x00a8:
                boolean r10 = r9.done
                io.reactivex.internal.fuseable.SimpleQueue<U> r11 = r9.queue
                if (r10 == 0) goto L_0x00c1
                if (r11 == 0) goto L_0x00b6
                boolean r10 = r11.isEmpty()
                if (r10 == 0) goto L_0x00c1
            L_0x00b6:
                r12.removeInner(r9)
                boolean r8 = r12.checkTerminate()
                if (r8 == 0) goto L_0x00c0
                return
            L_0x00c0:
                r8 = r1
            L_0x00c1:
                int r7 = r7 + 1
                if (r7 != r6) goto L_0x00ea
                r7 = r3
                goto L_0x00ea
            L_0x00c7:
                r0.onNext(r11)
                boolean r11 = r12.checkTerminate()
                if (r11 == 0) goto L_0x00a0
                return
            L_0x00d1:
                r8 = move-exception
                p016io.reactivex.exceptions.Exceptions.throwIfFatal(r8)
                r9.dispose()
                io.reactivex.internal.util.AtomicThrowable r10 = r12.errors
                r10.addThrowable(r8)
                boolean r8 = r12.checkTerminate()
                if (r8 == 0) goto L_0x00e4
                return
            L_0x00e4:
                r12.removeInner(r9)
                int r4 = r4 + 1
                r8 = r1
            L_0x00ea:
                int r4 = r4 + r1
                goto L_0x0089
            L_0x00ec:
                r12.lastIndex = r7
                r3 = r5[r7]
                long r3 = r3.f430id
                r12.lastId = r3
                r3 = r8
            L_0x00f5:
                if (r3 == 0) goto L_0x011a
                int r3 = r12.maxConcurrency
                r4 = 2147483647(0x7fffffff, float:NaN)
                if (r3 == r4) goto L_0x0004
                monitor-enter(r12)
                java.util.Queue<io.reactivex.ObservableSource<? extends U>> r3 = r12.sources     // Catch:{ all -> 0x0117 }
                java.lang.Object r3 = r3.poll()     // Catch:{ all -> 0x0117 }
                io.reactivex.ObservableSource r3 = (p016io.reactivex.ObservableSource) r3     // Catch:{ all -> 0x0117 }
                if (r3 != 0) goto L_0x0111
                int r3 = r12.wip     // Catch:{ all -> 0x0117 }
                int r3 = r3 - r1
                r12.wip = r3     // Catch:{ all -> 0x0117 }
                monitor-exit(r12)     // Catch:{ all -> 0x0117 }
                goto L_0x0004
            L_0x0111:
                monitor-exit(r12)     // Catch:{ all -> 0x0117 }
                r12.subscribeInner(r3)
                goto L_0x0004
            L_0x0117:
                r0 = move-exception
                monitor-exit(r12)     // Catch:{ all -> 0x0117 }
                throw r0
            L_0x011a:
                int r2 = -r2
                int r2 = r12.addAndGet(r2)
                if (r2 != 0) goto L_0x0004
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: p016io.reactivex.internal.operators.observable.ObservableFlatMap.MergeObserver.drainLoop():void");
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminate() {
            if (this.cancelled) {
                return true;
            }
            Throwable th = (Throwable) this.errors.get();
            if (this.delayErrors || th == null) {
                return false;
            }
            disposeAll();
            this.actual.onError(this.errors.terminate());
            return true;
        }

        /* access modifiers changed from: 0000 */
        public boolean disposeAll() {
            this.f431s.dispose();
            if (((InnerObserver[]) this.observers.get()) != CANCELLED) {
                InnerObserver<?, ?>[] innerObserverArr = (InnerObserver[]) this.observers.getAndSet(CANCELLED);
                if (innerObserverArr != CANCELLED) {
                    for (InnerObserver<?, ?> dispose : innerObserverArr) {
                        dispose.dispose();
                    }
                    return true;
                }
            }
            return false;
        }
    }

    public ObservableFlatMap(ObservableSource<T> observableSource, Function<? super T, ? extends ObservableSource<? extends U>> function, boolean z, int i, int i2) {
        super(observableSource);
        this.mapper = function;
        this.delayErrors = z;
        this.maxConcurrency = i;
        this.bufferSize = i2;
    }

    public void subscribeActual(Observer<? super U> observer) {
        if (!ObservableScalarXMap.tryScalarXMapSubscribe(this.source, observer, this.mapper)) {
            ObservableSource observableSource = this.source;
            MergeObserver mergeObserver = new MergeObserver(observer, this.mapper, this.delayErrors, this.maxConcurrency, this.bufferSize);
            observableSource.subscribe(mergeObserver);
        }
    }
}
