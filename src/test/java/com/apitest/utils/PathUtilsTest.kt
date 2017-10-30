package com.apitest.utils

import org.testng.Assert
import org.testng.annotations.Test
import com.apitest.utils.PathUtils.getResourceFile
import com.apitest.utils.PathUtils.getClassFolder

/**
 * Created by Yang on 2017/8/17.
 */
class PathUtilsTest{

    @Test
    fun getResourceWithSameName(){
        val path = javaClass.getResourceFile("csv")
        Assert.assertTrue(path.contains("com/apitest/utils/PathUtilsTest.csv"))
    }

    @Test
    fun getClassFolder(){
        val folder = javaClass.getClassFolder()
        Assert.assertTrue(folder.contains("com/apitest/utils"))
    }

}