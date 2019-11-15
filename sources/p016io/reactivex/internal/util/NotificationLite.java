package p016io.reactivex.internal.util;

import java.io.Serializable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.internal.util.NotificationLite */
public enum NotificationLite {
    COMPLETE;

    /* renamed from: io.reactivex.internal.util.NotificationLite$DisposableNotification */
    static final class DisposableNotification implements Serializable {
        private static final long serialVersionUID = -7482590109178395495L;

        /* renamed from: d */
        final Disposable f551d;

        DisposableNotification(Disposable disposable) {
            this.f551d = disposable;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("NotificationLite.Disposable[");
            sb.append(this.f551d);
            sb.append("]");
            return sb.toString();
        }
    }

    /* renamed from: io.reactivex.internal.util.NotificationLite$ErrorNotification */
    static final class ErrorNotification implements Serializable {
        private static final long serialVersionUID = -8759979445933046293L;

        /* renamed from: e */
        final Throwable f552e;

        ErrorNotification(Throwable th) {
            this.f552e = th;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("NotificationLite.Error[");
            sb.append(this.f552e);
            sb.append("]");
            return sb.toString();
        }

        public int hashCode() {
            return this.f552e.hashCode();
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof ErrorNotification)) {
                return false;
            }
            return ObjectHelper.equals(this.f552e, ((ErrorNotification) obj).f552e);
        }
    }

    /* renamed from: io.reactivex.internal.util.NotificationLite$SubscriptionNotification */
    static final class SubscriptionNotification implements Serializable {
        private static final long serialVersionUID = -1322257508628817540L;

        /* renamed from: s */
        final Subscription f553s;

        SubscriptionNotification(Subscription subscription) {
            this.f553s = subscription;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("NotificationLite.Subscription[");
            sb.append(this.f553s);
            sb.append("]");
            return sb.toString();
        }
    }

    public static <T> T getValue(Object obj) {
        return obj;
    }

    public static <T> Object next(T t) {
        return t;
    }

    public String toString() {
        return "NotificationLite.Complete";
    }

    public static Object complete() {
        return COMPLETE;
    }

    public static Object error(Throwable th) {
        return new ErrorNotification(th);
    }

    public static Object subscription(Subscription subscription) {
        return new SubscriptionNotification(subscription);
    }

    public static Object disposable(Disposable disposable) {
        return new DisposableNotification(disposable);
    }

    public static boolean isComplete(Object obj) {
        return obj == COMPLETE;
    }

    public static boolean isError(Object obj) {
        return obj instanceof ErrorNotification;
    }

    public static boolean isSubscription(Object obj) {
        return obj instanceof SubscriptionNotification;
    }

    public static boolean isDisposable(Object obj) {
        return obj instanceof DisposableNotification;
    }

    public static Throwable getError(Object obj) {
        return ((ErrorNotification) obj).f552e;
    }

    public static Subscription getSubscription(Object obj) {
        return ((SubscriptionNotification) obj).f553s;
    }

    public static Disposable getDisposable(Object obj) {
        return ((DisposableNotification) obj).f551d;
    }

    public static <T> boolean accept(Object obj, Subscriber<? super T> subscriber) {
        if (obj == COMPLETE) {
            subscriber.onComplete();
            return true;
        } else if (obj instanceof ErrorNotification) {
            subscriber.onError(((ErrorNotification) obj).f552e);
            return true;
        } else {
            subscriber.onNext(obj);
            return false;
        }
    }

    public static <T> boolean accept(Object obj, Observer<? super T> observer) {
        if (obj == COMPLETE) {
            observer.onComplete();
            return true;
        } else if (obj instanceof ErrorNotification) {
            observer.onError(((ErrorNotification) obj).f552e);
            return true;
        } else {
            observer.onNext(obj);
            return false;
        }
    }

    public static <T> boolean acceptFull(Object obj, Subscriber<? super T> subscriber) {
        if (obj == COMPLETE) {
            subscriber.onComplete();
            return true;
        } else if (obj instanceof ErrorNotification) {
            subscriber.onError(((ErrorNotification) obj).f552e);
            return true;
        } else if (obj instanceof SubscriptionNotification) {
            subscriber.onSubscribe(((SubscriptionNotification) obj).f553s);
            return false;
        } else {
            subscriber.onNext(obj);
            return false;
        }
    }

    public static <T> boolean acceptFull(Object obj, Observer<? super T> observer) {
        if (obj == COMPLETE) {
            observer.onComplete();
            return true;
        } else if (obj instanceof ErrorNotification) {
            observer.onError(((ErrorNotification) obj).f552e);
            return true;
        } else if (obj instanceof DisposableNotification) {
            observer.onSubscribe(((DisposableNotification) obj).f551d);
            return false;
        } else {
            observer.onNext(obj);
            return false;
        }
    }
}
