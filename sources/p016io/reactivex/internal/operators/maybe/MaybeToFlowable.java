package p016io.reactivex.internal.operators.maybe;

import org.reactivestreams.Subscriber;
import p016io.reactivex.Flowable;
import p016io.reactivex.MaybeObserver;
import p016io.reactivex.MaybeSource;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.fuseable.HasUpstreamMaybeSource;
import p016io.reactivex.internal.subscriptions.DeferredScalarSubscription;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeToFlowable */
public final class MaybeToFlowable<T> extends Flowable<T> implements HasUpstreamMaybeSource<T> {
    final MaybeSource<T> source;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeToFlowable$MaybeToFlowableSubscriber */
    static final class MaybeToFlowableSubscriber<T> extends DeferredScalarSubscription<T> implements MaybeObserver<T> {
        private static final long serialVersionUID = 7603343402964826922L;

        /* renamed from: d */
        Disposable f388d;

        MaybeToFlowableSubscriber(Subscriber<? super T> subscriber) {
            super(subscriber);
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f388d, disposable)) {
                this.f388d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T t) {
            complete(t);
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void cancel() {
            super.cancel();
            this.f388d.dispose();
        }
    }

    public MaybeToFlowable(MaybeSource<T> maybeSource) {
        this.source = maybeSource;
    }

    public MaybeSource<T> source() {
        return this.source;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        this.source.subscribe(new MaybeToFlowableSubscriber(subscriber));
    }
}
