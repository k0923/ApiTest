package com.apitest.utils

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.function.Consumer
import kotlin.reflect.KClass


object FileUtils {
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
                .mapTo(objects) { DataUtils.convertToObj(cls, it) }
        return objects
    }
}
