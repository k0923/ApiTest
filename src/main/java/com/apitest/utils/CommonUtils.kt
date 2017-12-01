package com.apitest.utils

import java.util.function.Supplier

object CommonUtils {
    fun getCartesianProduct(vararg data:Array<out Any?>?):Array<Array<Any?>> = getCartesianProductByArray(data)

    fun getCartesianProduct(vararg data:Supplier<Array<out Any?>?>) = getCartesianProductBySupplier(data)

    fun getCartesianProductBySupplier(data:Array<out Supplier<Array<out Any?>?>>):Array<Array<Any?>>{
        val myData = data.map { it.get() }
        var total = 1
        for(i in 0 until myData.size)
            total *= getSize(myData[i]?.size)
        val result = Array(total){_-> arrayOfNulls<Any?>(data.size)}
        var layer = total
        var index: Int
        for(i in 0 until data.size){
            layer /= getSize(myData[i]?.size)
            index = 0
            val size = getSize(myData[i]?.size)
            for(j in 0 until total){
                result[j][i] = when(myData[i]?.size){
                    null->null
                    0->null
                    else-> {
                        val d = data[i].get()
                        if(d?.size!=myData[i]?.size){
                            throw RuntimeException("supplier size should be fixed")
                        }
                        d!![index]
                    }
                }
                if((j+1)%layer == 0)
                    index++
                if(index>=size)
                    index = 0
            }
        }
        return result
    }


    //计算笛卡尔积
    fun getCartesianProductByArray(data:Array<out Array<out Any?>?>):Array<Array<Any?>>{
        var total = 1
        for(i in 0 until data.size)
            total *= getSize(data[i]?.size)
        val result = Array(total){_-> arrayOfNulls<Any?>(data.size)}
        var layer = total
        var index: Int
        for(i in 0 until data.size){
            layer /= getSize(data[i]?.size)
            index = 0
            val size = getSize(data[i]?.size)
            for(j in 0 until total){
                result[j][i] = when(data[i]?.size){
                    null->null
                    0->null
                    else->data[i]!![index]
                }
                if((j+1)%layer == 0)
                    index++
                if(index>=size)
                    index = 0
            }
        }
        return result
    }

    private fun getSize(size:Int?):Int = when(size){
        null->1
        0->1
        else->size
    }


}