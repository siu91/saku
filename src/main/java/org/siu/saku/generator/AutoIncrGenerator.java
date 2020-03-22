package org.siu.saku.generator;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.siu.saku.SakuUtil;
import org.siu.saku.generator.distributor.Distributor;
import org.siu.saku.jooq.tables.SakuPreRegister;
import org.siu.saku.jooq.tables.SakuShorturlMap;
import org.siu.saku.model.Url;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;

/**
 * 数据库自增生成器
 *
 * @Author Siu
 * @Date 2020/3/21 15:13
 * @Version 0.0.1
 */
@Slf4j
@Service
public class AutoIncrGenerator extends AbstractGenerator {

    @Resource(name = "longAdderDistributor")
    Distributor distributor;

    @Autowired
    protected DSLContext dsl;


    @Override
    public Url shorten(String url) {
        long id = distributor.next();
        while (!save2Db(id, url, SakuUtil.id2SUrl(id))) {
            id = distributor.next();
        }

        return new Url(SakuUtil.id2SUrl(id), url);
    }

    /**
     * @param id
     * @param lurl
     * @param lurlMd5
     * @return
     */
    private boolean save2Db(long id, String lurl, String lurlMd5) {
        boolean success = true;
        try {
            // 注册新的ID号段
            dsl.insertInto(SakuShorturlMap.SAKU_SHORTURL_MAP,
                    SakuShorturlMap.SAKU_SHORTURL_MAP.ID, SakuShorturlMap.SAKU_SHORTURL_MAP.L_URL, SakuShorturlMap.SAKU_SHORTURL_MAP.L_URL_MD5, SakuShorturlMap.SAKU_SHORTURL_MAP.CREATE_TIME)
                    .values(id, lurl, lurlMd5, new Timestamp(System.currentTimeMillis())).execute();
        } catch (Exception e) {
            success = false;
            log.error("save to DB error:{}" + e.getMessage());
        }

        return success;

    }


}
