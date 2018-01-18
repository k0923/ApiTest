package com.musical.models

import com.apitest.OrderMapper
import com.apitest.config.GlobalConfig
import com.musical.db.IProduct
import java.util.*
import kotlin.reflect.full.memberProperties

class Product{
    var id:String = UUID.randomUUID().toString().replace("-","")
    var file_id:String? = ""
    var region:String? = null
    var product:String? = null
    var test_type:String? = null
    var domain_name:String? = null
    var ip_address:String? = null
    var collect_time:Date? = null
    var url_count:Int? = null
    var total_duration:Double = 0.0
    var connect_duration:Double = 0.0
    var dns_duration:Double = 0.0
    var wait_duration:Double = 0.0
    var receive_duration:Double = 0.0
    var body_size:Double = 0.0
    var info:String = ""
    var create_time:Date = Date()
    var modify_time:Date = Date()
}


fun main(args:Array<String>){

    var sql = "insert into t_analyse_result values ("
    Product::class.memberProperties.forEach{
        sql += "#{product.${it.name}},"
    }
    sql = sql.substring(0,sql.length-1)+")"
    println(sql)

    val s = GlobalConfig.get("test");

    var mapper = GlobalConfig.get(IProduct::class.java)
    mapper!!.add(Product())


    println(UUID.randomUUID().toString().replace("-","").length)
}