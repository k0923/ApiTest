package com.apitest.utils

import com.apitest.dataProvider.Provider
import com.apitest.nglisteners.ApiTestListener
import org.apache.logging.log4j.LogManager
import java.lang.reflect.Executable
import java.lang.reflect.Parameter
import java.util.function.Function

import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation


object ScriptUtils {

    private val logger = LogManager.getLogger(SpringUtils::class.java)




    fun getDataFromProvider(para:Parameter,instance:Any?):Pair<List<Any?>,Function<List<Any?>,List<Any?>>>{
        val annotation = para.annotations.firstOrNull { ApiTestListener.dataProviders.contains(it.annotationClass) }
        val providerCls = annotation?.annotationClass?.findAnnotation<Provider>()?.value
        val provider = providerCls?.objectInstance ?: providerCls?.createInstance()
        return when{
            provider == null -> Pair(getEmptyConfigData(para), Function { DataUtils.clone(it)!! })
            else -> Pair(provider.getData(para,annotation!!,instance), Function { provider.clone(it) })
        }

    }



    fun getTestData(method:Executable,instance:Any?):Array<Array<Any?>>{
        val data = method.parameters.map {
            getDataFromProvider(it,instance)
        }.toTypedArray()

        return MathUtils.getResult(*data)
    }



    fun getEmptyConfigData(para:Parameter):List<Any?>{
        val tp = para.type
        return when{
            tp.isEnum -> tp.enumConstants.toList()
            else -> listOf(null)
        }
    }
}

