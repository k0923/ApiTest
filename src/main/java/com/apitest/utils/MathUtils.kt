package com.apitest.utils


object MathUtils {
    fun getResult(vararg data:Array<out Any?>):Array<Array<Any?>>{
        var total = 1
        data.forEach { total *= it.size }
        val result = Array(total,{arrayOfNulls<Any?>(data.size) })
        var layer = total
        (0 until data.size).forEach { j->
            layer  /= data[j].size
            var index = 0
            (0 until total).forEach{i->
                result[i][j] = data[j][index]
                if((i+1)%layer == 0)
                    index++
                if(index>=data[j].size)
                    index = 0
            }
        }
        return result
    }
}


