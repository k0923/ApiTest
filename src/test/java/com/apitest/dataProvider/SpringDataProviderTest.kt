package com.apitest.dataProvider


import org.testng.Assert
import org.testng.annotations.Test

class SpringDataProviderTest{



    @Test(groups = ["p1"])
    fun noDataProviderTest(){
        println("Hello")
    }

    @Test(groups = ["p1"])
    fun singleTestData(@Spring data:String){
        Assert.assertEquals(data,"singleTestData")
    }


    @Test(groups = ["p1"])
    fun batchTestData(@Spring(pattern = ".+") data:String){
        System.out.println(data)

    }



    @Test(groups = ["p1"])
    fun batchFilterTest(@Spring(pattern = "single.*") data:String){

        Assert.assertEquals(data,"singleTestData")
    }


    @Test(groups = ["p1"])
    fun otherFileTest(@Spring(files=["file.xml"]) data:String){
        Assert.assertEquals(data,"otherFileTest")
    }

    @Test(groups = ["p1"])
    fun getSystemData(@Spring(pattern = ".+")d:Long){
        println(d)
    }
//    ----------------












}