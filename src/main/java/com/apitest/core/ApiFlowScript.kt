package com.apitest.core

import com.apitest.utils.ScriptUtils
import com.apitest.utils.setValueByPath
import org.apache.logging.log4j.LogManager

class ApiFlowScript {

    @Transient
    private val logger = LogManager.getLogger(javaClass)

    private val apiScripts:MutableList<ApiScript> = ArrayList()

    fun addScript(apiScript: ApiScript){
        apiScripts.add(apiScript)
    }

    fun execute():MutableMap<String,Any?>{
        val flowData = HashMap<String,Any?>()
        apiScripts.forEach{
            var method = it.method
            var data = it.data
            if(data!=null){
                it.properties?.forEach{
                    data.setValueByPath(it.key,it.value)
                }
            }
            ScriptUtils.getRunMethod()(data,flowData,method!!)
        }
        return flowData
    }

    fun execute(data:ApiBaseData){
        var flowData = execute()
        flowData?.let { ScriptUtils.configFlowData(data,it) }
    }




}