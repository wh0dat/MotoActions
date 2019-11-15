package kotlin;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import kotlin.annotation.AnnotationTarget;
import kotlin.annotation.MustBeDocumented;
import kotlin.annotation.Target;

@MustBeDocumented
@SinceKotlin(version = "1.1")
@Target(allowedTargets = {AnnotationTarget.TYPE})
@Documented
@java.lang.annotation.Target({})
@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\b\u0012\u0006\u0010\u0002\u001a\u00020\u0003R\t\u0010\u0002\u001a\u00020\u0003¢\u0006\u0000¨\u0006\u0004"}, mo14495d2 = {"Lkotlin/ParameterName;", "", "name", "", "kotlin-stdlib"}, mo14496k = 1, mo14497mv = {1, 1, 10})
@Retention(RetentionPolicy.RUNTIME)
/* compiled from: Annotations.kt */
public @interface ParameterName {
    String name();
}
