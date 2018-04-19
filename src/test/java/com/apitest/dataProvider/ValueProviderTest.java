package com.apitest.dataProvider;

import com.apitest.utils.DataUtils;
import org.testng.annotations.Test;

public class ValueProviderTest {

    @Test
    public void test(@Value(ints = {1,2}) int a,@Value(ints = {3,4,5}) int b){

    }

    @Test
    public void t1(){
       int number = DataUtils.INSTANCE.clone(1);
       System.out.println(number);
    }

}
