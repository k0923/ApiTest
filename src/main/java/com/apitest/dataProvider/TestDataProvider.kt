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

//    @DataProvider
//    fun getConstructorData(method:Constructor<Any>):Array<Array<Any?>> = getFilterData(method,context)





    private fun getFilterData(testNgMethod: ITestNGMethod,context:ITestContext):Array<Array<Any?>>{
        val method = testNgMethod.constructorOrMethod.constructor ?: testNgMethod.constructorOrMethod.method





        val filters = method.getAnnotationsByType(Filter::class.java)
        var methodFilters:ArrayList<Pair<InvokeMode,KFunction<*>>>? = null
        if(filters.isNotEmpty()){
            methodFilters = ArrayList()
            filters.forEach {
                methodFilters.addAll(getFilter(it,method))
            }
        }
        val data = ScriptUtils.getTestData(method)
        methodFilters?.let { fs ->
            val filterData = ArrayList<Array<Any?>>()
            data.forEach { d ->
                if(!fs.any{
                    it.second.isAccessible = true
                    val result = when(it.first){
                        InvokeMode.BySelf -> { it.second.call(testNgMethod.instance,*d)}
                        InvokeMode.NewCreate -> {it.second.call(it.second.javaMethod!!.declaringClass.newInstance(),*d)}
                        else -> {it.second.call(*d)}
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

    private fun getFilter(filter: Filter, method:Executable):List<Pair<InvokeMode,KFunction<*>>>{
        // filter的方法为空
        if(filter.methods.isEmpty()){
            throw NullPointerException("filter should specify the method name")
        }

        val methods = filter.cls.functions.filter { filter.methods.contains(it.name) }

        //filter中的方法不存在
        filter.methods.forEach { m->
            if(methods.firstOrNull { it.name == m }==null){
                throw NoSuchMethodException("method:$m not found in cls:${filter.cls}")
            }
        }

        val validMethods = ArrayList<Pair<InvokeMode,KFunction<*>>>()

        methods.forEach { f->
            when{
                //返回类型不匹配
                !f.returnType.isSubtypeOf(Boolean::class.createType()) -> throw IllegalArgumentException("the return type of method:${f.name} should be boolean")
                //参数个数不匹配
                f.valueParameters.size != method.parameterCount -> throw IllegalArgumentException("parameter size not equal")
                //参数类型不匹配
                f.valueParameters.indices.any { !f.valueParameters[it].type.isSupertypeOf(method.parameterTypes[it].kotlin.createType()) } -> throw IllegalArgumentException("parameter type not match")
            }

            val canCreate = canCreate(filter,f,method)
            if(canCreate == InvokeMode.NotSupport){
                throw RuntimeException("The method of filter should match one of the following situations:" +
                        "1. Class of Filter should equal to the declaring class of the method" +
                        "2. The method of filter should be static " +
                        "3. The class of filter should contain the 0 arg constructor")
            }

            validMethods.add(Pair(canCreate,f))

        }

        return validMethods
    }

    private fun canCreate(filter:Filter,methodFromFilter:KFunction<*>, method:Executable):InvokeMode{
        return when{
            filter.cls == method.declaringClass && methodFromFilter.javaConstructor == null-> InvokeMode.BySelf //同一个类并且不是filter的方法不是构造函数
            Modifier.isStatic(methodFromFilter.javaMethod!!.modifiers)->InvokeMode.Static //filter中的方法是静态的
            filter.cls.constructors.any { c->c.parameters.isEmpty() } -> InvokeMode.NewCreate //filter中的类有无参构造函数
            else -> InvokeMode.NotSupport
        }
    }

}