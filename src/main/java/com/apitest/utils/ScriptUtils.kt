package com.apitest.utils

import com.apitest.dataProvider.IDataProvider
import com.apitest.dataProvider.IParameterProvider
import com.apitest.dataProvider.Provider
import com.apitest.extensions.ofType
import com.apitest.nglisteners.ApiTestListener
import org.apache.logging.log4j.LogManager
import java.lang.reflect.Executable
import java.lang.reflect.Parameter
import java.util.function.Function
import java.util.function.Supplier
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSuperclassOf

object ScriptUtils {

    private val logger = LogManager.getLogger(SpringUtils::class.java)

    fun getInstance(cls:KClass<*>):Any = cls.objectInstance ?: cls.createInstance()

    fun getDataTemplate(params:List<Any?>, other:List<Any?>, configAction:Function<Int,Array<out Any?>?>, otherAction:Function<Int,Array<out Any?>?>):Array<Array<Any?>>{
        val data = Array<Supplier<Array<out Any?>?>>(params.size,{ Supplier { null }})
        val consumer:(Iterable<Int>,Function<Int,Array<out Any?>?>)->Unit = { i,f->i.forEach{data[it]= Supplier { f.apply(it) }} }
        if(other.size>params.size){
            consumer(params.indices,configAction)
        }else{
            consumer(other.indices,configAction)
            consumer((other.size until params.size),otherAction)
        }
        return CommonUtils.getCartesianProductBySupplier(data)
    }

    fun getTestDataByPara(para:Parameter,instance:Any?):Array<out Any?>?{
        val annotations = para.annotations
        var provider: IDataProvider? = null
        var annotation:Annotation? = null
        annotations.forEach {
            if(ApiTestListener.dataProviders.contains(it.annotationClass)){
                val providerCls = it.annotationClass.findAnnotation<Provider>()!!.value
                provider = providerCls.objectInstance ?: providerCls.createInstance()
                annotation = it
            }

        }
        if(provider!=null){
            val data = provider!!.getData(para,annotation!!,instance)?.toTypedArray()
            return data
        }
        return  getEmptyConfigData(para)
    }


    fun getTestData(method:Executable,instance:Any?):Array<Array<Any?>>{
        val data = Array(method.parameterCount,{ Supplier {
            getTestDataByPara(method.parameters[it],instance)
        }})

        return CommonUtils.getCartesianProductBySupplier(data)
    }



    fun getEmptyConfigData(para:Parameter):Array<out Any?>?{
        val tp = para.type
        return when{
            tp.isEnum -> tp.enumConstants
            IParameterProvider::class.isSuperclassOf(tp.kotlin) -> ScriptUtils.getInstance(tp.kotlin).ofType(IParameterProvider::class.java)?.getData()?.toTypedArray()
            else -> null
        }
    }
}

