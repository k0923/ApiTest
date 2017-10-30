package com.apitest.spring.factoryBeans

import com.apitest.utils.DataUtils.repeat

class RepeatFactoryBean: AbstractFactoryBean<String>() {

    var template:String?=null

    var length:Int=0


    override fun getObject(): String? {
        if(template.isNullOrEmpty()){
            return null
        }

        template = template?.repeat{it.length>length}
        return template?.substring(0,length)
    }

    override fun getObjectType(): Class<*> {
        return String::class.java
    }



}