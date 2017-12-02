package com.apitest.dataProvider

import java.lang.reflect.Executable
import java.lang.reflect.Parameter

interface IDataProvider {

    fun getData(method:Executable,testDataConfig:TestDataConfig):Array<Array<Any?>>

    fun getData(para: Parameter, testDataConfig: TestDataConfig):List<Any?>

}