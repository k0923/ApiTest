package com.apitest.dataProvider

import com.apitest.utils.PathUtils.getClassFolder
import com.apitest.utils.SpringUtils
import org.springframework.context.ApplicationContext
import java.lang.reflect.Parameter
import java.util.regex.Pattern

object SpringDataProvider : AbstractDataProvider<Spring>() {
    override fun getGenericData(parameter: Parameter, annotation: Spring,testInstance:Any?): List<Any?>?  {
        val ctxs = getContext(parameter.declaringExecutable.declaringClass,annotation)
        val method = parameter.declaringExecutable
        val name = method.name.split(".").last()
        return when(annotation.pattern){
            "" -> {
                val bean = ctxs.firstOrNull { it.containsBean(name) }?.getBean(name)
                if(bean!=null) listOf(bean) else emptyList()
            }
            else->{
                getTestData(parameter,ctxs){Pattern.matches(annotation.pattern,it)}
            }
        }
    }




    private fun getTestData(para:Parameter, ctxs:List<ApplicationContext>, isMatch:(String)->Boolean):List<Any?>{
        val paraType = para.type
        val data = HashMap<String,Any?>()
        //todo 前面的Spring XML文件的BEAN可能被后面的覆盖
        ctxs.forEach { data.putAll(it.getBeansOfType(paraType)) }
        return data.filter { isMatch(it.key) }.map { it.value }
    }

    private fun getContext(cls:Class<*>,annotation: Spring):List<ApplicationContext>{
        return when{
            annotation.files.isEmpty() -> listOf(SpringUtils.getContext(cls) ?: throw NullPointerException("No context found of cls $cls"))
            else->annotation.files.map {
                SpringUtils.getContext("${cls.getClassFolder()}/$it") ?: throw NullPointerException("No context found of file $it")
            }.toList()
        }
    }
}