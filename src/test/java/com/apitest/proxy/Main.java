package com.apitest.proxy;

import net.sf.cglib.proxy.Enhancer;

public class Main {

    public static void main(String[] args){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(CglibTest.class);
        enhancer.setCallback(new MyMethodInterceptor());
        Object obj = enhancer.create();
        CglibTest test = (CglibTest) obj;
        test.sayHello();
    }


}
