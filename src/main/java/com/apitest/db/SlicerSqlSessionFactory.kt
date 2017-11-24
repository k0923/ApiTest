package com.apitest.db

import org.mybatis.spring.SqlSessionFactoryBean


class SlicerSqlSessionFactory<out T:ISlicer>(slicer:T):SqlSessionFactoryBean() {

    init {

        val dataSource = SlicerDataSource(slicer)
        dataSource.setTargetDataSources(slicer.getDataSource())

        dataSource.afterPropertiesSet()

        setDataSource(dataSource)
        setPlugins(arrayOf(SlicerPlugin(slicer)))
    }

}