package com.apitest.dataProvider
import com.apitest.utils.DataUtils
import java.lang.reflect.Parameter

@Suppress("UNCHECKED_CAST")
abstract class AbstractDataProvider<T:Annotation> : IDataProvider {

    abstract fun getGenericData(para:Parameter,annotation:T,testInstance:Any?):List<Any?>?

    override fun getData(parameter: Parameter, annotation: Annotation,testInstance:Any?): List<Any?>? {
        return getGenericData(parameter,annotation as T,testInstance)
    }

    fun clone(para: Parameter, testData: T, currentData: List<Any?>?) : List<Any?>?{
        return currentData?.let { DataUtils.clone(it) }
    }

}