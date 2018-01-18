package com.musical

import com.apitest.config.GlobalConfig
import com.apitest.dataProvider.Csv
import com.google.gson.Gson
import com.musical.db.IProduct
import com.musical.models.*
import org.testng.annotations.AfterClass
import org.testng.annotations.Test
import java.util.*
import java.io.FileWriter
import java.util.Locale
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList


class DataGenerateTest {

    var myData = ArrayList<AnalysisData>()

    var count:Int = 0

    @Test
    fun getData(countryEnum: CountryEnum, productEnum: ProductEnum, productTypeEnum: ProductTypeEnum, @Csv(files=["Date.csv"]) date:Map<String,String>){


        val format = SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
        val random = Random()

        val data = AnalysisData()
        myData.add(data)
        data.id = ++count
        data.country = countryEnum.name
        data.product = productEnum.name
        data.type = productTypeEnum.name
        data.domain = productTypeEnum.domain.replace("example", productEnum.name)
        data.date = date["Date"]
        data.duration = 300 + random.nextInt(300)
        data.connect = 200 + random.nextInt(100)
        data.dns = 100 + random.nextInt(100)
        data.wait = 50+random.nextInt(50)
        data.receive = 250 + random.nextInt(50)
        data.size = 500 + random.nextInt(500)

    }


    val insertDatas = ArrayList<Product>()

    val dateFormat = SimpleDateFormat("mm/dd/yyyy")

    val mapper = GlobalConfig.get(IProduct::class.java)

    @Test
    fun insertData(countryEnum: CountryEnum, productEnum: ProductEnum, productTypeEnum: ProductTypeEnum, @Csv(files=["Date.csv"]) date:Map<String,String>){
        val product = Product()
        val random = Random()
        product.file_id = (++count).toString()
        product.region = countryEnum.name
        product.product = productEnum.name
        product.test_type = productTypeEnum.name
        product.domain_name = productTypeEnum.domain
        product.ip_address = ""

        product.collect_time = dateFormat.parse(date["Date"])
        product.url_count = count
        product.total_duration =  (300 + random.nextInt(300)).toDouble()
        product.connect_duration = (200 + random.nextInt(100)).toDouble()
        product.dns_duration = (100 + random.nextInt(100)).toDouble()
        product.wait_duration = (50+random.nextInt(50)).toDouble()
        product.receive_duration = (250 + random.nextInt(50)).toDouble()
        product.body_size = (500 + random.nextInt(500)).toDouble()
        product.info = " "
        mapper!!.add(product)
    }

    @AfterClass
    fun writeToTxt(){

//        var coutries: List<Country>?=null
//        coutries = myData.groupBy { it.country }.map {
//            val country = Country()
//            country.name = it.key
//            country.products = it.value.groupBy { it.product }.map {
//                val product = Product()
//                product.name = it.key
//                product.productTypes = it.value.groupBy { it.productType }.map{
//                    val productType = ProductType()
//                    productType.name = it.key
//                    productType.datas = it.value.map {
//                        it
//                    }
//                    productType
//                }
//                product
//            }
//            country
//        }


        var myDataV2:Map<String,List<Info>>? = null

        myDataV2 = myData.groupBy { it.product!! }

        val gson = Gson()
        val writer = FileWriter("testData.json")
        val jsonStr = gson.toJson(myDataV2)
        writer.write(jsonStr)
        writer.close()


    }

}