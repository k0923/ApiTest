package com.apitest.annotations

@Target(AnnotationTarget.FUNCTION)
@Retention()
annotation class Debug(vararg val ids:String)