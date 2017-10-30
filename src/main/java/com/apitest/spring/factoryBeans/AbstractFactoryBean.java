package com.apitest.spring.factoryBeans;

import org.springframework.beans.factory.FactoryBean;

public abstract class AbstractFactoryBean<T> implements FactoryBean<T> {

    private Boolean singleton = false;

    @Override
    public boolean isSingleton() {
        return singleton;
    }

    public Boolean getSingleton() {
        return singleton;
    }

    public void setSingleton(Boolean singleton) {
        this.singleton = singleton;
    }
}
