package com.apitest.core.scriptTest;

import com.apitest.core.ApiScript;
import com.apitest.core.lifeCycleTest.ApiBaseDataLifeCycleTest;
import com.apitest.core.lifeCycleTest.LifeCycleTestData;
import kotlin.Pair;
import org.springframework.util.ReflectionUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class ApiScriptTest {

    @Test
    public void baseScriptTest(){
        Method m = ReflectionUtils.findMethod(ApiBaseDataLifeCycleTest.class,"test", LifeCycleTestData.class);
        ApiScript apiScript = new ApiScript(m);
        apiScript.execute();
    }

    @Test
    public void flowTest_001(TestFlowData data){
        Assert.assertNotNull(data.getName());
        Assert.assertNotEquals(data.getName(),"@name");
    }

    @Test
    public void flowTest_002(TestFlowData data){
        Assert.assertEquals(data.getName(),"18");
    }

}
