package com.apitest.annotations;

import java.lang.annotation.*;

@Repeatable(Filters.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface Filter {
    Class<?> cls();
    String[] methods() default {};
}
