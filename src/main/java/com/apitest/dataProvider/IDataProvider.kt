package com.apitest.dataProvider

import java.lang.reflect.Parameter

interface IDataProvider {

    fun getData(parameter: Parameter, annotation: Annotation,instance:Any?): Array<Any?>


}