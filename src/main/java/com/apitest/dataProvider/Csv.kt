package com.apitest.dataProvider

import com.apitest.utils.FileUtils
import com.apitest.utils.PathUtils.getClassFolder
import com.apitest.utils.ScriptUtils
import java.io.File
import java.lang.reflect.Executable
import java.lang.reflect.Parameter
import java.util.function.Consumer
import java.util.function.Supplier
import java.util.function.Function

object Csv : IDataProvider {

    override fun getData(para: Parameter, testDataConfig: TestDataConfig): List<Any?> {
        val files = getFiles(para.declaringExecutable.declaringClass, testDataConfig)
        val list = ArrayList<Any?>()
        files.forEach { list.addAll(FileUtils.read(it,para.type.kotlin)) }
        return list
    }

    private fun getFiles(cls:Class<*>,testDataConfig:TestDataConfig): List<File> {
        val filePaths = when(testDataConfig.paras.isEmpty()){
            true -> listOf("${cls.getClassFolder()}/${cls.simpleName}.csv")
            else -> testDataConfig.paras.map { "${cls.getClassFolder()}/$it" }
        }
        return filePaths.map { File(it) }
    }

}