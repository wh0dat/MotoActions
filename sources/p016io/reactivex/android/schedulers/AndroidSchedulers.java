package p016io.reactivex.android.schedulers;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Callable;
import p016io.reactivex.Scheduler;
import p016io.reactivex.android.plugins.RxAndroidPlugins;

/* renamed from: io.reactivex.android.schedulers.AndroidSchedulers */
public final class AndroidSchedulers {
    private static final Scheduler MAIN_THREAD = RxAndroidPlugins.initMainThreadScheduler(new Callable<Scheduler>() {
        public Scheduler call() throws Exception {
            return MainHolder.DEFAULT;
        }
    });

    /* renamed from: io.reactivex.android.schedulers.AndroidSchedulers$MainHolder */
    private static final class MainHolder {
        static final Scheduler DEFAULT = new HandlerScheduler(new Handler(Looper.getMainLooper()));

        private MainHolder() {
        }
    }

    public static Scheduler mainThread() {
        return RxAndroidPlugins.onMainThreadScheduler(MAIN_THREAD);
    }

    public static Scheduler from(Looper looper) {
        if (looper != null) {
            return new HandlerScheduler(new Handler(looper));
        }
        throw new NullPointerException("looper == null");
    }

    private AndroidSchedulers() {
        throw new AssertionError("No instances.");
    }
}
