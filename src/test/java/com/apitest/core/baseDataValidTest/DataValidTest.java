package com.apitest.core.baseDataValidTest;

import com.apitest.annotations.TestData;
import org.testng.annotations.Test;

public class DataValidTest {

    @Test
    @TestData
    public void test(TestContext data){
        data.validate();
    }

}
