package com.apitest.utils;

import com.apitest.common.JObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JsonUtilsTest {

    private String getField(String path){
        JObject obj = new JObject("{'a':1}");
        return obj.convertToSpelPath(path);
    }


    @Test
    public void singleFieldTest(){
        String path = getField("location");
        Assert.assertEquals(path,"['location']");
    }

    @Test
    public void functionFieldTest(){
        String path = getField("size()");
        Assert.assertEquals(path,"size()");
    }

    @Test
    public void pathTest(){
        String path = getField("test.abc");
        Assert.assertEquals(path,"['test']['abc']");
    }

    @Test
    public void pathWithFunctionFieldTest(){
        String path = getField("test.abc.size()");
        Assert.assertEquals(path,"['test']['abc'].size()");
    }


    @Test
    public void singleIndexTest(){
        String path = getField("[0]");
        Assert.assertEquals(path,"[0]");
    }


    @Test
    public void pathWithIndexTest(){
        String path = getField("test.abc.child[0]");
        Assert.assertEquals(path,"['test']['abc']['child'][0]");
    }
}
