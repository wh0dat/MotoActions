package kotlin.jvm.internal;

import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.collections.CharIterator;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0019\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\f\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\bH\u0002J\b\u0010\t\u001a\u00020\nH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\u000b"}, mo14495d2 = {"Lkotlin/jvm/internal/ArrayCharIterator;", "Lkotlin/collections/CharIterator;", "array", "", "([C)V", "index", "", "hasNext", "", "nextChar", "", "kotlin-stdlib"}, mo14496k = 1, mo14497mv = {1, 1, 10})
/* compiled from: ArrayIterators.kt */
final class ArrayCharIterator extends CharIterator {
    private final char[] array;
    private int index;

    public ArrayCharIterator(@NotNull char[] cArr) {
        Intrinsics.checkParameterIsNotNull(cArr, "array");
        this.array = cArr;
    }

    public boolean hasNext() {
        return this.index < this.array.length;
    }

    public char nextChar() {
        try {
            char[] cArr = this.array;
            int i = this.index;
            this.index = i + 1;
            return cArr[i];
        } catch (ArrayIndexOutOfBoundsException e) {
            this.index--;
            throw new NoSuchElementException(e.getMessage());
        }
    }
}
