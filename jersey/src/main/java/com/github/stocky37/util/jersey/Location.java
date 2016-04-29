package com.github.stocky37.util.jersey;

import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ParametersAreNonnullByDefault
public @interface Location {
	String path() default "";
	boolean isAbsolute() default false;
	LocationParam[] params() default {};
}
