package com.apitest.testng;

import com.apitest.utils.ICommonUtils;
import org.testng.annotations.Test;

public class DependencyTest implements ICommonUtils{



    @Test
    public void t1(){
        println("t1");
    }

    @Test(dependsOnMethods = {"t1"})
    public void t2(){
        println("t2");
    }

    @Test(dependsOnMethods = {"t2"})
    public void t3(){
        println("t3");
    }

}
