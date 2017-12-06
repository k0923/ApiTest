package com.apitest.dataProvider

import com.apitest.annotations.Filter
import com.apitest.annotations.TestData
import com.apitest.annotations.TestDatas
import com.apitest.testModels.Console
import com.apitest.testModels.Student
import org.testng.Assert
import org.testng.annotations.Test

class CsvDataProviderTest {

//    @Factory
//    constructor(console:Console)

    @Test
    @TestData(provider = Csv::class)
    @Filter(cls = ComboDataTest::class,methods = ["outerFilter"])
    fun test(student: Student,console:Console){
        Assert.assertEquals(student.name,"ZhouYang")
        Assert.assertEquals(student.age,22)
        Assert.assertEquals(student.isMan,true)
        Assert.assertEquals(student.money,10000.0)
    }

    @Test
    @TestDatas(
            value = [
                TestData(provider = Csv::class),
                TestData(provider = Csv::class),
                TestData(provider = Csv::class)
            ]
    )
    fun test1(student: Map<String,String>,stu1:Student,stu2:Map<String,String>){
        Assert.assertNotNull(student)
    }


}