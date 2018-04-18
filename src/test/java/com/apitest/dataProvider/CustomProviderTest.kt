package com.apitest.dataProvider

import com.apitest.testModels.Console
import org.testng.annotations.Test
import java.util.*
import kotlin.reflect.KTypeProjection
import kotlin.reflect.KVariance
import kotlin.reflect.full.createType
import kotlin.reflect.full.functions
import kotlin.reflect.full.isSubclassOf


class CustomProviderTest{



    @Test
    fun test(console: Console,@Func(name = "getData",args = ["1","2"]) myData: Int?){

    }





    fun getData(a:String,b:String):List<Int>?{
        println(a)
        println(b)
        val list = ArrayList<Int>()
        (1 .. 5).forEach {
            list.add(it)
        }
        return list
    }

}

