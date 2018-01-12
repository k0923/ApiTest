package com.apitest.dataProvider;

import com.apitest.annotations.*;
import com.apitest.testModels.Console;
import com.apitest.testModels.Student;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;


public class ComboDataTest {

    private static boolean outerFilter(Student student, Console console){return student.getName().equals("ZhouYang");}

    private static boolean filter1(Student student, String data){
        return student.getName().equals("Test");
    }

    @Test
    @Filter(cls = ComboDataTest.class,methods = {"filter1"})
    public void filterTest1(
            @Csv(files={"CsvDataProviderTest.csv"}) Student student,
            @Spring(files={"SpringDataProviderTest.xml"},pattern = ".+") String data) throws InterruptedException {
        Assert.assertEquals(student.getName(),"Test");
        Assert.assertNotNull(data);
    }

    public static boolean filter2(Student student,String data){
        return data.equals("FactoryTest");
    }

    @Test
    @Filter(cls=ComboDataTest.class,methods={"filter2"})
    public void filterTest2(
            @Csv(files={"CsvDataProviderTest.csv"}) Student student
            ,@Spring(files={"SpringDataProviderTest.xml"},pattern = ".+") String data){
        Assert.assertEquals(data,"FactoryTest");
        Assert.assertNotNull(student);
    }

    public static boolean filter3(Student student,String data){
        return student.getAge() == 200;
    }

    @Test
    @Filter(cls=ComboDataTest.class,methods = {"filter3"})
    public void filterTest3( @Csv(files={"CsvDataProviderTest.csv"}) Student student
            ,@Spring(files={"SpringDataProviderTest.xml"},pattern = ".+") String data){
        Assert.assertNotNull(data);
        Assert.assertEquals(student.getAge(),200);
    }


    private boolean filter11(Student student, String data){
        return student.getName().equals("Test");
    }

    public boolean filter22(Student student,String data){
        return data.equals("FactoryTest");
    }



    @Test
    @Filter(cls=ComboDataTest.class,methods = {"filter11","filter22"})
    @Filter(cls=OtherFilters.class,methods = {"filter3"})
    public void filterTest4( @Csv(files={"CsvDataProviderTest.csv"}) Student student
            ,@Spring(files={"SpringDataProviderTest.xml"},pattern = ".+") String data){
        Assert.assertEquals(student.getName(),"Test");
        Assert.assertEquals(data,"FactoryTest");
        Assert.assertEquals(student.getAge(),200);
    }

    @Test
    @Filter(cls=ComboDataTest.class,methods = {"filter1","filter2"})
    public void filterTest5( @Csv(files={"CsvDataProviderTest.csv"}) Student student
            ,@Spring(files={"SpringDataProviderTest.xml"},pattern = ".+") String data){
        Assert.assertEquals(student.getName(),"Test");
        Assert.assertEquals(data,"FactoryTest");
    }

    @Test
    @Filter(cls=ComboDataTest.class,methods = {"filter1"})
    @Filter(cls=ComboDataTest.class,methods = {"filter2"})
    public void filterTest6( @Csv(files={"CsvDataProviderTest.csv"}) Student student
            ,@Spring(files={"SpringDataProviderTest.xml"},pattern = ".+") String data){
        Assert.assertEquals(student.getName(),"Test");
        Assert.assertEquals(data,"FactoryTest");
    }

    @Test(groups = {"p1"})
    @Filter(cls=ComboDataTest.class,methods = {"filter1"})
    @Filter(cls=ComboDataTest.class,methods = {"filter2","filter3"})
    public void filterTest7( @Csv(files={"CsvDataProviderTest.csv"}) Student student
            ,@Spring(files={"SpringDataProviderTest.xml"},pattern = ".+") String data){
        Assert.assertEquals(student.getName(),"Test");
        Assert.assertEquals(data,"FactoryTest");
        Assert.assertEquals(student.getAge(),200);
    }

    private Set<Student> set = new HashSet<>();

    @Test(groups = {"p1"})
    @Parallel
    public void filterTest8(
            @Csv(files={"CsvDataProviderTest.csv"}) Student student
            , @Csv(files={"CsvDataProviderTest.csv"}) Student student1
            , @Spring(files={"SpringDataProviderTest.xml"},pattern = ".+") String data, Console source1) throws InterruptedException {
        synchronized (set){
            set.add(student);
            set.add(student1);
        }
    }

    @AfterClass
    public void show(){
        System.out.println(set.size());
    }

    @Test(groups = {"p1"})
    @Filter(cls=ComboDataTest.class,methods = {"filter3"})
    public void nullTest(@Csv(files={"CsvDataProviderTest.csv"}) Student student
            ,@Spring(files={"SpringDataProviderTest.xml"}) String data){
        Assert.assertNull(data);
        Assert.assertEquals(student.getAge(),200);
    }

    @Test(groups = {"p1"})
    public void enumTest(Console source){
        Assert.assertNotNull(source);
    }

    @Test(groups = {"p1"})
    @Filter(cls = ComboDataTest.class,methods = "filterEnum")
    public void enumTest1(Console console){
        Assert.assertEquals(console,Console.PS4);
    }

    @Test(groups = {"p1"})
    public void comboTest(
            @Csv(files={"CsvDataProviderTest.csv"}) Student student
            , @Spring(files={"SpringDataProviderTest.xml"},pattern = ".+") String data, Console console, DefaultPara para){

    }



    static boolean filterEnum(Console console){
        return console==Console.PS4;
    }



}
