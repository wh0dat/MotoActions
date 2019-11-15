package p016io.reactivex.internal.operators.observable;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.Observable;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.disposables.EmptyDisposable;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.queue.SpscLinkedArrayQueue;
import p016io.reactivex.internal.util.AtomicThrowable;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableCombineLatest */
public final class ObservableCombineLatest<T, R> extends Observable<R> {
    final int bufferSize;
    final Function<? super Object[], ? extends R> combiner;
    final boolean delayError;
    final ObservableSource<? extends T>[] sources;
    final Iterable<? extends ObservableSource<? extends T>> sourcesIterable;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableCombineLatest$CombinerObserver */
    static final class CombinerObserver<T, R> implements Observer<T> {
        final int index;
        final LatestCoordinator<T, R> parent;

        /* renamed from: s */
        final AtomicReference<Disposable> f411s = new AtomicReference<>();

        CombinerObserver(LatestCoordinator<T, R> latestCoordinator, int i) {
            this.parent = latestCoordinator;
            this.index = i;
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.setOnce(this.f411s, disposable);
        }

        public void onNext(T t) {
            this.parent.combine(t, this.index);
        }

        public void onError(Throwable th) {
            this.parent.onError(th);
            this.parent.combine(null, this.index);
        }

        public void onComplete() {
            this.parent.combine(null, this.index);
        }

        public void dispose() {
            DisposableHelper.dispose(this.f411s);
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableCombineLatest$LatestCoordinator */
    static final class LatestCoordinator<T, R> extends AtomicInteger implements Disposable {
        private static final long serialVersionUID = 8567835998786448817L;
        int active;
        final Observer<? super R> actual;
        volatile boolean cancelled;
        final Function<? super Object[], ? extends R> combiner;
        int complete;
        final boolean delayError;
        volatile boolean done;
        final AtomicThrowable errors = new AtomicThrowable();
        final T[] latest;
        final CombinerObserver<T, R>[] observers;
        final SpscLinkedArrayQueue<Object> queue;

        LatestCoordinator(Observer<? super R> observer, Function<? super Object[], ? extends R> function, int i, int i2, boolean z) {
            this.actual = observer;
            this.combiner = function;
            this.delayError = z;
            this.latest = (Object[]) new Object[i];
            this.observers = new CombinerObserver[i];
            this.queue = new SpscLinkedArrayQueue<>(i2);
        }

        public void subscribe(ObservableSource<? extends T>[] observableSourceArr) {
            CombinerObserver<T, R>[] combinerObserverArr = this.observers;
            int length = combinerObserverArr.length;
            for (int i = 0; i < length; i++) {
                combinerObserverArr[i] = new CombinerObserver<>(this, i);
            }
            lazySet(0);
            this.actual.onSubscribe(this);
            for (int i2 = 0; i2 < length && !this.cancelled; i2++) {
                observableSourceArr[i2].subscribe(combinerObserverArr[i2]);
            }
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                if (getAndIncrement() == 0) {
                    cancel(this.queue);
                }
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: 0000 */
        public void cancel(SpscLinkedArrayQueue<?> spscLinkedArrayQueue) {
            clear(spscLinkedArrayQueue);
            for (CombinerObserver<T, R> dispose : this.observers) {
                dispose.dispose();
            }
        }

        /* access modifiers changed from: 0000 */
        public void clear(SpscLinkedArrayQueue<?> spscLinkedArrayQueue) {
            synchronized (this) {
                Arrays.fill(this.latest, null);
            }
            spscLinkedArrayQueue.clear();
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x0057, code lost:
            if (r3 != false) goto L_0x005c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:0x0059, code lost:
            if (r7 == null) goto L_0x005c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:34:0x005b, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x005c, code lost:
            drain();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x005f, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void combine(T r7, int r8) {
            /*
                r6 = this;
                io.reactivex.internal.operators.observable.ObservableCombineLatest$CombinerObserver<T, R>[] r0 = r6.observers
                r0 = r0[r8]
                monitor-enter(r6)
                boolean r1 = r6.cancelled     // Catch:{ all -> 0x0060 }
                if (r1 == 0) goto L_0x000b
                monitor-exit(r6)     // Catch:{ all -> 0x0060 }
                return
            L_0x000b:
                T[] r1 = r6.latest     // Catch:{ all -> 0x0060 }
                int r1 = r1.length     // Catch:{ all -> 0x0060 }
                T[] r2 = r6.latest     // Catch:{ all -> 0x0060 }
                r2 = r2[r8]     // Catch:{ all -> 0x0060 }
                int r3 = r6.active     // Catch:{ all -> 0x0060 }
                if (r2 != 0) goto L_0x001a
                int r3 = r3 + 1
                r6.active = r3     // Catch:{ all -> 0x0060 }
            L_0x001a:
                int r4 = r6.complete     // Catch:{ all -> 0x0060 }
                if (r7 != 0) goto L_0x0023
                int r4 = r4 + 1
                r6.complete = r4     // Catch:{ all -> 0x0060 }
                goto L_0x0027
            L_0x0023:
                T[] r5 = r6.latest     // Catch:{ all -> 0x0060 }
                r5[r8] = r7     // Catch:{ all -> 0x0060 }
            L_0x0027:
                r8 = 0
                r5 = 1
                if (r3 != r1) goto L_0x002d
                r3 = r5
                goto L_0x002e
            L_0x002d:
                r3 = r8
            L_0x002e:
                if (r4 == r1) goto L_0x0034
                if (r7 != 0) goto L_0x0035
                if (r2 != 0) goto L_0x0035
            L_0x0034:
                r8 = r5
            L_0x0035:
                if (r8 != 0) goto L_0x0054
                if (r7 == 0) goto L_0x0047
                if (r3 == 0) goto L_0x0047
                io.reactivex.internal.queue.SpscLinkedArrayQueue<java.lang.Object> r8 = r6.queue     // Catch:{ all -> 0x0060 }
                T[] r1 = r6.latest     // Catch:{ all -> 0x0060 }
                java.lang.Object r1 = r1.clone()     // Catch:{ all -> 0x0060 }
                r8.offer(r0, r1)     // Catch:{ all -> 0x0060 }
                goto L_0x0056
            L_0x0047:
                if (r7 != 0) goto L_0x0056
                io.reactivex.internal.util.AtomicThrowable r8 = r6.errors     // Catch:{ all -> 0x0060 }
                java.lang.Object r8 = r8.get()     // Catch:{ all -> 0x0060 }
                if (r8 == 0) goto L_0x0056
                r6.done = r5     // Catch:{ all -> 0x0060 }
                goto L_0x0056
            L_0x0054:
                r6.done = r5     // Catch:{ all -> 0x0060 }
            L_0x0056:
                monitor-exit(r6)     // Catch:{ all -> 0x0060 }
                if (r3 != 0) goto L_0x005c
                if (r7 == 0) goto L_0x005c
                return
            L_0x005c:
                r6.drain()
                return
            L_0x0060:
                r7 = move-exception
                monitor-exit(r6)     // Catch:{ all -> 0x0060 }
                throw r7
            */
            throw new UnsupportedOperationException("Method not decompiled: p016io.reactivex.internal.operators.observable.ObservableCombineLatest.LatestCoordinator.combine(java.lang.Object, int):void");
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                SpscLinkedArrayQueue<Object> spscLinkedArrayQueue = this.queue;
                Observer<? super R> observer = this.actual;
                boolean z = this.delayError;
                int i = 1;
                do {
                    if (!checkTerminated(this.done, spscLinkedArrayQueue.isEmpty(), observer, spscLinkedArrayQueue, z)) {
                        while (true) {
                            boolean z2 = this.done;
                            boolean z3 = ((CombinerObserver) spscLinkedArrayQueue.poll()) == null;
                            if (!checkTerminated(z2, z3, observer, spscLinkedArrayQueue, z)) {
                                if (z3) {
                                    i = addAndGet(-i);
                                } else {
                                    try {
                                        observer.onNext(ObjectHelper.requireNonNull(this.combiner.apply((Object[]) spscLinkedArrayQueue.poll()), "The combiner returned a null"));
                                    } catch (Throwable th) {
                                        Exceptions.throwIfFatal(th);
                                        this.cancelled = true;
                                        cancel(spscLinkedArrayQueue);
                                        observer.onError(th);
                                        return;
                                    }
                                }
                            } else {
                                return;
                            }
                        }
                    } else {
                        return;
                    }
                } while (i != 0);
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminated(boolean z, boolean z2, Observer<?> observer, SpscLinkedArrayQueue<?> spscLinkedArrayQueue, boolean z3) {
            if (this.cancelled) {
                cancel(spscLinkedArrayQueue);
                return true;
            }
            if (z) {
                if (z3) {
                    if (z2) {
                        clear(this.queue);
                        Throwable terminate = this.errors.terminate();
                        if (terminate != null) {
                            observer.onError(terminate);
                        } else {
                            observer.onComplete();
                        }
                        return true;
                    }
                } else if (((Throwable) this.errors.get()) != null) {
                    cancel(spscLinkedArrayQueue);
                    observer.onError(this.errors.terminate());
                    return true;
                } else if (z2) {
                    clear(this.queue);
                    observer.onComplete();
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: 0000 */
        public void onError(Throwable th) {
            if (!this.errors.addThrowable(th)) {
                RxJavaPlugins.onError(th);
            }
        }
    }

    public ObservableCombineLatest(ObservableSource<? extends T>[] observableSourceArr, Iterable<? extends ObservableSource<? extends T>> iterable, Function<? super Object[], ? extends R> function, int i, boolean z) {
        this.sources = observableSourceArr;
        this.sourcesIterable = iterable;
        this.combiner = function;
        this.bufferSize = i;
        this.delayError = z;
    }

    public void subscribeActual(Observer<? super R> observer) {
        int i;
        ObservableSource<? extends T>[] observableSourceArr = this.sources;
        if (observableSourceArr == null) {
            Observable[] observableArr = new Observable[8];
            ObservableSource<? extends T>[] observableSourceArr2 = observableArr;
            int i2 = 0;
            for (ObservableSource<? extends T> observableSource : this.sourcesIterable) {
                if (i2 == observableSourceArr2.length) {
                    ObservableSource<? extends T>[] observableSourceArr3 = new ObservableSource[((i2 >> 2) + i2)];
                    System.arraycopy(observableSourceArr2, 0, observableSourceArr3, 0, i2);
                    observableSourceArr2 = observableSourceArr3;
                }
                int i3 = i2 + 1;
                observableSourceArr2[i2] = observableSource;
                i2 = i3;
            }
            i = i2;
            observableSourceArr = observableSourceArr2;
        } else {
            i = observableSourceArr.length;
        }
        if (i == 0) {
            EmptyDisposable.complete(observer);
            return;
        }
        LatestCoordinator latestCoordinator = new LatestCoordinator(observer, this.combiner, i, this.bufferSize, this.delayError);
        latestCoordinator.subscribe(observableSourceArr);
    }
}
