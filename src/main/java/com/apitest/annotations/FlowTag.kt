package com.apitest.annotations



@Target(allowedTargets = AnnotationTarget.FIELD)
@Retention
annotation class FlowTag(val name: String = "")