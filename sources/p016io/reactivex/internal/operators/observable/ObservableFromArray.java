package p016io.reactivex.internal.operators.observable;

import p016io.reactivex.Observable;
import p016io.reactivex.Observer;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.observers.BasicQueueDisposable;

/* renamed from: io.reactivex.internal.operators.observable.ObservableFromArray */
public final class ObservableFromArray<T> extends Observable<T> {
    final T[] array;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableFromArray$FromArrayDisposable */
    static final class FromArrayDisposable<T> extends BasicQueueDisposable<T> {
        final Observer<? super T> actual;
        final T[] array;
        volatile boolean disposed;
        boolean fusionMode;
        int index;

        FromArrayDisposable(Observer<? super T> observer, T[] tArr) {
            this.actual = observer;
            this.array = tArr;
        }

        public int requestFusion(int i) {
            if ((i & 1) == 0) {
                return 0;
            }
            this.fusionMode = true;
            return 1;
        }

        public T poll() {
            int i = this.index;
            T[] tArr = this.array;
            if (i == tArr.length) {
                return null;
            }
            this.index = i + 1;
            return ObjectHelper.requireNonNull(tArr[i], "The array element is null");
        }

        public boolean isEmpty() {
            return this.index == this.array.length;
        }

        public void clear() {
            this.index = this.array.length;
        }

        public void dispose() {
            this.disposed = true;
        }

        public boolean isDisposed() {
            return this.disposed;
        }

        /* access modifiers changed from: 0000 */
        public void run() {
            T[] tArr = this.array;
            int length = tArr.length;
            for (int i = 0; i < length && !isDisposed(); i++) {
                T t = tArr[i];
                if (t == null) {
                    Observer<? super T> observer = this.actual;
                    StringBuilder sb = new StringBuilder();
                    sb.append("The ");
                    sb.append(i);
                    sb.append("th element is null");
                    observer.onError(new NullPointerException(sb.toString()));
                    return;
                }
                this.actual.onNext(t);
            }
            if (!isDisposed()) {
                this.actual.onComplete();
            }
        }
    }

    public ObservableFromArray(T[] tArr) {
        this.array = tArr;
    }

    public void subscribeActual(Observer<? super T> observer) {
        FromArrayDisposable fromArrayDisposable = new FromArrayDisposable(observer, this.array);
        observer.onSubscribe(fromArrayDisposable);
        if (!fromArrayDisposable.fusionMode) {
            fromArrayDisposable.run();
        }
    }
}
