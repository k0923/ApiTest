package com.apitest.testModels

import com.apitest.core.ITestData

class StudentModel:ITestData {
    override var id:String = ""
    var age:Int = 0
    var name:String?=null
    var isMan:Boolean = true
    var money:Double = 0.12
}