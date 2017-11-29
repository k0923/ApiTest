package com.apitest.dataProvider

import com.apitest.annotations.TestData
import com.apitest.testModels.StudentModel
import org.testng.Assert
import org.testng.annotations.Test

class CsvDataProviderTest {

    @Test
    @TestData(source = DataSource.CSV)
    fun test(studentModel: StudentModel){
        Assert.assertEquals(studentModel.name,"ZhouYang")
        Assert.assertEquals(studentModel.age,22)
        Assert.assertEquals(studentModel.isMan,true)
        Assert.assertEquals(studentModel.money,10000.0)
    }


}