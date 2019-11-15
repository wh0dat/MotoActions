package kotlin.text;

import java.util.regex.Pattern;
import kotlin.Metadata;
import kotlin.internal.InlineOnly;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\f\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\b¨\u0006\u0003"}, mo14495d2 = {"toRegex", "Lkotlin/text/Regex;", "Ljava/util/regex/Pattern;", "kotlin-stdlib"}, mo14496k = 5, mo14497mv = {1, 1, 10}, mo14499xi = 1, mo14500xs = "kotlin/text/StringsKt")
/* compiled from: RegexExtensionsJVM.kt */
class StringsKt__RegexExtensionsJVMKt extends StringsKt__IndentKt {
    @InlineOnly
    private static final Regex toRegex(@NotNull Pattern pattern) {
        return new Regex(pattern);
    }
}
