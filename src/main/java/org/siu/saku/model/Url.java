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
    private String lurl;

    public Url(String lurl) {
        this.lurl = lurl;
    }

    public Url(String surl, String lurl) {
        this.surl = surl;
        this.lurl = lurl;
    }
}
