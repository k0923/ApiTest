package com.musical.models

import com.google.gson.annotations.SerializedName


open class Info:TestData(){
    var country:String? = null
    var type:String? = null

}









open class TestData{
    var date:String? = null

    @SerializedName("DURATION")
    var duration:Int? = null //总延时
    @SerializedName("CONNECT")
    var connect:Int? = null //连接时长
    @SerializedName("DNS")
    var dns:Int? = null//dns连接时长
    @SerializedName("WAIT")
    var wait:Int? = null //等待时长
    @SerializedName("TRANSFER")
    var receive:Int? = null //传输时长
    @SerializedName("SIZE")
    var size:Int? = null //传输大小
}


