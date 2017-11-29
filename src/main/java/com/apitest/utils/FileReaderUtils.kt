package com.apitest.utils

import com.apitest.dataProvider.TestDataConfig
import com.apitest.utils.PathUtils.getClassFolder
import java.io.File
import java.io.BufferedReader
import java.io.FileReader
import java.lang.reflect.Executable
import java.lang.reflect.Parameter
import java.util.function.Consumer
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KVisibility
import kotlin.reflect.full.createInstance
import kotlin.reflect.jvm.isAccessible


object FileReaderUtils {
    fun readAsTxt(file:File):String{
        val sb = StringBuilder()
        read(file, Consumer { sb.append(it) })
        return sb.toString()
    }

    fun read(file: File):List<String>{
        val list = ArrayList<String>()
        read(file, Consumer { list.add(it) })
        return list
    }

    fun read(file: File,consumer: Consumer<String>) {
        val br = BufferedReader(FileReader(file))
        br.lines().forEach(consumer)
        br.close()
    }

    fun read(file:File,split: Char):List<List<String>>{
        val stringList = ArrayList<List<String>>()
        read(file, Consumer { stringList.add(it.split(split)) })
        return stringList
    }

    fun <T:Any> read(file:File,cls: KClass<T>,split:Char=','):List<T>{
        val dataList = read(file,split)
        val dataMap = dataList.first().mapIndexed { index, s -> index to s }.toMap()
        val objects = ArrayList<T>()
        dataList.subList(1,dataList.size)
                .map { it.mapIndexed { index, s -> dataMap[index]!! to s }.toMap() }
                .mapTo(objects) { readAsObj(cls, it) }
        return objects
    }


    private fun getFile(cls:Class<*>,testDataConfig:TestDataConfig):File{
        val filePath = if(testDataConfig.file.isBlank()){
            "${cls.getClassFolder()}/${cls.simpleName}.csv"
        }else {"${cls.getClassFolder()}/${testDataConfig.file}"}
        return File(filePath)
    }

    fun read(para:Parameter,testDataConfig:TestDataConfig):List<Any?>{
        val file = getFile(para.declaringExecutable.declaringClass,testDataConfig)
        return read(file,para.type.kotlin)
    }

    fun read(method: Executable,testDataConfig:TestDataConfig):Array<Array<Any?>>{
        val paraClasses = method.parameterTypes
        if(paraClasses.size!=1){
            throw IllegalArgumentException("Only 1 parameter allowed in method:$method")
        }
        val file = getFile(method.declaringClass,testDataConfig)
        val data = read(file,method.parameterTypes[0].kotlin)
        return Array(data.size,{i-> arrayOf(data[i])})
    }


    private fun <T:Any> readAsObj(cls:KClass<T>,data:Map<String,String>):T{
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
