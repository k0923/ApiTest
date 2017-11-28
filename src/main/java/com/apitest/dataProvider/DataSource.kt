package com.apitest.dataProvider


import com.apitest.utils.FileReaderUtils
import com.apitest.utils.SpringUtils
import java.lang.reflect.Executable

enum class DataSource(val dataMethod:(Executable,TestDataConfig)->List<Any?>) {
    Spring(SpringUtils::getData),
    CSV(FileReaderUtils::read),
    Custom(CustomDataUtils::getData);
}