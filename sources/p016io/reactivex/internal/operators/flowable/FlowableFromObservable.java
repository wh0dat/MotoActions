package p016io.reactivex.internal.operators.flowable;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Flowable;
import p016io.reactivex.Observable;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableFromObservable */
public final class FlowableFromObservable<T> extends Flowable<T> {
    private final Observable<T> upstream;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableFromObservable$SubscriberObserver */
    static class SubscriberObserver<T> implements Observer<T>, Subscription {

        /* renamed from: d */
        private Disposable f286d;

        /* renamed from: s */
        private final Subscriber<? super T> f287s;

        public void request(long j) {
        }

        SubscriberObserver(Subscriber<? super T> subscriber) {
            this.f287s = subscriber;
        }

        public void onComplete() {
            this.f287s.onComplete();
        }

        public void onError(Throwable th) {
            this.f287s.onError(th);
        }

        public void onNext(T t) {
            this.f287s.onNext(t);
        }

        public void onSubscribe(Disposable disposable) {
            this.f286d = disposable;
            this.f287s.onSubscribe(this);
        }

        public void cancel() {
            this.f286d.dispose();
        }
    }

    public FlowableFromObservable(Observable<T> observable) {
        this.upstream = observable;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        this.upstream.subscribe((Observer<? super T>) new SubscriberObserver<Object>(subscriber));
    }
}
