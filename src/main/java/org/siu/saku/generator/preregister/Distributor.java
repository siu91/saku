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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * 派发ID
 * 使用 LongAdder 会冲突，可能逻辑有问题，但是换成AtomicLong 50个线程下并没有冲突
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
    private int sectionSize = 1000;

    protected final DSLContext dsl;


    /**
     * 自增ID派发器
     */
    private final AtomicLong globalStart = new AtomicLong(0);
    private final AtomicLong globalEnd = new AtomicLong(0);


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
    public long distributeId(String tid) throws CanNotRegisterNewStartIdError {
        if (this.globalStart.get() >= this.globalEnd.get()) {
            register();
        }
        return this.globalStart.addAndGet(1);
    }


    /**
     * 往数据库中注册一个号段
     * 并设置给 distributor
     */
    private synchronized void register() throws CanNotRegisterNewStartIdError {
        long nextStart = getNextStart();
        this.globalStart.set(nextStart);
        this.globalEnd.set(nextStart + sectionSize - 1);

        log.info("注册ID号段[{}-{}]", this.globalStart.get(), this.globalEnd.get() + 1);
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
