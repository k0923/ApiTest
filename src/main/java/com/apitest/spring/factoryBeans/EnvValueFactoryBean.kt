package com.apitest.spring.factoryBeans

import com.apitest.config.Environment
import com.apitest.config.GlobalConfig
import org.springframework.beans.factory.FactoryBean

class EnvValueFactoryBean:FactoryBean<Any>{

    var singleton:Boolean=true

    var envs:Map<Environment,Any?>? = null

    override fun isSingleton(): Boolean {
        return singleton
    }

    override fun getObjectType(): Class<*> {
        return Any::class.java
    }

    override fun getObject(): Any? {
        var env = GlobalConfig.currentEnv
        if(envs?.containsKey(env) == true){
            return envs?.get(env)
        }
        return null
    }

}