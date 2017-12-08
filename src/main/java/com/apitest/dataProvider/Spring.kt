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

    override fun getData(para: Parameter, testDataConfig: TestDataConfig): List<Any?>? {
        val ctxs = getContext(para.declaringExecutable.declaringClass,testDataConfig)
        val method = para.declaringExecutable
        val name = method.name.split(".").last()
        val qualifier = para.getAnnotation(Qualifier::class.java)
        return when(qualifier){
            null -> when(method.parameterCount){
                1 -> {
                    val bean =ctxs.firstOrNull { it.containsBean(name) }?.getBean(name)
                    if(bean!=null) listOf(bean) else emptyList()
                }
                else->getTestData(para,ctxs){true}
            }
            else -> getTestData(para,ctxs){Pattern.matches(qualifier.value,it)}
        }
    }

    private fun getTestData(para:Parameter,ctxs:List<ApplicationContext>,isMatch:(String)->Boolean):List<Any?>{
        val paraType = para.type
        val data = HashMap<String,Any?>()
        //todo 前面的Spring XML文件的BEAN可能被后面的覆盖
        ctxs.forEach { data.putAll(it.getBeansOfType(paraType)) }
        return data.filter { isMatch(it.key) }.map { it.value }
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