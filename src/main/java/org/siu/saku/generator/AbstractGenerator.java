package org.siu.saku.generator;

import org.siu.saku.model.Url;

/**
 * @Author Siu
 * @Date 2020/3/21 15:02
 * @Version 0.0.1
 */
public abstract class AbstractGenerator implements Generator {

    /**
     * @param url
     * @return
     */
    public abstract Url shorten(String url);

    /**
     * 通过短链获取真实URL
     *
     * @param surl
     * @return
     */
    abstract Url getUrl(String surl);


}
