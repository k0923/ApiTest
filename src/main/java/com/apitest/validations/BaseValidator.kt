package com.apitest.validations


import org.springframework.expression.spel.support.StandardEvaluationContext

abstract class BaseValidator(val property:String){

    var actualCtx:StandardEvaluationContext? = null
    var expectCtx:StandardEvaluationContext? = null

    abstract fun validate():ValidResult
}