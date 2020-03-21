package org.siu.saku.generator;

import org.siu.saku.model.Url;

/**
 * 短链生成器
 *
 * @Author Siu
 * @Date 2020/3/21 14:57
 * @Version 0.0.1
 */
public interface Generator {

    /**
     * 短链生成
     *
     * @param url
     * @return
     */
    Url generator(String url);
}
