package p016io.reactivex;

import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.functions.Cancellable;

/* renamed from: io.reactivex.SingleEmitter */
public interface SingleEmitter<T> {
    boolean isDisposed();

    void onError(Throwable th);

    void onSuccess(T t);

    void setCancellable(Cancellable cancellable);

    void setDisposable(Disposable disposable);
}
