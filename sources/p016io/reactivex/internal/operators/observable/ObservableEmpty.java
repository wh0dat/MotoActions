package p016io.reactivex.internal.operators.observable;

import p016io.reactivex.Observable;
import p016io.reactivex.Observer;
import p016io.reactivex.internal.disposables.EmptyDisposable;
import p016io.reactivex.internal.fuseable.ScalarCallable;

/* renamed from: io.reactivex.internal.operators.observable.ObservableEmpty */
public final class ObservableEmpty extends Observable<Object> implements ScalarCallable<Object> {
    public static final Observable<Object> INSTANCE = new ObservableEmpty();

    public Object call() {
        return null;
    }

    private ObservableEmpty() {
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super Object> observer) {
        EmptyDisposable.complete(observer);
    }
}
