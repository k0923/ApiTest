package com.apitest.dataProvider
import com.apitest.utils.DataUtils
import java.lang.reflect.Parameter

@Suppress("UNCHECKED_CAST")
abstract class AbstractDataProvider<T:Annotation> : IDataProvider {

    abstract fun getGenericData(para:Parameter,annotation:T,testInstance:Any?):List<Any?>?

    override fun getData(parameter: Parameter, annotation: Annotation,testInstance:Any?): List<Any?> {
        val result = getGenericData(parameter,annotation as T,testInstance)
        return when{
            result == null || result.size == 0 -> emptyList()
            else -> result
        }
    }

    override fun clone(data: List<Any?>): List<Any?> {
        return DataUtils.clone(data)!!
    }


}