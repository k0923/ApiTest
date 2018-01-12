package com.apitest.dataProvider

import com.apitest.annotations.Filter
import com.apitest.testModels.Console
import com.apitest.testModels.Student
import org.testng.Assert
import org.testng.annotations.Test

class CsvDataProviderTest {

//    @Factory
//    constructor(console:Console)

    @Test
    @Filter(cls = ComboDataTest::class,methods = ["outerFilter"])
    fun test(@Csv student: Student, console:Console){
        Assert.assertEquals(student.name,"ZhouYang")
        Assert.assertEquals(student.age,22)
        Assert.assertEquals(student.isMan,true)
        Assert.assertEquals(student.money,10000.0)
    }

    @Test
    fun test1(@Csv student: Map<String,String>, @Csv stu1:Student, @Csv stu2:Map<String,String>){
        Assert.assertNotNull(student)
    }


}