package p016io.reactivex.internal.schedulers;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/* renamed from: io.reactivex.internal.schedulers.RxThreadFactory */
public final class RxThreadFactory extends AtomicLong implements ThreadFactory {
    private static final long serialVersionUID = -7789753024099756196L;
    final String prefix;
    final int priority;

    public RxThreadFactory(String str) {
        this(str, 5);
    }

    public RxThreadFactory(String str, int i) {
        this.prefix = str;
        this.priority = i;
    }

    public Thread newThread(Runnable runnable) {
        StringBuilder sb = new StringBuilder(this.prefix);
        sb.append('-');
        sb.append(incrementAndGet());
        Thread thread = new Thread(runnable, sb.toString());
        thread.setPriority(this.priority);
        thread.setDaemon(true);
        return thread;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RxThreadFactory[");
        sb.append(this.prefix);
        sb.append("]");
        return sb.toString();
    }
}
