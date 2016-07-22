package com.github.stocky37.util.testing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface JacksonVerification {
	String fixture();
	String representation() default "";
	boolean serialise() default true;
	boolean deserialise() default true;
}
