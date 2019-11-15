package p016io.reactivex.internal.operators.maybe;

import p016io.reactivex.MaybeObserver;
import p016io.reactivex.MaybeSource;
import p016io.reactivex.Observable;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.fuseable.HasUpstreamMaybeSource;
import p016io.reactivex.internal.observers.DeferredScalarDisposable;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeToObservable */
public final class MaybeToObservable<T> extends Observable<T> implements HasUpstreamMaybeSource<T> {
    final MaybeSource<T> source;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeToObservable$MaybeToFlowableSubscriber */
    static final class MaybeToFlowableSubscriber<T> extends DeferredScalarDisposable<T> implements MaybeObserver<T> {
        private static final long serialVersionUID = 7603343402964826922L;

        /* renamed from: d */
        Disposable f389d;

        MaybeToFlowableSubscriber(Observer<? super T> observer) {
            super(observer);
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f389d, disposable)) {
                this.f389d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T t) {
            complete(t);
        }

        public void onError(Throwable th) {
            error(th);
        }

        public void onComplete() {
            complete();
        }

        public void dispose() {
            super.dispose();
            this.f389d.dispose();
        }
    }

    public MaybeToObservable(MaybeSource<T> maybeSource) {
        this.source = maybeSource;
    }

    public MaybeSource<T> source() {
        return this.source;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        this.source.subscribe(new MaybeToFlowableSubscriber(observer));
    }
}
