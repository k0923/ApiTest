package com.apitest.testModels

class Student :People() {
    override var id:String = ""
    var isMan:Boolean = true
    var money:Double = 0.12
    override fun toString(): String {
        return "Student(id='$id', isMan=$isMan, money=$money)"
    }
}