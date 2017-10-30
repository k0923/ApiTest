package com.apitest.core.baseDataValidTest;

import org.testng.annotations.Test;

public class DataValidTest {

    @Test
    public void test(TestContext data){
        data.validate();
    }

}
