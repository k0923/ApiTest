package com.apitest.common

import org.testng.annotations.Test
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap

class TestMethodLocal<T>(private val default:T?){

    val methodMap:MutableMap<Method?,T?> = ConcurrentHashMap()

    fun get():T?{
        var method = getMethod()
        return if(methodMap.containsKey(method)){
            methodMap[method]
        }else{
            methodMap.put(method,default)
            default
        }
    }

    fun set(value:T?){
        var method=getMethod()
        methodMap.put(method,value)
    }


    private fun getMethod():Method?{
        var traces = Thread.currentThread().stackTrace
        for(i in 1..traces.size){
            var trace = traces[i]
            var cls = Class.forName(trace.className)
            var methods = cls.methods.filter { it.name.equals(trace.methodName) }
            if(methods.size==1){
                var test = methods[0].getAnnotation(Test::class.java)
                test?.let {
                    return methods[0]
                }
//                if(test!=null){
//                    return methods[0]
//                }
            }
        }
        return null
    }
}