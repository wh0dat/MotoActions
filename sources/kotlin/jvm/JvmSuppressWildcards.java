package kotlin.jvm;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import kotlin.Metadata;
import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.AnnotationTarget;
import kotlin.annotation.MustBeDocumented;
import kotlin.annotation.Retention;
import kotlin.annotation.Target;

@MustBeDocumented
@Target(allowedTargets = {AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.TYPE})
@Retention(AnnotationRetention.BINARY)
@Documented
@java.lang.annotation.Target({ElementType.TYPE, ElementType.METHOD})
@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\n\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003R\t\u0010\u0002\u001a\u00020\u0003¢\u0006\u0000¨\u0006\u0004"}, mo14495d2 = {"Lkotlin/jvm/JvmSuppressWildcards;", "", "suppress", "", "kotlin-stdlib"}, mo14496k = 1, mo14497mv = {1, 1, 10})
@java.lang.annotation.Retention(RetentionPolicy.CLASS)
/* compiled from: JvmPlatformAnnotations.kt */
public @interface JvmSuppressWildcards {
    boolean suppress() default true;
}
