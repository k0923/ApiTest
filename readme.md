### TestNg-Ex
+ Introduction  
    TestNg-Ex is an extension for testng, mainly force on data provider
+ Dependency  
    JDK1.8
+ Configuration
    1. MAVEN dependency
    ``` xml
    <dependency>
        <groupId>com.github.phoenix-young</groupId>
        <artifactId>testng-ex</artifactId>
        <version>0.4.7</version>
    </dependency>
    ```
    2. IDE Config(I use IDEA)  
    Add listener **com.apitest.nglisteners.ApiTestListener** under ***Run->Edit Configurations->Defaults->TestNg->Listeners***
+ Basic
    1. Values Data Provider
    ``` java
    package com.apitest.sample;
    import com.apitest.dataProvider.Value;
    import org.testng.annotations.Test;

    public class ValueProviderTest {

        @Test
        public void intValue(@Value(ints = {1,2,3}) int value){

        }

        @Test void stringValue(@Value(strings = {"T1","T2","T3"})String value){
        
        }
    }

    ```
    2. Enum Data Provider
    ``` java
    public enum Company {
        Microsoft,
        Sony,
        Nintendo;
    }

    public enum Console {
        XBOXONE(Company.Microsoft,2000),
        XBOX360(Company.Microsoft,1500),
        SWITCH(Company.Nintendo,2300),
        PS4(Company.Sony,1700),
        PS3(Company.Sony,1300),
        WII(Company.Nintendo,800),
        WIIU(Company.Nintendo,1300);

        private Company company;
        private int price;

        Console(Company company,int price){
            this.company = company;
            this.price = price;
        }

        public Company getCompany() {
            return company;
        }

        public int getPrice() {
            return price;
        }
    }
    
    @Test
    public void enumInjectTest(Console console){

    }
    ```
    3. Function Data Provider
        + inner function
        ``` java
        public class InnerFunctionTest {

            @Test
            public void innerFuntion(@Func(name = "getValues") String value){

            }

            public List<String> getValues(){
                return Arrays.asList("A","B","C");
            }

        }
        ```

        + static function
        ``` java
        public class StaticFunctionProvider{

            public static List<Integer> getValues(){
                return Arrays.asList(1,2,3);
            }

        }
        ```

        ``` java
        public class StaticFunctionTest {

            @Test
            public void staticFunction(@Func(name = "getValues",provider = StaticFunctionProvider.class) int value){

            }
        }
        ```

        + outer instance function（Class need default parameterless construction)
        ``` java
        public class OuterFunctionProvider {
            public List<Console> getConsoles(){
                return Arrays.asList(Console.values()).stream().filter(c->c.getCompany() == Company.Microsoft).collect(Collectors.toList());
            }
        }
        ```

        ``` java
        public class OuterFunctionTest {
            @Test
            public void outerFunction(@Func(name = "getConsoles",provider = OuterFunctionProvider.class) Console console){
        
            }
        }
        ```

        + Function with parameters(only String parameter support)
        ``` java
        public class FunctionWithParameterTest {

            @Test
            public void functionWithParameter(@Func(name = "getData",args = {"5","2"})int value){
        
            }

            public List<Integer> getData(String p1,String p2){
                int value = Integer.parseInt(p1);
                List<Integer> list = new ArrayList<>();
                for(int i =0;i<value;i++){
                    list.add(i);
                }
                return list;
            }
        }
        ```
    4. CSV Data Provider
        + csv Data ( ***CSV file should put in with the test class in the same package*** )
            id | name | age
            -- | ---- | ---  
            1  | test1 | 22
            2  | test2 | 18
            3  | test3 | 19  

        + Create a POJO
        ``` java
        public class Student implements Serializable {

            private int id;

            private String name;

            private int age;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getAge() {
                return age;
            }

            public void setAge(int age) {
                this.age = age;
            }
        }
        ```
        ``` java
        public class csvDataProvider {

            /**
             * if no file specific, the extension will find csv using the same name of class
             * and here will look for file csvDataProvider.csv
             */
            @Test
            public void csvData(@Csv Student student){
            
            }

        
            @Test
            public void csvDataWithFileName(@Csv(files = "csvDataProvider.csv") Student student){
            
            }

            /**
             * Using Map instead of POJO
             */
            @Test
            public void csvDataUsingMap(@Csv Map<String,String> student){
            
            }
        }
        ```

+ Combination（if the parameter is POJO,it should implement the interface ***Serializable***)
``` java
public class CombinationProviderTest {

    /**
     * 7 console type with 3 company type
     * the result will be 7 * 3
     */
    @Test
    public void enumCombinationTest(Console console, Company company){

    }

    /**
     * 7 console type ,3 integer value ,2 string value
     * the result will be 7 * 3 * 2 = 42
     */
    @Test
    public void otherCombinationTest(Console console, @Value(ints = {1,2,3}) int value,@Value(strings = {"a","b"}) String str){
        
    }

}
```

+ Filter
``` java
package com.apitest.sample.combination;

import com.apitest.annotations.Filter;
import com.apitest.testModels.Company;
import com.apitest.testModels.Console;
import org.testng.annotations.Test;

public class CombinationWithFilterTest {


    /**
     * 7 console type,3 company type
     * filter only need company type as Microsoft
     * the result will be 7 * 1 = 7
     */
    @Test
    @Filter(method = "filterCompany")
    public void filterTest(Console console, Company company){

    }

    /**
     * Attention
     * 1. Filter method should has same parameter type with the test method
     * 2. Return type of filter method must be boolean
     */
    public boolean filterCompany(Console console,Company company){
        return company == Company.Microsoft;
    }


    public boolean filterCompany2(Console console,Company company){
        return console == Console.PS4 || console == Console.SWITCH;
    }


    /**
     * Mutiple filter support
     */
    @Test
    @Filter(method = "filterCompany")
    @Filter(method = "filterCompany2")
    public void filterTest2(Console console,Company company){
        
    }

}

```