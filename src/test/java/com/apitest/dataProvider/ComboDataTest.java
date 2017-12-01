package com.apitest.dataProvider;

import com.apitest.annotations.Filter;
import com.apitest.annotations.TestData;
import com.apitest.testModels.Student;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;


public class ComboDataTest {

    private static boolean outerFilter(Student student){return student.getName().equals("ZhouYang");}

    private static boolean filter1(Student student, String data){
        return student.getName().equals("Test");
    }

    @Test
    @TestData(source=DataSource.CSV,file = "CsvDataProviderTest.csv",parallel=true)
    @TestData(source=DataSource.Spring,file="SpringDataProviderTest.xml",parallel = true)
    @Filter(cls = ComboDataTest.class,methods = {"filter1"})
    public void filterTest1(Student student, String data) {
        Assert.assertEquals(student.getName(),"Test");
        Assert.assertNotNull(data);
    }

    public static boolean filter2(Student student,String data){
        return data.equals("FactoryTest");
    }

    @Test
    @TestData(source=DataSource.CSV,file = "CsvDataProviderTest.csv",parallel=true)
    @TestData(source=DataSource.Spring,file="SpringDataProviderTest.xml",parallel = true)
    @Filter(cls=ComboDataTest.class,methods={"filter2"})
    public void filterTest2(Student student,String data){
        Assert.assertEquals(data,"FactoryTest");
        Assert.assertNotNull(student);
    }

    public static boolean filter3(Student student,String data){
        return student.getAge() == 200;
    }

    @Test
    @TestData(source=DataSource.CSV,file = "CsvDataProviderTest.csv",parallel=true)
    @TestData(source=DataSource.Spring,file="SpringDataProviderTest.xml",parallel = true)
    @Filter(cls=ComboDataTest.class,methods = {"filter3"})
    public void filterTest3(Student student,String data){
        Assert.assertNotNull(data);
        Assert.assertEquals(student.getAge(),200);
    }

    @Test
    @TestData(source=DataSource.CSV,file = "CsvDataProviderTest.csv",parallel=true)
    @TestData(source=DataSource.Spring,file="SpringDataProviderTest.xml",parallel = true)
    @Filter(cls=ComboDataTest.class)
    public void filterTest4(Student student,String data){
        Assert.assertEquals(student.getName(),"Test");
        Assert.assertEquals(data,"FactoryTest");
        Assert.assertEquals(student.getAge(),200);
    }

    @Test
    @TestData(source=DataSource.CSV,file = "CsvDataProviderTest.csv",parallel=true)
    @TestData(source=DataSource.Spring,file="SpringDataProviderTest.xml",parallel = true)
    @Filter(cls=ComboDataTest.class,methods = {"filter1","filter2"})
    public void filterTest5(Student student,String data){
        Assert.assertEquals(student.getName(),"Test");
        Assert.assertEquals(data,"FactoryTest");
    }

    @Test
    @TestData(source=DataSource.CSV,file = "CsvDataProviderTest.csv",parallel=true)
    @TestData(source=DataSource.Spring,file="SpringDataProviderTest.xml",parallel = true)
    @Filter(cls=ComboDataTest.class,methods = {"filter1"})
    @Filter(cls=ComboDataTest.class,methods = {"filter2"})
    public void filterTest6(Student student,String data){
        Assert.assertEquals(student.getName(),"Test");
        Assert.assertEquals(data,"FactoryTest");
    }

    @Test
    @TestData(source=DataSource.CSV,file = "CsvDataProviderTest.csv",parallel=true)
    @TestData(source=DataSource.Spring,file="SpringDataProviderTest.xml",parallel = true)
    @Filter(cls=ComboDataTest.class,methods = {"filter1"})
    @Filter(cls=ComboDataTest.class,methods = {"filter2","filter3"})
    public void filterTest7(Student student,String data){
        Assert.assertEquals(student.getName(),"Test");
        Assert.assertEquals(data,"FactoryTest");
        Assert.assertEquals(student.getAge(),200);
    }

    private Set<Student> set = new HashSet<>();

    @Test
    @TestData(source=DataSource.CSV,file = "CsvDataProviderTest.csv",parallel=true)
    @TestData(source=DataSource.CSV,file = "CsvDataProviderTest.csv",parallel=true)
    @TestData(source=DataSource.Spring,file="SpringDataProviderTest.xml",parallel = true)
    public void filterTest8(Student student,Student student1,String data,DataSource source1){
        set.add(student);
        set.add(student1);
    }

    @AfterClass
    public void show(){
        System.out.println(set.size());
    }

    @Test
    @TestData(source=DataSource.CSV,file = "CsvDataProviderTest.csv",parallel=true)
    @TestData(source=DataSource.Spring,file="SpringDataProviderTest.xml",parallel = true)
    @Filter(cls=ComboDataTest.class,methods = {"filter3"})
    public void nullTest(Student student,@Qualifier String data){
        Assert.assertNull(data);
        Assert.assertEquals(student.getAge(),200);
    }




}
