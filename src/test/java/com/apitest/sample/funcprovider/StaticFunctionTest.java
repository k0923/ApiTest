package com.apitest.sample.funcprovider;

import com.apitest.dataProvider.Func;
import org.testng.annotations.Test;

public class StaticFunctionTest {

    @Test
    public void staticFunction(@Func(name = "getValues",provider = StaticFunctionProvider.class) int value){

    }

}


