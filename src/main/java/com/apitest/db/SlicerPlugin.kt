package com.apitest.db

import org.apache.ibatis.executor.Executor
import org.apache.ibatis.mapping.MappedStatement
import org.apache.ibatis.plugin.*
import org.apache.ibatis.session.ResultHandler
import org.apache.ibatis.session.RowBounds
import java.util.*

@Intercepts(Signature(type = Executor::class, method = "query", args = arrayOf(MappedStatement::class, Any::class, RowBounds::class, ResultHandler::class)))
class SlicerPlugin<out T:ISlicer>(private val process:T):Interceptor {
    override fun intercept(invocation: Invocation): Any {
        val p1:MappedStatement = invocation.args[0] as MappedStatement
        val cls = p1.id.substring(0,p1.id.lastIndexOf('.'))
        val m = p1.id.substring(p1.id.lastIndexOf('.')+1,p1.id.length)
        val method = Class.forName(cls).methods.first{it.name==m}
        if(Map::class.java.isAssignableFrom(invocation.args[1].javaClass)){
            process.process(method,invocation.args[1] as Map<String,Any?>)
        }else{
            process.process(method, mapOf("param1" to invocation.args[1]))
        }
        return invocation.proceed()
    }

    override fun setProperties(properties: Properties?) {

    }

    override fun plugin(target: Any?): Any = Plugin.wrap(target,this)
}