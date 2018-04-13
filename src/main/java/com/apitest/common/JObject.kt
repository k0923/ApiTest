package com.apitest.common

import com.apitest.utils.getValue
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.internal.LinkedTreeMap
import org.apache.commons.lang.StringUtils
import org.apache.logging.log4j.LogManager
import org.springframework.expression.spel.support.StandardEvaluationContext

class JObject(val jsonStr:String){

    private val logger = LogManager.getLogger(javaClass)
    private val ctx:StandardEvaluationContext

    init{
        val gson = Gson()
        val e = gson.fromJson(jsonStr,JsonElement::class.java)
        ctx = when (e) {
            is JsonArray ->StandardEvaluationContext(gson.fromJson(e,List::class.java))
            else -> StandardEvaluationContext(gson.fromJson(e,LinkedTreeMap::class.java))
        }
    }


    fun getByPath(path:String):Any?{
        val p = convertToSpelPath(path)
        return ctx.getValue(p)
    }


    fun convertToSpelPath(path:String):String{
        if (!path.contains(".") && path.contains("(")) {
            return path
        }
        val pathList = path.split("\\.".toRegex())
        var newPath = ""
        pathList.forEach {
            var p = it
            p = when {
                p.contains("[") -> {
                    val p1 = p.substring(p.indexOf("["), p.length)
                    p = p.substring(0, p.indexOf("["))
                    if (StringUtils.isEmpty(p)) {
                        p1
                    } else {
                        "['$p']$p1"
                    }

                }
                p.contains("(") -> "." + p
                else -> "['$p']"
            }
            newPath += p
        }
        return newPath
    }


}