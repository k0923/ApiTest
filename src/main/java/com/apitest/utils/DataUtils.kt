package com.apitest.utils

import com.apitest.utils.DataUtils.clone
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KVisibility
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.isSuperclassOf
import kotlin.reflect.jvm.isAccessible


object DataUtils{


    fun <T> clone(obj:T?):T?{
        return obj?.let {
            val byteOutStream = ByteArrayOutputStream()
            val outputStream = ObjectOutputStream(byteOutStream)
            outputStream.writeObject(obj)
            val byteInStream = ByteArrayInputStream(byteOutStream.toByteArray())
            val inputStream = ObjectInputStream(byteInStream)
            inputStream.use {
                return it.readObject() as T?
            }
        }
    }

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

    fun <T:Any> convertToObj(cls: KClass<T>, data:Map<String,String>):T{
        if(Map::class.isSuperclassOf(cls)){
            return data as T
        }
        val item = cls.createInstance()
        cls.members.forEach {
            when(it){
                is KMutableProperty ->{

                    if(data.containsKey(it.name)){
                        val strValue = data[it.name]!!
                        val value = when(it.returnType.classifier){
                            String::class->strValue
                            Int::class->strValue.toInt()
                            Boolean::class->strValue.toBoolean()
                            Double::class->strValue.toDouble()
                            Long::class->strValue.toLong()
                            Short::class->strValue.toShort()
                            Float::class->strValue.toFloat()
                            else -> null
                        }

                        if(it.visibility == KVisibility.PRIVATE){
                            it.isAccessible = true
                        }
                        it.setter.call(item,value)
                    }
                }
            }
        }
        return item
    }
}



