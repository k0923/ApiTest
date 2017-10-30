package com.apitest

import com.apitest.utils.setValueByPath
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeGroups
import org.testng.annotations.Test
import kotlin.reflect.KClass

class NgTest{

    val property:String? = null

    @BeforeClass
    fun beforeClass(){
        println("beforeClass")
    }

    @BeforeGroups(value= "abc")
    fun beforeGroup(){
        println("beforeGroup")
    }

    @Test(groups = arrayOf("abc"))
    fun groupabc(){
        println("groupabc")
    }

    @Test
    fun nogroup(){
        println("nogroup")
    }

    fun test(){


    }

}