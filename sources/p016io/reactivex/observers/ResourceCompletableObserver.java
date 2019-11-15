package p016io.reactivex.observers;

import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.disposables.ListCompositeDisposable;
import p016io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.observers.ResourceCompletableObserver */
public abstract class ResourceCompletableObserver implements CompletableObserver, Disposable {
    private final ListCompositeDisposable resources = new ListCompositeDisposable();

    /* renamed from: s */
    private final AtomicReference<Disposable> f559s = new AtomicReference<>();

    /* access modifiers changed from: protected */
    public void onStart() {
    }

    public final void add(Disposable disposable) {
        ObjectHelper.requireNonNull(disposable, "resource is null");
        this.resources.add(disposable);
    }

    public final void onSubscribe(Disposable disposable) {
        if (DisposableHelper.setOnce(this.f559s, disposable)) {
            onStart();
        }
    }

    public final void dispose() {
        if (DisposableHelper.dispose(this.f559s)) {
            this.resources.dispose();
        }
    }

    public final boolean isDisposed() {
        return DisposableHelper.isDisposed((Disposable) this.f559s.get());
    }
}
