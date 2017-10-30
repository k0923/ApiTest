package com.apitest.core

interface IDataLifeCycle{
    fun beforeRun()

    fun afterRun()

    fun finalRun()
}