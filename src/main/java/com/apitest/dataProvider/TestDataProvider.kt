package com.apitest.dataProvider

import com.apitest.annotations.Filter
import com.apitest.utils.ScriptUtils
import org.testng.annotations.DataProvider
import java.lang.reflect.Constructor
import java.lang.reflect.Executable
import java.lang.reflect.Method
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.*
import kotlin.reflect.jvm.isAccessible

class TestDataProvider {

    @DataProvider
    fun getData(method: Method):Array<Array<Any?>> = getFilterData(method)

    @DataProvider(parallel = true)
    fun getDataParallel(method: Method):Array<Array<Any?>> = getFilterData(method)

    @DataProvider
    fun getConstructorData(method:Constructor<Any>):Array<Array<Any?>> = getFilterData(method)


    private fun getFilterData(method: Executable):Array<Array<Any?>>{
        val filters = method.getAnnotationsByType(Filter::class.java)
        var methodFilters:ArrayList<KFunction<*>>? = null
        if(filters.isNotEmpty()){
            methodFilters = ArrayList()
            filters.forEach {
                methodFilters.addAll(getFilters(it,method))
            }
        }
        val data = ScriptUtils.getTestData(method)
        methodFilters?.let { fs ->
            val fMap = HashMap<KFunction<*>, HashMap<KParameter, Any?>>()
            fs.forEach {
                val paraMap = HashMap<KParameter, Any?>()
                it.parameters.forEach {
                    paraMap.put(it, null)
                }
                fMap.put(it, paraMap)
            }
            val filterData = ArrayList<Array<Any?>>()
            data.forEach { d ->
                if (!fs.any { f ->
                    val map = fMap[f]
                    f.parameters.indices.forEach {
                        map!![f.parameters[it]] = d[it]
                    }
                    f.isAccessible = true
                    f.callBy(map!!) == false
                }) {
                    filterData.add(d)
                }
            }
            return filterData.toTypedArray()
        }
        return data
    }

    private fun getFilters(filter:Filter,method:Executable):List<KFunction<*>>{
        val methodFilter:(KFunction<*>,Executable)->Boolean = {
            k,m->isMatch(k.parameters,m.parameterTypes) && k.returnType.isSubtypeOf(Boolean::class.createType())
        }
        val methods = when(filter.methods.size){
            0->filter.cls.staticFunctions.filter { methodFilter(it,method)}
            else->filter.cls.staticFunctions.filter { methodFilter(it,method) && filter.methods.contains(it.name) }
        }
        val methodNames = methods.map { it.name }
        filter.methods.forEach {
            if(!methodNames.contains(it)){
                throw NoSuchMethodException("Method:[$it] not exist or non-static or return type is not boolean or method parameters not matched")
            }
        }
        return methods
    }

    private fun isMatch(paraA:List<KParameter>,paraB:Array<Class<*>>):Boolean = when {
        paraA.size != paraB.size -> false
        else -> !(0 until paraA.size).any { !paraA[it].type.isSupertypeOf(paraB[it].kotlin.createType()) }
    }

}