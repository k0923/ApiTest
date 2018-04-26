package com.apitest.sample.funcprovider;

import com.apitest.dataProvider.Func;
import java.util.List;
import org.testng.annotations.Test;

import java.util.Arrays;

public class InnerFunctionTest {

    @Test
    public void innerFuntion(@Func(name = "getValues") String value){

    }

    public List<String> getValues(){
        return Arrays.asList("A","B","C");
    }

}
