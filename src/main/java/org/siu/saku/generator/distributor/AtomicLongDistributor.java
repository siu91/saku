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
@Component(value = "atomicLongDistributor")
@Slf4j
public class AtomicLongDistributor extends AbstractDistributor {

    /**
     * 自增ID派发器
     */
    private final AtomicLong current = new AtomicLong(0);
    private final AtomicLong currentEnd = new AtomicLong(0);


    public AtomicLongDistributor() {
        super();
    }

    @Override
    public long getNext() {
        if (this.current.get() >= this.currentEnd.get()) {
            register();
        }
        return this.current.addAndGet(1);
    }


    /**
     * 往数据库中注册一个号段
     * 并设置给 distributor
     */
    @Override
    public synchronized void register() {
        IdSection idSection = getNextIdSection();
        if (idSection.isSuccess()) {
            this.current.set(idSection.getStart());
            this.currentEnd.set(idSection.getEnd() - 1);
            log.info("注册ID号段[{}-{}]", idSection.getStart(), idSection.getEnd());
        }

    }


    @Override
    public long current() {
        return this.current.get();
    }
}
