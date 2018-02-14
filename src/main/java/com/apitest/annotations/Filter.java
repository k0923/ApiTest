package com.apitest.annotations;

import java.lang.annotation.*;

@Repeatable(Filters.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface Filter {
    Class<?> cls() default Object.class;
    String method() default "";
    String[] args() default {};
}
