package com.apitest.dataProvider;

import com.apitest.testModels.Console;
import com.apitest.utils.DataUtils;
import com.google.gson.JsonElement;
import org.testng.Assert;
import org.testng.annotations.Test;


import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class JsonProviderTest  {

    @Test
    public void t1(@Json(file = "hello.+")  InnerModel json,Console console){
        Assert.assertEquals(json.getMsg(),"Hello World");
    }




    @Test
    public void deepCloneTest(){
        InnerModel model = new InnerModel();
        model.setMsg("Helo");
        List<InnerModel> models = Arrays.asList(model);
        List<InnerModel> model2 = DataUtils.INSTANCE.clone(models);

    }


}

class InnerModel implements Serializable{

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String msg;
}
