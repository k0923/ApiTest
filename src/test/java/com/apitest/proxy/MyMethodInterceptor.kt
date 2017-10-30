package com.apitest.proxy

import net.sf.cglib.proxy.MethodInterceptor
import net.sf.cglib.proxy.MethodProxy
import java.lang.reflect.Method

class MyMethodInterceptor:MethodInterceptor {
    override fun intercept(p0: Any?, p1: Method?, p2: Array<out Any>?, p3: MethodProxy?): Any? {
        println("Start")
        var obj = p3?.invokeSuper(p0,p2)
        println("End")
        return obj
    }
}