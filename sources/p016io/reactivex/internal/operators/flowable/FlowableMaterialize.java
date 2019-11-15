package p016io.reactivex.internal.operators.flowable;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import p016io.reactivex.Notification;
import p016io.reactivex.internal.subscribers.SinglePostCompleteSubscriber;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableMaterialize */
public final class FlowableMaterialize<T> extends AbstractFlowableWithUpstream<T, Notification<T>> {

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableMaterialize$MaterializeSubscriber */
    static final class MaterializeSubscriber<T> extends SinglePostCompleteSubscriber<T, Notification<T>> {
        private static final long serialVersionUID = -3740826063558713822L;

        MaterializeSubscriber(Subscriber<? super Notification<T>> subscriber) {
            super(subscriber);
        }

        public void onNext(T t) {
            this.produced++;
            this.actual.onNext(Notification.createOnNext(t));
        }

        public void onError(Throwable th) {
            complete(Notification.createOnError(th));
        }

        public void onComplete() {
            complete(Notification.createOnComplete());
        }

        /* access modifiers changed from: protected */
        public void onDrop(Notification<T> notification) {
            if (notification.isOnError()) {
                RxJavaPlugins.onError(notification.getError());
            }
        }
    }

    public FlowableMaterialize(Publisher<T> publisher) {
        super(publisher);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super Notification<T>> subscriber) {
        this.source.subscribe(new MaterializeSubscriber(subscriber));
    }
}
