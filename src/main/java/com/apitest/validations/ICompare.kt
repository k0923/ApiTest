package com.apitest.validations

interface ICompare<in T>{
    fun compare(actual:T,expect:T):Boolean
}