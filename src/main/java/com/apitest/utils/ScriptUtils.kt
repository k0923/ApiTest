package com.apitest.utils

import com.apitest.annotations.FlowTag
import com.apitest.annotations.TestData
import com.apitest.core.ApiBaseData
import com.apitest.core.IDataLifeCycle
import com.apitest.dataProvider.IDataProvider
import com.apitest.dataProvider.IParameterProvider
import com.apitest.dataProvider.TestDataConfig
import com.apitest.extensions.ofType
import org.apache.logging.log4j.LogManager
import org.springframework.util.ReflectionUtils
import org.testng.IHookCallBack
import org.testng.ITestResult
import java.lang.reflect.Executable
import java.lang.reflect.Method
import java.lang.reflect.Parameter
import java.util.function.Consumer
import java.util.function.Supplier
import java.util.regex.Pattern
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.isSuperclassOf

object ScriptUtils {

    private val logger = LogManager.getLogger(SpringUtils::class.java)

    fun <T> getTestData(cls: Class<*>, methodName: String, dataCls: Class<*>, filter: (T) -> Boolean): T? {
        val method = cls.getMethod(methodName, dataCls)
        if (method != null) {
            val datas = getTestData(method)
            return datas.first {
                val data = it as T
                if (data != null) {
                    filter(data)
                } else
                    false
            } as T
        }
        return null
    }

    fun getProvider(cls:KClass<out IDataProvider>):IDataProvider = cls.objectInstance ?: cls.createInstance()

    fun getInstance(cls:KClass<*>):Any = cls.objectInstance ?: cls.createInstance()


    fun getTestData(method: Executable): Array<Array<Any?>> {
        val testDataConfigs = getTestDataConfig(method)
        val data= Array<Supplier<Array<out Any?>?>>(method.parameterCount,{_-> Supplier { null }})
        val defaultConsumer:Consumer<Iterable<Int>> = Consumer {
            it.forEach{
                val provider = getProvider(testDataConfigs[it].provider)
                data[it] = Supplier { provider.getData(method.parameters[it],testDataConfigs[it]).toTypedArray() }
            }
        }
        when(testDataConfigs.size){
            1 -> return getProvider(testDataConfigs[0].provider).getData(method,testDataConfigs[0])                          //testDataConfigs[0].source.dataFromMethod(method,testDataConfigs[0])
            else-> {
                if(testDataConfigs.size>=method.parameterCount){
                    defaultConsumer.accept(method.parameters.indices)
                }else{
                    defaultConsumer.accept(testDataConfigs.indices)
                    (testDataConfigs.size until method.parameterCount).forEach { data[it] = Supplier { getEmptyConfigData(method.parameters[it]) } }
                }
            }
        }
        return CommonUtils.getCartesianProductBySupplier(data)
    }


    fun getTestDataConfig(data: TestData?):TestDataConfig{
        val testDataConfig = TestDataConfig()
        if (data != null) {
            with(testDataConfig) {
                provider = data.provider
                paras = data.paras
            }
        }
        return testDataConfig
    }

    fun getTestDataConfig(method:Executable):Array<TestDataConfig>{
        val testDatas = method.getAnnotationsByType(TestData::class.java)
        return when(testDatas.size){
            0 -> arrayOf(getTestDataConfig(null))
            else -> testDatas.map { getTestDataConfig(it) }.toTypedArray()
        }
    }



    fun getEmptyConfigData(para:Parameter):Array<out Any?>?{
        val tp = para.type
        return when{
            tp.isEnum -> tp.enumConstants
            IParameterProvider::class.isSuperclassOf(tp.kotlin) -> ScriptUtils.getInstance(tp.kotlin).ofType(IParameterProvider::class.java)?.getData()?.toTypedArray()
            else -> throw RuntimeException("No data provider for parameter:$para")
        }
    }


    fun getRunMethod(runMethod: (() -> Unit)? = null): (data: Any?, flowData: MutableMap<String, Any?>?, method: Method) -> Any? {
        return { data, flowData, method ->
            var result: Any? = null
            try {
                flowData?.let { fd -> data?.ofType(ApiBaseData::class.java)?.let { ScriptUtils.configFlowData(it, fd) } }
                data?.ofType(IDataLifeCycle::class.java)?.beforeRun()
                when (runMethod) {
                    null -> {
                        result = when (data) {
                            null -> ReflectionUtils.invokeMethod(method, method.declaringClass.newInstance())
                            else -> ReflectionUtils.invokeMethod(method, method.declaringClass.newInstance(), data)
                        }
                    }
                    else -> {
                        runMethod()
                    }
                }
                data?.ofType(IDataLifeCycle::class.java)?.afterRun()
                flowData?.let { fd -> data?.let { ScriptUtils.addFlowData(it, fd) } }
            } finally {
                data?.ofType(IDataLifeCycle::class.java)?.finalRun()
            }
            result
        }
    }

    fun execute(script: Any, methodName: String, filter: (Any?) -> Boolean, defaultExecution: (() -> Unit)? = null) {
        val method: Method? = script.javaClass.methods.firstOrNull {
            it.name == methodName && it.parameterCount == 1
        } ?: throw RuntimeException("method:$methodName not found")
        val data = getTestData(method!!).firstOrNull(filter) ?: throw RuntimeException("test data not found with script:$script,method:$methodName")

        execute(script, data, method, defaultExecution)
    }


    fun execute(script: Any, data: Any, method: Method, defaultExecution: (() -> Unit)? = null) {
        if (method.parameterCount != 1 || !method.parameterTypes[0].isAssignableFrom(data.javaClass)) {
            throw IllegalArgumentException("method:$method not match with data:$data")
        }
        val flowData = data.ofType(ApiBaseData::class.java)?.preApiScript?.execute()
        getRunMethod(defaultExecution)(data, flowData, method)
    }

    fun execute(data: Any, callBack: IHookCallBack, testResult: ITestResult) {
        execute(testResult.instance, data, testResult.method.constructorOrMethod.method, {
            callBack.runTestMethod(testResult)
        })
    }

    fun configFlowData(data: ApiBaseData, flowDatas: Map<String, Any?>) {
        if (flowDatas.isEmpty()) {
            return
        }
        //var simpleContext = StandardEvaluationContext(data)
        val pattern = Pattern.compile("(^@[a-zA-Z0-9_$]+)[.]?(.*)")
        data.flowData?.forEach {
            var subKey: String? = null
            var key = it.value
            var y = pattern.matcher(it.key)
            if (y.find()) {
                key = y.group(1)
                subKey = y.group(2)
            }
            if (flowDatas.containsKey(key)) {
                var value = flowDatas[key]
                if (!subKey.isNullOrBlank()) {
                    value = value!!.getValueByPath(subKey!!)
                } else {
                    logger.info("从Flow里获取key:{}的值为:{}", key, value)
                }
                data.setValueByPath(it.key,value)
            } else {
                logger.warn("未能找到Flow Data:{}", key)
            }

        }

    }


    fun addFlowData(obj: Any, flowDatas: MutableMap<String, Any?>) {
        ReflectionUtils.doWithFields(obj.javaClass, { f ->
            val flowTag = f.getAnnotation(FlowTag::class.java)
            flowTag?.let {
                var name = it.name
                name = if (name.isBlank()) f.name else name
                name = "@" + name
                f.isAccessible = true
                flowDatas.put(name, f.get(obj))
            }
        })
    }
}

