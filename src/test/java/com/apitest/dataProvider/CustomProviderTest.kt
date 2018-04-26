package com.apitest.dataProvider

import com.apitest.testModels.Console
import com.sun.javaws.exceptions.InvalidArgumentException
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

    @Test
    fun t2(console:Console,@Func(name = "getData2") myData:Int?){

    }

    fun getData2():List<Int>?{
        throw InvalidArgumentException(arrayOf("abc"))
        //return (1 .. 5).toList()
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

