package p016io.reactivex.internal.observers;

import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.Notification;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.CompositeException;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Consumer;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.observers.ToNotificationObserver */
public final class ToNotificationObserver<T> extends AtomicReference<Disposable> implements Observer<T>, Disposable {
    private static final long serialVersionUID = -7420197867343208289L;
    final Consumer<? super Notification<Object>> consumer;

    public ToNotificationObserver(Consumer<? super Notification<Object>> consumer2) {
        this.consumer = consumer2;
    }

    public void onSubscribe(Disposable disposable) {
        DisposableHelper.setOnce(this, disposable);
    }

    public void onNext(T t) {
        if (t == null) {
            ((Disposable) get()).dispose();
            onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
            return;
        }
        try {
            this.consumer.accept(Notification.createOnNext(t));
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            ((Disposable) get()).dispose();
            onError(th);
        }
    }

    public void onError(Throwable th) {
        try {
            this.consumer.accept(Notification.createOnError(th));
        } catch (Throwable th2) {
            Exceptions.throwIfFatal(th2);
            RxJavaPlugins.onError(new CompositeException(th, th2));
        }
    }

    public void onComplete() {
        try {
            this.consumer.accept(Notification.createOnComplete());
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            RxJavaPlugins.onError(th);
        }
    }

    public void dispose() {
        DisposableHelper.dispose(this);
    }

    public boolean isDisposed() {
        return DisposableHelper.isDisposed((Disposable) get());
    }
}
