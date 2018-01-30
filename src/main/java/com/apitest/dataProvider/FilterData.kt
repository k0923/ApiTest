package com.apitest.dataProvider

import kotlin.reflect.KFunction

class FilterData(var invokeMode:InvokeMode,var method:KFunction<*>) {

    var args:Array<String>? = null

}