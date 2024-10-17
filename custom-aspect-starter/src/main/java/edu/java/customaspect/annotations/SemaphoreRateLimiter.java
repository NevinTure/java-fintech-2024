package edu.java.customaspect.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface SemaphoreRateLimiter {

    String value() default "default";
}
