package com.apitest.validations

import org.springframework.expression.spel.support.StandardEvaluationContext

interface IValidator {

    fun validate(actual:StandardEvaluationContext,expected:StandardEvaluationContext)

}