package com.apitest.dataProvider;

import com.apitest.annotations.TestData;
import com.apitest.testModels.Student;
import org.testng.annotations.Test;

public class CustomDataProviderTest {

    @Test
    @TestData(source = DataSource.Custom,dataProvider = MyDataProvider.class)
    public void Test(Student model){

    }

}
