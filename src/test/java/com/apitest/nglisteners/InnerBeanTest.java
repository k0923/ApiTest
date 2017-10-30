package com.apitest.nglisteners;

import com.apitest.testModels.StudentModel;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testng.annotations.Test;

@Component
public class InnerBeanTest {

    @Autowired
    private StudentModel mytestStudent;

    @Test
    public void mytestStudent(){
        //System.out.println(studentModel.getMoney());
        Assert.assertNotNull(mytestStudent);
    }

}
