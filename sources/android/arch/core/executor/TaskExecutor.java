package android.arch.core.executor;

import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;

@RestrictTo({Scope.LIBRARY_GROUP})
public abstract class TaskExecutor {
    public abstract void executeOnDiskIO(Runnable runnable);

    public abstract boolean isMainThread();

    public abstract void postToMainThread(Runnable runnable);

    public void executeOnMainThread(Runnable runnable) {
        if (isMainThread()) {
            runnable.run();
        } else {
            postToMainThread(runnable);
        }
    }
}
