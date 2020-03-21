package org.siu.saku.generator.preregister;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.siu.saku.jooq.tables.SakuPreRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * 派发ID
 *
 * @Author Siu
 * @Date 2020/3/21 15:19
 * @Version 0.0.1
 */
@Component
@Slf4j
public class Distributor {

    private static final int MAX = 10;

    protected final DSLContext dsl;

    /**
     * 自增ID派发器
     */
    private LongAdder distributor = new LongAdder();
    private AtomicLong start = new AtomicLong(0);

    @Autowired
    public Distributor(DSLContext dsl) {
        this.dsl = dsl;
        register();
    }

    /**
     * 派发ID
     *
     * @return
     */
    public long distributor() {
        if (distributor.sum() < MAX) {
            distributor.increment();
        } else {
            register();
        }
        return distributor.sum() + start.get();
    }


    /**
     * 往数据库中注册一个号段
     * 并设置给 distributor
     */
    public synchronized void register() {
        long nextStart = getNextStart();
        this.distributor.reset();
        this.start.set(nextStart);
        log.info("注册新号段[{}-{}]", nextStart, nextStart + MAX);

    }

    /**
     * 获取下一个号段
     *
     * @return
     */
    private Long getNextStart() {
        Long currentMax = null;

        while (currentMax == null) {
            Object currentMax0 = dsl.select(SakuPreRegister.SAKU_PRE_REGISTER.END_NO.max()).from(SakuPreRegister.SAKU_PRE_REGISTER).fetch().getValue(0, 0);
            if (currentMax0 == null) {
                currentMax = 0L;
            } else {
                currentMax = (Long) currentMax0;
            }
        }

        long end = currentMax + MAX;
        dsl.insertInto(SakuPreRegister.SAKU_PRE_REGISTER,
                SakuPreRegister.SAKU_PRE_REGISTER.START_NO, SakuPreRegister.SAKU_PRE_REGISTER.END_NO, SakuPreRegister.SAKU_PRE_REGISTER.CREATE_TIME)
                .values(currentMax, end, new Timestamp(System.currentTimeMillis())).execute();

        return currentMax;


    }


}
