package p016io.reactivex.android;

import android.os.Looper;
import java.util.concurrent.atomic.AtomicBoolean;
import p016io.reactivex.android.schedulers.AndroidSchedulers;
import p016io.reactivex.disposables.Disposable;

/* renamed from: io.reactivex.android.MainThreadDisposable */
public abstract class MainThreadDisposable implements Disposable {
    private final AtomicBoolean unsubscribed = new AtomicBoolean();

    /* access modifiers changed from: protected */
    public abstract void onDispose();

    public static void verifyMainThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Expected to be called on the main thread but was ");
            sb.append(Thread.currentThread().getName());
            throw new IllegalStateException(sb.toString());
        }
    }

    public final boolean isDisposed() {
        return this.unsubscribed.get();
    }

    public final void dispose() {
        if (!this.unsubscribed.compareAndSet(false, true)) {
            return;
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            onDispose();
        } else {
            AndroidSchedulers.mainThread().scheduleDirect(new Runnable() {
                public void run() {
                    MainThreadDisposable.this.onDispose();
                }
            });
        }
    }
}
