package com.apitest.db

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource
import java.lang.reflect.Method
import java.text.MessageFormat
import javax.sql.DataSource

open class DataSource1{
    var from:Int = 0
    var to:Int = 99
    var userName:String? = "mordercenter_u"
    var password:String? = "ali88"
    var templateUrl:String? = "jdbc:mysql://mypay3.testdb.alipay.net:3306/mordercenter{0}?useUnicode=true&characterEncoding=utf8"
    var key = "00"

    fun getDataSource(): Map<Any, DataSource> {
        var map = HashMap<Any, DataSource>()
        for(i in from..to){
            var dataSource = MysqlDataSource()
            var index = if (i<10) "0"+i.toString() else i.toString()
            dataSource.setUrl(MessageFormat.format(templateUrl,index))
            dataSource.setPassword(password)
            dataSource.user = userName
            map.put(index+key,dataSource)
        }
        return map
    }
}

class DataSource2:DataSource1(){
    init{
        templateUrl="jdbc:mysql://mypay1.testdb.alipay.net:3310/mordercenter_rfo{0}?useUnicode=true"
        userName = "morder_rfo_u"
        key = "03"
    }
}

class DataSource3:DataSource1(){
    init{
        templateUrl="jdbc:mysql://mypay1.testdb.alipay.net:3310/mordercenter_lfo{0}?useUnicode=true"
        userName="morder_lfo_u"
        key="02"
    }
}


class SplitDataSlicer:ISlicer{

    var currentIndex:ThreadLocal<String> = ThreadLocal()

    override fun getKey(): Any {
        return currentIndex.get()
    }

    override fun process(method: Method, args: Map<String, Any?>) {
        val current = args["arg0"].toString()
        val key = current.substring(20,24)
        currentIndex.set(key)
    }

    override fun getDataSource(): Map<Any, DataSource> {
        val source1 = DataSource1()
        val source2 = DataSource2()
        val source3 = DataSource3()

        val dataSource = HashMap<Any, DataSource>()
        dataSource.putAll(source1.getDataSource())
        dataSource.putAll(source2.getDataSource())
        dataSource.putAll(source3.getDataSource())
        return dataSource

    }
}