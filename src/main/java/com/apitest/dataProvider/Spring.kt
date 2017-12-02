package com.apitest.dataProvider

import com.apitest.extensions.ofType
import com.apitest.utils.CommonUtils
import com.apitest.utils.PathUtils.getClassFolder
import com.apitest.utils.ScriptUtils
import com.apitest.utils.SpringUtils
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationContext
import java.lang.reflect.Executable
import java.lang.reflect.Parameter
import java.util.function.Supplier
import java.util.regex.Pattern
import kotlin.reflect.full.isSuperclassOf

object Spring:IDataProvider{
    override fun getData(method: Executable, testDataConfig: TestDataConfig): Array<Array<Any?>> {
        val params = method.parameters
        return when(testDataConfig.paras.isEmpty() && !SpringUtils.isContextExisted(method.declaringClass)){
            true->getMutipleParameterData(params,{
                ScriptUtils.getEmptyConfigData(it)
            })
            else->{
                val ctx = getContext(method.declaringClass,testDataConfig)
                when(params.size){
                    1-> getOnlyOneParameterData(method,params[0],ctx)
                    else->getMutipleParameterData(params,ctx)
                }
            }
        }
    }

    override fun getData(para: Parameter, testDataConfig: TestDataConfig): List<Any?> {
        val ctx = getContext(para.declaringExecutable.declaringClass,testDataConfig)
        return getTestData(para,ctx)
    }


    private fun getTestData(para:Parameter,ctxs:List<ApplicationContext>,methodName:String?=null):List<Any?>{
        val paraType = para.type
        val qualifier = para.getAnnotation(Qualifier::class.java)
        val data = HashMap<String,Any?>()
        ctxs.forEach { data.putAll(it.getBeansOfType(paraType)) } //todo 前面的Spring XML文件的BEAN可能被后面的覆盖
        return when{
            data.isEmpty() && paraType.isEnum -> paraType.enumConstants.toList()
            qualifier == null && methodName != null -> data.filter { it.key == methodName }.map { it.value }
            else->{
                when(qualifier){
                    null -> data.map { it.value }
                    else -> data.filter { Pattern.matches(qualifier.value,it.key) }.map { it.value }
                }
            }
        }
    }

    private fun getMutipleParameterData(paras:Array<Parameter>,function:(Parameter)->Array<out Any?>?):Array<Array<Any?>>{
        val dataList = Array<Supplier<Array<out Any?>?>>(paras.size,{ _-> Supplier { null }})
        (0 until paras.size).forEach { dataList[it] = Supplier { function(paras[it]) } }
        return CommonUtils.getCartesianProductBySupplier(dataList)
    }

    private fun getMutipleParameterData(paras:Array<Parameter>,ctxs:List<ApplicationContext>):Array<Array<Any?>>{
        return getMutipleParameterData(paras,{ getTestData(it,ctxs).toTypedArray() })
    }



    private fun getOnlyOneParameterData(method:Executable,para:Parameter,ctxs:List<ApplicationContext>):Array<Array<Any?>>{
        val name = method.name.split(".").last()
        val data = getTestData(para,ctxs,name)
        return Array(data.size,{ arrayOf(data[it])})
    }

    private fun getContext(cls:Class<*>,testDataConfig:TestDataConfig):List<ApplicationContext>{
        return when{
            testDataConfig.paras.isEmpty() -> listOf(SpringUtils.getContext(cls) ?: throw NullPointerException("No context found of cls $cls"))
            else->testDataConfig.paras.map {
                SpringUtils.getContext("${cls.getClassFolder()}/$it") ?: throw NullPointerException("No context found of file $it")
            }.toList()
        }
    }
}