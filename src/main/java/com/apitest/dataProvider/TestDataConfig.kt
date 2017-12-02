package com.apitest.dataProvider

import kotlin.reflect.KClass

class TestDataConfig(
        var provider:KClass<out IDataProvider> = Spring::class,
        var paras: Array<String> = emptyArray()
)