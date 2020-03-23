package org.siu.saku.filter;


/**
 * 布隆过滤器
 *
 * @Author Siu
 * @Date 2020/3/23 15:52
 * @Version 0.0.1
 */
public abstract class AbstractBloomFilter  implements Filter {

    /**
     * 放入过滤器
     * @param key
     */
    public abstract void doPut(String key);

    @Override
    public void put(String s) {
        if (!exist(s)) {
            doPut(s);
        }

    }

}
