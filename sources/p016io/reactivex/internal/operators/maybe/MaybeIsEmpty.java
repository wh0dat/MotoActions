package p016io.reactivex.internal.operators.maybe;

import p016io.reactivex.MaybeObserver;
import p016io.reactivex.MaybeSource;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeIsEmpty */
public final class MaybeIsEmpty<T> extends AbstractMaybeWithUpstream<T, Boolean> {

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeIsEmpty$IsEmptyMaybeObserver */
    static final class IsEmptyMaybeObserver<T> implements MaybeObserver<T>, Disposable {
        final MaybeObserver<? super Boolean> actual;

        /* renamed from: d */
        Disposable f381d;

        IsEmptyMaybeObserver(MaybeObserver<? super Boolean> maybeObserver) {
            this.actual = maybeObserver;
        }

        public void dispose() {
            this.f381d.dispose();
        }

        public boolean isDisposed() {
            return this.f381d.isDisposed();
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f381d, disposable)) {
                this.f381d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T t) {
            this.actual.onSuccess(Boolean.valueOf(false));
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onComplete() {
            this.actual.onSuccess(Boolean.valueOf(true));
        }
    }

    public MaybeIsEmpty(MaybeSource<T> maybeSource) {
        super(maybeSource);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super Boolean> maybeObserver) {
        this.source.subscribe(new IsEmptyMaybeObserver(maybeObserver));
    }
}
