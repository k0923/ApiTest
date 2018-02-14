package com.apitest.config

import com.apitest.utils.SpringUtils
import org.apache.logging.log4j.LogManager
import org.springframework.context.ApplicationContext
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.core.io.ClassPathResource
import java.util.*


object GlobalConfig {

    private var ctx: AbstractApplicationContext? = null

    fun getCtx():AbstractApplicationContext? {
        return ctx
    }

    private var lazyCtx:AbstractApplicationContext? = null

    var currentEnv: Environment = Environment.sit

    private val testEnvConfigPath = "testEnv.properties"



    private val envConfigPath = "apitestconfig/env.properties"

    private val globalConfigPath = "apitestconfig/globalConfig.xml"

    private val lazyConfigPath = "apitestconfig/lazyConfig.xml"

    private val envPropertyKey = "env"

    private val retryCountPropertyKey = "retryCount"

    private val globalFilePath = "globalFile"

    private val retryCountDefault = 5

    private val logger = LogManager.getLogger(GlobalConfig::class.java)

    private var retryCount:Int =0

    init {
        val envConfigResource = ClassPathResource(testEnvConfigPath)

        if(envConfigResource.exists()){
            val props = Properties()
            props.load(envConfigResource.inputStream)
            props.getProperty(envPropertyKey)?.let { currentEnv = Environment.valueOf(it.trim().toLowerCase()) }
            props.getProperty(retryCountPropertyKey)?.let { retryCount = Integer.parseInt(it) }
            props.getProperty(globalFilePath)?.let {
                val contextResource = ClassPathResource(it)
                if(contextResource.exists()){
                    ctx = SpringUtils.getContext(contextResource);
                }
            }


            if(retryCount> retryCountDefault || retryCount<0){
                throw IllegalArgumentException("Global apitestconfig of retryCount should between 0 and $retryCountDefault")
            }
        }else{
            logger.warn("未找到配置文件testEnv.properties,全局上下文为空")
        }


        logger.info("设置当前环境为:$currentEnv")
        logger.info("设置全局的重置次数为:$retryCount")
    }




    fun <T> getLazy(cls:Class<T>):T? = getTemplateLazy { it.getBean(cls) }

    fun <T> getLazy(id:String,cls:Class<T>):T? = getTemplateLazy { it.getBean(id,cls) }

    fun getLazy(id:String):Any? = getTemplateLazy { it.getBean(id) }

    fun getLazy(id:String,vararg args:Any?):Any? = getTemplateLazy { it.getBean(id,args) }

    fun <T> get(cls:Class<T>):T? = getTemplate { it.getBean(cls) }

    fun <T> get(id:String,cls:Class<T>):T? = getTemplate { it.getBean(id,cls) }

    fun get(id:String):Any? = getTemplate { it.getBean(id) }

    fun get(id:String,vararg args:Any?):Any? = getTemplate { it.getBean(id,args) }


    private fun <T> getTemplate(method:(ApplicationContext)->T):T? = ctx?.let{ return method(it) }

    private fun <T> getTemplateLazy(method:(ApplicationContext)->T):T?{
        if(lazyCtx == null){
            val contextResource = ClassPathResource(lazyConfigPath)
            if(contextResource.exists()){
                lazyCtx = SpringUtils.getContext(contextResource)
            }
        }
        return method(lazyCtx!!)
    }



}