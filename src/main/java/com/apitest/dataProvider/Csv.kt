package com.apitest.dataProvider

import com.apitest.utils.FileUtils
import com.apitest.utils.PathUtils.getClassFolder
import java.io.File
import java.lang.reflect.Executable
import java.lang.reflect.Parameter

class Csv : IDataProvider {
    override fun getData(method: Executable, testDataConfig: TestDataConfig): Array<Array<Any?>> {
        val paraClasses = method.parameterTypes
        if(paraClasses.size!=1){
            throw IllegalArgumentException("Only 1 parameter allowed in method:$method")
        }
        val file = getFile(method.declaringClass, testDataConfig)
        val data = FileUtils.read(file, method.parameterTypes[0].kotlin)
        return Array(data.size,{i-> arrayOf(data[i])})
    }

    override fun getData(para: Parameter, testDataConfig: TestDataConfig): List<Any?> {
        val file = getFile(para.declaringExecutable.declaringClass, testDataConfig)
        return FileUtils.read(file, para.type.kotlin)
    }

    private fun getFile(cls:Class<*>,testDataConfig:TestDataConfig): File {
        val filePath = if(testDataConfig.paras.isEmpty()){
            "${cls.getClassFolder()}/${cls.simpleName}.csv"
        }else {"${cls.getClassFolder()}/${testDataConfig.paras[0]}"}
        return File(filePath)
    }

}