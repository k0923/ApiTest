package com.apitest.dataProvider

import java.lang.reflect.Executable
import kotlin.reflect.full.createInstance

object CustomDataUtils{
    fun getData(method: Executable, testDataConfig: TestDataConfig):Array<Array<Any?>>{
        val paraClasses = method.parameterTypes
        if(paraClasses.size!=1){
            throw IllegalArgumentException("Only 1 parameter allowed in method:$method")
        }

        if(testDataConfig.dataProvider == IDataProvider::class){
            throw IllegalArgumentException("Custom Data Provider should implement interface IDataProvider")
        }


        val dataProvider = testDataConfig.dataProvider.createInstance()
        return dataProvider.getData(method,testDataConfig).map { arrayOf(it) }.toTypedArray()
    }
}