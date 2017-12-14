package com.apitest.dataProvider

import com.apitest.annotations.TestData
import com.apitest.utils.FileUtils
import com.apitest.utils.PathUtils.getClassFolder
import com.apitest.utils.ScriptUtils
import java.io.File
import java.lang.reflect.Executable
import java.lang.reflect.Parameter
import java.util.function.Consumer
import java.util.function.Supplier
import java.util.function.Function

object Csv : AbstractDataProvider() {

    override fun getData(para: Parameter, testData: TestData): List<Any?> {
        val files = getFiles(para.declaringExecutable.declaringClass, testData)
        val list = ArrayList<Any?>()
        files.forEach { list.addAll(FileUtils.read(it,para.type.kotlin)) }
        return list
    }

    private fun getFiles(cls:Class<*>,testData: TestData): List<File> {
        val filePaths = when(testData.paras.isEmpty()){
            true -> listOf("${cls.getClassFolder()}/${cls.simpleName}.csv")
            else -> testData.paras.map { "${cls.getClassFolder()}/$it" }
        }
        return filePaths.map { File(it) }
    }

}