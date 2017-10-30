package com.apitest.dataProvider

import com.apitest.common.TestMethodLocal
import org.testng.Assert
import org.testng.annotations.Test

class SpringDataProviderTest{

    var count = TestMethodLocal(0)

    @Test
    fun noDataProviderTest(){
        println("Hello")
    }

    @Test
    fun singleTestData(data:String){
        Assert.assertEquals(data,"singleTestData")
    }


    @Test
    @TestData(single = false)
    fun batchTestData(data:String){
        System.out.println(data)
        count.set(count.get()?.plus(1))
    }



    @Test
    @TestData(single = false,pattern = "single.*")
    fun batchFilterTest(data:String){
        count.set(count.get()?.plus(1))
        Assert.assertEquals(data,"singleTestData")
    }


    @Test
    @TestData(file = "file.xml")
    fun otherFileTest(data:String){
        Assert.assertEquals(data,"otherFileTest")
    }

    @Test
    @TestData(single = false)
    fun getSystemData(d:Long){
        println(d)
    }





}