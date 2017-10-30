package com.apitest.dataProvider


import com.apitest.utils.ScriptUtils
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.testng.annotations.DataProvider
import java.lang.reflect.Method

class SpringDataProvider{

    private val logger:Logger = LogManager.getLogger(javaClass)

    @DataProvider(name="spring")
    fun getData(method:Method):Array<Array<Any?>>{
        var data = ScriptUtils.getTestData(method)
        return Array(data.size){ index->Array(1){data[index]}}
    }

    @DataProvider(name="spring-parallel",parallel = true)
    fun getPData(method:Method):Array<Array<Any?>>{
        return getData(method)
    }



}