package com.apitest.dataProvider

import com.apitest.utils.ScriptUtils
import org.testng.annotations.DataProvider
import java.lang.reflect.Method

class CsvDataProvider {

    @DataProvider(name="csv")
    fun getData(method: Method):Array<Array<Any?>>{
        var data = ScriptUtils.getTestData(method)
        return Array(data.size){ index->Array(1){data[index]}}
    }

    @DataProvider(name="csv-parallel",parallel = true)
    fun getPData(method: Method):Array<Array<Any?>>{
        return getData(method)
    }
}