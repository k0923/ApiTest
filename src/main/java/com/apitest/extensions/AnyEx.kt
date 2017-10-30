package com.apitest.extensions



fun <T> Any.ofType(clazz:Class<T>):T?{
    return if(clazz.isAssignableFrom(this.javaClass)){
        this as T
    }else{
        null
    }
}

fun <T> T?.or(backup:T?):T?{
    return this ?: backup
}

