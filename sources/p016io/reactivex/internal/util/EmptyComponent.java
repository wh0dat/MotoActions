package p016io.reactivex.internal.util;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.MaybeObserver;
import p016io.reactivex.Observer;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.util.EmptyComponent */
public enum EmptyComponent implements Subscriber<Object>, Observer<Object>, MaybeObserver<Object>, SingleObserver<Object>, CompletableObserver, Subscription, Disposable {
    INSTANCE;

    public void cancel() {
    }

    public void dispose() {
    }

    public boolean isDisposed() {
        return true;
    }

    public void onComplete() {
    }

    public void onNext(Object obj) {
    }

    public void onSuccess(Object obj) {
    }

    public void request(long j) {
    }

    public static <T> Subscriber<T> asSubscriber() {
        return INSTANCE;
    }

    public static <T> Observer<T> asObserver() {
        return INSTANCE;
    }

    public void onSubscribe(Disposable disposable) {
        disposable.dispose();
    }

    public void onSubscribe(Subscription subscription) {
        subscription.cancel();
    }

    public void onError(Throwable th) {
        RxJavaPlugins.onError(th);
    }
}
