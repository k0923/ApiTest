package com.apitest.testModels;

import com.sun.org.apache.bcel.internal.generic.SWITCH;

public enum Console {
    XBOXONE(Company.Microsoft,2000),
    XBOX360(Company.Microsoft,1500),
    SWITCH(Company.Nintendo,2300),
    PS4(Company.Sony,1700),
    PS3(Company.Sony,1300),
    WII(Company.Nintendo,800),
    WIIU(Company.Nintendo,1300);

    private Company company;
    private int price;

    Console(Company company,int price){
        this.company = company;
        this.price = price;
    }

    public Company getCompany() {
        return company;
    }

    public int getPrice() {
        return price;
    }
}
