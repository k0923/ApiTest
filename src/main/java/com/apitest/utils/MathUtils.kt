package com.apitest.utils

import java.util.function.Function

object MathUtils {
    fun getResult(vararg data:Array<out Any?>):Array<Array<Any?>>{
        return when{
            data.size == 0 -> emptyArray()
            data.any { it.size == 0 } -> emptyArray()
            data.size == 1 -> Array(data[0].size,{ arrayOf(data[0][it])})
            else -> {
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
                result
            }


        }
    }

    fun getResult(vararg providers:Pair<List<Any?>,Function<List<Any?>,List<Any?>>>):Array<Array<Any?>>{
        return when{
            providers.size == 0 -> emptyArray()
            providers.size == 1 -> Array(providers[0].first.size,{ arrayOf(providers[0].first[it])})
            providers.any { it.first.size == 0 } -> emptyArray()
            else -> {
                var total = 1
                providers.forEach { total *= it.first.size }
                val result = Array(total,{arrayOfNulls<Any?>(providers.size) })
                var layer = total
                (0 until providers.size).forEach { j->
                    val size = providers[j].first.size
                    layer  /= size
                    val dataList = Array(total/size,{providers[j].second.apply(providers[j].first)})
                    var index = 0
                    var secondIndex = 0
                    (0 until total).forEach{i->
                        result[i][j] = dataList[index++][secondIndex]
                        if((i+1)%layer == 0){
                            secondIndex++
                        }
                        if (index >= dataList.size){
                            index = 0
                        }
                        if (secondIndex >= size)
                            secondIndex = 0
                    }
                }
                result
            }
        }
    }
}



