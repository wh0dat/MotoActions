package p016io.reactivex.observers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import p016io.reactivex.Notification;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.CompositeException;
import p016io.reactivex.functions.Predicate;
import p016io.reactivex.internal.functions.Functions;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.util.ExceptionHelper;
import p016io.reactivex.observers.BaseTestConsumer;

/* renamed from: io.reactivex.observers.BaseTestConsumer */
public abstract class BaseTestConsumer<T, U extends BaseTestConsumer<T, U>> implements Disposable {
    protected boolean checkSubscriptionOnce;
    protected long completions;
    protected final CountDownLatch done = new CountDownLatch(1);
    protected final List<Throwable> errors = new ArrayList();
    protected int establishedFusionMode;
    protected int initialFusionMode;
    protected Thread lastThread;
    protected final List<T> values = new ArrayList();

    public abstract U assertNotSubscribed();

    public abstract U assertSubscribed();

    public final Thread lastThread() {
        return this.lastThread;
    }

    public final List<T> values() {
        return this.values;
    }

    public final List<Throwable> errors() {
        return this.errors;
    }

    public final long completions() {
        return this.completions;
    }

    public final boolean isTerminated() {
        return this.done.getCount() == 0;
    }

    public final int valueCount() {
        return this.values.size();
    }

    public final int errorCount() {
        return this.errors.size();
    }

    /* access modifiers changed from: protected */
    public final AssertionError fail(String str) {
        StringBuilder sb = new StringBuilder(str.length() + 64);
        sb.append(str);
        sb.append(" (");
        sb.append("latch = ");
        sb.append(this.done.getCount());
        sb.append(", ");
        sb.append("values = ");
        sb.append(this.values.size());
        sb.append(", ");
        sb.append("errors = ");
        sb.append(this.errors.size());
        sb.append(", ");
        sb.append("completions = ");
        sb.append(this.completions);
        sb.append(')');
        AssertionError assertionError = new AssertionError(sb.toString());
        if (!this.errors.isEmpty()) {
            if (this.errors.size() == 1) {
                assertionError.initCause((Throwable) this.errors.get(0));
            } else {
                assertionError.initCause(new CompositeException((Iterable<? extends Throwable>) this.errors));
            }
        }
        return assertionError;
    }

    public final U await() throws InterruptedException {
        if (this.done.getCount() == 0) {
            return this;
        }
        this.done.await();
        return this;
    }

    public final boolean await(long j, TimeUnit timeUnit) throws InterruptedException {
        return this.done.getCount() == 0 || this.done.await(j, timeUnit);
    }

    public final U assertComplete() {
        long j = this.completions;
        if (j == 0) {
            throw fail("Not completed");
        } else if (j <= 1) {
            return this;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Multiple completions: ");
            sb.append(j);
            throw fail(sb.toString());
        }
    }

    public final U assertNotComplete() {
        long j = this.completions;
        int i = (j > 1 ? 1 : (j == 1 ? 0 : -1));
        if (i == 0) {
            throw fail("Completed!");
        } else if (i <= 0) {
            return this;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Multiple completions: ");
            sb.append(j);
            throw fail(sb.toString());
        }
    }

    public final U assertNoErrors() {
        if (this.errors.size() == 0) {
            return this;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Error(s) present: ");
        sb.append(this.errors);
        throw fail(sb.toString());
    }

    public final U assertError(Throwable th) {
        return assertError(Functions.equalsWith(th));
    }

    public final U assertError(Class<? extends Throwable> cls) {
        return assertError(Functions.isInstanceOf(cls));
    }

    public final U assertError(Predicate<Throwable> predicate) {
        int size = this.errors.size();
        if (size == 0) {
            throw fail("No errors");
        }
        boolean z = false;
        Iterator it = this.errors.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            try {
                if (predicate.test((Throwable) it.next())) {
                    z = true;
                    break;
                }
            } catch (Exception e) {
                throw ExceptionHelper.wrapOrThrow(e);
            }
        }
        if (!z) {
            throw fail("Error not present");
        } else if (size == 1) {
            return this;
        } else {
            throw fail("Error present but other errors as well");
        }
    }

    public final U assertValue(T t) {
        if (this.values.size() != 1) {
            StringBuilder sb = new StringBuilder();
            sb.append("Expected: ");
            sb.append(valueAndClass(t));
            sb.append(", Actual: ");
            sb.append(this.values);
            throw fail(sb.toString());
        }
        Object obj = this.values.get(0);
        if (ObjectHelper.equals(t, obj)) {
            return this;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Expected: ");
        sb2.append(valueAndClass(t));
        sb2.append(", Actual: ");
        sb2.append(valueAndClass(obj));
        throw fail(sb2.toString());
    }

    public final U assertValue(Predicate<T> predicate) {
        assertValueAt(0, predicate);
        if (this.values.size() <= 1) {
            return this;
        }
        throw fail("Value present but other values as well");
    }

    public final U assertValueAt(int i, Predicate<T> predicate) {
        if (this.values.size() == 0) {
            throw fail("No values");
        } else if (i >= this.values.size()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid index: ");
            sb.append(i);
            throw fail(sb.toString());
        } else {
            try {
                if (predicate.test(this.values.get(i))) {
                    return this;
                }
                throw fail("Value not present");
            } catch (Exception e) {
                throw ExceptionHelper.wrapOrThrow(e);
            }
        }
    }

    public static String valueAndClass(Object obj) {
        if (obj == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(obj);
        sb.append(" (class: ");
        sb.append(obj.getClass().getSimpleName());
        sb.append(")");
        return sb.toString();
    }

    public final U assertValueCount(int i) {
        int size = this.values.size();
        if (size == i) {
            return this;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Value counts differ; Expected: ");
        sb.append(i);
        sb.append(", Actual: ");
        sb.append(size);
        throw fail(sb.toString());
    }

    public final U assertNoValues() {
        return assertValueCount(0);
    }

    public final U assertValues(T... tArr) {
        int size = this.values.size();
        if (size != tArr.length) {
            StringBuilder sb = new StringBuilder();
            sb.append("Value count differs; Expected: ");
            sb.append(tArr.length);
            sb.append(" ");
            sb.append(Arrays.toString(tArr));
            sb.append(", Actual: ");
            sb.append(size);
            sb.append(" ");
            sb.append(this.values);
            throw fail(sb.toString());
        }
        for (int i = 0; i < size; i++) {
            Object obj = this.values.get(i);
            T t = tArr[i];
            if (!ObjectHelper.equals(t, obj)) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Values at position ");
                sb2.append(i);
                sb2.append(" differ; Expected: ");
                sb2.append(valueAndClass(t));
                sb2.append(", Actual: ");
                sb2.append(valueAndClass(obj));
                throw fail(sb2.toString());
            }
        }
        return this;
    }

    public final U assertValueSet(Collection<? extends T> collection) {
        if (collection.isEmpty()) {
            assertNoValues();
            return this;
        }
        for (Object next : this.values) {
            if (!collection.contains(next)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Value not in the expected collection: ");
                sb.append(valueAndClass(next));
                throw fail(sb.toString());
            }
        }
        return this;
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0059  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0074  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final U assertValueSequence(java.lang.Iterable<? extends T> r6) {
        /*
            r5 = this;
            java.util.List<T> r0 = r5.values
            java.util.Iterator r0 = r0.iterator()
            java.util.Iterator r6 = r6.iterator()
            r1 = 0
        L_0x000b:
            boolean r2 = r6.hasNext()
            boolean r3 = r0.hasNext()
            if (r2 == 0) goto L_0x0057
            if (r3 != 0) goto L_0x0018
            goto L_0x0057
        L_0x0018:
            java.lang.Object r2 = r6.next()
            java.lang.Object r3 = r0.next()
            boolean r4 = p016io.reactivex.internal.functions.ObjectHelper.equals(r3, r2)
            if (r4 != 0) goto L_0x0054
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r0 = "Values at position "
            r6.append(r0)
            r6.append(r1)
            java.lang.String r0 = " differ; Expected: "
            r6.append(r0)
            java.lang.String r0 = valueAndClass(r3)
            r6.append(r0)
            java.lang.String r0 = ", Actual: "
            r6.append(r0)
            java.lang.String r0 = valueAndClass(r2)
            r6.append(r0)
            java.lang.String r6 = r6.toString()
            java.lang.AssertionError r5 = r5.fail(r6)
            throw r5
        L_0x0054:
            int r1 = r1 + 1
            goto L_0x000b
        L_0x0057:
            if (r2 == 0) goto L_0x0074
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r0 = "More values received than expected ("
            r6.append(r0)
            r6.append(r1)
            java.lang.String r0 = ")"
            r6.append(r0)
            java.lang.String r6 = r6.toString()
            java.lang.AssertionError r5 = r5.fail(r6)
            throw r5
        L_0x0074:
            if (r3 == 0) goto L_0x0091
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r0 = "Fever values received than expected ("
            r6.append(r0)
            r6.append(r1)
            java.lang.String r0 = ")"
            r6.append(r0)
            java.lang.String r6 = r6.toString()
            java.lang.AssertionError r5 = r5.fail(r6)
            throw r5
        L_0x0091:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: p016io.reactivex.observers.BaseTestConsumer.assertValueSequence(java.lang.Iterable):io.reactivex.observers.BaseTestConsumer");
    }

    public final U assertTerminated() {
        if (this.done.getCount() != 0) {
            throw fail("Subscriber still running!");
        }
        long j = this.completions;
        if (j > 1) {
            StringBuilder sb = new StringBuilder();
            sb.append("Terminated with multiple completions: ");
            sb.append(j);
            throw fail(sb.toString());
        }
        int size = this.errors.size();
        if (size > 1) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Terminated with multiple errors: ");
            sb2.append(size);
            throw fail(sb2.toString());
        } else if (j == 0 || size == 0) {
            return this;
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Terminated with multiple completions and errors: ");
            sb3.append(j);
            throw fail(sb3.toString());
        }
    }

    public final U assertNotTerminated() {
        if (this.done.getCount() != 0) {
            return this;
        }
        throw fail("Subscriber terminated!");
    }

    public final boolean awaitTerminalEvent() {
        try {
            await();
            return true;
        } catch (InterruptedException unused) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    public final boolean awaitTerminalEvent(long j, TimeUnit timeUnit) {
        try {
            return await(j, timeUnit);
        } catch (InterruptedException unused) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    public final U assertErrorMessage(String str) {
        int size = this.errors.size();
        if (size == 0) {
            throw fail("No errors");
        } else if (size == 1) {
            String message = ((Throwable) this.errors.get(0)).getMessage();
            if (ObjectHelper.equals(str, message)) {
                return this;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Error message differs; Expected: ");
            sb.append(str);
            sb.append(", Actual: ");
            sb.append(message);
            throw fail(sb.toString());
        } else {
            throw fail("Multiple errors");
        }
    }

    public final List<List<Object>> getEvents() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(values());
        arrayList.add(errors());
        ArrayList arrayList2 = new ArrayList();
        for (long j = 0; j < this.completions; j++) {
            arrayList2.add(Notification.createOnComplete());
        }
        arrayList.add(arrayList2);
        return arrayList;
    }

    public final U assertResult(T... tArr) {
        return assertSubscribed().assertValues(tArr).assertNoErrors().assertComplete();
    }

    public final U assertFailure(Class<? extends Throwable> cls, T... tArr) {
        return assertSubscribed().assertValues(tArr).assertError(cls).assertNotComplete();
    }

    public final U assertFailure(Predicate<Throwable> predicate, T... tArr) {
        return assertSubscribed().assertValues(tArr).assertError(predicate).assertNotComplete();
    }

    public final U assertFailureAndMessage(Class<? extends Throwable> cls, String str, T... tArr) {
        return assertSubscribed().assertValues(tArr).assertError(cls).assertErrorMessage(str).assertNotComplete();
    }

    public final U awaitDone(long j, TimeUnit timeUnit) {
        try {
            if (!this.done.await(j, timeUnit)) {
                dispose();
            }
            return this;
        } catch (InterruptedException e) {
            dispose();
            throw ExceptionHelper.wrapOrThrow(e);
        }
    }

    public final U assertEmpty() {
        return assertSubscribed().assertNoValues().assertNoErrors().assertNotComplete();
    }
}
