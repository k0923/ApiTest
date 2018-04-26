package com.apitest.sample.csvprovider;

import com.apitest.dataProvider.Csv;
import org.testng.annotations.Test;

import java.util.Map;

public class csvDataProvider {

    /**
     * Csv不指定File默认为当前类名同名CSV文件
     * 此例为csvDataProvider.csv
     * @param student
     */
    @Test
    public void csvData(@Csv Student student){

    }

    /**
     * 指定csv文件名
     */
    @Test
    public void csvDataWithFileName(@Csv(files = "csvDataProvider.csv") Student student){

    }

    /**
     * 用MAP类型代替POJO
     */
    @Test
    public void csvDataUsingMap(@Csv Map<String,String> student){

    }
}
