package com.apitest.core


import com.apitest.extensions.ofType
import com.apitest.utils.ScriptUtils
import org.apache.logging.log4j.LogManager
import java.lang.reflect.Method
import java.util.*
import kotlin.collections.HashSet

class ApiScript(val clazz:Class<*>, val methodName:String){
    private val logger = LogManager.getLogger(javaClass)

    constructor(method:Method):this(method.declaringClass,method.name)

    var properties:Map<String,Object>?=null

    var method:Method? = null
        get(){
            when(field){
                null->{
                    var methods = clazz.methods.filter { it.name == methodName && it.parameterCount<2 }
                    when(methods.size){
                        1->method = methods[0]
                        0->throw NoSuchMethodException("method:$methodName not found in class:$clazz")
                        else->throw RuntimeException("same methodname:$methodName found in class:$clazz")
                    }
                }
            }
            return field!!
        }
        private set


    var data:Any?=null
        get(){
            when(field){
                null->{
                    var datas = ScriptUtils.getTestData(method!!)
                    field = when(datas.size){
                        0 -> null
                        1 -> datas[0][0]
                        else -> throw RuntimeException("Multiple data got from method:$method")
                    }
                }
            }
            return field
        }
        private set



    fun execute():MutableMap<String,Any?>{
        var scriptStack = Stack<ApiScript>()
        scriptStack.push(this)
        data?.ofType(ApiBaseData::class.java)?.let {
            var scriptSet = HashSet<String>()
            var tempScript = it.preApiScript
            while(tempScript!=null){
                var key = "${tempScript.clazz.name}:${tempScript.methodName}"
                if(scriptSet.contains(key)){
                    throw RuntimeException("script:$tempScript alreay exists")
                }
                scriptSet.add(key)
                scriptStack.add(tempScript)
                var tempData = tempScript.data
                tempScript = when(tempData){
                    null->null
                    else->tempData!!.ofType(ApiBaseData::class.java)?.preApiScript
                }
            }
        }
        var flowScript = ApiFlowScript()
        while(scriptStack.isNotEmpty()){
            flowScript.addScript(scriptStack.pop())
        }
        return flowScript.execute()
    }



}