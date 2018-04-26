package com.apitest.sample.funcprovider;

import com.apitest.testModels.Company;
import com.apitest.testModels.Console;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OuterFunctionProvider {
    public List<Console> getConsoles(){
        return Arrays.asList(Console.values()).stream().filter(c->c.getCompany() == Company.Microsoft).collect(Collectors.toList());
    }
}
