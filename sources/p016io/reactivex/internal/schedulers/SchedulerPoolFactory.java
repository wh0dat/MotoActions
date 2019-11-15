package p016io.reactivex.internal.schedulers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.schedulers.SchedulerPoolFactory */
public final class SchedulerPoolFactory {
    static final Map<ScheduledThreadPoolExecutor, Object> POOLS = new ConcurrentHashMap();
    public static final boolean PURGE_ENABLED;
    static final String PURGE_ENABLED_KEY = "rx2.purge-enabled";
    public static final int PURGE_PERIOD_SECONDS;
    static final String PURGE_PERIOD_SECONDS_KEY = "rx2.purge-period-seconds";
    static final AtomicReference<ScheduledExecutorService> PURGE_THREAD = new AtomicReference<>();

    private SchedulerPoolFactory() {
        throw new IllegalStateException("No instances!");
    }

    static {
        boolean z;
        Properties properties = System.getProperties();
        int i = 1;
        if (properties.containsKey(PURGE_ENABLED_KEY)) {
            z = Boolean.getBoolean(PURGE_ENABLED_KEY);
            if (z && properties.containsKey(PURGE_PERIOD_SECONDS_KEY)) {
                i = Integer.getInteger(PURGE_PERIOD_SECONDS_KEY, 1).intValue();
            }
        } else {
            z = true;
        }
        PURGE_ENABLED = z;
        PURGE_PERIOD_SECONDS = i;
        start();
    }

    public static void start() {
        while (true) {
            ScheduledExecutorService scheduledExecutorService = (ScheduledExecutorService) PURGE_THREAD.get();
            if (scheduledExecutorService == null || scheduledExecutorService.isShutdown()) {
                ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(1, new RxThreadFactory("RxSchedulerPurge"));
                if (PURGE_THREAD.compareAndSet(scheduledExecutorService, newScheduledThreadPool)) {
                    newScheduledThreadPool.scheduleAtFixedRate(new Runnable() {
                        public void run() {
                            try {
                                Iterator it = new ArrayList(SchedulerPoolFactory.POOLS.keySet()).iterator();
                                while (it.hasNext()) {
                                    ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = (ScheduledThreadPoolExecutor) it.next();
                                    if (scheduledThreadPoolExecutor.isShutdown()) {
                                        SchedulerPoolFactory.POOLS.remove(scheduledThreadPoolExecutor);
                                    } else {
                                        scheduledThreadPoolExecutor.purge();
                                    }
                                }
                            } catch (Throwable th) {
                                RxJavaPlugins.onError(th);
                            }
                        }
                    }, (long) PURGE_PERIOD_SECONDS, (long) PURGE_PERIOD_SECONDS, TimeUnit.SECONDS);
                    return;
                }
                newScheduledThreadPool.shutdownNow();
            } else {
                return;
            }
        }
    }

    public static void shutdown() {
        ((ScheduledExecutorService) PURGE_THREAD.get()).shutdownNow();
        POOLS.clear();
    }

    public static ScheduledExecutorService create(ThreadFactory threadFactory) {
        ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(1, threadFactory);
        if (newScheduledThreadPool instanceof ScheduledThreadPoolExecutor) {
            POOLS.put((ScheduledThreadPoolExecutor) newScheduledThreadPool, newScheduledThreadPool);
        }
        return newScheduledThreadPool;
    }
}
