package org.siu.saku.model;


import lombok.Getter;
import lombok.Setter;

/**
 * @Author Siu
 * @Date 2020/3/21 15:08
 * @Version 0.0.1
 */

public class Url {
    @Setter
    @Getter
    private String surl;
    @Getter
    private String url;

    public Url(String url) {
        this.url = url;
    }

    public Url(String surl, String url) {
        this.surl = surl;
        this.url = url;
    }
}
