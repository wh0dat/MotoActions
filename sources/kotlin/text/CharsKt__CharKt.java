package kotlin.text;

import kotlin.Metadata;
import kotlin.internal.InlineOnly;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u000b\n\u0002\u0010\f\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00022\b\b\u0002\u0010\u0004\u001a\u00020\u0001\u001a\n\u0010\u0005\u001a\u00020\u0001*\u00020\u0002\u001a\u0015\u0010\u0006\u001a\u00020\u0007*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0007H\n¨\u0006\b"}, mo14495d2 = {"equals", "", "", "other", "ignoreCase", "isSurrogate", "plus", "", "kotlin-stdlib"}, mo14496k = 5, mo14497mv = {1, 1, 10}, mo14499xi = 1, mo14500xs = "kotlin/text/CharsKt")
/* compiled from: Char.kt */
class CharsKt__CharKt extends CharsKt__CharJVMKt {
    public static final boolean isSurrogate(char c) {
        return 55296 <= c && 57343 >= c;
    }

    @InlineOnly
    private static final String plus(char c, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(c));
        sb.append(str);
        return sb.toString();
    }

    public static /* bridge */ /* synthetic */ boolean equals$default(char c, char c2, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return CharsKt.equals(c, c2, z);
    }

    public static final boolean equals(char c, char c2, boolean z) {
        if (c == c2) {
            return true;
        }
        if (!z) {
            return false;
        }
        return Character.toUpperCase(c) == Character.toUpperCase(c2) || Character.toLowerCase(c) == Character.toLowerCase(c2);
    }
}
