package com.apitest.sample;

import com.apitest.dataProvider.Value;
import org.testng.annotations.Test;


public class ValueProviderTest {

    @Test
    public void intValue(@Value(ints = {1,2,3}) int value){

    }

    @Test void stringValue(@Value(strings = {"T1","T2","T3"})String value){

    }

}
