package com.apitest


import org.testng.ITestContext
import org.testng.TestRunner
import org.testng.annotations.DataProvider
import org.testng.annotations.Factory
import org.testng.annotations.Test
import java.lang.reflect.Method

class Practice{
    @Test(dataProviderClass = DP::class,dataProvider = "getData")
    fun t1(i:Int){
        //println(i)
    }

    @Test(dataProviderClass = DP::class,dataProvider = "getData")
    fun t2(i:Int){
        //println(i)
    }
}

class Practice1{
    @Test(dataProviderClass = DP::class,dataProvider = "getData")
    fun t1(i:Int){
        println(this.hashCode())
    }
}

class Practice2
@Factory(dataProviderClass = DP::class,dataProvider = "getData")
constructor(private val i: Int) {

    @Test(dataProviderClass = DP::class,dataProvider = "getData")
    fun test(y:Int) {
        println(this.hashCode())
    }

}


class DP{

    @DataProvider
    fun getData(context:ITestContext,m: Method?):Array<Array<out Any?>>{

        if(context is TestRunner){
            if(context.testClasses.isNotEmpty()){

                println("index:${context.test.index}")
                context.testClasses?.iterator()?.next()?.getInstances(false)?.forEach {
                    println("D: ${it.hashCode()}")
                }
            }

        }
        //context.currentXmlTest.
        //println(context.currentXmlTest.index)
        return Array(2,{i->arrayOf(i)})
    }
}


