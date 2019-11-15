package p016io.reactivex.internal.operators.maybe;

import p016io.reactivex.Maybe;
import p016io.reactivex.MaybeObserver;
import p016io.reactivex.MaybeSource;
import p016io.reactivex.Single;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.fuseable.FuseToMaybe;
import p016io.reactivex.internal.fuseable.HasUpstreamMaybeSource;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeIsEmptySingle */
public final class MaybeIsEmptySingle<T> extends Single<Boolean> implements HasUpstreamMaybeSource<T>, FuseToMaybe<Boolean> {
    final MaybeSource<T> source;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeIsEmptySingle$IsEmptyMaybeObserver */
    static final class IsEmptyMaybeObserver<T> implements MaybeObserver<T>, Disposable {
        final SingleObserver<? super Boolean> actual;

        /* renamed from: d */
        Disposable f382d;

        IsEmptyMaybeObserver(SingleObserver<? super Boolean> singleObserver) {
            this.actual = singleObserver;
        }

        public void dispose() {
            this.f382d.dispose();
            this.f382d = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.f382d.isDisposed();
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f382d, disposable)) {
                this.f382d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T t) {
            this.f382d = DisposableHelper.DISPOSED;
            this.actual.onSuccess(Boolean.valueOf(false));
        }

        public void onError(Throwable th) {
            this.f382d = DisposableHelper.DISPOSED;
            this.actual.onError(th);
        }

        public void onComplete() {
            this.f382d = DisposableHelper.DISPOSED;
            this.actual.onSuccess(Boolean.valueOf(true));
        }
    }

    public MaybeIsEmptySingle(MaybeSource<T> maybeSource) {
        this.source = maybeSource;
    }

    public MaybeSource<T> source() {
        return this.source;
    }

    public Maybe<Boolean> fuseToMaybe() {
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeIsEmpty<T>(this.source));
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super Boolean> singleObserver) {
        this.source.subscribe(new IsEmptyMaybeObserver(singleObserver));
    }
}
