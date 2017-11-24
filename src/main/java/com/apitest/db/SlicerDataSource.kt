package com.apitest.db

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource

class SlicerDataSource<out T:ISlicer>(private val slicer:T):AbstractRoutingDataSource() {

    override fun determineCurrentLookupKey(): Any = slicer.getKey()
}