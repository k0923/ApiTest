package com.apitest.utils

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.util.*


object DataUtils{

    fun <T:Serializable> T.clone():T{
        val byteOutStream = ByteArrayOutputStream()
        val outputStream = ObjectOutputStream(byteOutStream)
        outputStream.writeObject(this)

        val byteInStream = ByteArrayInputStream(byteOutStream.toByteArray())
        val inputStream = ObjectInputStream(byteInStream)
        val newObj = inputStream.readObject() as T
        inputStream.close()
        return newObj
    }

    @Synchronized
    fun getSystemTime():Long{
        Thread.sleep(1)
        return Date().time
    }

    fun getSystemTime(prefix:String?=null,suffix:String?=null):String{
        var time = getSystemTime()
        return "${prefix ?: ""}$time${suffix ?: ""}"
    }

    fun String.repeat(until:(String)->Boolean):String?{
        if(this.isNullOrEmpty()){
            return null
        }
        var temp = this
        while(!until(temp)) {
            temp += temp
        }
        return temp
    }
}



