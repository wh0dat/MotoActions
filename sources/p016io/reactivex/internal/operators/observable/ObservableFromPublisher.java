package p016io.reactivex.internal.operators.observable;

import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Observable;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.internal.operators.observable.ObservableFromPublisher */
public final class ObservableFromPublisher<T> extends Observable<T> {
    final Publisher<? extends T> source;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableFromPublisher$PublisherSubscriber */
    static final class PublisherSubscriber<T> implements Subscriber<T>, Disposable {
        final Observer<? super T> actual;

        /* renamed from: s */
        Subscription f438s;

        PublisherSubscriber(Observer<? super T> observer) {
            this.actual = observer;
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f438s, subscription)) {
                this.f438s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(LongCompanionObject.MAX_VALUE);
            }
        }

        public void dispose() {
            this.f438s.cancel();
            this.f438s = SubscriptionHelper.CANCELLED;
        }

        public boolean isDisposed() {
            return this.f438s == SubscriptionHelper.CANCELLED;
        }
    }

    public ObservableFromPublisher(Publisher<? extends T> publisher) {
        this.source = publisher;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        this.source.subscribe(new PublisherSubscriber(observer));
    }
}
