package com.apitest.sample.funcprovider;

import com.apitest.dataProvider.Func;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class FunctionWithParameterTest {

    @Test
    public void functionWithParameter(@Func(name = "getData",args = {"5","2"})int value){

    }

    public List<Integer> getData(String p1,String p2){
        int value = Integer.parseInt(p1);
        List<Integer> list = new ArrayList<>();
        for(int i =0;i<value;i++){
            list.add(i);
        }
        return list;
    }
}
