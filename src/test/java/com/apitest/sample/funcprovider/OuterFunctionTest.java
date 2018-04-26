package com.apitest.sample.funcprovider;

import com.apitest.dataProvider.Func;
import com.apitest.testModels.Console;
import org.testng.annotations.Test;

public class OuterFunctionTest {
    @Test
    public void outerFunction(@Func(name = "getConsoles",provider = OuterFunctionProvider.class) Console console){

    }
}
