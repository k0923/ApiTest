package com.apitest.nglisteners;

import org.testng.annotations.Test;

public class MethodDependencyTest {

    @Test
    public void testA(){

    }


    @Test(dependsOnMethods = "testA")
    public void testB(){

    }

    @Test(dependsOnMethods = "testB")
    public void testC(){

    }

}
