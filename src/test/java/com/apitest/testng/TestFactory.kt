package com.apitest.testng


import org.testng.annotations.DataProvider
import java.lang.reflect.Constructor


class TestFactory{

    @DataProvider
    fun getData(context:Constructor<Any>):Array<Array<Int>>{
        var ans = context.annotations
        return Array(2){i->Array(1){i}}
    }

}
