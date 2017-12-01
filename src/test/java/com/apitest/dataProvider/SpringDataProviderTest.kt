package com.apitest.dataProvider

import com.apitest.annotations.TestData
import com.apitest.common.TestMethodLocal
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
//    ----------------
    @Test
    fun test(){
        val m = this::myFun.javaMethod

        val config = ScriptUtils.getTestDataConfig(m!!)[0]
        val result = SpringUtils.getData(m,config)

    }

    @Test
    fun myFun(data1:String,@Qualifier(".+Test.+")data2:String,data3:String){

    }

    @Test
    fun myFun1(@Qualifier data1:String){

    }

    @Test
    fun myFun2(data1:String,@Qualifier data2:String?){

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