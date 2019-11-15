package p016io.reactivex.observers;

import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.disposables.ListCompositeDisposable;
import p016io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.observers.ResourceObserver */
public abstract class ResourceObserver<T> implements Observer<T>, Disposable {
    private final ListCompositeDisposable resources = new ListCompositeDisposable();

    /* renamed from: s */
    private final AtomicReference<Disposable> f561s = new AtomicReference<>();

    /* access modifiers changed from: protected */
    public void onStart() {
    }

    public final void add(Disposable disposable) {
        ObjectHelper.requireNonNull(disposable, "resource is null");
        this.resources.add(disposable);
    }

    public final void onSubscribe(Disposable disposable) {
        if (DisposableHelper.setOnce(this.f561s, disposable)) {
            onStart();
        }
    }

    public final void dispose() {
        if (DisposableHelper.dispose(this.f561s)) {
            this.resources.dispose();
        }
    }

    public final boolean isDisposed() {
        return DisposableHelper.isDisposed((Disposable) this.f561s.get());
    }
}
