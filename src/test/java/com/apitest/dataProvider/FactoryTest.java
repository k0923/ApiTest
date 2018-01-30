package com.apitest.dataProvider;

import com.apitest.annotations.Filter;
import org.testng.Assert;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class FactoryTest {

    private String d;

    @Factory
    @Filter(cls = FactoryTest.class,method = "filterData")
    public FactoryTest(@Spring(files = {"SpringDataProviderTest.xml"},pattern = ".+") String data){
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
