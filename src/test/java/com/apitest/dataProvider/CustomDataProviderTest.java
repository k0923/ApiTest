package com.apitest.dataProvider;

import com.apitest.annotations.TestData;
import com.apitest.testModels.StudentModel;
import org.testng.annotations.Test;

public class CustomDataProviderTest {

    @Test
    @TestData(source = DataSource.Custom,dataProvider = MyDataProvider.class)
    public void Test(StudentModel model){

    }

}
