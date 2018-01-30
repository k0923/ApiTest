package com.apitest.dataProvider;

import com.apitest.annotations.Filter;
import com.apitest.testModels.Console;
import com.apitest.testModels.Student;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;


public class SingleDataTest {

    /*
    未有TestData定义，使用默认模式即
    找到同类名的Spring配置文件(本例为SpringDataTest.xml)
    并且找到id=partA_stu1的Bean
    */
    @Test
    public void partA_stu1(@Spring Student student){
        //在Spring中id=stu1的student的name叫WANG_A
        Assert.assertEquals(student.getName(),"WANG_A");
    }

    @Test
    public void stu(@Spring Student student){
        //由于Spring中未有id=stu的student,故为空
        Assert.assertNull(student);
    }

    /*
    使用注解Qualifier来匹配
    即找到对应Spring配置文件中所有类型为Student并且id满足正则表达式.+的student
    此例中由于正则表达式匹配所有id，所以该用例将会运行多次
     */
    @Test
    public void stus(@Spring(pattern = ".+") Student student){
        Assert.assertNotNull(student);
    }

    /*
    匹配所有id以partA为开头的student
     */
    @Test
    public void partA(@Spring(pattern = "partA.+") Student student){
        Assert.assertNotNull(student);
    }

    /*
    使用外部文件作为数据源,为简单起见，设置文件名为SpringDataTest.xml
    加入注解TestData,设置paras为SpringDataTest.xml
     */
    @Test
    public void outSpringFile(@Spring(files = {"SingleDataTest.xml"}) Student student){
        //在Spring中id=outFile的student的name叫WANG_A
        Assert.assertEquals(student.getName(),"WANG_A");
    }

    /*
    数据过滤器要满足三个条件
    1. 方法必须为static
    2. 方法返回值必须为boolean
    3. 方法的参数必须为注入参数的类型或者父类型
     */
    public static boolean studentFilter1(Student student){
        return student.isMan();
    }

    /*
    首先根据正则(.+)匹配到所有的student
    然后匹配过滤器SpringDataTest.class的方法studentFilter1
    最终返回满足符合过滤器的student
     */
    @Test
    @Filter(cls = SingleDataTest.class,method = "studentFilter1")
    public void filterStus(@Spring(pattern = ".+") Student student){
        Assert.assertEquals(student.isMan(),true);
    }


    /*
    使用CSV数据源
    需要使用注解TestData，若不设置paras属性则默认找到同类名的CSV文件(本例为SpringDataTest.csv)
    注意：Student的各个属性和CSV的列名相同
     */
    @Test
    public void beanFromCsv(@Csv Student student){
        Assert.assertNotNull(student);
    }

    /*
    也可以使用Map作为CSV的输入类型，key为列名，value为对应的文本
     */
    @Test
    public void mapFromCsv(@Csv Map<String,String> student){
        Assert.assertNotNull(student);
    }

    /*
    使用过滤器
     */
    @Test
    @Filter(cls = SingleDataTest.class,method = "studentFilter1")
    public void csvFilter(@Csv Student student){
        Assert.assertEquals(student.isMan(),true);
    }

    /*
    使用外部数据,使用相对路径
     */
    @Test
    public void outCSVFile(@Csv(files={"SingleDataTest.csv"}) Student student){
        Assert.assertNotNull(student);
    }

    /*
    自注入,Console为枚举类型
    由于Spring中未配置Console类型的BEAN，此处注入Console所有的枚举值
     */
    @Test
    public void selfInject(Console console){
        Assert.assertNotNull(console);
    }

    /*
    数据过滤器要满足三个条件
    1. 方法必须为static
    2. 方法返回值必须为boolean
    3. 方法的参数必须为注入参数的类型或者父类型
    */
    public static boolean filterConsole(Console console){
        return console == Console.PS4;
    }

    /*
    自注入,使用过滤器
     */
    @Test
    @Filter(cls=SingleDataTest.class,method = "filterConsole")
    public void selfInjectFilter(Console console){
        Assert.assertEquals(console,Console.PS4);
    }

}
