package com.apitest.dataProvider;


import com.apitest.testModels.Console;
import org.jetbrains.annotations.Nullable;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class DefaultPara implements IParameterProvider<DefaultPara> {

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Nullable
    @Override
    public List<DefaultPara> getData() {
        DefaultPara p1 = new DefaultPara();
        p1.setId(12);

        DefaultPara p2 = new DefaultPara();
        p2.setId(13);

        return Arrays.asList(p1,p2);
    }

    @Test
    public void t1(DefaultPara para,Console console){

    }
}
