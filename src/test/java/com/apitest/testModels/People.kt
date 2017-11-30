package com.apitest.testModels

import com.apitest.core.ITestData

open class People:ITestData {
    override var id: String = ""
    var age:Int = 0
    var name:String?=null
}