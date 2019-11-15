package kotlin;

import java.lang.annotation.Documented;
import java.lang.annotation.RetentionPolicy;
import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.MustBeDocumented;
import kotlin.annotation.Retention;
import kotlin.annotation.Target;

@MustBeDocumented
@Target(allowedTargets = {})
@Retention(AnnotationRetention.BINARY)
@Documented
@java.lang.annotation.Target({})
@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0011\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u001c\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0012\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00030\u0005\"\u00020\u0003R\t\u0010\u0002\u001a\u00020\u0003¢\u0006\u0000R\u0011\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00030\u0005¢\u0006\u0000¨\u0006\u0006"}, mo14495d2 = {"Lkotlin/ReplaceWith;", "", "expression", "", "imports", "", "kotlin-stdlib"}, mo14496k = 1, mo14497mv = {1, 1, 10})
@java.lang.annotation.Retention(RetentionPolicy.CLASS)
/* compiled from: Annotations.kt */
public @interface ReplaceWith {
    String expression();

    String[] imports();
}
