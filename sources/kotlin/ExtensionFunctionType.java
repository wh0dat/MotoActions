package kotlin;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import kotlin.annotation.AnnotationTarget;
import kotlin.annotation.MustBeDocumented;

@MustBeDocumented
@Documented
@Target({})
@kotlin.annotation.Target(allowedTargets = {AnnotationTarget.TYPE})
@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0000¨\u0006\u0002"}, mo14495d2 = {"Lkotlin/ExtensionFunctionType;", "", "kotlin-stdlib"}, mo14496k = 1, mo14497mv = {1, 1, 10})
@Retention(RetentionPolicy.RUNTIME)
/* compiled from: Annotations.kt */
public @interface ExtensionFunctionType {
}
