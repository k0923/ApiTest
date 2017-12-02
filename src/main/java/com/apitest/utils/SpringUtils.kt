package com.apitest.utils

import com.apitest.spring.beanConfig.ApiBaseDataBeanConfigProcessor
import com.apitest.utils.PathUtils.getResourceFile
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.context.support.StaticApplicationContext
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import java.io.File
import java.util.concurrent.ConcurrentHashMap

object SpringUtils{

    private val contextCache:MutableMap<String,AbstractApplicationContext> = ConcurrentHashMap()

    private val logger = LogManager.getLogger(javaClass)

    fun isContextExisted(cls:Class<*>):Boolean{
        val file = cls.getResourceFile("xml")
        return File(file).exists()
    }

    fun getContext(cls:Class<*>): AbstractApplicationContext? {
        val file =cls.getResourceFile("xml")
        return getContext(file)
    }

    fun getContext(file:String): AbstractApplicationContext? = getContext(FileSystemResource(file))

    fun getContext(resource:Resource):AbstractApplicationContext?{
        if(contextCache.containsKey(resource.file.absolutePath)){
            return contextCache[resource.file.absolutePath]
        }
        return getContext(mapOf("ApiBaseDataBeanConfigProcessor" to ApiBaseDataBeanConfigProcessor::class.java
            //,"ApiBatchDataBeanConfigProcessor" to ApiBatchDataBeanConfigProcessor::class.java
        ),resource)
    }

    @Synchronized
    private fun getContext(defaultBeans:Map<String,Class<*>>?,vararg resources: Resource): AbstractApplicationContext {
        val ctx = StaticApplicationContext()
        val xdr = XmlBeanDefinitionReader(ctx)
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


}