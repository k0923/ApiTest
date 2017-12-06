package com.apitest.dataProvider

import com.apitest.utils.PathUtils.getClassFolder
import com.apitest.utils.ScriptUtils
import com.apitest.utils.SpringUtils
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationContext
import java.lang.reflect.Executable
import java.lang.reflect.Parameter
import java.util.function.Function
import java.util.regex.Pattern

object Spring:IDataProvider{
    override fun getData(method: Executable, testDataConfig: TestDataConfig): Array<Array<Any?>> {
        val params = method.parameters
        return when(testDataConfig.paras.isEmpty() && !SpringUtils.isContextExisted(method.declaringClass)){
            true-> {
                val defaultAction = Function<Int,Array<out Any?>?>{
                    ScriptUtils.getEmptyConfigData(params[it])
                }
                ScriptUtils.getDataTemplate(params.toList(),params.toList(),defaultAction,defaultAction)
            }
            else->{
                val ctx = getContext(method.declaringClass,testDataConfig)
                getMutipleParameterData(method,ctx)
            }
        }
    }

    override fun getData(para: Parameter, testDataConfig: TestDataConfig): List<Any?>? {
        val ctx = getContext(para.declaringExecutable.declaringClass,testDataConfig)
        return getOnlyOneParameterData(para.declaringExecutable,para,ctx)
    }

    private fun getTestData(para:Parameter,ctxs:List<ApplicationContext>,isMatch:(String)->Boolean):List<Any?>{
        val paraType = para.type
        val data = HashMap<String,Any?>()
        //todo 前面的Spring XML文件的BEAN可能被后面的覆盖
        ctxs.forEach { data.putAll(it.getBeansOfType(paraType)) }
        return data.filter { isMatch(it.key) }.map { it.value }
    }




    private fun getMutipleParameterData(method:Executable,ctxs:List<ApplicationContext>):Array<Array<Any?>>{
        val ps = method.parameters.toList()
        val defaultFunction = Function<Int,Array<out Any?>?> {
            getOnlyOneParameterData(method,ps[it],ctxs)?.toTypedArray()
        }
        return ScriptUtils.getDataTemplate(ps,ps,defaultFunction,defaultFunction)
    }




    private fun getOnlyOneParameterData(method:Executable,para:Parameter,ctxs:List<ApplicationContext>):List<Any?>?{
        val name = method.name.split(".").last()
        val qualifier = para.getAnnotation(Qualifier::class.java)
        val data = when(qualifier){
            null -> when(method.parameterCount){
                1 -> {
                    val bean =ctxs.firstOrNull { it.containsBean(name) }?.getBean(name)
                    if(bean!=null) listOf(bean) else emptyList()
                }
                else->getTestData(para,ctxs){true}
            }
            else -> getTestData(para,ctxs){Pattern.matches(qualifier.value,it)}
        }
        return if(data.isNotEmpty()){
            data
        }else{
            ScriptUtils.getEmptyConfigData(para)?.toList()
        }
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