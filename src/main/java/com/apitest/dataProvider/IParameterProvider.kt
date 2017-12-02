package com.apitest.dataProvider

interface IParameterProvider<T:IParameterProvider<T>> {

    fun getData():List<T>?

}