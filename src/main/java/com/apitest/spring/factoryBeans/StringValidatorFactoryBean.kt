package com.apitest.spring.factoryBeans

import com.apitest.validations.FetchMode
import com.apitest.validations.StrVltMethod
import com.apitest.validations.StringValidator
import org.springframework.beans.factory.FactoryBean

class StringValidatorFactoryBean: FactoryBean<StringValidator>{

    var property:String? = null

    var expect:String? = null

    var method:StrVltMethod = StrVltMethod.Regex

    var fetchMode:FetchMode=FetchMode.ByValue


    override fun getObjectType(): Class<*> {
        return StringValidator::class.java
    }

    override fun isSingleton(): Boolean {
        return false
    }

    override fun getObject(): StringValidator {
        val actualPath = property ?:throw NullPointerException("property could not be null")
        return StringValidator(actualPath,expect,method,fetchMode)
    }
}