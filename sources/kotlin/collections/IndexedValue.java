package kotlin.collections;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00012\u00020\u0002B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00028\u0000¢\u0006\u0002\u0010\u0006J\t\u0010\f\u001a\u00020\u0004HÆ\u0003J\u000e\u0010\r\u001a\u00028\u0000HÆ\u0003¢\u0006\u0002\u0010\nJ(\u0010\u000e\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00028\u0000HÆ\u0001¢\u0006\u0002\u0010\u000fJ\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0002HÖ\u0003J\t\u0010\u0013\u001a\u00020\u0004HÖ\u0001J\t\u0010\u0014\u001a\u00020\u0015HÖ\u0001R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0013\u0010\u0005\u001a\u00028\u0000¢\u0006\n\n\u0002\u0010\u000b\u001a\u0004\b\t\u0010\n¨\u0006\u0016"}, mo14495d2 = {"Lkotlin/collections/IndexedValue;", "T", "", "index", "", "value", "(ILjava/lang/Object;)V", "getIndex", "()I", "getValue", "()Ljava/lang/Object;", "Ljava/lang/Object;", "component1", "component2", "copy", "(ILjava/lang/Object;)Lkotlin/collections/IndexedValue;", "equals", "", "other", "hashCode", "toString", "", "kotlin-stdlib"}, mo14496k = 1, mo14497mv = {1, 1, 10})
/* compiled from: IndexedValue.kt */
public final class IndexedValue<T> {
    private final int index;
    private final T value;

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.lang.Object, code=T, for r2v0, types: [java.lang.Object] */
    @org.jetbrains.annotations.NotNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static /* bridge */ /* synthetic */ kotlin.collections.IndexedValue copy$default(kotlin.collections.IndexedValue r0, int r1, T r2, int r3, java.lang.Object r4) {
        /*
            r4 = r3 & 1
            if (r4 == 0) goto L_0x0006
            int r1 = r0.index
        L_0x0006:
            r3 = r3 & 2
            if (r3 == 0) goto L_0x000c
            T r2 = r0.value
        L_0x000c:
            kotlin.collections.IndexedValue r0 = r0.copy(r1, r2)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.collections.IndexedValue.copy$default(kotlin.collections.IndexedValue, int, java.lang.Object, int, java.lang.Object):kotlin.collections.IndexedValue");
    }

    public final int component1() {
        return this.index;
    }

    public final T component2() {
        return this.value;
    }

    @NotNull
    public final IndexedValue<T> copy(int i, T t) {
        return new IndexedValue<>(i, t);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof IndexedValue) {
            IndexedValue indexedValue = (IndexedValue) obj;
            return (this.index == indexedValue.index) && Intrinsics.areEqual((Object) this.value, (Object) indexedValue.value);
        }
    }

    public int hashCode() {
        int i = this.index * 31;
        T t = this.value;
        return i + (t != null ? t.hashCode() : 0);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("IndexedValue(index=");
        sb.append(this.index);
        sb.append(", value=");
        sb.append(this.value);
        sb.append(")");
        return sb.toString();
    }

    public IndexedValue(int i, T t) {
        this.index = i;
        this.value = t;
    }

    public final int getIndex() {
        return this.index;
    }

    public final T getValue() {
        return this.value;
    }
}
