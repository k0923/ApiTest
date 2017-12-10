package com.apitest.utils;

public interface ICommonUtils {

    default void println(Object msg){
        System.out.println(msg);
    }

}
