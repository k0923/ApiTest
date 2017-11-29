package com.apitest.spring.commons;

import com.apitest.dataProvider.DataSource;
import org.testng.annotations.Test;

public class BatchDataTest {

    @Test(threadPoolSize = 20)
    @com.apitest.annotations.TestData(source = DataSource.Spring,parallel = true)
    public void batch(TestData d){

        System.out.println(d.getName());


    }

}
