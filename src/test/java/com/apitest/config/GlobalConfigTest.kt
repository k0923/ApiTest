package com.apitest.config

import com.apitest.annotations.Inject
import com.apitest.annotations.Scope
import com.apitest.testModels.Student
import org.springframework.stereotype.Component
import org.testng.Assert
import org.testng.annotations.Test

@Component
class GlobalConfigTest{

    @Inject(scope = Scope.Global)
    private var stu:Student? = null

    @Test
    fun injectFromGlobal(){
        Assert.assertEquals(stu?.age,18)
        Assert.assertEquals(stu?.name,"gyx")
    }


}