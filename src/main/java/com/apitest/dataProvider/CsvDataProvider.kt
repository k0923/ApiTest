package com.apitest.dataProvider

import com.apitest.utils.FileUtils
import com.apitest.utils.PathUtils.getClassFolder
import java.io.File
import java.lang.reflect.Parameter

object CsvDataProvider : AbstractDataProvider<Csv>() {
    override fun getGenericData(parameter: Parameter, annotation: Csv): List<Any?>? {
        val files = getFiles(parameter.declaringExecutable.declaringClass, annotation)
        val list = ArrayList<Any?>()
        files.forEach { list.addAll(FileUtils.read(it,parameter.type.kotlin)) }
        return list
    }

    private fun getFiles(cls:Class<*>,annotation: Csv): List<File> {
        val filePaths = when(annotation.files.isEmpty()){
            true -> listOf("${cls.getClassFolder()}/${cls.simpleName}.csv")
            else -> annotation.files.map { "${cls.getClassFolder()}/$it" }
        }
        return filePaths.map { File(it) }
    }
}