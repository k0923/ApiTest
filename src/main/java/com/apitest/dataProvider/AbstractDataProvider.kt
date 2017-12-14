package com.apitest.dataProvider

import com.apitest.annotations.TestData
import com.apitest.utils.DataUtils
import java.lang.reflect.Parameter

abstract class AbstractDataProvider : IDataProvider {


    override fun clone(para: Parameter, testData: TestData, currentData: List<Any?>?) : List<Any?>?{
        return currentData?.let { DataUtils.clone(it) }
    }

}