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

/* renamed from: io.reactivex.internal.operators.observable.ObservableZip */
public final class ObservableZip<T, R> extends Observable<R> {
    final int bufferSize;
    final boolean delayError;
    final ObservableSource<? extends T>[] sources;
    final Iterable<? extends ObservableSource<? extends T>> sourcesIterable;
    final Function<? super Object[], ? extends R> zipper;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableZip$ZipCoordinator */
    static final class ZipCoordinator<T, R> extends AtomicInteger implements Disposable {
        private static final long serialVersionUID = 2983708048395377667L;
        final Observer<? super R> actual;
        volatile boolean cancelled;
        final boolean delayError;
        final ZipObserver<T, R>[] observers;
        final T[] row;
        final Function<? super Object[], ? extends R> zipper;

        ZipCoordinator(Observer<? super R> observer, Function<? super Object[], ? extends R> function, int i, boolean z) {
            this.actual = observer;
            this.zipper = function;
            this.observers = new ZipObserver[i];
            this.row = (Object[]) new Object[i];
            this.delayError = z;
        }

        public void subscribe(ObservableSource<? extends T>[] observableSourceArr, int i) {
            ZipObserver<T, R>[] zipObserverArr = this.observers;
            int length = zipObserverArr.length;
            for (int i2 = 0; i2 < length; i2++) {
                zipObserverArr[i2] = new ZipObserver<>(this, i);
            }
            lazySet(0);
            this.actual.onSubscribe(this);
            for (int i3 = 0; i3 < length && !this.cancelled; i3++) {
                observableSourceArr[i3].subscribe(zipObserverArr[i3]);
            }
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                if (getAndIncrement() == 0) {
                    clear();
                }
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: 0000 */
        public void clear() {
            ZipObserver<T, R>[] zipObserverArr;
            for (ZipObserver<T, R> zipObserver : this.observers) {
                zipObserver.dispose();
                zipObserver.queue.clear();
            }
        }

        public void drain() {
            int i;
            if (getAndIncrement() == 0) {
                ZipObserver<T, R>[] zipObserverArr = this.observers;
                Observer<? super R> observer = this.actual;
                T[] tArr = this.row;
                boolean z = this.delayError;
                int i2 = 1;
                while (true) {
                    int length = zipObserverArr.length;
                    int i3 = 0;
                    int i4 = 0;
                    int i5 = 0;
                    while (i3 < length) {
                        ZipObserver<T, R> zipObserver = zipObserverArr[i3];
                        if (tArr[i4] == null) {
                            boolean z2 = zipObserver.done;
                            T poll = zipObserver.queue.poll();
                            boolean z3 = poll == null;
                            i = i3;
                            if (!checkTerminated(z2, z3, observer, z, zipObserver)) {
                                if (!z3) {
                                    tArr[i4] = poll;
                                } else {
                                    i5++;
                                }
                            } else {
                                return;
                            }
                        } else {
                            ZipObserver<T, R> zipObserver2 = zipObserver;
                            i = i3;
                            if (zipObserver2.done && !z) {
                                Throwable th = zipObserver2.error;
                                if (th != null) {
                                    clear();
                                    observer.onError(th);
                                    return;
                                }
                            }
                        }
                        i4++;
                        i3 = i + 1;
                    }
                    if (i5 != 0) {
                        i2 = addAndGet(-i2);
                        if (i2 == 0) {
                            return;
                        }
                    } else {
                        try {
                            observer.onNext(ObjectHelper.requireNonNull(this.zipper.apply(tArr.clone()), "The zipper returned a null value"));
                            Arrays.fill(tArr, null);
                        } catch (Throwable th2) {
                            Throwable th3 = th2;
                            Exceptions.throwIfFatal(th3);
                            clear();
                            observer.onError(th3);
                            return;
                        }
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminated(boolean z, boolean z2, Observer<? super R> observer, boolean z3, ZipObserver<?, ?> zipObserver) {
            if (this.cancelled) {
                clear();
                return true;
            }
            if (z) {
                if (!z3) {
                    Throwable th = zipObserver.error;
                    if (th != null) {
                        clear();
                        observer.onError(th);
                        return true;
                    } else if (z2) {
                        clear();
                        observer.onComplete();
                        return true;
                    }
                } else if (z2) {
                    Throwable th2 = zipObserver.error;
                    clear();
                    if (th2 != null) {
                        observer.onError(th2);
                    } else {
                        observer.onComplete();
                    }
                    return true;
                }
            }
            return false;
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableZip$ZipObserver */
    static final class ZipObserver<T, R> implements Observer<T> {
        volatile boolean done;
        Throwable error;
        final ZipCoordinator<T, R> parent;
        final SpscLinkedArrayQueue<T> queue;

        /* renamed from: s */
        final AtomicReference<Disposable> f505s = new AtomicReference<>();

        ZipObserver(ZipCoordinator<T, R> zipCoordinator, int i) {
            this.parent = zipCoordinator;
            this.queue = new SpscLinkedArrayQueue<>(i);
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.setOnce(this.f505s, disposable);
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

        public void dispose() {
            DisposableHelper.dispose(this.f505s);
        }
    }

    public ObservableZip(ObservableSource<? extends T>[] observableSourceArr, Iterable<? extends ObservableSource<? extends T>> iterable, Function<? super Object[], ? extends R> function, int i, boolean z) {
        this.sources = observableSourceArr;
        this.sourcesIterable = iterable;
        this.zipper = function;
        this.bufferSize = i;
        this.delayError = z;
    }

    public void subscribeActual(Observer<? super R> observer) {
        ObservableSource<? extends T>[] observableSourceArr;
        int i;
        ObservableSource<? extends T>[] observableSourceArr2 = this.sources;
        if (observableSourceArr2 == null) {
            Observable[] observableArr = new Observable[8];
            observableSourceArr = observableArr;
            i = 0;
            for (ObservableSource<? extends T> observableSource : this.sourcesIterable) {
                if (i == observableSourceArr.length) {
                    ObservableSource<? extends T>[] observableSourceArr3 = new ObservableSource[((i >> 2) + i)];
                    System.arraycopy(observableSourceArr, 0, observableSourceArr3, 0, i);
                    observableSourceArr = observableSourceArr3;
                }
                int i2 = i + 1;
                observableSourceArr[i] = observableSource;
                i = i2;
            }
        } else {
            observableSourceArr = observableSourceArr2;
            i = observableSourceArr2.length;
        }
        if (i == 0) {
            EmptyDisposable.complete(observer);
        } else {
            new ZipCoordinator(observer, this.zipper, i, this.delayError).subscribe(observableSourceArr, this.bufferSize);
        }
    }
}
