package com.apitest.utils

import com.apitest.core.ITestData
import com.apitest.dataProvider.TestData
import com.apitest.dataProvider.TestDataConfig
import com.apitest.utils.PathUtils.getClassFolder
import java.io.File
import java.io.BufferedReader
import java.io.FileReader
import java.lang.reflect.Executable
import java.lang.reflect.Method
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


    fun read(method: Executable,testDataConfig:TestDataConfig):List<Any?>{
        val paraClasses = method.parameterTypes
        if(paraClasses.size!=1){
            throw IllegalArgumentException("Only 1 parameter allowed in method:$method")
        }
        var filePath = if(testDataConfig.file.isBlank()){
            "${method.declaringClass.getClassFolder()}/${method.declaringClass.simpleName}.csv"
        }else {"${method.declaringClass.getClassFolder()}/${testDataConfig.file}"}
        var data = read(File(filePath),method.parameterTypes[0].kotlin)
        return with(testDataConfig){
            when(single){
                true->{
                    if(method.parameterTypes[0] is ITestData){
                        listOf(data.first { (it as ITestData).id == method.name })
                    }else{
                        listOf(data.first())
                    }
                }
                else->{
                    if(method.parameterTypes[0] is ITestData){
                        data.filter { (it as ITestData).id == method.name }.toList()
                    }else{
                        data
                    }
                }
            }
        }
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
