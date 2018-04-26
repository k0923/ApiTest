package com.apitest.sample.combination;

import com.apitest.annotations.Filter;
import com.apitest.testModels.Company;
import com.apitest.testModels.Console;
import org.testng.annotations.Test;

public class CombinationWithFilterTest {


    /**
     * console有7个枚举值，company有3个枚举值
     * 过滤器只需要company == Company.Microsoft
     * 最终生成7*1=7组测试数组
     */
    @Test
    @Filter(method = "filterCompany")
    public void filterTest(Console console, Company company){

    }

    /**
     * 注意事项
     * 1. 过滤方法的参数类型和个数必须与测试方法一致
     * 2. 过滤方法的返回类型一律为布尔类型
     */
    public boolean filterCompany(Console console,Company company){
        return company == Company.Microsoft;
    }


    public boolean filterCompany2(Console console,Company company){
        return console == Console.PS4 || console == Console.SWITCH;
    }


    /**
     * 由于有两组过滤器
     * 最终生成console == Console.PS4 || console == Console.SWITCH 并且 company == Company.Microsoft 两组测试数据
     */
    @Test
    @Filter(method = "filterCompany")
    @Filter(method = "filterCompany2")
    public void filterTest2(Console console,Company company){

    }

}
