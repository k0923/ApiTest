package com.apitest.dataProvider

import com.apitest.utils.DataUtils
import java.lang.reflect.Parameter

interface IDataProvider {

    fun getData(parameter: Parameter, annotation: Annotation): List<Any?>?


}