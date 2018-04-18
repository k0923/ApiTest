package com.apitest.dataProvider

import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.lang.reflect.Parameter
import kotlin.reflect.*
import kotlin.reflect.full.createType
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.jvm.javaMethod
import kotlin.reflect.jvm.javaType


object FuncDataProvider : AbstractDataProvider<Func>() {


    override fun getGenericData(para: Parameter, annotation: Func,testInstance:Any?): List<Any?>? {
        val method = getMethod(para,annotation)

        val obj = when{
            Modifier.isStatic(method.modifiers) -> null //静态方法
            method.declaringClass == testInstance?.javaClass -> testInstance //实例方法
            method.declaringClass.kotlin.objectInstance != null -> method.declaringClass.kotlin.objectInstance //object方法
            else -> method.declaringClass?.newInstance() //其他类方法
        }

        return method.invoke(obj,*annotation.args) as List<Any?>?
    }



    private fun getMethod(para:Parameter,annotation: Func):Method{
        if (annotation.name==""){
            throw IllegalArgumentException("name shoud not be empty")
        }
        val dataCls = if (annotation.provider == Any::class) para.declaringExecutable.declaringClass else annotation.provider.java



        return dataCls.methods.first { it.returnType.kotlin.isSubclassOf(List::class) && it.parameters.size ==annotation.args.size && it.name == annotation.name } ?:throw NoSuchMethodException(annotation.name)

//        return dataCls.declaredFunctions.find { it.returnType.isSubtypeOf(List::class.createType(listOf(KTypeProjection(KVariance.OUT,para.parameterizedType)))) && it.parameters.size ==annotation.args.size && it.name == annotation.name} ?:throw NoSuchMethodException(annotation.name)

    }


}