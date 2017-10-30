package com.apitest.dataProvider;

import org.testng.Assert;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class FactoryTest {

    private String d;

    @Factory
    @TestData(file = "SpringDataProviderTest.xml")
    public FactoryTest(String data){
        this.d = data;
    }


    @Test(groups = {"factory"})
    public void test(){
        Assert.assertEquals(d,"FactoryTest");
    }

}
