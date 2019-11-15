package kotlin.text;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0003"}, mo14495d2 = {"<anonymous>", "", "it", "invoke"}, mo14496k = 3, mo14497mv = {1, 1, 10})
/* compiled from: Indent.kt */
final class StringsKt__IndentKt$prependIndent$1 extends Lambda implements Function1<String, String> {
    final /* synthetic */ String $indent;

    StringsKt__IndentKt$prependIndent$1(String str) {
        this.$indent = str;
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "it");
        if (!StringsKt.isBlank(str)) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.$indent);
            sb.append(str);
            return sb.toString();
        } else if (str.length() < this.$indent.length()) {
            return this.$indent;
        } else {
            return str;
        }
    }
}
