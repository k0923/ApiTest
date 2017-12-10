package com.apitest.dataProvider;

import com.apitest.testModels.Student;

public class OtherFilters {

    public OtherFilters(){
        System.out.println("OtherFilters");
    }

    public boolean filter3(Student student, String data){
        return student.getAge() == 200;
    }
}
