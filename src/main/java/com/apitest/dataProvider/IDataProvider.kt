package com.apitest.dataProvider

import java.lang.reflect.Parameter

interface IDataProvider {



    fun getData(para: Parameter, testDataConfig: TestDataConfig):List<Any?>?


}