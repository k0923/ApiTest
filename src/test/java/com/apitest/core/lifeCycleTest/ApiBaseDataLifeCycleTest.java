package com.apitest.core.lifeCycleTest;

import com.apitest.annotations.TestData;
import com.apitest.core.ITestScript;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ApiBaseDataLifeCycleTest implements ITestScript {

    @Test
    @TestData
    public void test(LifeCycleTestData data){
        Assert.assertEquals(data.getBeforeResult(),"before");
        Assert.assertNull(data.getAfterResult());
        Assert.assertNull(data.getFinalResult());
    }



}
