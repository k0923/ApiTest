package com.apitest.dataProvider

import com.apitest.annotations.TestData
import com.apitest.utils.ScriptUtils
import java.lang.reflect.Parameter

object Self:AbstractDataProvider() {
    override fun getData(para: Parameter, testData: TestData): List<Any?>? {
        return ScriptUtils.getEmptyConfigData(para)?.toList()
    }
}