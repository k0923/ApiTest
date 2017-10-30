package com.apitest.spring.factoryBeans


import com.apitest.utils.DataUtils

class SystemTimeFactoryBean: AbstractFactoryBean<Any>() {

    var prefix:String? = null
    var suffix:String? = null


    override fun getObjectType(): Class<*> {
        return if(!prefix.isNullOrEmpty() || !suffix.isNullOrEmpty()){
            String.javaClass
        }else{
            Long.javaClass
        }
    }

    override fun getObject(): Any {
        return when(objectType){
            Long.javaClass->DataUtils.getSystemTime()
            else->DataUtils.getSystemTime(prefix,suffix)
        }
    }


}