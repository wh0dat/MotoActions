package p016io.reactivex.internal.operators.maybe;

import p016io.reactivex.MaybeObserver;
import p016io.reactivex.MaybeSource;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.CompositeException;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.BiConsumer;
import p016io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeDoOnEvent */
public final class MaybeDoOnEvent<T> extends AbstractMaybeWithUpstream<T, T> {
    final BiConsumer<? super T, ? super Throwable> onEvent;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeDoOnEvent$DoOnEventMaybeObserver */
    static final class DoOnEventMaybeObserver<T> implements MaybeObserver<T>, Disposable {
        final MaybeObserver<? super T> actual;

        /* renamed from: d */
        Disposable f367d;
        final BiConsumer<? super T, ? super Throwable> onEvent;

        DoOnEventMaybeObserver(MaybeObserver<? super T> maybeObserver, BiConsumer<? super T, ? super Throwable> biConsumer) {
            this.actual = maybeObserver;
            this.onEvent = biConsumer;
        }

        public void dispose() {
            this.f367d.dispose();
            this.f367d = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.f367d.isDisposed();
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f367d, disposable)) {
                this.f367d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T t) {
            this.f367d = DisposableHelper.DISPOSED;
            try {
                this.onEvent.accept(t, null);
                this.actual.onSuccess(t);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.actual.onError(th);
            }
        }

        public void onError(Throwable th) {
            this.f367d = DisposableHelper.DISPOSED;
            try {
                this.onEvent.accept(null, th);
            } catch (Throwable th2) {
                Exceptions.throwIfFatal(th2);
                th = new CompositeException(th, th2);
            }
            this.actual.onError(th);
        }

        public void onComplete() {
            this.f367d = DisposableHelper.DISPOSED;
            try {
                this.onEvent.accept(null, null);
                this.actual.onComplete();
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.actual.onError(th);
            }
        }
    }

    public MaybeDoOnEvent(MaybeSource<T> maybeSource, BiConsumer<? super T, ? super Throwable> biConsumer) {
        super(maybeSource);
        this.onEvent = biConsumer;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> maybeObserver) {
        this.source.subscribe(new DoOnEventMaybeObserver(maybeObserver, this.onEvent));
    }
}
