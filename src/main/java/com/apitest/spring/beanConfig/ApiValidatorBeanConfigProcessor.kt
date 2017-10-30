//package com.apitest.spring.beanConfig
//
//import org.apache.commons.lang.StringUtils
//import org.apache.logging.log4j.LogManager
//import org.springframework.beans.BeansException
//import org.springframework.beans.factory.config.BeanDefinition
//import org.springframework.beans.factory.config.BeanDefinitionHolder
//import org.springframework.beans.factory.config.BeanFactoryPostProcessor
//import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
//import org.springframework.beans.factory.support.ManagedList
//import java.util.HashMap
//
//class ApiValidatorBeanConfigProcessor : BeanFactoryPostProcessor {
//
//    @Throws(BeansException::class)
//    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
//
//        processBatchValidator(beanFactory)
//    }
//
//
//    private fun processBatchValidator(beanFactory: ConfigurableListableBeanFactory) {
//        val names = beanFactory.getBeanNamesForType(BatchValidator::class.java, true, true)
//        for (n in names) {
//            val bean = beanFactory.getBeanDefinition(n)
//            val validators = getValidatorBeanDefinitionHolder(bean)
//            processBatchValidators(bean, beanFactory, validators)
//
//            println()
//        }
//    }
//
//
//    private fun mergeValidatorBean(sourceList: ManagedList<BeanDefinitionHolder>?, tmpList: ManagedList<BeanDefinitionHolder>) {
//
//        val sourceMap = convertValidatorToMap(sourceList)
//        val tmpMap = convertValidatorToMap(tmpList)
//        tmpMap.forEach { k, v ->
//            if (sourceMap.containsKey(k)) {
//                val sourceBean = sourceMap[k].getBeanDefinition()
//                val sourceValidatorPV = sourceBean.propertyValues.getPropertyValue(validatorProp)
//                if (sourceValidatorPV != null) {
//                    val tmpBean = v.beanDefinition
//
//                    val tmpValidatorPV = tmpBean.propertyValues.getPropertyValue(validatorProp)
//
//                    val sourceBeanValidators = sourceValidatorPV.value as ManagedList<BeanDefinitionHolder>
//                    val tmpValidators = tmpValidatorPV.value as ManagedList<BeanDefinitionHolder>
//
//                    mergeValidatorBean(sourceBeanValidators, tmpValidators)
//
//                    sourceValidatorPV.setAttribute(validatorProp, tmpValidatorPV)
//                }
//
//            } else {
//                sourceList!!.add(v)
//            }
//        }
//    }
//
//    private fun convertValidatorToMap(list: ManagedList<BeanDefinitionHolder>?): Map<String, BeanDefinitionHolder> {
//        if (list == null || list.size == 0)
//            return HashMap()
//        val maps = HashMap<String, BeanDefinitionHolder>()
//        list.forEach { l ->
//            val bean = l.beanDefinition
//            val propertyValue = bean.propertyValues.getPropertyValue(pathProp).value.toString()
//            //String key = bean.getBeanClassName() + ":" + propertyValue;
//            maps.put(propertyValue, l)
//        }
//        return maps
//    }
//
//
//    private fun getValidatorBeanDefinitionHolder(bean: BeanDefinition): ManagedList<BeanDefinitionHolder>? {
//        val pv = bean.propertyValues.getPropertyValue(validatorProp)
//        return if (pv != null && pv.value != null) {
//            pv.value as ManagedList<BeanDefinitionHolder>
//        } else null
//    }
//
//    private fun processBatchValidators(bean: BeanDefinition, beanFactory: ConfigurableListableBeanFactory, props: ManagedList<BeanDefinitionHolder>?) {
//        if (StringUtils.isEmpty(bean.parentName))
//            return
//        val parentBean = beanFactory.getBeanDefinition(bean.parentName)
//        val parentValidator = getValidatorBeanDefinitionHolder(parentBean)
//        if (parentValidator != null) {
//            mergeValidatorBean(props, parentValidator)
//        }
//        processBatchValidators(parentBean, beanFactory, props)
//    }
//
//    companion object {
//
//        private val validatorProp = "validators"
//
//        private val pathProp = "propertyPath"
//
//
//        @Transient private val logger = LogManager.getLogger(ApiValidatorBeanConfigProcessor::class.java)
//    }
//
//
//}