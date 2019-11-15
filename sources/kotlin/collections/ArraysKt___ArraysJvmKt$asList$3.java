package kotlin.collections;

import java.util.RandomAccess;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000!\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\b*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00060\u0003j\u0002`\u0004B\u0005¢\u0006\u0002\u0010\u0005J\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0002H\u0002J\u0016\u0010\f\u001a\u00020\u00022\u0006\u0010\r\u001a\u00020\u0002H\u0002¢\u0006\u0002\u0010\u000eJ\u0010\u0010\u000f\u001a\u00020\u00022\u0006\u0010\u000b\u001a\u00020\u0002H\u0016J\b\u0010\u0010\u001a\u00020\nH\u0016J\u0010\u0010\u0011\u001a\u00020\u00022\u0006\u0010\u000b\u001a\u00020\u0002H\u0016R\u0014\u0010\u0006\u001a\u00020\u00028VX\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\b¨\u0006\u0012"}, mo14495d2 = {"kotlin/collections/ArraysKt___ArraysJvmKt$asList$3", "Lkotlin/collections/AbstractList;", "", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "([I)V", "size", "getSize", "()I", "contains", "", "element", "get", "index", "(I)Ljava/lang/Integer;", "indexOf", "isEmpty", "lastIndexOf", "kotlin-stdlib"}, mo14496k = 1, mo14497mv = {1, 1, 10})
/* compiled from: _ArraysJvm.kt */
public final class ArraysKt___ArraysJvmKt$asList$3 extends AbstractList<Integer> implements RandomAccess {
    final /* synthetic */ int[] receiver$0;

    ArraysKt___ArraysJvmKt$asList$3(int[] iArr) {
        this.receiver$0 = iArr;
    }

    public final /* bridge */ boolean contains(Object obj) {
        if (obj instanceof Integer) {
            return contains(((Number) obj).intValue());
        }
        return false;
    }

    public final /* bridge */ int indexOf(Object obj) {
        if (obj instanceof Integer) {
            return indexOf(((Number) obj).intValue());
        }
        return -1;
    }

    public final /* bridge */ int lastIndexOf(Object obj) {
        if (obj instanceof Integer) {
            return lastIndexOf(((Number) obj).intValue());
        }
        return -1;
    }

    public int getSize() {
        return this.receiver$0.length;
    }

    public boolean isEmpty() {
        return this.receiver$0.length == 0;
    }

    public boolean contains(int i) {
        return ArraysKt.contains(this.receiver$0, i);
    }

    @NotNull
    public Integer get(int i) {
        return Integer.valueOf(this.receiver$0[i]);
    }

    public int indexOf(int i) {
        return ArraysKt.indexOf(this.receiver$0, i);
    }

    public int lastIndexOf(int i) {
        return ArraysKt.lastIndexOf(this.receiver$0, i);
    }
}
