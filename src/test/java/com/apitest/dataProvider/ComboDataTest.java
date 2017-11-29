package com.apitest.dataProvider;

import com.apitest.annotations.Filter;
import com.apitest.annotations.TestData;
import com.apitest.testModels.StudentModel;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;


public class ComboDataTest {



//    @Factory
//    @TestData(source=DataSource.CSV,file = "CsvDataProviderTest.csv",parallel=true)
//    @TestData(source=DataSource.Spring,file="SpringDataProviderTest.xml",parallel = true)
//    public ComboDataTest(StudentModel studentModel,String data){
//
//    }

    static int count = 0;

    public static boolean filter(StudentModel studentModel,String data){
        boolean result = ++count%3==0;
        System.out.println(count);
        return result;
    }


    @Test
    @TestData(source=DataSource.CSV,file = "CsvDataProviderTest.csv",parallel=true)
    @TestData(source=DataSource.Spring,file="SpringDataProviderTest.xml",parallel = true)
    @Filter(cls = ComboDataTest.class)
    //@Filter(cls=ComboDataTest.class)
    public void test(StudentModel studentModel,String data) throws InterruptedException {

    }







}
