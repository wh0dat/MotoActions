package kotlin;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import kotlin.annotation.AnnotationTarget;
import kotlin.annotation.MustBeDocumented;
import kotlin.annotation.Target;

@MustBeDocumented
@Target(allowedTargets = {AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CONSTRUCTOR, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.TYPEALIAS})
@Documented
@java.lang.annotation.Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE})
@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u001c\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007R\t\u0010\u0006\u001a\u00020\u0007¢\u0006\u0000R\t\u0010\u0002\u001a\u00020\u0003¢\u0006\u0000R\t\u0010\u0004\u001a\u00020\u0005¢\u0006\u0000¨\u0006\b"}, mo14495d2 = {"Lkotlin/Deprecated;", "", "message", "", "replaceWith", "Lkotlin/ReplaceWith;", "level", "Lkotlin/DeprecationLevel;", "kotlin-stdlib"}, mo14496k = 1, mo14497mv = {1, 1, 10})
@Retention(RetentionPolicy.RUNTIME)
/* compiled from: Annotations.kt */
public @interface Deprecated {
    DeprecationLevel level() default DeprecationLevel.WARNING;

    String message();

    ReplaceWith replaceWith() default @ReplaceWith(expression = "", imports = {});
}
