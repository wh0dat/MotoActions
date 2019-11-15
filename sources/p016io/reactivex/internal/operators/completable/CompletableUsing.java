package p016io.reactivex.internal.operators.completable;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.Completable;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.CompletableSource;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.CompositeException;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Consumer;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.disposables.EmptyDisposable;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.completable.CompletableUsing */
public final class CompletableUsing<R> extends Completable {
    final Function<? super R, ? extends CompletableSource> completableFunction;
    final Consumer<? super R> disposer;
    final boolean eager;
    final Callable<R> resourceSupplier;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableUsing$UsingObserver */
    static final class UsingObserver<R> extends AtomicReference<Object> implements CompletableObserver, Disposable {
        private static final long serialVersionUID = -674404550052917487L;
        final CompletableObserver actual;

        /* renamed from: d */
        Disposable f240d;
        final Consumer<? super R> disposer;
        final boolean eager;

        UsingObserver(CompletableObserver completableObserver, R r, Consumer<? super R> consumer, boolean z) {
            super(r);
            this.actual = completableObserver;
            this.disposer = consumer;
            this.eager = z;
        }

        public void dispose() {
            this.f240d.dispose();
            this.f240d = DisposableHelper.DISPOSED;
            disposeResourceAfter();
        }

        /* access modifiers changed from: 0000 */
        public void disposeResourceAfter() {
            Object andSet = getAndSet(this);
            if (andSet != this) {
                try {
                    this.disposer.accept(andSet);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    RxJavaPlugins.onError(th);
                }
            }
        }

        public boolean isDisposed() {
            return this.f240d.isDisposed();
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f240d, disposable)) {
                this.f240d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onError(Throwable th) {
            this.f240d = DisposableHelper.DISPOSED;
            if (this.eager) {
                Object andSet = getAndSet(this);
                if (andSet != this) {
                    try {
                        this.disposer.accept(andSet);
                    } catch (Throwable th2) {
                        Exceptions.throwIfFatal(th2);
                        th = new CompositeException(th, th2);
                    }
                } else {
                    return;
                }
            }
            this.actual.onError(th);
            if (!this.eager) {
                disposeResourceAfter();
            }
        }

        public void onComplete() {
            this.f240d = DisposableHelper.DISPOSED;
            if (this.eager) {
                Object andSet = getAndSet(this);
                if (andSet != this) {
                    try {
                        this.disposer.accept(andSet);
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        this.actual.onError(th);
                        return;
                    }
                } else {
                    return;
                }
            }
            this.actual.onComplete();
            if (!this.eager) {
                disposeResourceAfter();
            }
        }
    }

    public CompletableUsing(Callable<R> callable, Function<? super R, ? extends CompletableSource> function, Consumer<? super R> consumer, boolean z) {
        this.resourceSupplier = callable;
        this.completableFunction = function;
        this.disposer = consumer;
        this.eager = z;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver completableObserver) {
        try {
            Object call = this.resourceSupplier.call();
            try {
                ((CompletableSource) ObjectHelper.requireNonNull(this.completableFunction.apply(call), "The completableFunction returned a null CompletableSource")).subscribe(new UsingObserver(completableObserver, call, this.disposer, this.eager));
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                RxJavaPlugins.onError(th);
            }
        } catch (Throwable th2) {
            Exceptions.throwIfFatal(th2);
            EmptyDisposable.error(th2, completableObserver);
        }
    }
}
