//package com.apitest.dataProvider
//
//import kotlin.reflect.KClass
//
//
//@Retention(AnnotationRetention.RUNTIME)
//@Target(AnnotationTarget.FUNCTION)
//@Repeatable
////todo 目前kotlin不支持repeatable
//annotation class TestData(
//        val source: DataSource = DataSource.Spring,
//        val single:Boolean=true,
//        val dataProvider: KClass<out IDataProvider<*>> = IDataProvider::class,
//        val parallel:Boolean = false,
//        val file:String="",
//        val pattern:String="")
//
//
//
//
//
