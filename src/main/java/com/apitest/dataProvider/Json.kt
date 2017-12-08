package com.apitest.dataProvider

import com.apitest.utils.FileUtils
import com.apitest.utils.PathUtils.getClassFolder
import com.apitest.utils.ScriptUtils
import com.google.gson.Gson
import java.io.File
import java.lang.reflect.Executable
import java.lang.reflect.Parameter
import java.util.function.Function

object Json :IDataProvider {

    val gson = Gson()

    fun getData(method: Executable, testDataConfig: TestDataConfig): Array<Array<Any?>> {
        val files = getFiles(method.declaringClass, testDataConfig)
        val defaultFunction = Function<Int,Array<out Any?>?>{
            arrayOf(gson.fromJson(FileUtils.readAsTxt(files[it]),method.parameterTypes[it]))
        }
        val otherFunction = Function<Int,Array<out Any?>?>{
            ScriptUtils.getEmptyConfigData(method.parameters[it])
        }
        return ScriptUtils.getDataTemplate(method.parameters.toList(),files,defaultFunction,otherFunction)
    }

    override fun getData(para: Parameter, testDataConfig: TestDataConfig): List<Any?>? {
        return getFiles(para.declaringExecutable.declaringClass,testDataConfig).map {
            gson.fromJson(FileUtils.readAsTxt(it),para.type)
        }
    }

    private fun getFiles(cls:Class<*>,testDataConfig:TestDataConfig): List<File> {
        val filePaths = when(testDataConfig.paras.isEmpty()){
            true -> listOf("${cls.getClassFolder()}/${cls.simpleName}.json")
            else -> testDataConfig.paras.map { "${cls.getClassFolder()}/$it" }
        }
        return filePaths.map { File(it) }
    }
}