package kotlin.text;

import kotlin.Metadata;
import kotlin.jvm.JvmField;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bÂ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo14495d2 = {"Lkotlin/text/ScreenFloatValueRegEx;", "", "()V", "value", "Lkotlin/text/Regex;", "kotlin-stdlib"}, mo14496k = 1, mo14497mv = {1, 1, 10})
/* compiled from: StringNumberConversionsJVM.kt */
final class ScreenFloatValueRegEx {
    public static final ScreenFloatValueRegEx INSTANCE = new ScreenFloatValueRegEx();
    @NotNull
    @JvmField
    public static final Regex value;

    static {
        String str = "(\\p{Digit}+)";
        String str2 = "(\\p{XDigit}+)";
        StringBuilder sb = new StringBuilder();
        sb.append("[eE][+-]?");
        sb.append(str);
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("(0[xX]");
        sb3.append(str2);
        sb3.append("(\\.)?)|");
        sb3.append("(0[xX]");
        sb3.append(str2);
        sb3.append("?(\\.)");
        sb3.append(str2);
        sb3.append(')');
        String sb4 = sb3.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append('(');
        sb5.append(str);
        sb5.append("(\\.)?(");
        sb5.append(str);
        sb5.append("?)(");
        sb5.append(sb2);
        sb5.append(")?)|");
        sb5.append("(\\.(");
        sb5.append(str);
        sb5.append(")(");
        sb5.append(sb2);
        sb5.append(")?)|");
        sb5.append("((");
        sb5.append(sb4);
        sb5.append(")[pP][+-]?");
        sb5.append(str);
        sb5.append(')');
        String sb6 = sb5.toString();
        StringBuilder sb7 = new StringBuilder();
        sb7.append("[\\x00-\\x20]*[+-]?(NaN|Infinity|((");
        sb7.append(sb6);
        sb7.append(")[fFdD]?))[\\x00-\\x20]*");
        value = new Regex(sb7.toString());
    }

    private ScreenFloatValueRegEx() {
    }
}
