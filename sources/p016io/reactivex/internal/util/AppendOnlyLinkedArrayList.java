package p016io.reactivex.internal.util;

import org.reactivestreams.Subscriber;
import p016io.reactivex.Observer;
import p016io.reactivex.functions.BiPredicate;
import p016io.reactivex.functions.Predicate;

/* renamed from: io.reactivex.internal.util.AppendOnlyLinkedArrayList */
public class AppendOnlyLinkedArrayList<T> {
    final int capacity;
    final Object[] head;
    int offset;
    Object[] tail = this.head;

    /* renamed from: io.reactivex.internal.util.AppendOnlyLinkedArrayList$NonThrowingPredicate */
    public interface NonThrowingPredicate<T> extends Predicate<T> {
        boolean test(T t);
    }

    public AppendOnlyLinkedArrayList(int i) {
        this.capacity = i;
        this.head = new Object[(i + 1)];
    }

    public void add(T t) {
        int i = this.capacity;
        int i2 = this.offset;
        if (i2 == i) {
            Object[] objArr = new Object[(i + 1)];
            this.tail[i] = objArr;
            this.tail = objArr;
            i2 = 0;
        }
        this.tail[i2] = t;
        this.offset = i2 + 1;
    }

    public void setFirst(T t) {
        this.head[0] = t;
    }

    public void forEachWhile(NonThrowingPredicate<? super T> nonThrowingPredicate) {
        int i = this.capacity;
        for (Object[] objArr = this.head; objArr != null; objArr = objArr[i]) {
            for (int i2 = 0; i2 < i; i2++) {
                Object[] objArr2 = objArr[i2];
                if (objArr2 == null || nonThrowingPredicate.test(objArr2)) {
                    break;
                }
            }
        }
    }

    public <U> boolean accept(Subscriber<? super U> subscriber) {
        Object[] objArr = this.head;
        int i = this.capacity;
        while (true) {
            if (objArr == null) {
                return false;
            }
            for (int i2 = 0; i2 < i; i2++) {
                Object[] objArr2 = objArr[i2];
                if (objArr2 == null || NotificationLite.acceptFull((Object) objArr2, subscriber)) {
                    break;
                }
            }
            objArr = objArr[i];
        }
    }

    public <U> boolean accept(Observer<? super U> observer) {
        Object[] objArr = this.head;
        int i = this.capacity;
        while (true) {
            if (objArr == null) {
                return false;
            }
            for (int i2 = 0; i2 < i; i2++) {
                Object[] objArr2 = objArr[i2];
                if (objArr2 == null || NotificationLite.acceptFull((Object) objArr2, observer)) {
                    break;
                }
            }
            objArr = objArr[i];
        }
    }

    public <S> void forEachWhile(S s, BiPredicate<? super S, ? super T> biPredicate) throws Exception {
        Object[] objArr = this.head;
        int i = this.capacity;
        while (true) {
            int i2 = 0;
            while (i2 < i) {
                Object[] objArr2 = objArr[i2];
                if (objArr2 != null && !biPredicate.test(s, objArr2)) {
                    i2++;
                } else {
                    return;
                }
            }
            objArr = objArr[i];
        }
    }
}
