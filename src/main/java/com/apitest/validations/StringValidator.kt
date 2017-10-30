package com.apitest.validations

import com.apitest.utils.getValue

class StringValidator(property:String
                      ,val expect:String? = null
                      ,val method:StrVltMethod = StrVltMethod.Regex
                      ,val from:FetchMode = FetchMode.ByContext):BaseValidator(property){


    override fun validate():ValidResult {
        val actualValue = actualCtx?.getValue(property)?.toString()
        val expectValue = when(from){
            FetchMode.ByContext -> if(expect==null) null else expectCtx?.getValue(expect)?.toString()
            else -> expect
        }
        var isPass = method.compare(actualValue,expectValue)
        return ValidResult(actualValue,expectValue,isPass,method)
    }


}
