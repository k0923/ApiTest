package com.apitest.dataProvider

import com.apitest.annotations.TestData
import com.apitest.utils.FileUtils
import com.apitest.utils.PathUtils.getClassFolder
import com.apitest.utils.ScriptUtils
import com.google.gson.Gson
import java.io.File
import java.lang.reflect.Executable
import java.lang.reflect.Parameter
import java.util.function.Function

object Json :AbstractDataProvider() {

    val gson = Gson()

    fun getData(method: Executable, testData: TestData): Array<Array<Any?>> {
        val files = getFiles(method.declaringClass, testData)
        val defaultFunction = Function<Int,Array<out Any?>?>{
            arrayOf(gson.fromJson(FileUtils.readAsTxt(files[it]),method.parameterTypes[it]))
        }
        val otherFunction = Function<Int,Array<out Any?>?>{
            ScriptUtils.getEmptyConfigData(method.parameters[it])
        }
        return ScriptUtils.getDataTemplate(method.parameters.toList(),files,defaultFunction,otherFunction)
    }

    override fun getData(para: Parameter, testData: TestData): List<Any?>? {
        return getFiles(para.declaringExecutable.declaringClass,testData).map {
            gson.fromJson(FileUtils.readAsTxt(it),para.type)
        }
    }

    private fun getFiles(cls:Class<*>,testData: TestData): List<File> {
        val filePaths = when(testData.paras.isEmpty()){
            true -> listOf("${cls.getClassFolder()}/${cls.simpleName}.json")
            else -> testData.paras.map { "${cls.getClassFolder()}/$it" }
        }
        return filePaths.map { File(it) }
    }
}