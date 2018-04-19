package com.apitest.dataProvider

import java.lang.reflect.Parameter

object ValueProvider:AbstractDataProvider<Value>() {
    override fun getGenericData(para: Parameter, annotation: Value, testInstance: Any?): List<Any?>? {

        return when{
            annotation.strings.size > 0 ->annotation.strings.toList()
            annotation.longs.size > 0 -> annotation.longs.toList()
            annotation.ints.size > 0 -> annotation.ints.toList()
            annotation.doubles.size > 0 -> annotation.doubles.toList()
            annotation.shorts.size > 0 -> annotation.shorts.toList()
            annotation.floats.size > 0 -> annotation.floats.toList()
            annotation.classes.size > 0 -> annotation.classes.toList()
            annotation.bytes.size > 0 -> annotation.bytes.toList()
            annotation.chars.size > 0 -> annotation.chars.toList()
            else -> null
        }
    }

    override fun clone(data: List<Any?>): List<Any?> {
        return data
    }
}