package com.apitest.dataProvider

import java.lang.reflect.Parameter

interface IDataProvider {

    fun getData(parameter: Parameter, annotation: Annotation,instance:Any?): List<Any?>


    fun clone(data:List<Any?>):List<Any?>
}