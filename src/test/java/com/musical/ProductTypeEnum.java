package com.musical;

public enum ProductTypeEnum {
    VIDEO("example.video.cdn.com"),
    FEED("example.feed.cdn.com"),
    IMAGE("example.feed.image.com");

    public String getDomain() {
        return domain;
    }

    private String domain;

    private ProductTypeEnum(String domainName){
        this.domain = domainName;
    }
}
