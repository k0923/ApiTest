package com.apitest.dataProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Provider(JsonProvider.class)
public @interface Json {

    String file() default "";

    Class<? extends IGsonBuilder> builder() default IGsonBuilder.class;
}
