package com.apitest.utils

import com.apitest.annotations.FlowTag
import com.apitest.core.ApiBaseData
import com.apitest.core.IDataLifeCycle
import com.apitest.dataProvider.DataSource
import com.apitest.dataProvider.TestData
import com.apitest.dataProvider.TestDataConfig
import com.apitest.extensions.ofType
import org.apache.logging.log4j.LogManager
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.util.ReflectionUtils
import org.testng.IHookCallBack
import org.testng.ITestResult
import java.lang.reflect.Executable
import java.lang.reflect.Method
import java.util.regex.Pattern

object ScriptUtils {

    private val logger = LogManager.getLogger(SpringUtils::class.java)

    fun <T> getTestData(cls: Class<*>, methodName: String, dataCls: Class<*>, filter: (T) -> Boolean): T? {
        var method = cls.getMethod(methodName, dataCls)
        if (method != null) {
            var datas = getTestData(method)
            return datas.first {
                var data = it as T
                if (data != null) {
                    filter(data)
                } else
                    false
            } as T
        }
        return null
    }


    fun getTestData(method: Executable): List<Any?> {
        val testDataConfig = getTestDataConfig(method)
        return testDataConfig.source.dataMethod(method,testDataConfig)
    }

    fun getTestDataConfig(method:Executable):TestDataConfig{
        val testDataConfig = TestDataConfig()
        val testData = method.getAnnotation(TestData::class.java)
        if (testData != null) {
            with(testDataConfig) {
                single = testData.single
                pattern = testData.pattern
                file = testData.file
                source = testData.source
                dataProvider = testData.dataProvider
                parallel = testData.parallel
            }
        }
        return testDataConfig
    }

//    fun getTestDataConfig(testData: TestData?): TestDataConfig {
//        val testDataConfig = TestDataConfig()
//        if (testData != null) {
//            with(testDataConfig) {
//                single = testData.single
//                pattern = testData.pattern
//                file = testData.file
//            }
//        }
//        return testDataConfig
//    }

//    fun getTestDataConfig(method: Method): TestDataConfig {
//        val testConfig = method.getAnnotation(TestData::class.java)
//        return getTestDataConfig(testConfig)
//    }

    fun getTestMethod(clazz: Class<*>, methodName: String): Method {
        return clazz.methods.firstOrNull {
            it.name == methodName && it.parameterCount == 1
        } ?: throw RuntimeException("method:$methodName not found")
    }


    fun getRunMethod(runMethod: (() -> Unit)? = null): (data: Any?, flowData: MutableMap<String, Any?>?, method: Method) -> Any? {
        return { data, flowData, method ->
            var result: Any? = null
            try {
                flowData?.let { fd -> data?.ofType(ApiBaseData::class.java)?.let { ScriptUtils.configFlowData(it, fd) } }
                data?.ofType(IDataLifeCycle::class.java)?.let { it.beforeRun() }
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
                data?.ofType(IDataLifeCycle::class.java)?.let { it.afterRun() }
                flowData?.let { fd -> data?.let { ScriptUtils.addFlowData(it, fd) } }
            } finally {
                data?.ofType(IDataLifeCycle::class.java)?.let { it.finalRun() }
            }
            result
        }
    }

    fun execute(script: Any, methodName: String, filter: (Any?) -> Boolean, defaultExecution: (() -> Unit)? = null) {
        var method: Method? = script.javaClass.methods.firstOrNull {
            it.name == methodName && it.parameterCount == 1
        } ?: throw RuntimeException("method:$methodName not found")
        var data = getTestData(method!!).firstOrNull(filter) ?: throw RuntimeException("test data not found with script:$script,method:$methodName")

        execute(script, data, method, defaultExecution)
    }


    fun execute(script: Any, data: Any, method: Method, defaultExecution: (() -> Unit)? = null) {
        if (method.parameterCount != 1 || !method.parameterTypes[0].isAssignableFrom(data.javaClass)) {
            throw IllegalArgumentException("method:$method not match with data:$data")
        }
        var flowData = data.ofType(ApiBaseData::class.java)?.preApiScript?.execute()
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
        var simpleContext = StandardEvaluationContext(data)
        var pattern = Pattern.compile("(^@[a-zA-Z0-9_$]+)[.]?(.*)")
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
            var flowTag = f.getAnnotation(FlowTag::class.java)
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

