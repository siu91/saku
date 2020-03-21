package org.siu.saku.generator.preregister;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.siu.saku.exception.CanNotRegisterNewStartIdError;
import org.siu.saku.jooq.tables.SakuPreRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 注册时申请每一段ID的大小，默认1000
     */
    @Setter
    private int sectionSize = 5;

    protected final DSLContext dsl;

    /**
     * 自增ID派发器
     */
    private final LongAdder globalStart = new LongAdder();
    private final LongAdder globalEnd = new LongAdder();


    @Autowired
    public Distributor(DSLContext dsl) throws CanNotRegisterNewStartIdError {
        this.dsl = dsl;
        register();
    }

    /**
     * 派发ID
     *
     * @return
     */
    public long distributeId() throws CanNotRegisterNewStartIdError {
        if (this.globalStart.sum() < this.globalEnd.sum()) {
            this.globalStart.increment();
        } else {
            register();
        }
        return this.globalStart.sum();
    }


    /**
     * 往数据库中注册一个号段
     * 并设置给 distributor
     */
    private synchronized void register() throws CanNotRegisterNewStartIdError {
        long nextStart = getNextStart();
        this.globalStart.add(nextStart - this.globalStart.sum());
        this.globalEnd.add(nextStart - this.globalStart.sum() + sectionSize);

        log.info("注册ID号段[{}-{}]", this.globalStart.sum(), this.globalEnd.sum());
    }

    /**
     * 获取下一个号段
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public long getNextStart() throws CanNotRegisterNewStartIdError {
        long start;
        try {
            // 获取当前最大的ID
            Object currentMax = dsl.select(SakuPreRegister.SAKU_PRE_REGISTER.END_NO.max()).from(SakuPreRegister.SAKU_PRE_REGISTER).fetch().getValue(0, 0);
            if (currentMax == null) {
                start = 0L;
            } else {
                start = (Long) currentMax;
            }

            long end = start + sectionSize;
            // 注册新的ID号段
            dsl.insertInto(SakuPreRegister.SAKU_PRE_REGISTER,
                    SakuPreRegister.SAKU_PRE_REGISTER.START_NO, SakuPreRegister.SAKU_PRE_REGISTER.END_NO, SakuPreRegister.SAKU_PRE_REGISTER.CREATE_TIME)
                    .values(start, end, new Timestamp(System.currentTimeMillis())).execute();
        } catch (Exception e) {
            throw new CanNotRegisterNewStartIdError(e.getMessage());
        }

        return start;


    }


}
