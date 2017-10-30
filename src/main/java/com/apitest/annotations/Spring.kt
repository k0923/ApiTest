package com.apitest.annotations

@Target(allowedTargets = AnnotationTarget.FUNCTION)
@Retention
annotation class Spring(
        val isSingle:Boolean=true,
        val fileLocation:String="",
        val pattern:String="")
