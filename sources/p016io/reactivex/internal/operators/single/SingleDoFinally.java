package p016io.reactivex.internal.operators.single;

import java.util.concurrent.atomic.AtomicInteger;
import p016io.reactivex.Single;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.SingleSource;
import p016io.reactivex.annotations.Experimental;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Action;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

@Experimental
/* renamed from: io.reactivex.internal.operators.single.SingleDoFinally */
public final class SingleDoFinally<T> extends Single<T> {
    final Action onFinally;
    final SingleSource<T> source;

    /* renamed from: io.reactivex.internal.operators.single.SingleDoFinally$DoFinallyObserver */
    static final class DoFinallyObserver<T> extends AtomicInteger implements SingleObserver<T>, Disposable {
        private static final long serialVersionUID = 4109457741734051389L;
        final SingleObserver<? super T> actual;

        /* renamed from: d */
        Disposable f510d;
        final Action onFinally;

        DoFinallyObserver(SingleObserver<? super T> singleObserver, Action action) {
            this.actual = singleObserver;
            this.onFinally = action;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f510d, disposable)) {
                this.f510d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T t) {
            this.actual.onSuccess(t);
            runFinally();
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
            runFinally();
        }

        public void dispose() {
            this.f510d.dispose();
            runFinally();
        }

        public boolean isDisposed() {
            return this.f510d.isDisposed();
        }

        /* access modifiers changed from: 0000 */
        public void runFinally() {
            if (compareAndSet(0, 1)) {
                try {
                    this.onFinally.run();
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    RxJavaPlugins.onError(th);
                }
            }
        }
    }

    public SingleDoFinally(SingleSource<T> singleSource, Action action) {
        this.source = singleSource;
        this.onFinally = action;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> singleObserver) {
        this.source.subscribe(new DoFinallyObserver(singleObserver, this.onFinally));
    }
}
