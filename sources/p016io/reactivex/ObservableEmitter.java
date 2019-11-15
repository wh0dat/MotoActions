package p016io.reactivex;

import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.functions.Cancellable;

/* renamed from: io.reactivex.ObservableEmitter */
public interface ObservableEmitter<T> extends Emitter<T> {
    boolean isDisposed();

    ObservableEmitter<T> serialize();

    void setCancellable(Cancellable cancellable);

    void setDisposable(Disposable disposable);
}
