package com.apitest.utils

import java.util.*
import java.util.regex.Pattern


object DateUtils{



    fun getTimeByBuffer(buffer: String):Long{
        var time = Date().time
        val bufferTime = getBuffer(buffer)
        time += bufferTime
        return time
    }

    fun getDateByBuffer(buffer:String):Date{
        val time = this.getTimeByBuffer(buffer)
        return Date(time)
    }


    private fun getBuffer(buffer:String):Long{
        val year = getNumber(buffer, "y")
        val day = getNumber(buffer, "d")
        val hour = getNumber(buffer, "h")
        val minute = getNumber(buffer, "m")
        val second = getNumber(buffer, "s")
        val operator = getGroup1(buffer, "([+-])")
        var seconds = second +
                60 * minute +
                3600 * hour +
                3600 * 24 * day+
                3600 * 24 * 365 * year
        if (operator == "-")
            seconds *= -1
        return seconds * 1000
    }

    private fun getNumber(input:String,unit:String):Long{
        val regex = "([0-9]+)$unit"
        val result = getGroup1(input, regex)
        return if(result==null)
            0L
        else
            java.lang.Long.parseLong(result)
    }

    private fun getGroup1(input:String,regex:String):String?{
        val pattern = Pattern.compile(regex)
        val y = pattern.matcher(input)
        return when{
            y.find()->y.group(1)
            else -> null
        }
    }
}






