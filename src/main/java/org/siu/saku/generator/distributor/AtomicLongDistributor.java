package org.siu.saku.generator.distributor;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.siu.saku.model.IdSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

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
public class AtomicLongDistributor extends AbstractDistributor {

    /**
     * 自增ID派发器
     */
    private final AtomicLong globalStart = new AtomicLong(0);
    private final AtomicLong globalEnd = new AtomicLong(0);

    @Autowired
    public AtomicLongDistributor(DSLContext dsl) {
        super(dsl);
        register();
    }


    @Override
    public long doDistributeId() {
        if (this.globalStart.get() >= this.globalEnd.get()) {
            register();
        }
        return this.globalStart.addAndGet(1);
    }


    /**
     * 往数据库中注册一个号段
     * 并设置给 distributor
     */
    @Override
    public synchronized void register() {
        IdSection idSection = getNextIdSection();
        if (idSection.isSuccess()) {
            this.globalStart.set(idSection.getStart());
            this.globalEnd.set(idSection.getEnd() - 1);
            log.info("注册ID号段[{}-{}]", idSection.getStart(), idSection.getEnd());
        }

    }


}
