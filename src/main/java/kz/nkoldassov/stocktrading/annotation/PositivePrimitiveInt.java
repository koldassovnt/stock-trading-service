package kz.nkoldassov.stocktrading.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({ElementType.FIELD})
public @interface PositivePrimitiveInt {

    String message() default "int must be positive";

    boolean includeZero() default true;

}