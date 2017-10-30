//package com.apitest.nglisteners
//
//import org.apache.logging.log4j.LogManager
//import org.apache.logging.log4j.Logger
//import org.testng.ITestNGListener
//
//abstract class ApiTestNgListener<T:ITestNGListener>{
//    private val handlers:MutableMap<Class<*>,T>  = HashMap()
//
//    private val logger:Logger = LogManager.getLogger(javaClass)
//
//    protected fun registerHandler(cls:Class<*>,handler:T) = handlers.put(cls,handler)
//
//    protected fun execute(matcher:(Class<*>)->Boolean,consumer:(T)->Unit){
//        if(handlers.isNotEmpty()){
//            handlers.forEach{
//                if(matcher(it.key)){
//                   consumer(it.value)
//                }
//            }
//        }
//    }
//}