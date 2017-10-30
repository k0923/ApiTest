package com.apitest.utils


/**
 * Created by Yang on 2017/8/17.
 */

object PathUtils{
    fun Class<*>.getResourceFile(extension:String):String{
        val folder = this.getClassFolder()
        return "$folder/${this.simpleName}.$extension"

    }

    fun Class<*>.getClassFolder():String{
        val location = this.protectionDomain.codeSource.location.path

        val prefix= if(location.contains("target/test")) "src/test/java" else "src/main/java"

        var classFolder = this.name.replace(".","/")

        val lastIndex = classFolder.lastIndexOf("/")
        classFolder = if (lastIndex>0) classFolder.substring(0,lastIndex) else ""

        return "${System.getProperty("user.dir").replace("\\","/")}/$prefix/$classFolder"
    }
}



