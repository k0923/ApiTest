package com.apitest.dataProvider

import com.apitest.annotations.Filter
import com.apitest.annotations.TestData
import com.apitest.testModels.Student
import org.testng.Assert
import org.testng.annotations.Test

class CsvDataProviderTest {



    @Test
    @TestData(provider = Csv::class)
    @Filter(cls = ComboDataTest::class,methods = ["outerFilter"])
    fun test(student: Student){
        Assert.assertEquals(student.name,"ZhouYang")
        Assert.assertEquals(student.age,22)
        Assert.assertEquals(student.isMan,true)
        Assert.assertEquals(student.money,10000.0)
    }


}