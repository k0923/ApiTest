package com.apitest.dataProvider

import com.apitest.utils.ScriptUtils
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.testng.annotations.DataProvider
import java.lang.reflect.Constructor
import java.util.concurrent.ConcurrentHashMap

class FactoryDataProvider {
    private val logger: Logger = LogManager.getLogger(javaClass)

    companion object {
        val groupMap = ConcurrentHashMap<Any,String>()

        fun addGroup(obj:Any,groupId:String) {
            groupMap.put(obj, groupId)
        }

        fun getGroup(obj:Any):String?{
            return groupMap[obj]
        }
    }

    @DataProvider
    fun getData(constructor: Constructor<Any>):Array<Array<Any?>>{
        val data = ScriptUtils.getTestData(constructor)
        return Array(data.size){ index->Array(1){data[index]}}
    }
}