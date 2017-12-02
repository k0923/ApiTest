package com.apitest.annotations;

import com.apitest.dataProvider.IDataProvider;
import com.apitest.dataProvider.Spring;

import java.lang.annotation.*;


@Repeatable(TestDatas.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestData {
    Class<? extends IDataProvider> provider() default Spring.class;
    String[] paras() default {};
}
