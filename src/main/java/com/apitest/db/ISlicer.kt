package com.apitest.db

import java.lang.reflect.Method
import javax.sql.DataSource

interface ISlicer {

    fun getKey():Any

    fun process(method: Method, args:Map<String,Any?>)

    fun getDataSource():Map<Any, DataSource>

}