package com.apitest.dataProvider

import com.apitest.annotations.TestData
import com.apitest.common.TestMethodLocal
import com.apitest.testModels.Console
import com.apitest.testModels.Student
import com.apitest.utils.ScriptUtils
import com.apitest.utils.SpringUtils
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
    fun singleTestData(data:String){
        Assert.assertEquals(data,"singleTestData")
    }


    @Test(groups = ["p1"])
    fun batchTestData(@Qualifier(".+") data:String){
        System.out.println(data)
        count.set(count.get()?.plus(1))
    }



    @Test(groups = ["p1"])
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
    fun getSystemData(@Qualifier(".+")d:Long){
        println(d)
    }
//    ----------------
    @Test
    fun test(){
        val m = this::myFun.javaMethod

        val config = ScriptUtils.getTestDataConfig(m!!)[0]
        val result = Spring.getData(m,config)


    }

    @Test
    fun myFun(data1:String,@Qualifier(".+Test.+")data2:String,data3:String){

    }

    @Test
    fun myFun1(@Qualifier data1:String?){

    }

    @Test
    fun myFun2(data1:String,@Qualifier data2:String?){

    }

    @Test
    fun myFun3(data1:String,console:Console){

    }


    val set = HashSet<Student>()

    @Test
    fun myFun3(stu1:Student,stu2:Student){
       set.add(stu1)
        set.add(stu2)
    }

    @AfterMethod
    fun show(){
        println(set.size)
    }




}