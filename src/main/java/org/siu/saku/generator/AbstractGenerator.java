package org.siu.saku.generator;

import org.jooq.DSLContext;
import org.siu.saku.model.Url;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author Siu
 * @Date 2020/3/21 15:02
 * @Version 0.0.1
 */
public abstract class AbstractGenerator implements Generator {

    @Autowired
    protected DSLContext dsl;


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
