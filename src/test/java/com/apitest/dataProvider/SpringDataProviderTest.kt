package com.apitest.dataProvider

import com.apitest.annotations.TestData
import com.apitest.common.TestMethodLocal
import com.apitest.testModels.Console
import com.apitest.testModels.Student
import com.apitest.utils.ScriptUtils
import org.springframework.beans.factory.annotation.Qualifier
import org.testng.Assert
import org.testng.annotations.AfterMethod
import org.testng.annotations.Test
import kotlin.reflect.jvm.javaMethod

class SpringDataProviderTest{

    var count = TestMethodLocal(0)

    @Test(groups = ["p1"])
    fun noDataProviderTest(){
        println("Hello")
    }

    @Test(groups = ["p1"])
    @TestData
    fun singleTestData(data:String){
        Assert.assertEquals(data,"singleTestData")
    }


    @Test(groups = ["p1"])
    @TestData
    fun batchTestData(@Qualifier(".+") data:String){
        System.out.println(data)
        count.set(count.get()?.plus(1))
    }



    @Test(groups = ["p1"])
    @TestData
    fun batchFilterTest(@Qualifier("single.*") data:String){
        count.set(count.get()?.plus(1))
        Assert.assertEquals(data,"singleTestData")
    }


    @Test(groups = ["p1"])
    @TestData(paras = ["file.xml"])
    fun otherFileTest(data:String){
        Assert.assertEquals(data,"otherFileTest")
    }

    @Test(groups = ["p1"])
    @TestData
    fun getSystemData(@Qualifier(".+")d:Long){
        println(d)
    }
//    ----------------












}