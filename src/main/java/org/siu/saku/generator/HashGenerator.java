package org.siu.saku.generator;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.siu.saku.filter.LocalBloomFilter;
import org.siu.saku.jooq.tables.SakuShorturlMap;
import org.siu.saku.jooq.tables.SakuUrlMap;
import org.siu.saku.model.Url;
import org.siu.saku.util.MurmurHash;
import org.siu.saku.util.SakuUtil;
import org.springframework.dao.DuplicateKeyException;

import java.sql.Timestamp;

/**
 * hash生成器
 *
 * @Author Siu
 * @Date 2020/3/21 15:13
 * @Version 0.0.1
 */
@Slf4j
public class HashGenerator extends AbstractGenerator {

    protected final DSLContext dsl;
    protected final LocalBloomFilter filter;

    public HashGenerator(DSLContext dsl, LocalBloomFilter filter) {
        this.dsl = dsl;
        this.filter = filter;
    }

    @Override
    public Url shorten(String url) {
        Url urlObject = new Url(url);
        return doShorten(urlObject);
    }


    private Url doShorten(Url urlObject) {
        long id = MurmurHash.hash(urlObject.getUrl());
        String surl = SakuUtil.id2SUrl(id);
        // 判断过滤器中是否有记录
        boolean exist = filter.exist(surl);
        if (exist) {
            return urlObject.setSurl(surl);
        }

        Boolean ret = save2Db(urlObject.getUrl(), surl);
        if (ret == null) {
            // 异常
            return urlObject;
        } else if (ret) {
            // 正常
            return urlObject.setSurl(surl);
        } else {
            // 冲突，重试

            // 第一次冲突记录原始短链
            if (urlObject.getDuplicate() == 0) {
                urlObject.setFirstSurl(surl);
            }
            // 冲突超过3次，判定为相同的url，重复请求，直接返回
            if (urlObject.getDuplicate() >= 3) {
                // 放入过滤器，下次直接过滤
                filter.put(urlObject.getFirstSurl());
                log.warn("冲突超过3次，判定为重复请求：{}", urlObject.getUrl());
                return urlObject.setSurl(surl);
            } else {
                urlObject.duplicate();
                return doShorten(urlObject);
            }
        }

    }

    @Override
    public Url getUrl(String surl) {
        Record record = dsl.select(SakuUrlMap.SAKU_URL_MAP.L_URL).from(SakuUrlMap.SAKU_URL_MAP)
                .where(SakuUrlMap.SAKU_URL_MAP.S_URL.equal(surl)).fetchOne();


        return new Url(surl, record == null ? null : record.getValue(0).toString()).unDuplicate();
    }


    /**
     * @param url
     * @param surl
     * @return
     */
    private Boolean save2Db(String url, String surl) {
        try {
            // 注册新的ID号段
            dsl.insertInto(SakuUrlMap.SAKU_URL_MAP,
                    SakuUrlMap.SAKU_URL_MAP.L_URL, SakuUrlMap.SAKU_URL_MAP.S_URL, SakuShorturlMap.SAKU_SHORTURL_MAP.CREATE_TIME)
                    .values(url, surl, new Timestamp(System.currentTimeMillis())).execute();
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                log.debug("save to DB error:{}" + e.getMessage());
                return false;
            } else {
                log.error("save to DB error:{}" + e.getMessage());
                return null;
            }

        }

        return true;

    }


}
