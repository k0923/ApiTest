//package com.apitest.spring.beanConfig
//
////import com.apitest.core.ApiBaseData
//import org.apache.commons.lang.StringUtils
//import org.springframework.beans.BeansException
//import org.springframework.beans.MutablePropertyValues
//import org.springframework.beans.PropertyValue
//import org.springframework.beans.factory.config.*
//import org.springframework.beans.factory.support.GenericBeanDefinition
//import org.springframework.beans.factory.support.ManagedMap
//import java.util.*
//
//class ApiBaseDataBeanConfigProcessor:BeanFactoryPostProcessor{
//
//    @Throws(BeansException::class)
//    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
//        processApiBaseData(beanFactory)
//    }
//
//    private fun processApiBaseData(beanFactory: ConfigurableListableBeanFactory) {
//        val names = beanFactory.getBeanNamesForType(ApiBaseData::class.java, true, true)
//        names.map { beanFactory.getBeanDefinition(it) }.forEach{changeBeanDefinition(it)}
//    }
//
//    private fun changeBeanDefinition(bean: BeanDefinition) {
//        val pvs = bean.propertyValues.propertyValueList
//        val flowData = HashMap<String, String>()
//        pvs.removeIf { p ->
//            val v = p.value
//            if (v != null && v is TypedStringValue) {
//
//                val result = when{
//                    StringUtils.startsWith(v.value,"@@")->false
//                    StringUtils.startsWith(v.value,"@")->true
//                    else -> null
//                }
//
//                if (result != null) {
//                    if (result === false) {
//                        v.value = StringUtils.right(v.value, v.value.length - 1)
//                    }
//                    if (result === true) {
//                        val name = p.name
//                        flowData.put(name, v.value)
//                    }
//                }
//                result != null
//            }
//            false
//        }
//        addFlowData(bean, flowData)
//    }
//
//
//    private fun addFlowData(bean: BeanDefinition?, maps: Map<String, String>?) {
//
//        if (bean == null || maps == null || maps.size < 1)
//            return
//
//        var flowProperty: PropertyValue? = bean.propertyValues.getPropertyValue("flowData")
//
//        val map: ManagedMap<TypedStringValue, TypedStringValue>
//        if (flowProperty == null) {
//            val flowDefinition = GenericBeanDefinition()
//            flowDefinition.beanClass = MapFactoryBean::class.java
//            flowDefinition.scope = "prototype"
//            map = ManagedMap()
//
//            val v1 = PropertyValue("sourceMap", map)
//            val v2 = PropertyValue("targetMapClass", "java.util.HashMap")
//            val values = MutablePropertyValues()
//            values.addPropertyValue(v1)
//            values.addPropertyValue(v2)
//            flowDefinition.propertyValues = values
//
//            val holder = BeanDefinitionHolder(flowDefinition, UUID.randomUUID().toString())
//            flowProperty = PropertyValue("flowData", holder)
//
//            bean.propertyValues.addPropertyValue(flowProperty)
//        } else {
//            val v = (flowProperty.value as BeanDefinitionHolder).beanDefinition.propertyValues.getPropertyValue("sourceMap")
//            map = v.value as ManagedMap<TypedStringValue, TypedStringValue>
//        }
//
//        for ((key1, value1) in maps) {
//            val key = TypedStringValue(key1)
//            val value = TypedStringValue(value1)
//            map.put(key, value)
//        }
//
//    }
//
//}