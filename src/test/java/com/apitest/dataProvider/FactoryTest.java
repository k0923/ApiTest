package com.apitest.dataProvider;

import com.apitest.annotations.Filter;
import com.apitest.annotations.TestData;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.Assert;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class FactoryTest {

    private String d;

    @Factory
    @TestData(paras = "SpringDataProviderTest.xml")
    @Filter(cls = FactoryTest.class,methods = "filterData")
    public FactoryTest(@Qualifier(".+") String data){
        this.d = data;
    }


    @Test(groups = {"factory"})
    public void test(){
        Assert.assertEquals(d,"FactoryTest");
    }


    public static boolean filterData(String d){
        return "FactoryTest".equals(d);
    }

}
