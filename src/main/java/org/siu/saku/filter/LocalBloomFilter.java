package org.siu.saku.filter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.io.*;
import java.nio.charset.Charset;

/**
 * local 布隆过滤器
 *
 * @Author Siu
 * @Date 2020/3/23 15:55
 * @Version 0.0.1
 */
@Slf4j
public class LocalBloomFilter extends AbstractBloomFilter implements InitializingBean, DisposableBean {

    public LocalBloomFilter() {
    }

    public LocalBloomFilter(String filterFilePath) {
        this.filterFilePath = filterFilePath;
    }

    private BloomFilter<String> filter;

    /**
     * 过滤器本地存储文件
     */
    private String filterFilePath = "./filter/local.filter";


    @Override
    public boolean exist(String key) {
        return this.filter.test(key);
    }

    @Override
    public void doPut(String key) {
        this.filter.put(key);
    }

    /**
     * 持久化
     */
    public void save() {
        //数据持久化到本地
        File f = new File(filterFilePath);
        OutputStream out = null;
        try {
            out = new FileOutputStream(f);
            filter.writeTo(out);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    @Override
    public void afterPropertiesSet() {
        //将之前持久化的数据加载到Filter
        File f = new File(filterFilePath);

        if (f.exists()) {
            InputStream in = null;
            try {
                in = new FileInputStream(f);
                filter = BloomFilter.readFrom(in, Funnels.stringFunnel(Charset.defaultCharset()));
            } catch (IOException e) {
                log.error("初始化异常", e);
            }
        } else {
            filter = BloomFilter.create(
                    Funnels.stringFunnel(Charset.defaultCharset()),
                    500,
                    0.01);

        }


    }

    @Override
    public void destroy() throws Exception {
        // 持久化
        save();

    }
}
