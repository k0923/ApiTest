package com.apitest.dataProvider

interface IParameterProvider<T> {

    fun getData():List<T>?

}