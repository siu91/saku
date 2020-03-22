package org.siu.saku.generator;

import org.siu.saku.model.Url;

/**
 * @Author Siu
 * @Date 2020/3/21 15:02
 * @Version 0.0.1
 */
public abstract class AbstractGenerator implements Generator {

    @Override
    public Url generator(String url) {
        return shorten(url);
    }

    /**
     * @param url
     * @return
     */
    public abstract Url shorten(String url);


}
