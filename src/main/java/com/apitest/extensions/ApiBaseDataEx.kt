package com.apitest.extensions

import com.apitest.core.ApiBaseData
import com.apitest.utils.ScriptUtils

fun <T:ApiBaseData> T.execute(){
    this.preApiScript?.execute()?.let { ScriptUtils.configFlowData(this,it) }
}