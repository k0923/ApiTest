package com.apitest.join

import com.apitest.utils.CommonUtils

fun test(){

}


fun merge(vararg data:Array<out Any?>?):Array<Array<Any?>>{
    var total = 1
    for(i in 0 until data.size)
        total *= data[i]?.size ?:1
    val result = Array(total,{_-> arrayOfNulls<Any?>(data.size)})
    var layer = total
    var index: Int
    for(i in 0 until data.size){
        layer /= data[i]?.size ?:1
        index = 0
        val size = data[i]?.size ?:1
        for(j in 0 until total){
            result[j][i] = if(data[i] == null) null else data[i]!![index]
            if((j+1)%layer == 0)
                index++
            if(index>=size)
                index = 0
        }
    }
    return result
}

fun main(args:Array<String>){
    val array1 = arrayOf(1,2,3)
    val array2 = arrayOf("a")
    val array3 = arrayOf("b","d")
    val array4 = arrayOf(array1,array2,array3)
    val result = CommonUtils.getCartesianProduct(array1,array2,array3)
    result.forEach {
        print("{")
        it.forEach { print(it);print(",") }
        print("}")
        println()
    }
}