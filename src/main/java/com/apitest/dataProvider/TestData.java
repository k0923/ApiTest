package com.apitest.dataProvider;

import java.lang.annotation.*;


@Repeatable(TestDatas.class)
public @interface TestData {
    DataSource source() default DataSource.Spring;
    boolean single() default true;
    Class<? extends IDataProvider> dataProvider() default IDataProvider.class;
    boolean parallel() default false;
    String file() default "";
    String pattern() default "";
}
