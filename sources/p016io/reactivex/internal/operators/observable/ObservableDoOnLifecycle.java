package p016io.reactivex.internal.operators.observable;

import p016io.reactivex.Observable;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.functions.Action;
import p016io.reactivex.functions.Consumer;
import p016io.reactivex.internal.observers.DisposableLambdaObserver;

/* renamed from: io.reactivex.internal.operators.observable.ObservableDoOnLifecycle */
public final class ObservableDoOnLifecycle<T> extends AbstractObservableWithUpstream<T, T> {
    private final Action onDispose;
    private final Consumer<? super Disposable> onSubscribe;

    public ObservableDoOnLifecycle(Observable<T> observable, Consumer<? super Disposable> consumer, Action action) {
        super(observable);
        this.onSubscribe = consumer;
        this.onDispose = action;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        this.source.subscribe(new DisposableLambdaObserver(observer, this.onSubscribe, this.onDispose));
    }
}
