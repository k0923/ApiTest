package com.apitest.annotations;

import com.apitest.dataProvider.DataSource;
import com.apitest.dataProvider.IDataProvider;

import java.lang.annotation.*;


@Repeatable(TestDatas.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestData {
    DataSource source() default DataSource.Spring;
    boolean single() default true;
    Class<? extends IDataProvider> dataProvider() default IDataProvider.class;
    boolean parallel() default false;
    String file() default "";
    String pattern() default "";
}
