package com.apitest.testng;

import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class FactoryTest {
    private int i;

    public FactoryTest(){

    }

    public FactoryTest(int i){
        this.i = i;
    }

    @Test
    public void test(){
        System.out.println(i);
    }



}

