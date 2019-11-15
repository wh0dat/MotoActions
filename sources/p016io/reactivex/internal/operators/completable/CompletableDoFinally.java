package p016io.reactivex.internal.operators.completable;

import java.util.concurrent.atomic.AtomicInteger;
import p016io.reactivex.Completable;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.CompletableSource;
import p016io.reactivex.annotations.Experimental;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Action;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

@Experimental
/* renamed from: io.reactivex.internal.operators.completable.CompletableDoFinally */
public final class CompletableDoFinally extends Completable {
    final Action onFinally;
    final CompletableSource source;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableDoFinally$DoFinallyObserver */
    static final class DoFinallyObserver extends AtomicInteger implements CompletableObserver, Disposable {
        private static final long serialVersionUID = 4109457741734051389L;
        final CompletableObserver actual;

        /* renamed from: d */
        Disposable f234d;
        final Action onFinally;

        DoFinallyObserver(CompletableObserver completableObserver, Action action) {
            this.actual = completableObserver;
            this.onFinally = action;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f234d, disposable)) {
                this.f234d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
            runFinally();
        }

        public void onComplete() {
            this.actual.onComplete();
            runFinally();
        }

        public void dispose() {
            this.f234d.dispose();
            runFinally();
        }

        public boolean isDisposed() {
            return this.f234d.isDisposed();
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

    public CompletableDoFinally(CompletableSource completableSource, Action action) {
        this.source = completableSource;
        this.onFinally = action;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver completableObserver) {
        this.source.subscribe(new DoFinallyObserver(completableObserver, this.onFinally));
    }
}
