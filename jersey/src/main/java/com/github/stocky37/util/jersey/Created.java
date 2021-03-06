package com.github.stocky37.util.jersey;

import javax.ws.rs.core.Response;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Status(Response.Status.CREATED)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Created {}
