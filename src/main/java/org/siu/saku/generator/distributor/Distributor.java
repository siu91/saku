package org.siu.saku.generator.distributor;


/**
 * 派发
 *
 * @Author Siu
 * @Date 2020/3/21 15:19
 * @Version 0.0.1
 */
public interface Distributor {

    /**
     * 派发ID
     *
     * @return
     */
    long next();

    /**
     * 当前值
     *
     * @return
     */
    long current();


    /**
     * 重复的长链请求，把生成ID返回派发器
     */
    void back(long id);

}
