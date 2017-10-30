package com.apitest.dataProvider

import java.lang.reflect.Executable

interface IDataProvider<out T> {

    fun getData(method:Executable,testDataConfig:TestDataConfig):List<T>

}