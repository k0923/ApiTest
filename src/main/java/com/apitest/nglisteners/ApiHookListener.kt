//package com.apitest.nglisteners
//
//import com.apitest.core.ApiBaseData
//import com.apitest.core.IDataLifeCycle
//import com.apitest.core.ITestScript
//import com.apitest.utils.ScriptUtils
//import org.testng.IHookCallBack
//import org.testng.IHookable
//import org.testng.ITestResult
//
//class ApiHookListener:ApiTestNgListener<IHookable>(),IHookable {
//
//    init{
//        registerHandler(ITestScript::class.java,ITestScriptHook())
//    }
//
//    override fun run(callBack: IHookCallBack, testResult: ITestResult) {
//        execute({ true }, { i -> i.run(callBack, testResult) })
//    }
//
//    inner class ITestScriptHook:IHookable{
//        override fun run(callBack: IHookCallBack, testResult: ITestResult) {
//
//            if(callBack.parameters.size == 1){
//                val data = callBack.parameters[0]
//                if(data is ApiBaseData || data is IDataLifeCycle){
//                    ScriptUtils.execute(data,callBack,testResult)
//                    return
//                }
//            }
//            callBack.runTestMethod(testResult)
//        }
//    }
//}