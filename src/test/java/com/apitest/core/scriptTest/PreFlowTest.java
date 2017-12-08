package com.apitest.core.scriptTest;

import com.apitest.annotations.TestData;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PreFlowTest {

    @Test
    @TestData
    public void preFlow1(TestFlowData data){
        Assert.assertEquals(data.getName(),"preFlowName");
    }

    @Test
    @TestData
    public void preFlow2(PreFlowData data){
        System.out.println(data.getAge());

    }

}
