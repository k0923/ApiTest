package com.apitest

import com.apitest.config.GlobalConfig
import com.apitest.dataProvider.Spring
import org.apache.ibatis.annotations.Select
import org.testng.annotations.Test

class DbTest {

//    @Test
//    fun test(@Spring(pattern = "o1") orderId: String) {
//        var mapper = GlobalConfig.get(OrderMapper::class.java)
//
//        println(mapper?.getOrderByOrderNo(orderId, "ORDER_CLOSED"))
//    }
}


interface OrderMapper {

    @Select("select * from morc_order_0\${param1.substring(20,22)} where order_no = #{param1} and status = #{param2}")
    fun getOrderByOrderNo(orderNo: String, status: String):
            //@Select("select * from morc_order_0{table} where order_no = #{id} and status = ${status}")
            //@Slicer(expression = "substring(20,22)")
            Map<String, Any>


    @Select("select * from morc_order_0\${value.substring(20,22)} where order_no = #{orderNo}")
    fun getOrder(values: Map<String, Any>): Map<String, Any>
}

