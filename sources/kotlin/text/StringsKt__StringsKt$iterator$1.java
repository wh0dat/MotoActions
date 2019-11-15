package kotlin.text;

import kotlin.Metadata;
import kotlin.collections.CharIterator;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u001f\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\f\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\t\u0010\u0005\u001a\u00020\u0006H\u0002J\b\u0010\u0007\u001a\u00020\bH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u000e¢\u0006\u0002\n\u0000¨\u0006\t"}, mo14495d2 = {"kotlin/text/StringsKt__StringsKt$iterator$1", "Lkotlin/collections/CharIterator;", "(Ljava/lang/CharSequence;)V", "index", "", "hasNext", "", "nextChar", "", "kotlin-stdlib"}, mo14496k = 1, mo14497mv = {1, 1, 10})
/* compiled from: Strings.kt */
public final class StringsKt__StringsKt$iterator$1 extends CharIterator {
    private int index;
    final /* synthetic */ CharSequence receiver$0;

    StringsKt__StringsKt$iterator$1(CharSequence charSequence) {
        this.receiver$0 = charSequence;
    }

    public char nextChar() {
        CharSequence charSequence = this.receiver$0;
        int i = this.index;
        this.index = i + 1;
        return charSequence.charAt(i);
    }

    public boolean hasNext() {
        return this.index < this.receiver$0.length();
    }
}
