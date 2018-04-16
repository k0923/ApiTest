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

    @Test
    fun tmp(){
        val method = CustomProviderTest::class.java.methods.first { it.name == "getData" && it.returnType.kotlin.isSubclassOf(List::class) }
        print(method)
    }
//    @Test
//    fun tmp(){
//        val tp = List::class.createType(listOf(KTypeProjection(KVariance.OUT,Int::class.createType())))
//        var a = ArrayList<Int>()
//        print(a.javaClass.kotlin.issu)
//
//    }



    fun getData(a:String,b:String):List<Int>?{
//        println(a)
//        println(b)
        val list = ArrayList<Int>()
        (1 .. 5).forEach {
            list.add(it)
        }
        return list
    }

}

