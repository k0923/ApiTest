package com.apitest.dataProvider;

import com.apitest.testModels.StudentModel;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Executable;
import java.util.Arrays;
import java.util.List;

public class MyDataProvider implements IDataProvider<StudentModel> {
    @NotNull
    @Override
    public List<StudentModel> getData(@NotNull Executable method,@NotNull TestDataConfig config) {
        StudentModel model = new StudentModel();
        model.setAge(12);
        model.setName("sb");
        model.setMoney(888);
        model.setMan(false);
        model.setId("123");

        StudentModel model2 = new StudentModel();
        model2.setAge(13);
        model2.setName("sb again");
        model2.setMoney(123);
        model2.setMan(true);
        model2.setId("234");

        return Arrays.asList(model,model2);
    }
}
