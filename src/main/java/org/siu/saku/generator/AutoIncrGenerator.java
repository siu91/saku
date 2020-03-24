package org.siu.saku.generator;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.siu.saku.filter.Filter;
import org.siu.saku.util.MD5Util;
import org.siu.saku.util.SakuUtil;
import org.siu.saku.generator.distributor.Distributor;
import org.siu.saku.jooq.tables.SakuShorturlMap;
import org.siu.saku.model.Url;
import org.springframework.dao.DuplicateKeyException;

import java.sql.Timestamp;

/**
 * 数据库自增生成器
 *
 * @Author Siu
 * @Date 2020/3/21 15:13
 * @Version 0.0.1
 */
@Slf4j
public class AutoIncrGenerator extends AbstractGenerator {

    private final Distributor distributor;
    protected final Filter filter;

    protected final DSLContext dsl;

    public AutoIncrGenerator(Distributor distributor, Filter filter, DSLContext dsl) {
        this.distributor = distributor;
        this.filter = filter;
        this.dsl = dsl;
    }


    @Override
    public Url shorten(String url) {
        long id = distributor.next();
        String md5 = MD5Util.encode(url);
        // 判断为重复的URL
        if (this.filter.exist(md5)) {
            String sulr = getSurlByLurlMd5(md5);
            return new Url(sulr, url);
        }

        Boolean ret = save2Db(id, url, md5);
        if (ret == null) {
            distributor.back(id);
            return new Url(null, url);
        } else if (ret) {
            return new Url(SakuUtil.id2SUrl(id), url);
        } else {
            distributor.back(id);
            // 加入过滤器，下次直接查库获取短链
            filter.put(md5);
            String surl = getSurlByLurlMd5(md5);
            return new Url(surl, url);
        }

    }


    @Override
    public Url getUrl(String surl) {
        long id = SakuUtil.sUrl2Id(surl);
        Record record = dsl.select(SakuShorturlMap.SAKU_SHORTURL_MAP.L_URL).from(SakuShorturlMap.SAKU_SHORTURL_MAP)
                .where(SakuShorturlMap.SAKU_SHORTURL_MAP.ID.equal(id)).fetchOne();

        return new Url(surl, record == null ? null : record.getValue(0).toString());

    }


    /**
     * 通过MD5获取短链
     *
     * @param lurlMd5
     * @return
     */
    public String getSurlByLurlMd5(String lurlMd5) {
        Record record = dsl.select(SakuShorturlMap.SAKU_SHORTURL_MAP.ID).from(SakuShorturlMap.SAKU_SHORTURL_MAP)
                .where(SakuShorturlMap.SAKU_SHORTURL_MAP.L_URL_MD5.equal(lurlMd5)).fetchOne();
        Long id = record == null ? null : (long) record.getValue(0);
        return id == null ? null : SakuUtil.id2SUrl(id);
    }

    /**
     * @param id
     * @param lurl
     * @param lurlMd5
     * @return
     */
    private Boolean save2Db(long id, String lurl, String lurlMd5) {
        // TODO lurlMd5 设置成唯一，插入的时候重复即为重复的URL
        // TODO 重复的URL 把ID返回 派发器，防止重复的URL把自增ID消耗完
        try {
            // 注册新的ID号段
            dsl.insertInto(SakuShorturlMap.SAKU_SHORTURL_MAP,
                    SakuShorturlMap.SAKU_SHORTURL_MAP.ID, SakuShorturlMap.SAKU_SHORTURL_MAP.L_URL, SakuShorturlMap.SAKU_SHORTURL_MAP.L_URL_MD5, SakuShorturlMap.SAKU_SHORTURL_MAP.CREATE_TIME)
                    .values(id, lurl, lurlMd5, new Timestamp(System.currentTimeMillis())).execute();
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                log.debug("save to DB error:{}" + e.getMessage());
                if (e.getMessage().contains(lurlMd5)) {
                    return false;
                }
                return null;
            } else {
                log.error("save to DB error:{}" + e.getMessage());
                return null;
            }
        }

        return true;

    }


}
