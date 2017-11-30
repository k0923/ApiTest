package com.apitest.dataProvider;

import com.apitest.testModels.Student;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Executable;
import java.util.Arrays;
import java.util.List;

public class MyDataProvider implements IDataProvider<Student> {
    @NotNull
    @Override
    public List<Student> getData(@NotNull Executable method, @NotNull TestDataConfig config) {
        Student model = new Student();
        model.setAge(12);
        model.setName("sb");
        model.setMoney(888);
        model.setMan(false);
        model.setId("123");

        Student model2 = new Student();
        model2.setAge(13);
        model2.setName("sb again");
        model2.setMoney(123);
        model2.setMan(true);
        model2.setId("234");

        return Arrays.asList(model,model2);
    }
}
