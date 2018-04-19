package com.apitest.nglisteners

import com.apitest.annotations.Inject
import com.apitest.annotations.Parallel
import com.apitest.annotations.Scope
import com.apitest.config.GlobalConfig
import com.apitest.dataProvider.*
import com.apitest.utils.SpringUtils
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.stereotype.Component
import org.testng.*
import org.testng.annotations.*
import java.lang.reflect.Constructor
import java.lang.reflect.Method
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

open class ApiTestListener: IHookable, IAnnotationTransformer2, ISuiteListener,IClassListener{

    companion object {
        val dataSet:MutableSet<Any?> = HashSet()
        val dataProviders:MutableSet<KClass<out Annotation>> = HashSet()
    }

    init {
        registerDataProvider(Spring::class)
        registerDataProvider(Csv::class)
        registerDataProvider(Func::class)
        registerDataProvider(Value::class)
        registerDataProvider(Json::class)
    }





    fun registerDataProvider(cls:KClass<out Annotation>){
        cls.findAnnotation<Provider>() ?: throw IllegalArgumentException(cls.simpleName)
        dataProviders.add(cls)
    }


    override fun onBeforeClass(testClass: ITestClass?) {
        synchronized(dataSet){
            testClass?.realClass?.getAnnotation(Component::class.java)?.let {
                testClass.getInstances(false)?.forEach { obj->
                    if(!dataSet.contains(obj)){
                        dataSet.add(obj)
                        testClass.realClass.declaredFields.forEach { f->
                            f.getAnnotation(Inject::class.java)?.let {
                                val context = when(it.scope){
                                    Scope.ClassPath -> SpringUtils.getContext(testClass.realClass)
                                    Scope.Global -> GlobalConfig.getCtx()
                                }

                                when(context!!.containsBean(f.name)){
                                    true->{
                                        f.isAccessible = true
                                        f.set(obj,context.getBean(f.name,f.type))
                                    }
                                    else->{
                                        if(it.required){
                                            throw NoSuchBeanDefinitionException(testClass.realClass,f.name,"No such Bean found")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onAfterClass(testClass: ITestClass?) {

    }

    override fun run(callBack: IHookCallBack, testResult: ITestResult) {
        callBack.runTestMethod(testResult)
    }

    override fun transform(annotation: IConfigurationAnnotation?, testClass: Class<*>?, testConstructor: Constructor<*>?, testMethod: Method?) {

    }

    override fun transform(annotation: IDataProviderAnnotation?, method: Method?) {

    }

    override fun transform(annotation: IFactoryAnnotation, method: Method?) {
        if(annotation.dataProvider.isNotBlank()){
            return
        }
        if(method==null){
            annotation.dataProvider = "getData"
            annotation.dataProviderClass = TestDataProvider::class.java
        }
    }

    override fun transform(annotation: ITestAnnotation?, testClass: Class<*>?, testConstructor: Constructor<*>?, method: Method?) {

        method?.parameters?.let {
            if (it.isNotEmpty()) {
                if (annotation?.dataProvider?.isEmpty() == true) {
                    val parallel = method.getAnnotation(Parallel::class.java)
                    annotation.dataProviderClass = TestDataProvider::class.java
                    annotation.dataProvider = if(parallel==null) "getData" else "getDataParallel"
                }
            }
        }
//todo testng这边有个BUG，已经反馈给testng团队了，等修复完才能用，否则没屌用

//        val set = HashSet<String>()
//        annotation?.dependsOnMethods?.let {
//            it.forEach {
//                set.addAll(getDependMethods(it,method!!.declaringClass))
//            }
//        }
//
//        if(set.isNotEmpty()){
//            annotation?.dependsOnMethods = set.toTypedArray()
//        }

    }

    private fun getDependMethods(method:String,clz:Class<*>):Set<String>{
        val set = hashSetOf(method)
        val methods = clz.methods.filter { it.name == method }
        methods.forEach {
            val testAnnotation = it.getAnnotation(Test::class.java)
            testAnnotation?.let {
                it.dependsOnMethods.forEach {
                    set.addAll(getDependMethods(it,clz))
                }
            }
        }
        return set
    }

    override fun onFinish(suite: ISuite?) {
        SpringUtils.clearAll()
        dataSet.clear()
    }

    override fun onStart(suite: ISuite?) {
    }

}