package com.apitest.sample.combination;

import com.apitest.dataProvider.Value;
import com.apitest.testModels.Company;
import com.apitest.testModels.Console;
import org.testng.annotations.Test;

public class CombinationProviderTest {

    /**
     * console有7个枚举值，company有3个枚举值
     * 最终会生成7*3=21组测试数据
     */
    @Test
    public void enumCombinationTest(Console console, Company company){

    }

    /**
     * console有7个枚举值，value有3组数字，str有两组字符串
     * 最终会生成 7*3*2=42组测试数据
     */
    @Test
    public void otherCombinationTest(Console console, @Value(ints = {1,2,3}) int value,@Value(strings = {"a","b"}) String str){

    }

}
