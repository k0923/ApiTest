package com.apitest.core

import com.apitest.validations.BaseValidator
import org.apache.logging.log4j.LogManager
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.testng.Assert


abstract class ApiBaseData:ITestData{

    override var id:String = ""

    @Transient
    val logger = LogManager.getLogger(javaClass)!!

    var flowData:Map<String,String>? = null

    var preApiScript: ApiScript? = null

    var validators:MutableList<BaseValidator>? = null

    fun validate(){
        val ctx = StandardEvaluationContext(this)
        var isAllPassed = true
        validators?.forEach {
            it.actualCtx = ctx
            it.expectCtx = ctx
            var result = it.validate()
            val msg = "字符串校验${if (result.isPass) "" else "不"}通过,检验属性:${it.property},预期值:${result.expect},实际值:${result.actual},检验方法:${result.validMethod}"
            when(result.isPass){
                true->logger.info(msg)
                else->{
                    logger.error(msg)
                    isAllPassed = false
                }
            }
        }


        Assert.assertTrue(isAllPassed,"检查未通过，请查看log")

    }
}