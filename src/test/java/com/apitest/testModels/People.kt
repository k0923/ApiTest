package com.apitest.testModels



open class People {
    var id: String = ""
    var age:Int = 0
    var name:String?=null
}

class Student :People() {
    var isMan:Boolean = true
    var money:Double = 0.12
    override fun toString(): String {
        return "Student(id='$id', isMan=$isMan, money=$money)"
    }
}

enum class Console {
    XBOX,
    PS4,
    PS3,
    WII,
    WIIU
}