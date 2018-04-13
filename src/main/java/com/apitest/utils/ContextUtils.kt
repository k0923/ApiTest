package com.apitest.utils

import org.apache.logging.log4j.LogManager
import org.springframework.expression.EvaluationContext
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext


fun Any.getValueByPath(property: String): Any? {
    if (this is EvaluationContext) {
        return this.getValue(property)
    }
    return StandardEvaluationContext(this).getValue(property)
}

fun EvaluationContext.getValue(property: String): Any? {
    val logger = LogManager.getLogger()
    val parser = SpelExpressionParser()
    logger.debug("开始从上下文: {} 获取属性：{}", this.rootObject, property)
    val value = parser.parseExpression(property).getValue(this)
    logger.debug("当前属性：{} 的值为: {}", property, value)
    return value
}

fun Any.setValueByPath(property: String, value: Any?) {
    if (this is EvaluationContext) {
        this.setValue(property, value)
    }
    StandardEvaluationContext(this).setValue(property, value)
}

fun EvaluationContext.setValue(property: String, value: Any?) {
    val logger = LogManager.getLogger()
    val parser = SpelExpressionParser()
    try {
        parser.parseExpression(property).setValue(this, value)
    } catch (e: Exception) {
        logger.error("赋值失败:{}", e)
        throw e
    }
}






