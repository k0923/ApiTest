package com.apitest.spring.utils;

import com.apitest.spring.utils.models.SpringTestModel;
import org.springframework.util.ReflectionUtils;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class ReflectionUtilsTest {

    @Test
    public void getMethodTest(){
        Method m = ReflectionUtils.findMethod(SpringTestModel.class,"test");

    }

}
