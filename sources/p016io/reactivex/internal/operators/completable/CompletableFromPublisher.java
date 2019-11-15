package p016io.reactivex.internal.operators.completable;

import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Completable;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.internal.operators.completable.CompletableFromPublisher */
public final class CompletableFromPublisher<T> extends Completable {
    final Publisher<T> flowable;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableFromPublisher$FromPublisherSubscriber */
    static final class FromPublisherSubscriber<T> implements Subscriber<T>, Disposable {

        /* renamed from: cs */
        final CompletableObserver f236cs;

        /* renamed from: s */
        Subscription f237s;

        public void onNext(T t) {
        }

        FromPublisherSubscriber(CompletableObserver completableObserver) {
            this.f236cs = completableObserver;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f237s, subscription)) {
                this.f237s = subscription;
                this.f236cs.onSubscribe(this);
                subscription.request(LongCompanionObject.MAX_VALUE);
            }
        }

        public void onError(Throwable th) {
            this.f236cs.onError(th);
        }

        public void onComplete() {
            this.f236cs.onComplete();
        }

        public void dispose() {
            this.f237s.cancel();
            this.f237s = SubscriptionHelper.CANCELLED;
        }

        public boolean isDisposed() {
            return this.f237s == SubscriptionHelper.CANCELLED;
        }
    }

    public CompletableFromPublisher(Publisher<T> publisher) {
        this.flowable = publisher;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver completableObserver) {
        this.flowable.subscribe(new FromPublisherSubscriber(completableObserver));
    }
}
