package com.apitest.spring.beanConfig

import com.apitest.extensions.ofType
import com.apitest.spring.common.BatchData
import org.springframework.beans.factory.config.*
import org.springframework.beans.factory.support.ManagedList
import org.springframework.core.ParameterizedTypeReference

class ApiBatchDataBeanConfigProcessor : BeanFactoryPostProcessor{


    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
        val names = beanFactory.getBeanNamesForType(BatchData::class.java)
        names.forEach { processBean(beanFactory.getBeanDefinition(it)) }
    }

    private fun processBean(bean:BeanDefinition){
        val limit = bean.propertyValues.getPropertyValue("limit").value.ofType(TypedStringValue::class.java)!!.value.toString().toInt()
        val refBean = bean.propertyValues.getPropertyValue("refBean").value.ofType(TypedStringValue::class.java)!!.value.toString()
        val data = bean.propertyValues.getPropertyValue("data")
        if(data==null){
            val value=ManagedList<Any?>()
            bean.propertyValues.addPropertyValue("data",value)
            createBatchData(refBean,limit, value)
        }else{
           createBatchData(refBean,limit, data.value as ManagedList<Any?>)
        }
    }

    private fun createBatchData(refBean:String,limit:Int,list:ManagedList<Any?>){
        list.clear()
        for(i in 1..limit){
            list.add(RuntimeBeanReference(refBean))
        }
    }
}