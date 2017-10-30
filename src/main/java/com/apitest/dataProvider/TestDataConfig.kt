package com.apitest.dataProvider

import kotlin.reflect.KClass

data class TestDataConfig(
        var source: DataSource = DataSource.Spring,
        var single:Boolean=true,
        var dataProvider: KClass<out IDataProvider<*>> = IDataProvider::class,
        var parallel:Boolean = false,
        var file:String="",
        var pattern:String="")