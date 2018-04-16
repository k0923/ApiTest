package com.apitest.dataProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Provider(FuncDataProvider.class)
public @interface Func {

    Class<?> provider() default Object.class;

    String name() default "";

    String[] args() default {};

}
