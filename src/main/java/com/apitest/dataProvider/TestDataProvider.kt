package com.apitest.dataProvider

import com.apitest.utils.ScriptUtils
import org.testng.annotations.DataProvider
import java.lang.reflect.Method

class TestDataProvider {

    @DataProvider
    fun getData(method: Method):Array<Array<Any?>>{
        val data = ScriptUtils.getTestData(method)
        return Array(data.size){ index->Array(1){data[index]}}
    }

    @DataProvider(parallel = true)
    fun getDataParallel(method: Method):Array<Array<Any?>> = getData(method)

}