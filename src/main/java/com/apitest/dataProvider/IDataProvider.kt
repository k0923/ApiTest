package com.apitest.dataProvider

import com.apitest.annotations.TestData
import java.lang.reflect.Parameter

interface IDataProvider {

    fun getData(para: Parameter, testData: TestData):List<Any?>?

    fun clone(para:Parameter,testData:TestData,currentData:List<Any?>?):List<Any?>?
}