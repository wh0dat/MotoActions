package p016io.reactivex.internal.operators.maybe;

import p016io.reactivex.MaybeObserver;
import p016io.reactivex.MaybeSource;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.CompositeException;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeOnErrorReturn */
public final class MaybeOnErrorReturn<T> extends AbstractMaybeWithUpstream<T, T> {
    final Function<? super Throwable, ? extends T> valueSupplier;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeOnErrorReturn$OnErrorReturnMaybeObserver */
    static final class OnErrorReturnMaybeObserver<T> implements MaybeObserver<T>, Disposable {
        final MaybeObserver<? super T> actual;

        /* renamed from: d */
        Disposable f386d;
        final Function<? super Throwable, ? extends T> valueSupplier;

        OnErrorReturnMaybeObserver(MaybeObserver<? super T> maybeObserver, Function<? super Throwable, ? extends T> function) {
            this.actual = maybeObserver;
            this.valueSupplier = function;
        }

        public void dispose() {
            this.f386d.dispose();
        }

        public boolean isDisposed() {
            return this.f386d.isDisposed();
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f386d, disposable)) {
                this.f386d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T t) {
            this.actual.onSuccess(t);
        }

        public void onError(Throwable th) {
            try {
                this.actual.onSuccess(ObjectHelper.requireNonNull(this.valueSupplier.apply(th), "The valueSupplier returned a null value"));
            } catch (Throwable th2) {
                Exceptions.throwIfFatal(th2);
                this.actual.onError(new CompositeException(th, th2));
            }
        }

        public void onComplete() {
            this.actual.onComplete();
        }
    }

    public MaybeOnErrorReturn(MaybeSource<T> maybeSource, Function<? super Throwable, ? extends T> function) {
        super(maybeSource);
        this.valueSupplier = function;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> maybeObserver) {
        this.source.subscribe(new OnErrorReturnMaybeObserver(maybeObserver, this.valueSupplier));
    }
}
