package p016io.reactivex.internal.operators.maybe;

import p016io.reactivex.MaybeObserver;
import p016io.reactivex.MaybeSource;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeMap */
public final class MaybeMap<T, R> extends AbstractMaybeWithUpstream<T, R> {
    final Function<? super T, ? extends R> mapper;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeMap$MapMaybeObserver */
    static final class MapMaybeObserver<T, R> implements MaybeObserver<T>, Disposable {
        final MaybeObserver<? super R> actual;

        /* renamed from: d */
        Disposable f383d;
        final Function<? super T, ? extends R> mapper;

        MapMaybeObserver(MaybeObserver<? super R> maybeObserver, Function<? super T, ? extends R> function) {
            this.actual = maybeObserver;
            this.mapper = function;
        }

        public void dispose() {
            Disposable disposable = this.f383d;
            this.f383d = DisposableHelper.DISPOSED;
            disposable.dispose();
        }

        public boolean isDisposed() {
            return this.f383d.isDisposed();
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f383d, disposable)) {
                this.f383d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T t) {
            try {
                this.actual.onSuccess(ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null item"));
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.actual.onError(th);
            }
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onComplete() {
            this.actual.onComplete();
        }
    }

    public MaybeMap(MaybeSource<T> maybeSource, Function<? super T, ? extends R> function) {
        super(maybeSource);
        this.mapper = function;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super R> maybeObserver) {
        this.source.subscribe(new MapMaybeObserver(maybeObserver, this.mapper));
    }
}
