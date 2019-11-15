package p016io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicInteger;
import p016io.reactivex.Observable;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.BiPredicate;
import p016io.reactivex.internal.disposables.ArrayCompositeDisposable;
import p016io.reactivex.internal.queue.SpscLinkedArrayQueue;

/* renamed from: io.reactivex.internal.operators.observable.ObservableSequenceEqual */
public final class ObservableSequenceEqual<T> extends Observable<Boolean> {
    final int bufferSize;
    final BiPredicate<? super T, ? super T> comparer;
    final ObservableSource<? extends T> first;
    final ObservableSource<? extends T> second;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSequenceEqual$EqualCoordinator */
    static final class EqualCoordinator<T> extends AtomicInteger implements Disposable {
        private static final long serialVersionUID = -6178010334400373240L;
        final Observer<? super Boolean> actual;
        volatile boolean cancelled;
        final BiPredicate<? super T, ? super T> comparer;
        final ObservableSource<? extends T> first;
        final EqualObserver<T>[] observers;
        final ArrayCompositeDisposable resources = new ArrayCompositeDisposable(2);
        final ObservableSource<? extends T> second;

        /* renamed from: v1 */
        T f460v1;

        /* renamed from: v2 */
        T f461v2;

        EqualCoordinator(Observer<? super Boolean> observer, int i, ObservableSource<? extends T> observableSource, ObservableSource<? extends T> observableSource2, BiPredicate<? super T, ? super T> biPredicate) {
            this.actual = observer;
            this.first = observableSource;
            this.second = observableSource2;
            this.comparer = biPredicate;
            EqualObserver<T>[] equalObserverArr = new EqualObserver[2];
            this.observers = equalObserverArr;
            equalObserverArr[0] = new EqualObserver<>(this, 0, i);
            equalObserverArr[1] = new EqualObserver<>(this, 1, i);
        }

        /* access modifiers changed from: 0000 */
        public boolean setDisposable(Disposable disposable, int i) {
            return this.resources.setResource(i, disposable);
        }

        /* access modifiers changed from: 0000 */
        public void subscribe() {
            EqualObserver<T>[] equalObserverArr = this.observers;
            this.first.subscribe(equalObserverArr[0]);
            this.second.subscribe(equalObserverArr[1]);
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.resources.dispose();
                if (getAndIncrement() == 0) {
                    EqualObserver<T>[] equalObserverArr = this.observers;
                    equalObserverArr[0].queue.clear();
                    equalObserverArr[1].queue.clear();
                }
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: 0000 */
        public void cancel(SpscLinkedArrayQueue<T> spscLinkedArrayQueue, SpscLinkedArrayQueue<T> spscLinkedArrayQueue2) {
            this.cancelled = true;
            spscLinkedArrayQueue.clear();
            spscLinkedArrayQueue2.clear();
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                EqualObserver<T>[] equalObserverArr = this.observers;
                EqualObserver<T> equalObserver = equalObserverArr[0];
                SpscLinkedArrayQueue<T> spscLinkedArrayQueue = equalObserver.queue;
                EqualObserver<T> equalObserver2 = equalObserverArr[1];
                SpscLinkedArrayQueue<T> spscLinkedArrayQueue2 = equalObserver2.queue;
                int i = 1;
                while (!this.cancelled) {
                    boolean z = equalObserver.done;
                    if (z) {
                        Throwable th = equalObserver.error;
                        if (th != null) {
                            cancel(spscLinkedArrayQueue, spscLinkedArrayQueue2);
                            this.actual.onError(th);
                            return;
                        }
                    }
                    boolean z2 = equalObserver2.done;
                    if (z2) {
                        Throwable th2 = equalObserver2.error;
                        if (th2 != null) {
                            cancel(spscLinkedArrayQueue, spscLinkedArrayQueue2);
                            this.actual.onError(th2);
                            return;
                        }
                    }
                    if (this.f460v1 == null) {
                        this.f460v1 = spscLinkedArrayQueue.poll();
                    }
                    boolean z3 = this.f460v1 == null;
                    if (this.f461v2 == null) {
                        this.f461v2 = spscLinkedArrayQueue2.poll();
                    }
                    boolean z4 = this.f461v2 == null;
                    if (z && z2 && z3 && z4) {
                        this.actual.onNext(Boolean.valueOf(true));
                        this.actual.onComplete();
                        return;
                    } else if (!z || !z2 || z3 == z4) {
                        if (!z3 && !z4) {
                            try {
                                if (!this.comparer.test(this.f460v1, this.f461v2)) {
                                    cancel(spscLinkedArrayQueue, spscLinkedArrayQueue2);
                                    this.actual.onNext(Boolean.valueOf(false));
                                    this.actual.onComplete();
                                    return;
                                }
                                this.f460v1 = null;
                                this.f461v2 = null;
                            } catch (Throwable th3) {
                                Exceptions.throwIfFatal(th3);
                                cancel(spscLinkedArrayQueue, spscLinkedArrayQueue2);
                                this.actual.onError(th3);
                                return;
                            }
                        }
                        if (z3 || z4) {
                            i = addAndGet(-i);
                            if (i == 0) {
                                return;
                            }
                        }
                    } else {
                        cancel(spscLinkedArrayQueue, spscLinkedArrayQueue2);
                        this.actual.onNext(Boolean.valueOf(false));
                        this.actual.onComplete();
                        return;
                    }
                }
                spscLinkedArrayQueue.clear();
                spscLinkedArrayQueue2.clear();
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSequenceEqual$EqualObserver */
    static final class EqualObserver<T> implements Observer<T> {
        volatile boolean done;
        Throwable error;
        final int index;
        final EqualCoordinator<T> parent;
        final SpscLinkedArrayQueue<T> queue;

        EqualObserver(EqualCoordinator<T> equalCoordinator, int i, int i2) {
            this.parent = equalCoordinator;
            this.index = i;
            this.queue = new SpscLinkedArrayQueue<>(i2);
        }

        public void onSubscribe(Disposable disposable) {
            this.parent.setDisposable(disposable, this.index);
        }

        public void onNext(T t) {
            this.queue.offer(t);
            this.parent.drain();
        }

        public void onError(Throwable th) {
            this.error = th;
            this.done = true;
            this.parent.drain();
        }

        public void onComplete() {
            this.done = true;
            this.parent.drain();
        }
    }

    public ObservableSequenceEqual(ObservableSource<? extends T> observableSource, ObservableSource<? extends T> observableSource2, BiPredicate<? super T, ? super T> biPredicate, int i) {
        this.first = observableSource;
        this.second = observableSource2;
        this.comparer = biPredicate;
        this.bufferSize = i;
    }

    public void subscribeActual(Observer<? super Boolean> observer) {
        EqualCoordinator equalCoordinator = new EqualCoordinator(observer, this.bufferSize, this.first, this.second, this.comparer);
        observer.onSubscribe(equalCoordinator);
        equalCoordinator.subscribe();
    }
}
