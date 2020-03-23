package org.siu.saku.generator;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
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

    public HashGenerator(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Url shorten(String url) {
        long id = MurmurHash.hash(url);
        String surl = SakuUtil.id2SUrl(id);
        if (save2Db(url, surl)) {
            return new Url(surl, url);
        } else {
            return new Url(null, url);
        }


    }

    @Override
    public Url getUrl(String surl) {
        String lurl = dsl.select(SakuUrlMap.SAKU_URL_MAP.L_URL).from(SakuUrlMap.SAKU_URL_MAP)
                .where(SakuUrlMap.SAKU_URL_MAP.S_URL.equal(surl)).fetchOne().getValue(0).toString();

        return new Url(surl, lurl);
    }


    /**
     * @param url
     * @param surl
     * @return
     */
    private boolean save2Db(String url, String surl) {
        boolean success = true;
        try {
            // 注册新的ID号段
            dsl.insertInto(SakuUrlMap.SAKU_URL_MAP,
                    SakuUrlMap.SAKU_URL_MAP.L_URL, SakuUrlMap.SAKU_URL_MAP.S_URL, SakuShorturlMap.SAKU_SHORTURL_MAP.CREATE_TIME)
                    .values(url, surl, new Timestamp(System.currentTimeMillis())).execute();
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                log.debug("save to DB error:{}" + e.getMessage());
            } else {
                success = false;
                log.error("save to DB error:{}" + e.getMessage());
            }

        }

        return success;

    }


}
