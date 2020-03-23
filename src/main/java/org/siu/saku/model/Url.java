package org.siu.saku.model;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.siu.saku.constant.Constant;

/**
 * @Author Siu
 * @Date 2020/3/21 15:08
 * @Version 0.0.1
 */
@Accessors(chain = true)
public class Url {
    @Setter
    @Getter
    private String surl;
    @Getter
    private String url;

    @Getter
    private int duplicate;


    public Url(String url) {
        this.url = url;
    }

    public Url(String surl, String url) {
        this.surl = surl;
        this.url = url;
    }

    public void duplicate() {
        this.url = this.url + Constant.DUPLICATION;
        this.duplicate++;
    }

    public Url unDuplicate() {
        while (this.url != null && this.url.endsWith(Constant.DUPLICATION)) {
            this.url = this.url.substring(0, this.url.length() - Constant.DUPLICATION.length());
        }
        return this;
    }
}
