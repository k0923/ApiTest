package com.apitest.spring.extension;

import com.apitest.core.ITestScript;
import com.apitest.dataProvider.Spring;
import com.apitest.spring.factoryBeans.StringValidatorFactoryBean;
import com.apitest.validations.FetchMode;
import com.apitest.validations.StrVltMethod;
import com.apitest.validations.StringValidator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Date;

public class SpringExtensionTest implements ITestScript {
    @Test
    public void dateBeanDefinitionParserTest(@Spring(files = {"testData.xml"}) Date date){
        Date now = new Date();
        long d20 = 20*24*3600*1000;
        Assert.assertTrue(date.getTime()-now.getTime()>d20);
    }

    @Test
    public void systemTimeBeanDefinitionParserTest_pure(@Spring(files = {"testData.xml"}) Long time){
        System.out.println(time);
        Assert.assertNotNull(time);
    }

    @Test
    public void systemTimeBeanDefinitionParserTest_pure_prefix_suffix(@Spring(files = {"testData.xml"}) String data){
        System.out.println(data);
        Assert.assertNotNull(data);
    }

    @Test
    public void envValueBeanDefinitionParserTest(@Spring(files = {"testData.xml"}) String data){
        Assert.assertEquals(data,"sit_abc");
    }


    @Test
    public void singletonTimeTest(@Spring(files = {"testData.xml"})Long time){
        //ScriptUtils.INSTANCE.getcon
    }

    @Test
    public void strVlt(@Spring(files = {"testData.xml"})StringValidator validator){
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
    public void repeatTest(@Spring(files = {"testData.xml"})String repeat){
        Assert.assertEquals(repeat.length(),512);
    }

    @Test
    public void repeatTest2(@Spring(files = {"testData.xml"})String repeat){
        Assert.assertEquals(repeat.length(),512);
    }

    @Test
    public void repeatTest3(@Spring(files = {"testData.xml"})String repeat){
        Assert.assertEquals(repeat.length(),256);
    }





}
