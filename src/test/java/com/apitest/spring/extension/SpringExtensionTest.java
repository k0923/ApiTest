package com.apitest.spring.extension;

import com.apitest.core.ITestScript;
import com.apitest.annotations.TestData;
import com.apitest.spring.factoryBeans.StringValidatorFactoryBean;
import com.apitest.validations.FetchMode;
import com.apitest.validations.StrVltMethod;
import com.apitest.validations.StringValidator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Date;

public class SpringExtensionTest implements ITestScript {
    @Test
    @TestData(paras = "testData.xml")
    public void dateBeanDefinitionParserTest(Date date){
        Date now = new Date();
        long d20 = 20*24*3600*1000;
        Assert.assertTrue(date.getTime()-now.getTime()>d20);
    }

    @Test
    @TestData(paras = "testData.xml")
    public void systemTimeBeanDefinitionParserTest_pure(Long time){
        System.out.println(time);
        Assert.assertNotNull(time);
    }

    @Test
    @TestData(paras = "testData.xml")
    public void systemTimeBeanDefinitionParserTest_pure_prefix_suffix(String data){
        System.out.println(data);
        Assert.assertNotNull(data);
    }

    @Test
    @TestData(paras="testData.xml")
    public void envValueBeanDefinitionParserTest(String data){
        Assert.assertEquals(data,"sit_abc");
    }


    @Test
    @TestData(paras = "testData.xml")
    public void singletonTimeTest(Long time){
        //ScriptUtils.INSTANCE.getcon
    }

    @Test
    @TestData(paras="testData.xml")
    public void strVlt(StringValidator validator){
        Assert.assertEquals(validator.getProperty(),"abc");
        Assert.assertEquals(validator.getExpect(),"bcd");
        Assert.assertEquals(validator.getMethod(), StrVltMethod.Contains);
        Assert.assertEquals(validator.getFrom(), FetchMode.ByContext);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void strVltError(){
        StringValidatorFactoryBean bean = new StringValidatorFactoryBean();
        bean.getObject();
    }

    @Test
    @TestData(paras="testData.xml")
    public void repeatTest(String repeat){
        Assert.assertEquals(repeat.length(),512);
    }

    @Test
    @TestData(paras="testData.xml")
    public void repeatTest2(String repeat){
        Assert.assertEquals(repeat.length(),512);
    }

    @Test
    @TestData(paras="testData.xml")
    public void repeatTest3(String repeat){
        Assert.assertEquals(repeat.length(),256);
    }





}
