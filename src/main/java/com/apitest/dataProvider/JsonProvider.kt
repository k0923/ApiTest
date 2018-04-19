package com.apitest.dataProvider

import com.apitest.utils.FileUtils
import java.io.File
import java.lang.reflect.Parameter
import com.apitest.utils.PathUtils.getClassFolder
import com.google.gson.Gson
import java.security.InvalidParameterException
import kotlin.reflect.full.createInstance

object JsonProvider:AbstractDataProvider<Json>() {

    override fun getGenericData(para: Parameter, annotation: Json, testInstance: Any?): List<Any?>? {
        if(annotation.file.isEmpty()){
            throw InvalidParameterException("file name could not be empty")
        }
        val gson = if (annotation.builder == IGsonBuilder::class) Gson() else annotation.builder.createInstance().gson
        val files = getMatchedFiles(para.declaringExecutable.declaringClass,annotation.file)
        return files.map { FileUtils.readAsTxt(it) }.map { gson.fromJson(it,para.type) }
    }

    private fun getMatchedFiles(cls:Class<*>,pattern:String):List<File>{
        val regex = Regex(pattern)
        val folder = cls.getClassFolder()
        val directory = File(folder)
        return directory.listFiles().filter { it.extension.toLowerCase() == "json" && regex.matches(it.name) }
    }
}