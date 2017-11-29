package com.apitest.dataProvider

import com.apitest.utils.ScriptUtils
import org.testng.annotations.DataProvider
import java.lang.reflect.Method

class TestDataProvider {

    @DataProvider
    fun getData(method: Method):Array<Array<Any?>>{
        val data = ScriptUtils.getTestData(method)
        return if(data.isEmpty()){
            Array(0){_-> arrayOfNulls<Any?>(method.parameterCount)}
        }else{
            if(data[0] is Array<*>){
                val myData = data as List<Array<Any?>>
                Array(data.size){i->Array(method.parameterCount){j->myData[i][j]}}
            }else{
                Array(data.size){ index->Array(1){data[index]}}
            }
        }


    }

    @DataProvider(parallel = true)
    fun getDataParallel(method: Method):Array<Array<Any?>> = getData(method)

}