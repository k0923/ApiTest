package com.apitest.core.baseDataValidTest;

import com.apitest.dataProvider.Spring;
import org.testng.annotations.Test;

public class DataValidTest {

    @Test
    public void test(@Spring TestContext data){
        data.validate();
    }

}
