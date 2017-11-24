package com.apitest.dataProvider


import com.apitest.utils.FileReaderUtils
import com.apitest.utils.SpringUtils
import java.lang.reflect.Executable

enum class DataSource(val dataMethod:(Executable,TestDataConfig)->List<Any?>) {
    Spring(SpringUtils::getData),
    CSV(FileReaderUtils::read),
    Custom(CustomDataUtils::getData);
    //Spring({m,d-> SpringUtils.getData(m,d) }),
    //CSV({m,d-> FileReaderUtils.read(m,d) }),
    //Custom({m,d-> CustomDataUtils.getData(m,d) });
}