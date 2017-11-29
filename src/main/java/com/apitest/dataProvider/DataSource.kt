package com.apitest.dataProvider


import com.apitest.utils.FileReaderUtils
import com.apitest.utils.SpringUtils
import java.lang.reflect.Executable
import java.lang.reflect.Parameter

enum class DataSource(val dataFromMethod:(Executable,TestDataConfig)->List<Any?>,val dataFromPara:(Parameter,TestDataConfig)->List<Any?>) {
    Spring(SpringUtils::getData,SpringUtils::getData),
    CSV(FileReaderUtils::read,{_,_->kotlin.collections.listOfNotNull()}),
    Custom(CustomDataUtils::getData,{_,_->kotlin.collections.listOfNotNull()});
}