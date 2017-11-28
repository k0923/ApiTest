package com.apitest.utils

import com.apitest.core.ITestData
import com.apitest.dataProvider.TestDataConfig
import com.apitest.extensions.ofType
import com.apitest.spring.beanConfig.ApiBaseDataBeanConfigProcessor
import com.apitest.spring.beanConfig.ApiBatchDataBeanConfigProcessor
import com.apitest.spring.common.BatchData
import com.apitest.utils.PathUtils.getClassFolder
import com.apitest.utils.PathUtils.getResourceFile
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader
import org.springframework.context.ApplicationContext
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.context.support.StaticApplicationContext
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import java.lang.reflect.Executable
import java.lang.reflect.Parameter
import java.util.concurrent.ConcurrentHashMap
import java.util.regex.Pattern
import javax.annotation.Nullable

object SpringUtils{

    private val contextCache:MutableMap<String,AbstractApplicationContext> = ConcurrentHashMap()

    private val logger = LogManager.getLogger(javaClass)

    fun getContext(cls:Class<*>): AbstractApplicationContext? {
        var file =cls.getResourceFile("xml")
        return getContext(file)
    }

    fun getContext(file:String): AbstractApplicationContext? = getContext(FileSystemResource(file))

    fun getContext(resource:Resource):AbstractApplicationContext?{
        if(contextCache.containsKey(resource.file.absolutePath)){
            return contextCache[resource.file.absolutePath]
        }
        return getContext(mapOf("ApiBaseDataBeanConfigProcessor" to ApiBaseDataBeanConfigProcessor::class.java
            ,"ApiBatchDataBeanConfigProcessor" to ApiBatchDataBeanConfigProcessor::class.java),resource)
    }

    @Synchronized
    private fun getContext(defaultBeans:Map<String,Class<*>>?,vararg resources: Resource): AbstractApplicationContext {
        var ctx = StaticApplicationContext()
        var xdr = XmlBeanDefinitionReader(ctx)
        defaultBeans?.forEach { ctx.registerSingleton(it.key,it.value) }
        if(resources.isNotEmpty()){
            resources.forEach { xdr.loadBeanDefinitions(it) }
        }
        ctx.refresh()

        resources.forEach {
            contextCache.put(it.file.absolutePath,ctx)
        }


        return ctx
    }

    fun clearAll(){
        contextCache.forEach{_,v->
            try{
                v.close()
            }catch (e:Exception){
                logger.error(e)
            }
        }
        contextCache.clear()
    }

    fun getDataV2(method:Executable,testDataConfig: TestDataConfig):List<Any>?{
        val params = method.parameters
        if(params.isEmpty()){
            throw IllegalArgumentException("No parameter found in method:$method")
        }
        val ctx = if(testDataConfig.file.isBlank()){
            getContext(method.declaringClass)
        }else{
            getContext("${method.declaringClass.getClassFolder()}/${testDataConfig.file}")
        }
        val dataList = arrayOfNulls<Array<out Any?>>(params.size)
        var i = 0
        params.forEach {
            dataList[i++] = getData(it,ctx!!)?.toTypedArray()
        }
        val result = CommonUtils.getCartesianProductByArray(dataList)
        return result.toList()
    }


    private fun getData(para:Parameter,ctx:ApplicationContext):List<Any>?{
        val nullable = para.getAnnotation(Nullable::class.java)
        if(nullable!=null)
            return null
        val paraType = para.type
        val qualifier = para.getAnnotation(Qualifier::class.java)
        return when(qualifier){
            null -> ctx.getBeansOfType(paraType).map { it.value }
            else -> ctx.getBeansOfType(paraType).filter { Pattern.matches(qualifier.value,it.key) }.map { it.value }.toList()
        }
    }


    fun getData(method: Executable,testDataConfig:TestDataConfig):List<Any?>{
        val paraClasses = method.parameterTypes
        if(paraClasses.size!=1){
            throw IllegalArgumentException("Only 1 parameter allowed in method:$method")
        }
        val ctx = if(testDataConfig.file.isBlank()){
            getContext(method.declaringClass)
        }else{
            getContext("${method.declaringClass.getClassFolder()}/${testDataConfig.file}")
        }
        //由于构造函数的name是全路径
        var name = method.name.split(".").last()
        return with(testDataConfig){
            when(single){
                true->{
                    val data = ctx?.getBean(name)
                    data?.ofType(ITestData::class.java)?.let {it.id = name}
                    if(data is BatchData){
                        data.data
                    }else{
                        listOf(data)
                    }
                }
                false->{
                    var mappedDatas = ctx?.getBeansOfType(paraClasses[0])
                    if(pattern.isNotEmpty()){
                        mappedDatas = mappedDatas?.filter {
                            Pattern.matches(pattern,it.key)
                        }
                    }
                    val list = ArrayList<Any?>()
                    mappedDatas?.forEach { k, v ->
                        list.add(v)
                        v.ofType(ITestData::class.java)?.let{it.id = k}
                    }
                    list
                }
            }
        }
    }
}