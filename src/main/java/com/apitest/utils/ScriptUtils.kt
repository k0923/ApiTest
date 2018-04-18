package com.apitest.utils

import com.apitest.dataProvider.Provider
import com.apitest.nglisteners.ApiTestListener
import org.apache.logging.log4j.LogManager
import java.lang.reflect.Executable
import java.lang.reflect.Parameter

import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation


object ScriptUtils {

    private val logger = LogManager.getLogger(SpringUtils::class.java)


    fun getDataFromProvider(para:Parameter,instance:Any?):Array<out Any?>{
        val annotation = para.annotations.firstOrNull { ApiTestListener.dataProviders.contains(it.annotationClass) }
        val providerCls = annotation?.annotationClass?.findAnnotation<Provider>()?.value
        val provider = providerCls?.objectInstance ?: providerCls?.createInstance()
        return when{
            provider == null -> getEmptyConfigData(para)
            else -> provider.getData(para,annotation!!,instance)
        }
    }



    fun getTestData(method:Executable,instance:Any?):Array<Array<Any?>>{
        val data = Array(method.parameterCount,{ getDataFromProvider(method.parameters[it],instance)})
        return MathUtils.getResult(*data)
    }



    fun getEmptyConfigData(para:Parameter):Array<out Any?>{
        val tp = para.type
        return when{
            tp.isEnum && tp.enumConstants.size > 0 -> tp.enumConstants
            else -> arrayOfNulls(1)
        }
    }
}

