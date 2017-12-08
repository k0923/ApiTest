package com.apitest.dataProvider

import com.apitest.utils.ScriptUtils
import java.lang.reflect.Parameter

object Self:IDataProvider {
    override fun getData(para: Parameter, testDataConfig: TestDataConfig): List<Any?>? {
        return ScriptUtils.getEmptyConfigData(para)?.toList()
    }
}