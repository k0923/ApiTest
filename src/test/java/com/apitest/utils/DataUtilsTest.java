package com.apitest.utils;

import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class DataUtilsTest {

    @Test
    public void getSystemTimeTest(){
        Assert.assertEquals(DataUtils.INSTANCE.getSystemTime(),new Date().getTime());
    }

    @Test
    public void getSystemTimeWithPrefixAndSurfixTest(){
        String data = DataUtils.INSTANCE.getSystemTime("a","b");
        Assert.assertTrue(data.startsWith("a"));
        Assert.assertTrue(data.endsWith("b"));
    }

    @Test
    public void repeatTest(){
        String data = "abc";
        data = DataUtils.INSTANCE.repeat(data,s->s.length()>20);
        //data = StringUtilsKt.repeat(data,s->s.length() > 20);

    }

    Set<String> timeSet = new HashSet<>();

    @Test(invocationCount = 30,threadPoolSize = 10)
    public void systemTimeTest(){
        Long systemTime = DataUtils.INSTANCE.getSystemTime();
        if(timeSet.contains(systemTime.toString())){
            throw new RuntimeException("existed");
        }
        timeSet.add(systemTime.toString());
    }

    @Test(invocationCount = 30,threadPoolSize = 10)
    public void systemTimeWithPrefixTest(){
        String time = DataUtils.INSTANCE.getSystemTime("B","E");
        if(timeSet.contains(time)){
            throw new RuntimeException("existed");
        }
        timeSet.add(time);
    }


}
