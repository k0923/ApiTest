package com.musical;

import com.musical.models.Product;

public enum ProductEnum {
    DOU_YIN("DOU_YIN_A.test.com","DOU_YIN_B.test.com","DOU_YIN_C.test.com"),
    INS("INS_A.test.com","INS_B.test.com"),
    LIKE("LIKE_A.test_A.com","LIKE_B.test_B.com"),
    HYPSTAR("HYPSTAR.test.com"),
    KWAI("KWAI.test.com"),
    MUSE("MUSE1.test.com","MUSE2.test.com","TIKTOK1.test.com","TIKTOK3.test.com","MUSE5.test.com","MUSE6.test.com"),
    TIKTOK("TIKTOK1.test.com","TIKTOK2.test.com","TIKTOK3.test.com"),
    FG("FG.test.com");

    private ProductEnum(String... domains){
        this.domains = domains;
    }

    private String[] domains;

    public String[] getDomains() {
        return domains;
    }
}
