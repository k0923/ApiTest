package com.apitest.dataProvider

import com.apitest.annotations.Filter
import com.apitest.utils.ScriptUtils
import org.testng.ITestContext
import org.testng.ITestNGMethod
import org.testng.annotations.DataProvider
import java.lang.reflect.Constructor
import java.lang.reflect.Executable
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.*
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaConstructor
import kotlin.reflect.jvm.javaMethod

class TestDataProvider {

    @DataProvider
    fun getData(method: ITestNGMethod,context:ITestContext):Array<Array<Any?>> = getFilterData(method,context)

    @DataProvider(parallel = true)
    fun getDataParallel(method: ITestNGMethod,context:ITestContext):Array<Array<Any?>> = getFilterData(method,context)

    private fun getFilterData(testNgMethod: ITestNGMethod,context:ITestContext):Array<Array<Any?>>{
        val method = testNgMethod.constructorOrMethod.constructor ?: testNgMethod.constructorOrMethod.method

        val filters = method.getAnnotationsByType(Filter::class.java)
        var methodFilters:ArrayList<FilterData>? = null
        if(filters.isNotEmpty()){
            methodFilters = ArrayList()
            filters.forEach {
                methodFilters.add(getFilter(it,method))
            }
        }
        val data = ScriptUtils.getTestData(method)
        methodFilters?.let { fs ->
            val filterData = ArrayList<Array<Any?>>()
            data.forEach { d ->
                if(!fs.any{
                    it.method.isAccessible = true
                    var tempData = d
                    if(it.args != null){
                        val newList = d.toMutableList()
                        newList.add(it.args)
                        tempData = newList.toTypedArray()
                    }
                    val result = when(it.invokeMode){
                        InvokeMode.BySelf -> { it.method.call(testNgMethod.instance,*tempData)}
                        InvokeMode.NewCreate -> {it.method.call(it.method.javaMethod!!.declaringClass.newInstance(),*tempData)}
                        else -> {it.method.call(*tempData)}
                    }
                    result == false
                }){
                    filterData.add(d)
                }
            }
            return filterData.toTypedArray()
        }
        return data
    }

    private fun getFilter(filter: Filter,method:Executable):FilterData{
       if(filter.method.isEmpty()){
           throw NullPointerException("filter should specify the method name")
       }
        var filterCls = filter.cls
        if(filterCls == Object::class){
            filterCls = method.declaringClass.kotlin
        }

        val m = filterCls.functions.find {
            it.name == filter.method
        }
        if (m == null){
            throw NoSuchMethodException("method:${filter.method} not found in cls:${filterCls}")
        }
        when{
            //返回类型不匹配
            !m.returnType.isSubtypeOf(Boolean::class.createType()) -> throw IllegalArgumentException("the return type of method:${m.name} should be boolean")
            //参数个数不匹配
            m.valueParameters.size != method.parameterCount + if(filter.args.size > 0)  1 else 0 -> throw IllegalArgumentException("parameter size not equal")
            //参数类型不匹配
            method.parameterTypes.indices.any{!method.parameterTypes[it].kotlin.createType().isSubtypeOf(m.valueParameters[it].type)} -> throw IllegalArgumentException("parameter type not match")
//            m.valueParameters.indices.any { !m.valueParameters[it].type.isSupertypeOf(method.parameterTypes[it].kotlin.createType()) } -> throw IllegalArgumentException("parameter type not match")
        }
        val invokeMode = canCreate(filter,m,method)
        if(invokeMode == InvokeMode.NotSupport){
            throw RuntimeException("The method of filter should match one of the following situations:" +
                    "1. Class of Filter should equal to the declaring class of the method" +
                    "2. The method of filter should be static " +
                    "3. The class of filter should contain the 0 arg constructor")
        }
        val filterData = FilterData(invokeMode,m)
        if(filter.args.isNotEmpty()){
            filterData.args = filter.args
        }

        return filterData
    }



    private fun canCreate(filter:Filter,methodFromFilter:KFunction<*>, method:Executable):InvokeMode{
        return when{
            (filter.cls == method.declaringClass || filter.cls == Object::class.java) && methodFromFilter.javaConstructor == null-> InvokeMode.BySelf //同一个类并且不是filter的方法不是构造函数
            Modifier.isStatic(methodFromFilter.javaMethod!!.modifiers)->InvokeMode.Static //filter中的方法是静态的
            filter.cls.constructors.any { c->c.parameters.isEmpty() } -> InvokeMode.NewCreate //filter中的类有无参构造函数
            else -> InvokeMode.NotSupport
        }
    }

}